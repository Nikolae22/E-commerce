import {inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, map, Observable, pipe} from 'rxjs';
import {Cart, CartItemAdd, StripeSession} from '../shared/modal/cart.modal';
import {isPlatformBrowser} from '@angular/common';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CartService {

  platformId = inject(PLATFORM_ID);
  http = inject(HttpClient);

  private keyStorage = 'cart';
  private  keySessionId='stripe-session-id'

  private addToCart$ = new BehaviorSubject<Array<CartItemAdd>>([])

  addedToCart = this.addToCart$.asObservable();


  constructor() {
    const cartFromLocalStorage = this.getCartFromLocalStorage();
    this.addToCart$.next(cartFromLocalStorage);
  }

  private getCartFromLocalStorage(): Array<CartItemAdd> {
    if (isPlatformBrowser(this.platformId)) {
      const cartProducts = localStorage.getItem(this.keyStorage);
      if (cartProducts) {
        return JSON.parse(cartProducts) as CartItemAdd[];
      } else {
        return [];
      }
    } else {
      return [];
    }
  }

  addToCart(publicId: string, command: 'add' | 'remove'): void {
    if (isPlatformBrowser(this.platformId)) {
      const itemToAdd: CartItemAdd = {publicId, quantity: 1};
      const cartFromLocalStorage = this.getCartFromLocalStorage();
      if (cartFromLocalStorage.length !== 0) {
        const productExists = cartFromLocalStorage.find(item => item.publicId === publicId);
        if (productExists) {
          if (command === 'add') {
            productExists.quantity++;
          } else if (command === 'remove') {
            productExists.quantity--;
          }
        } else {
          cartFromLocalStorage.push(itemToAdd);
        }
      } else {
        cartFromLocalStorage.push(itemToAdd);
      }
      localStorage.setItem(this.keyStorage, JSON.stringify(cartFromLocalStorage));
      this.addToCart$.next(cartFromLocalStorage);
    }
  }

  removeFromCart(publicId: string): void {
    if (isPlatformBrowser(this.platformId)) {
      const cartFromLocalStorage = this.getCartFromLocalStorage();
      const productExist = cartFromLocalStorage.find(item => item.publicId === publicId);
      if (productExist) {
        cartFromLocalStorage.splice(cartFromLocalStorage.indexOf(productExist), 1)
        localStorage.setItem(this.keyStorage, JSON.stringify(cartFromLocalStorage));
        this.addToCart$.next(cartFromLocalStorage);
      }
    }
  }

  getCartDetail(): Observable<Cart> {
    const cartFromLocalStorage = this.getCartFromLocalStorage();
    const publicIdsForUrl = cartFromLocalStorage.reduce(
      (acc, item) =>
        `${acc}${acc.length > 0 ? ',' : ''}${item.publicId}`, '');
    return this.http
      .get<Cart>(`${environment.apiUrl}/orders/get-car-details`, {
        params: {productsIds: publicIdsForUrl},})
      .pipe(
        map((cart=>this.mapQuantity(cart,cartFromLocalStorage)))
      )
  }

  private mapQuantity(
    cart: Cart,
    cartFromLocalStorage: Array<CartItemAdd>
  ): Cart {
    for (const cartItem of cartFromLocalStorage) {
      const foundProduct = cart.products.find(
        (item) => item.publicId === cartItem.publicId
      );
      if (foundProduct) {
        foundProduct.quantity = cartItem.quantity;
      }
    }
    return cart;
  }

  initPaymentSession(cart:Array<CartItemAdd>):Observable<StripeSession>{
    return this.http.post<StripeSession>(`${environment.apiUrl}/orders/init-payment`,cart);
  }

  storeSessionId(sessionId:string){
    if (isPlatformBrowser(this.platformId)){
      localStorage.setItem(this.keySessionId,sessionId);
    }
  }

  getSessionId():string{
    if (isPlatformBrowser(this.platformId)){
      const stripeSessionId = localStorage.getItem(this.keySessionId);
      if (stripeSessionId){
        return stripeSessionId;
      }
    }
    return  '';
  }

  deleteSessionId():void{
    if (isPlatformBrowser(this.platformId)){
      localStorage.removeItem(this.keySessionId)
    }
  }

}
