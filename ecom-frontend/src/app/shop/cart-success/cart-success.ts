import {afterNextRender, Component, inject} from '@angular/core';
import {injectQueryParams} from 'ngxtension/inject-query-params';
import {CartService} from '../cart-service';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'ecom-cart-success',
  imports: [
    FaIconComponent
  ],
  templateUrl: './cart-success.html',
  styleUrl: './cart-success.scss',
})
export class CartSuccess {

  sessionId=injectQueryParams('session_id');

  cartService=inject(CartService);

  isValidAccess=true;

  constructor() {
    afterNextRender(()=>this.verifySession());
  }

  verifySession(){
    const sessionLocalStorage = this.cartService.getSessionId();
    if (sessionLocalStorage !== this.sessionId()){
      this.isValidAccess=false;
    }else{
      this.cartService.deleteSessionId();
      this.cartService.clearCart();
    }

  }

}
