import {Component, inject, PLATFORM_ID} from '@angular/core';
import {Order} from '../../shared/service/order';
import {Pagination} from '../../shared/modal/request.modal';
import {injectQuery} from '@tanstack/angular-query-experimental';
import {lastValueFrom} from 'rxjs';
import {OrderedItems} from '../../shared/modal/order.model';
import {CurrencyPipe, isPlatformBrowser} from '@angular/common';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'ecom-admin-orders',
  imports: [
    FaIconComponent,
    CurrencyPipe
  ],
  templateUrl: './admin-orders.html',
  styleUrl: './admin-orders.scss',
})
export class AdminOrders {
  orderService = inject(Order);

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: [],
  };

  platformId = inject(PLATFORM_ID);

  ordersAdminQuery = injectQuery(() => ({
    queryKey: ['admin-orders', this.pageRequest],
    queryFn: () =>
      lastValueFrom(this.orderService.getOrdersForAdmin(this.pageRequest)),
  }));

  computeItemsName(items: OrderedItems[]) {
    return items.map((item) => item.name).join(', ');
  }

  computeItemsQuantity(items: OrderedItems[]) {
    return items.reduce((acc, item) => acc + item.quantity, 0);
  }

  computeTotal(items: OrderedItems[]) {
    return items.reduce((acc, item) => acc + item.price * item.quantity, 0);
  }

  checkIfPlatformBrowser() {
    return isPlatformBrowser(this.platformId);
  }

}
