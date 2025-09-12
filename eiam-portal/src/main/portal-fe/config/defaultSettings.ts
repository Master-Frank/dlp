/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import {ProLayoutProps} from '@ant-design/pro-components';

const Settings: ProLayoutProps & {
  logo?: string;
  siderWidth?: number;
} = {
  navTheme: 'light',
  colorPrimary: '#1677FF',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: true,
  fixSiderbar: true,
  splitMenus: false,
  colorWeak: false,
  title: '统一登录平台',
  logo: '/logo.svg',
  iconfontUrl: '',
  siderWidth: 215,
};

export default Settings;
