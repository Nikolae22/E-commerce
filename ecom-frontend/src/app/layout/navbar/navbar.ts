import {Component, inject, OnInit} from '@angular/core';
import { RouterLink } from "@angular/router";
import { FaIconComponent } from "@fortawesome/angular-fontawesome";
import { Oauth2 } from '../../auth/oauth2';
import {UserProduct} from '../../shared/service/user-product';
import {injectQuery} from '@tanstack/angular-query-experimental';
import {lastValueFrom, Observable} from 'rxjs';
import {ClickOutside} from 'ngxtension/click-outside';
import {CartService} from '../../shop/cart-service';


@Component({
  selector: 'ecom-navbar',
  imports: [RouterLink, FaIconComponent, ClickOutside],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit{
  oauth2Service=inject(Oauth2);
  productService=inject(UserProduct);
  cartService=inject(CartService)

  nbItemsInCart=0

  connectedUserQuery=this.oauth2Service.connectedUserQuery;

  categoryQuery=injectQuery(()=>({
    queryKey: ['categories'],
    queryFn: ()=>lastValueFrom(this.productService.findAllCategories())
  }))

  login(){
    this.closeDropDown();
    this.oauth2Service.login();
  }

  logout(){
    this.closeDropDown();
    this.oauth2Service.logout();
  }

  isConnected():boolean{
    return this.connectedUserQuery?.status() === 'success'
    && this.connectedUserQuery?.data()?.email !== this.oauth2Service.notConnected;
  }

  closeDropDown(){
    const bodyElemet=document.activeElement as HTMLBodyElement;
    if(bodyElemet){
      bodyElemet.blur();
    }
  }

  closeMenu(menu:HTMLDetailsElement){
    menu.removeAttribute('open')
  }

  ngOnInit(){
    this.listenToCart();
  }

  private listenToCart(){
    this.cartService.addedToCart.subscribe(productsInCart=>{
      this.nbItemsInCart=productsInCart.reduce(
        (acc,product)=>acc + product.quantity,0)
    })
  }

}
