import { Component, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FaConfig, FaIconLibrary, FaIconComponent, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontAwesomeIcons } from './shared/font-awesome-icons';
import { Navbar } from "./layout/navbar/navbar";
import { Footer } from "./layout/footer/footer";
import { Oauth2 } from './auth/oauth2';
import { CommonModule, isPlatformBrowser, NgClass } from '@angular/common';
import { ToastService } from './shared/toast/toast-service';

@Component({
  selector: 'ecom-root',
  imports: [RouterOutlet, FontAwesomeModule, Navbar,
     Footer, NgClass,CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('ecom-frontend');

  private faIconLibrary=inject(FaIconLibrary);
  private faConfig=inject(FaConfig)
  private oauth2Service=inject(Oauth2);
   toastService=inject(ToastService)

  platformId=inject(PLATFORM_ID);

  constructor(){
    if(isPlatformBrowser(this.platformId)){
      this.oauth2Service.initAuthentication();
    }
    this.oauth2Service.connectedUserQuery=this.oauth2Service.fetch();
  }

  ngOnInit(): void {
    this.initFontAwesome();
    this.toastService.show('Hello from toast cancel me from ngOnit this is. atest','SUCCESS')
  }


  private initFontAwesome(){
    this.faConfig.defaultPrefix='far';
    this.faIconLibrary.addIcons(...fontAwesomeIcons)
  }
} 
 