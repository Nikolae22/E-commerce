import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FaConfig, FaIconLibrary, FaIconComponent, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontAwesomeIcons } from './shared/font-awesome-icons';
import { Navbar } from "./layout/navbar/navbar";
import { Footer } from "./layout/footer/footer";

@Component({
  selector: 'ecom-root',
  imports: [RouterOutlet, FontAwesomeModule, Navbar, Footer],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('ecom-frontend');

  private faIconLibrary=inject(FaIconLibrary);
  private faConfig=inject(FaConfig)

  ngOnInit(): void {
    this.initFontAwesome();
  }


  private initFontAwesome(){
    this.faConfig.defaultPrefix='far';
    this.faIconLibrary.addIcons(...fontAwesomeIcons)
  }
} 
 