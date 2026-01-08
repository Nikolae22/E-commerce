import { Component } from '@angular/core';
import { RouterLink } from "@angular/router";
import { Featured } from "./featured/featured";

@Component({
  selector: 'ecom-home',
  imports: [RouterLink, Featured],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

}
