import {Component, effect, inject, OnInit} from '@angular/core';
import {injectParams} from 'ngxtension/inject-params';
import {UserProduct} from '../../shared/service/user-product';
import {Router} from '@angular/router';
import {ToastService} from '../../shared/toast/toast-service';
import {Pagination} from '../../shared/modal/request.modal';
import {interval, lastValueFrom, take} from 'rxjs';
import {injectQuery} from '@tanstack/angular-query-experimental';
import {CurrencyPipe, NgStyle} from '@angular/common';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {ProductCard} from '../product-card/product-card';
import {CartService} from '../cart-service';
import {Product} from '../../admin/model/product.model';

@Component({
  selector: 'ecom-product-detail',
  imports: [
    CurrencyPipe,
    NgStyle,
    FaIconComponent,
    ProductCard
  ],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.scss',
})
export class ProductDetail{

  publicId = injectParams('publicId')
  productService = inject(UserProduct);
  router = inject(Router);
  toastService = inject(ToastService)
  cartService=inject(CartService)
  lastPublicId = '';

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: []
  }

  labelAddToCart='Add to Cart';
  iconAddToCart='shopping-cart'

  constructor() {
    effect(() => this.handlePublicIdChange())
    effect(() => this.handleProductQueryError())
    effect(() => this.handleRelatedProductQueryError())

  }



  productQuery = injectQuery(() => ({
    queryKey: ['product', this.publicId()],
    queryFn: () => lastValueFrom(this.productService.findOneByPublicId(this.publicId()!))
  }))

  relatedProductQuery = injectQuery(() => ({
    queryKey: ['related-product', this.publicId(), this.pageRequest],
    queryFn: () => lastValueFrom(this.productService
      .findRelatedProduct(this.pageRequest, this.publicId()!))
  }))

  private handlePublicIdChange() {
    if (this.publicId()) {
      if (this.lastPublicId != this.publicId() && this.lastPublicId !== '') {
        this.relatedProductQuery.refetch();
        this.productQuery.refetch();
      }
      this.lastPublicId = this.publicId()!;
    }
  }

  private handleRelatedProductQueryError() {
    if (this.relatedProductQuery.isError()) {
      this.toastService.show("Error Failed to load related Product", 'ERROR')
    }
  }

  private handleProductQueryError() {
    if (this.productQuery.isError()) {
      this.toastService.show("Error Failed to load  Product", 'ERROR')
    }
  }

  addToCart(productToAdd:Product){
    this.cartService.addToCart(productToAdd.publicId,'add');
    this.labelAddToCart='Added to cart';
    this.iconAddToCart='check';

    interval(3000).pipe(take(1)).subscribe(()=>{
      this.labelAddToCart='Add to cart';
      this.iconAddToCart='shopping-cart'
    })
  }

}
