// server.config.ts
import { ApplicationConfig, mergeApplicationConfig } from '@angular/core';
import { provideServerRendering } from '@angular/platform-server';
import { appConfig } from './app.config';
import { WINDOW_PROVIDER } from './provider/window.provider';

export const serverConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(), // SSR senza withRoutes
    WINDOW_PROVIDER
  ]
};

export const config = mergeApplicationConfig(appConfig, serverConfig);
