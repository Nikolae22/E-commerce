
import { Component, inject } from '@angular/core';
import { AdminProductService } from '../../admin-product-service';
import { ToastService } from '../../../shared/toast/toast-service';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateCategoryFormConten, Product, ProductCategory } from '../../model/product.model';
import { Router } from '@angular/router';
import { injectMutation } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { NgxControlError } from 'ngxtension/control-error';



@Component({
  selector: 'ecom-create-category',
  imports: [ReactiveFormsModule,NgxControlError],
  templateUrl: './create-category.html',
  styleUrl: './create-category.scss',
})
export class CreateCategory {

  formBuilder=inject(FormBuilder);
  productService=inject(AdminProductService);
  toastService=inject(ToastService);
  router=inject(Router)


  name=new FormControl<string>('',{nonNullable:true,validators:[Validators.required]})

  public createForm=this.formBuilder.nonNullable.group<CreateCategoryFormConten>({
    name:this.name
  })

  loading=false;

  createMutation=injectMutation(()=>({
    mutationFn:
    (categoryToCreate:ProductCategory)=> lastValueFrom(this.productService.createCategory(categoryToCreate)),
    onSettled: ()=>this.onCreationSettled(),
    onSuccess:()=>this.onCreationSuccess(),
    onError:()=>this.onCreationError()
  }))

  create():void{
    const categoryToCreate:ProductCategory={
      name:this.createForm.getRawValue().name
    }

    this.loading=false;
    this.createMutation.mutate(categoryToCreate);
  }


  private onCreationSettled():void{
    this.loading=false;
  }

  private onCreationSuccess():void{
    this.toastService.show('Category created','SUCCESS')
    this.router.navigate(['/admin/categories/list'])
  }

  private onCreationError():void{
    this.toastService.show('Error when creating category','ERROR')
  }
}
