import { Component, inject } from '@angular/core';
import { UserProduct } from '../../shared/service/user-product';
import { Pagination } from '../../shared/modal/request.modal';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { last, lastValueFrom } from 'rxjs';
import { ProductCard } from "../../shop/product-card/product-card";

@Component({
  selector: 'ecom-featured',
  imports: [ProductCard],
  templateUrl: './featured.html',
  styleUrl: './featured.scss',
})
export class Featured {
  productService = inject(UserProduct);

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: [],
  };

  featuredProductQuery = injectQuery(() => ({
    queryKey: ['featured-product', this.pageRequest],
    queryFn: () => lastValueFrom(this.productService.findAllFeaturedProducts(this.pageRequest)),
  }));
}
