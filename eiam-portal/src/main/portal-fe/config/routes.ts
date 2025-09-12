/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
export default [
  {
    layout: false,
    hideInMenu: true,
    name: 'login',
    path: '/login',
    component: './Login',
  },
  {
    layout: false,
    hideInMenu: true,
    name: 'session-expired',
    path: '/session-expired',
    component: './SessionExpired',
  },
  {
    path: '/',
    redirect: '/application',
  },
  //暂时隐藏工作台菜单
  {
    hideInMenu: true,
    name: 'workplace',
    icon: 'HomeOutlined',
    path: '/workplace',
    component: './Workplace',
  },
  // 我的应用
  {
    name: 'application',
    icon: 'AppstoreOutlined',
    path: '/application',
    component: './Application',
  },
  // 我的账户
  {
    name: 'account',
    icon: 'UserOutlined',
    path: '/account',
    component: './Account',
  },
  // 系统审计
  {
    name: 'audit',
    icon: 'AuditOutlined',
    path: '/audit',
    component: './Audit',
  },
  // 会话管理
  {
    name: 'session',
    icon: 'SecurityScanOutlined',
    path: '/session',
    component: './Session',
  },
  {
    path: '*',
    layout: false,
    component: './404',
  },
];
