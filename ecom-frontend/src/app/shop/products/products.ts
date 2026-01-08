import {Component, effect, inject} from '@angular/core';
import {injectQueryParams} from 'ngxtension/inject-query-params';
import {ProductsFilter} from './products-filter/products-filter';
import {UserProduct} from '../../shared/service/user-product';
import {Router} from '@angular/router';
import {ToastService} from '../../shared/toast/toast-service';
import {Pagination} from '../../shared/modal/request.modal';
import {ProductFilter} from '../../admin/model/product.model';
import {injectQuery} from '@tanstack/angular-query-experimental';
import {lastValueFrom} from 'rxjs';
import {ProductCard} from '../product-card/product-card';

@Component({
  selector: 'ecom-products',
  imports: [
    ProductsFilter,
    ProductCard
  ],
  templateUrl: './products.html',
  styleUrl: './products.scss',
})
export class Products {

  category = injectQueryParams('category');
  size = injectQueryParams('size')
  sort = injectQueryParams('sort')
  productService = inject(UserProduct)
  router = inject(Router)
  toastService = inject(ToastService)

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: ['createdDate,desc']
  }

  filterProducts: ProductFilter = {
    category: this.category(),
    size: this.size() ? this.size()! : '',
    sort: [this.sort() ? this.sort()! : ''],
  }

  lastCategory = "";

  constructor() {
  effect(()=>this.handleFilteredProductsQueryError());
  effect(()=>this.handleParameterChange())
  }

  filteredProductsQuery = injectQuery(() => ({
    queryKey: ['products', this.filterProducts],
    queryFn: () => lastValueFrom(this.productService.filter(this.pageRequest, this.filterProducts))
  }))


  onFilterChange(filterProduct: ProductFilter) {
    filterProduct.category = this.category();
    this.filterProducts = filterProduct;
    this.pageRequest.sort = filterProduct.sort;
    this.router.navigate(['/products'], {
      queryParams: {
        ...filterProduct
      }
    });
    this.filteredProductsQuery.refetch();
  }

  private handleFilteredProductsQueryError(){
    if (this.filteredProductsQuery.isError()){
      this.toastService.show('Error Failed to load products','ERROR')
    }
  }


  private handleParameterChange(){
    if (this.category()){
      if (this.lastCategory !=this.category() && this.lastCategory !== ''){
       this.filterProducts= {
          category: this.category(),
          size: this.size() ? this.size()! : '',
          sort: [this.sort() ? this.sort()! : ''],
        };
       this.filteredProductsQuery.refetch();
      }
    }
  }
}
