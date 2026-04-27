import {TranslocoGlobalConfig} from '@jsverse/transloco-utils';

const config: TranslocoGlobalConfig = {
  rootTranslationsPath: 'public/i18n/',
  langs: ['pl', 'en'],
  keysManager: {
    addMissingKeys: true,
    replace: false,
    sort: true
  }
};

export default config;
