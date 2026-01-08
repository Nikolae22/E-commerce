import { QueryKey } from './../../../../../node_modules/@tanstack/query-core/src/types';
import { Component, effect, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ToastService } from '../../../shared/toast/toast-service';
import { AdminProductService } from '../../admin-product-service';
import {
  injectMutation,
  injectQuery,
  injectQueryClient,
} from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { FaIconComponent } from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'ecom-admin-categories',
  imports: [RouterLink, FaIconComponent],
  templateUrl: './admin-categories.html',
  styleUrl: './admin-categories.scss',
})
export class AdminCategories {
  productAdminServic = inject(AdminProductService);
  tostService = inject(ToastService);
  queryClient = injectQueryClient();

  constructor(){
    effect(()=>this.handleCategoriesQueryError())
  }


  categoriesQuery = injectQuery(() => ({
    queryKey: ['categories'],
    queryFn: () => lastValueFrom(this.productAdminServic.findAllCategories()),
  }));

  deleteMutation = injectMutation(() => ({
    mutationFn: (categoryPublicId: string) =>
      lastValueFrom(this.productAdminServic.deleteCategory(categoryPublicId)),
    onSuccess:()=>this.onDeletionSuccess(),
    onError: ()=>this.onDeletionError()
  }));

  private onDeletionSuccess(): void {
    this.queryClient.invalidateQueries({ queryKey: ['categories'] });
    this.tostService.show('Category Deleted', 'SUCCESS');
  }

  private onDeletionError(): void {
    this.tostService.show('Issue when deleting cateogry', 'ERROR');
  }

  private handleCategoriesQueryError() {
    if (this.categoriesQuery.isError()) {
      this.tostService.show('Error! Failed to laod Categories', 'ERROR');
    }
  }

  deleteCategory(publicId: string) {
    this.deleteMutation.mutate(publicId);
  }
}
