import { Component, input } from '@angular/core';
import { Product } from '../../admin/model/product.model';
import { RouterLink } from "@angular/router";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'ecom-product-card',
  imports: [RouterLink,CommonModule],
  templateUrl: './product-card.html',
  styleUrl: './product-card.scss',
})
export class ProductCard {

  product=input.required<Product>();

}
