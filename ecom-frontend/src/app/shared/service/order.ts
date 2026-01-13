import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {createPaginationOption, Page, Pagination} from '../modal/request.modal';
import {Observable} from 'rxjs';
import {AdminOrderDetail, UserOrderDetail} from '../modal/order.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Order {

  http=inject(HttpClient);

  getOrdersForConnectedUser(pageRequest:Pagination):Observable<Page<UserOrderDetail>>{
    const params = createPaginationOption(pageRequest);
    return this.http.get<Page<UserOrderDetail>>(`${environment.apiUrl}/orders/user`,{params})
  }

  getOrdersForAdmin(pageRequest:Pagination):Observable<Page<AdminOrderDetail>>{
    const params = createPaginationOption(pageRequest);
    return this.http.get<Page<AdminOrderDetail>>(`${environment.apiUrl}/orders/admin`,{params})
  }


}
