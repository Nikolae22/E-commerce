import {Component, effect, inject, input, output} from '@angular/core';
import {FormBuilder, FormControl, FormRecord, ReactiveFormsModule, Validators} from '@angular/forms';
import {FilterProductsFormContent, ProductFilter, ProductFilterForm, sizes} from '../../../admin/model/product.model';

@Component({
  selector: 'ecom-products-filter',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './products-filter.html',
  styleUrl: './products-filter.scss',
})
export class ProductsFilter {

  sort = input<string>('createdDate,asc')
  size = input<string>();

  productFilter = output<ProductFilter>();
  formBuilder = inject(FormBuilder);

  constructor() {
    effect(() => this.updateSizeFormValue())
    effect(() => this.updateSortFormValue())

    this.formFilterProduct.valueChanges.subscribe(() =>
      this.onFilterChange(this.formFilterProduct.getRawValue())
    )
  }

  formFilterProduct = this.formBuilder.nonNullable.group<FilterProductsFormContent>({
    sort: new FormControl<string>(this.sort().split(',')[1], {nonNullable: true, validators: [Validators.required]}),
    size: this.buildSizeFormControl()
  })

  private buildSizeFormControl(): FormRecord<FormControl<boolean>> {
    const sizeFormControl = this.formBuilder.nonNullable.record<FormControl<boolean>>({});
    for (const size of sizes) {
      sizeFormControl.addControl(size, new FormControl<boolean>(false, {nonNullable: true}))
    }
    return sizeFormControl;
  }


  private onFilterChange(filter: Partial<ProductFilterForm>) {
    const filterProduct: ProductFilter = {
      size: '',
      sort: [`createdDate, ${filter.sort}`]
    }
    let sizes: [string, boolean][] = [];
    if (filter.size !== undefined) {
      sizes = Object.entries(filter.size)
    }

    for (const [sizeKey, sizeValue] of sizes) {
      if (sizeValue) {
        if (filterProduct.size?.length == 0) {
          filterProduct.size = sizeKey;
        } else {
          filterProduct.size += `${sizeKey}`;
        }
      }
    }
    this.productFilter.emit(filterProduct);
  }


  public getSizeFromGroup(): FormRecord<FormControl<boolean>> {
    return this.formFilterProduct.get('size') as FormRecord<FormControl<boolean>>;
  }

  private updateSizeFormValue() {
    if (this.size()) {
      const sizes = this.size()!.split('.');
      for (const size of sizes) {
        this.getSizeFromGroup().get(size)!.setValue(true, {emitEvent: false})
      }
    }
  }

  private updateSortFormValue() {
    if (this.sort()) {
      this.formFilterProduct.controls.sort.setValue(this.sort().split('.')[1], {emitEvent: false})
    }
  }

  protected readonly sizes = sizes;
}
