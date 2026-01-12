import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import {AbstractSecurityStorage, authInterceptor, LogLevel, provideAuth} from 'angular-auth-oidc-client'
import { environment } from '../environments/environment.development';
import {provideQueryClient,QueryClient} from '@tanstack/angular-query-experimental'
import { from } from 'rxjs';
import { SsrStorage } from './auth/ssr-storage';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes), provideClientHydration(withEventReplay()),
    provideHttpClient(withFetch(),
  withInterceptors([authInterceptor()])),
  provideAuth({
    config: {
      authority: environment.kinde.authority,
      redirectUrl: environment.kinde.redirectUrl,
      postLogoutRedirectUri: environment.kinde.postLogoutRedirectUrl,
      clientId: environment.kinde.clientId,
      scope: 'apenid profile email offline',
      responseType: 'code',
      silentRenew: true,
      useRefreshToken: true,
      logLevel: LogLevel.Warn,
      secureRoutes: [environment.apiUrl],
      customParamsAuthRequest:{
        audience: environment.kinde.audience
      },
    },
  }),
  {provide: AbstractSecurityStorage, useClass: SsrStorage},
   provideQueryClient(new QueryClient())
    // provideStripe(environment.stripePublishableKey) npm i ngx-stripe
  ]
};
