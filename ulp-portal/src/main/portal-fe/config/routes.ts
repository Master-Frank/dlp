/*
 * ulp-portal - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    layout: false,
    hideInMenu: true,
    name: 'mfa-setup',
    path: '/mfa/setup',
    component: './MFA/Setup',
  },
  {
    layout: false,
    hideInMenu: true,
    name: 'mfa-challenge',
    path: '/mfa/challenge',
    component: './MFA/Challenge',
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
