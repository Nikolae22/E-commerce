import { Component, inject } from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminProductService } from '../../admin-product-service';
import { ToastService } from '../../../shared/toast/toast-service';
import { Router } from '@angular/router';
import {
  BaseProdcut,
  CreateProductFormContent,
  ProductPicture,
  ProductSizes,
} from '../../model/product.model';
import { faL } from '@fortawesome/free-solid-svg-icons';
import { injectMutation, injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { NgxControlError } from "ngxtension/control-error";
import { sizes } from '../../model/product.model';

@Component({
  selector: 'ecom-create-product',
  imports: [ReactiveFormsModule, NgxControlError],
  templateUrl: './create-product.html',
  styleUrl: './create-product.scss',
})
export class CreateProduct {
  public fromBuilder = inject(FormBuilder);
  productService = inject(AdminProductService);
  tostService = inject(ToastService);
  router = inject(Router);

  public productPicture = new Array<ProductPicture>();

  name = new FormControl<string>('', { nonNullable: true, validators: [Validators.required] });
  description = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });
  price = new FormControl<number>(0, { nonNullable: true, validators: [Validators.required] });
  size = new FormControl<ProductSizes>('XS', {
    nonNullable: true,
    validators: [Validators.required],
  });
  category = new FormControl<string>('', { nonNullable: true, validators: [Validators.required] });
  brand = new FormControl<string>('', { nonNullable: true, validators: [Validators.required] });
  color = new FormControl<string>('', { nonNullable: true, validators: [Validators.required] });
  featured = new FormControl<boolean>(false, {
    nonNullable: true,
    validators: [Validators.required],
  });
  pictures = new FormControl<Array<ProductPicture>>([], {
    nonNullable: true,
    validators: [Validators.required],
  });
  stock = new FormControl<number>(0, { nonNullable: true, validators: [Validators.required] });

  public createFrom = this.fromBuilder.nonNullable.group<CreateProductFormContent>({
    brand: this.brand,
    color: this.color,
    description: this.description,
    name: this.name,
    price: this.price,
    size: this.size,
    category: this.category,
    featured: this.featured,
    pictures: this.pictures,
    stock: this.stock,
  });

  loading = false;

  categoryQuey=injectQuery(()=>({
    queryKey: ['categories'],
    queryFn: ()=>lastValueFrom(this.productService.findAllCategories())
  }))

  creationMutation = injectMutation(() => ({
    mutationFn: (product: BaseProdcut) => lastValueFrom(this.productService.createProduct(product)),
    onSettled: () => this.onCreationSettled(),
    onSuccess: () => this.onCreationSuccess(),
    onError: () => this.onCreationError(),
  }));

  create(): void {
    const prodctToCreate: BaseProdcut = {
      brand: this.createFrom.getRawValue().brand,
      color: this.createFrom.getRawValue().color,
      description: this.createFrom.getRawValue().description,
      name: this.createFrom.getRawValue().name,
      price: this.createFrom.getRawValue().price,
      size: this.createFrom.getRawValue().size,
      category: {
        publicId: this.createFrom.getRawValue().category.split('+')[0],
        name: this.createFrom.getRawValue().category.split('+')[1],
      },
      featured: this.createFrom.getRawValue().featured,
      pictures: this.productPicture,
      nbInStock: this.createFrom.getRawValue().stock,
    };

    this.loading = true;
    this.creationMutation.mutate(prodctToCreate);
  }

  private extractFileFromTarget(target:EventTarget | null):FileList | null{
    const htmlInputTarget=target as HTMLInputElement;
    if(target === null || htmlInputTarget.files === null){
      return null;
    }
    return htmlInputTarget.files;
  }

  onUploadNewPicture(target:EventTarget | null){
    this.productPicture=[];
    const picturesFileList=this.extractFileFromTarget(target);
    if(picturesFileList !==null){
      for(let i =0; i<picturesFileList.length;i++){
        let picture=picturesFileList.item(i);
        if(picture !==null){
          const productPicture:ProductPicture={
            file:picture,
            mimeType: picture.type
          };
          this.productPicture.push(productPicture);
        }
      }
    }

  }

  private onCreationSettled() {
    this.loading = false;
  }

  private onCreationSuccess() {
    this.router.navigate(['/admin/product/list']);
    this.tostService.show('Product Created', 'ERROR');
  }

  private onCreationError() {
    this.tostService.show('Something went wrong', 'ERROR');
  }
  protected readonly sizes = sizes;
}
