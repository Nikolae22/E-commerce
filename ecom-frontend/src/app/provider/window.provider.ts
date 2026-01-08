import { InjectionToken, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

export const WINDOW = new InjectionToken<Window | null>('WINDOW');

export const WINDOW_PROVIDER = {
  provide: WINDOW,
  useFactory: (platformId: object) =>
    isPlatformBrowser(platformId) ? window : null,
  deps: [PLATFORM_ID],
};
