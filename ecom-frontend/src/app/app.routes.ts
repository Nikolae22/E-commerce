import {Routes} from '@angular/router';
import {AdminCategories} from './admin/category/admin-categories/admin-categories';
import {CreateCategory} from './admin/category/create-category/create-category';
import {roleCheckGuard} from './auth/role-check.guards';
import {AdminProduct} from './admin/product/admin-product/admin-product';
import {CreateProduct} from './admin/product/create-product/create-product';
import {Home} from './home/home';
import {ProductDetail} from './shop/product-detail/product-detail';
import {Products} from './shop/products/products';
import {Cart} from './shop/cart/cart';
import {CartSuccess} from './shop/cart-success/cart-success';
import {UserOrders} from './user/user-orders/user-orders';
import {AdminOrders} from './admin/admin-orders/admin-orders';

export const routes: Routes = [
  {
    path: 'admin/categories/list',
    component: AdminCategories,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: 'admin/categories/create',
    component: CreateCategory,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: 'admin/products/list',
    component: AdminProduct,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: 'admin/orders/list',
    component: AdminOrders,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: 'admin/products/create',
    component: CreateProduct,
    canActivate: [roleCheckGuard],
    data: {
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
    path: 'products',
    component: Products
  },
  {
    path: 'cart',
    component: Cart
  },
  {
    path: 'cart/success',
    component: CartSuccess
  },
  {
    path:'users/order',
    component: UserOrders
  },
  {
    path: 'we',
    component: CreateCategory
  },
  {
    path: "we2",
    component: CreateProduct
  }

];
