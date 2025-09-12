/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import {defineConfig} from '@umijs/max';

export default defineConfig({
  define: {
    'process.env.PREVIEW_ENV': true,
  },
});
