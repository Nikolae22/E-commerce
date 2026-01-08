import { Product } from '../../admin/model/product.model';
import { createPaginationOption, Page } from '../modal/request.modal';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Pagination } from '../modal/request.modal';
import { environment } from '../../../environments/environment.production';

@Injectable({
  providedIn: 'root',
})
export class UserProduct {

  http=inject(HttpClient);

  findAllFeaturedProducts(pageRequest:Pagination):Observable<Page<Product>>{
      const params=createPaginationOption(pageRequest);
      return this.http.get<Page<Product>>(`${environment.apiUrl}/products-shop/featured`,{params})
  }

  findOneByPublicId(publicId:string):Observable<Product>{
    return  this.http.get<Product>(`${environment.apiUrl}/products-shop/fine-one`,
      {params: {publicId} })
  }

  findRelatedProduct(pageRequest:Pagination,productPublicId:string):Observable<Page<Product>>{
    let params=createPaginationOption(pageRequest);
    params=params.append('publicId',productPublicId)
    return this.http.get<Page<Product>>(`${environment.apiUrl}/products-shop/related`,{params})
  }

}
