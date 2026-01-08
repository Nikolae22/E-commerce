import { Component, effect, inject } from '@angular/core';
import { AdminProductService } from '../../admin-product-service';
import { ToastService } from '../../../shared/toast/toast-service';
import { injectMutation, injectQuery, injectQueryClient } from '@tanstack/angular-query-experimental';
import { Pagination } from '../../../shared/modal/request.modal';
import { lastValueFrom } from 'rxjs';
import e from 'express';
import { RouterLink } from "@angular/router";
import { CommonModule } from '@angular/common';
import { FaIconComponent } from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'ecom-admin-product',
  imports: [RouterLink, CommonModule, FaIconComponent],
  templateUrl: './admin-product.html',
  styleUrl: './admin-product.scss',
})
export class AdminProduct {


  productService=inject(AdminProductService);
  toastService=inject(ToastService);
  queryClient=injectQueryClient();

  pageRequest:Pagination={

    page:0,
    size:20,
    sort:['createdDate,desc']
  }

  constructor(){
    effect(()=>this.handleProductQueryError())
  }

  productQuery=injectQuery(()=>({
    queryKey:['products',this.pageRequest],
    queryFn: ()=>lastValueFrom(this.productService.findAllProducts(this.pageRequest))
  }))

  deleteMutation=injectMutation(()=>({
    mutationFn:
    (productPublicId:string)=> lastValueFrom(this.productService.deleteProduct(productPublicId)),
    onSuccess:()=>this.onDeletionSuccess(),
    onError:()=>this.onDeletionError()
  }))

  deleteProduct(publicId:string){
    this.deleteMutation.mutate(publicId);
  }

  private onDeletionSuccess(){
    this.queryClient.invalidateQueries({queryKey:['products']})
    this.toastService.show("Product deleted",'SUCCESS')
  }

  private onDeletionError(){
    this.toastService.show('Issue when deleting product',"ERROR")
  }
  
  private handleProductQueryError(){
    if(this.productQuery.isError()){
      this.toastService.show('Error failed to load products','ERROR')
    }
  }

}
