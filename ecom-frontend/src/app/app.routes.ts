import { Routes } from '@angular/router';
import { AdminCategories } from './admin/category/admin-categories/admin-categories';
import { CreateCategory } from './admin/category/create-category/create-category';
import { roleCheckGuard } from './auth/role-check.guards';
import { AdminProduct } from './admin/product/admin-product/admin-product';
import { CreateProduct } from './admin/product/create-product/create-product';
import { Home } from './home/home';
import {ProductDetail} from './shop/product-detail/product-detail';

export const routes: Routes = [
    {
        path: 'admin/categories/list',
        component: AdminCategories,
        canActivate: [roleCheckGuard],
        data:{
            authorities: ['ROLE_ADMIN']
        }
    },
     {
        path: 'admin/categories/create',
        component: CreateCategory,
        canActivate: [roleCheckGuard],
        data:{
            authorities: ['ROLE_ADMIN']
        }
    },
     {
        path: 'admin/products/list',
        component: AdminProduct,
        canActivate: [roleCheckGuard],
        data:{
            authorities: ['ROLE_ADMIN']
        }
    },
     {
        path: 'admin/products/create',
        component: CreateProduct,
        canActivate: [roleCheckGuard],
        data:{
            authorities: ['ROLE_ADMIN']
        }
    },
    {
        path: '',
        component: Home
    },
  {
    path: 'product/:publicId',
    component: ProductDetail
  },
  {
    path: 'we',
    component:CreateCategory
  },
  {
    path:"we2",
    component: CreateProduct
  }

];
