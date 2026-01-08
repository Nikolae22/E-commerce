import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { CreateQueryResult, injectQuery } from '@tanstack/angular-query-experimental';
import { ConnectedUser } from '../shared/modal/user.modal';
import { firstValueFrom, Observable } from 'rxjs';
import { environment } from '../../environments/environment.production';

@Injectable({
  providedIn: 'root',
})
export class Oauth2 {
  http = inject(HttpClient);
  oidsSecurityService = inject(OidcSecurityService);

  connectedUserQuery:CreateQueryResult<ConnectedUser> | undefined;

  // connectedUserQuery = injectQuery(() => ({
  //   queryKey: ['connected-user'],
  //   queryFn: () => this.http.get<ConnectedUser>('/api/me'),
  // }));

  notConnected = 'NOT_CONNECTED';

  fetch():CreateQueryResult<ConnectedUser>{
    return injectQuery(()=>({
      queryKey: ['connected-user'],
      queryFn: ()=>firstValueFrom(this.fetchUserHttp(false))
    }))
  }

  fetchUserHttp(forceResync:boolean):Observable<ConnectedUser>{
    const params=new HttpParams().set('forceResync',forceResync);
    return this.http.get<ConnectedUser>(`${environment.apiUrl}/users/authenticated`,{params})
  }

  login() {
    this.oidsSecurityService.authorize();
  }

  logout() {
    this.oidsSecurityService.logoff().subscribe((value) => console.log(value));
  }

  initAuthentication() {
    this.oidsSecurityService.checkAuth().subscribe((authInfo) => {
      if (authInfo.isAuthenticated) {
        console.log('connected');
      } else {
        console.log('not connected');
      }
    });
  }

  hasAnyAuthorities(connectedUser: ConnectedUser, authorities: Array<string> | string): boolean {
    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }
    if (connectedUser.authorities) {
      return connectedUser.authorities.some(
        (authority: string) => authorities.includes(authority));
    }else{
      return false;
    }
  }
}
