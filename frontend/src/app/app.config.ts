import {ApplicationConfig, inject, isDevMode, provideBrowserGlobalErrorListeners} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideServiceWorker} from '@angular/service-worker';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {provideApollo} from 'apollo-angular';
import {HttpLink} from 'apollo-angular/http';
import {InMemoryCache} from '@apollo/client';
import {TranslocoHttpLoader} from './transloco-loader';
import {provideTransloco} from '@jsverse/transloco';
import {AutoRefreshTokenService, provideKeycloak, UserActivityService, withAutoRefreshToken} from 'keycloak-angular';
import {authInterceptor} from './core/interceptors/auth-interceptor';
import {environment} from '../environments/environment';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes), provideServiceWorker('ngsw-worker.js', {
      enabled: !isDevMode(),
      registrationStrategy: 'registerWhenStable:30000'
    }),
    provideServiceWorker('ngsw-worker.js', {
      enabled: !isDevMode(),
      registrationStrategy: 'registerWhenStable:30000'
    }),
    provideKeycloak({
      config: environment.keycloak,
      initOptions: {
        onLoad: undefined, // important for start page and demo
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
        checkLoginIframe: false,
        enableLogging: true
      },
      features: [
        withAutoRefreshToken()
      ]
    }),
    provideApollo(() => {
      const httpLink = inject(HttpLink);
      return {
        link: httpLink.create({uri: environment.graphqlUrl}),
        cache: new InMemoryCache(),
      };
    }),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideTransloco({
      config: {
        availableLangs: ['pl', 'en'],
        defaultLang: 'pl',
        reRenderOnLangChange: true,
        prodMode: !isDevMode(),
      },
      loader: TranslocoHttpLoader
    }),
    AutoRefreshTokenService,
    UserActivityService
  ]
};
