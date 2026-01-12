import {Component, effect, inject, OnInit, PLATFORM_ID} from '@angular/core';
import {CartService} from '../cart-service';
import {Router, RouterLink} from '@angular/router';
import {ToastService} from '../../shared/toast/toast-service';
import {Oauth2} from '../../auth/oauth2';
import {CartItem, CartItemAdd, StripeSession} from '../../shared/modal/cart.modal';
import {injectMutation, injectQuery} from '@tanstack/angular-query-experimental';
import {lastValueFrom} from 'rxjs';
import {CurrencyPipe, isPlatformBrowser} from '@angular/common';
import {StripeService} from 'ngx-stripe';


@Component({
  selector: 'ecom-cart',
  imports: [
    RouterLink,
    CurrencyPipe
  ],
  templateUrl: './cart.html',
  styleUrl: './cart.scss',
})
export class Cart implements OnInit {

  cartService = inject(CartService)
  route = inject(Router)
  toastService = inject(ToastService)
  oauth2Service = inject(Oauth2)
  cart: Array<CartItem> = [];
  isInitPaymentSessionLoading = false;
  stripeService = inject(StripeService)


  platformId = inject(PLATFORM_ID);

  labelCheckout = 'Login to Checkout';

  action: 'login' | 'checkout' = 'login';

  cartQuery = injectQuery(() => ({
    queryKey: ['cart'],
    queryFn: () => lastValueFrom(this.cartService.getCartDetail())
  }))

  initPaymentSession = injectMutation(() => ({
    mutationFn: (cart: Array<CartItemAdd>) =>
      lastValueFrom(this.cartService.initPaymentSession(cart)),
    onSuccess: (result: StripeSession) => this.onSessionCreateSuccess(result),
  }));


  constructor() {
    this.extractListToUpdate();
    this.checkedUserLoggedIn();

  }

  private extractListToUpdate() {
    effect(() => {
      if (this.cartQuery.isSuccess()) {
        this.cart = this.cartQuery.data().products;
      }
    })
  }

  private checkedUserLoggedIn() {
    effect(() => {
      const connectedUserQuery = this.oauth2Service.connectedUserQuery;
      if (connectedUserQuery?.isError) {
        this.labelCheckout = 'Login to checkout';
        this.action = 'login';
      } else if (connectedUserQuery?.isSuccess()) {
        this.labelCheckout = 'Checkout';
        this.action = 'checkout';
      }
    })
  }

  ngOnInit() {
    this.cartService.addedToCart.subscribe(cart => this.updateQuantity(cart));
  }

  private updateQuantity(cartUpdated: Array<CartItemAdd>) {
    for (const cartItemToUpdated of this.cart) {
      const itemToUpdated = cartUpdated.find(item => item.publicId === cartItemToUpdated.publicId);
      if (itemToUpdated) {
        cartItemToUpdated.quantity = itemToUpdated.quantity;
      } else {
        this.cart.splice(this.cart.indexOf(cartItemToUpdated), 1)
      }
    }
  }

  addQuantityToCart(publicId: string) {
    this.cartService.addToCart(publicId, 'add');
  }

  removeQuantityFromCart(publicId: string, quantity: number) {
    if (quantity > 1) {
      this.cartService.addToCart(publicId, 'remove');
    }
  }

  removeItem(publicId: string) {
    const itemToRemoveIndex = this.cart.findIndex(item => item.publicId === publicId);
    if (itemToRemoveIndex) {
      this.cart.splice(itemToRemoveIndex, 1);
    }
    this.cartService.removeFromCart(publicId);
  }

  computeTotal() {
    return this.cart.reduce((acc, item) => acc + item.price * item.quantity, 0)
  }

  checkIfEmptyCart(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return (
        this.cartQuery.isSuccess() &&
        this.cartQuery.data().products.length === 0
      );
    } else {
      return false;
    }
  }

  checkout() {
    if (this.action === 'login') {
      this.oauth2Service.login();
    } else if (this.action === 'checkout') {
      this.isInitPaymentSessionLoading = true;
      const cartItemsAdd = this.cart.map(
        (item) =>
          ({publicId: item.publicId, quantity: item.quantity} as CartItemAdd)
      );
      this.initPaymentSession.mutate(cartItemsAdd);
    }
  }

  private onSessionCreateSuccess(sessionId: StripeSession) {
    this.cartService.storeSessionId(sessionId.id);
    // 2. Perform a standard browser redirect using the session URL
    if (sessionId.url) {
      window.location.href = sessionId.url;
    } else {
      this.isInitPaymentSessionLoading = false;
      this.toastService.show('Order error: Session URL missing', 'ERROR');
    }
  }

  // OLD VERSION non so se quella sopra fa anche serche in StripeSession ho agiunto url
  //   this.stripeService
  //     .redirectToCheckout({sessionId: sessionId.id})
  //     .subscribe({
  //       error: err => {
  //         this.isInitPaymentSessionLoading = false;
  //         this.toastService.show(`Order error ${err.message}`, 'ERROR');
  //       }
  //     });
  // }
}
