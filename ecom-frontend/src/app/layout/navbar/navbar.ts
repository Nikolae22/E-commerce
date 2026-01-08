import { Component, inject } from '@angular/core';
import { RouterLink } from "@angular/router";
import { FaIconComponent } from "@fortawesome/angular-fontawesome";
import { Oauth2 } from '../../auth/oauth2';

@Component({
  selector: 'ecom-navbar',
  imports: [RouterLink, FaIconComponent],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  oauth2Service=inject(Oauth2);

  connectedUserQuery=this.oauth2Service.connectedUserQuery;
  
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

}
