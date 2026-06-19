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
import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';
import { message as Msg, notification } from 'antd';
import { parse, stringify } from 'querystring';
import { history } from '@@/core/history';

import { isLoginPath, isSessionExpiredPath, SESSION_EXPIRED_PATH } from './utils/utils';

/**
 * @name 错误处理
 * pro 自带的错误处理， 可以在这里做自己的改动
 * @doc https://umijs.org/docs/max/request#配置
 */
export const requestConfig: RequestConfig = {
  withCredentials: true,
  xsrfHeaderName: 'ulp-csrf',
  xsrfCookieName: 'ulp-csrf-cookie',
  // 错误处理： umi@3 的错误处理方案。
  errorConfig: {
    // 错误接收及处理
    errorHandler: (error: any, opts: any) => {
      if (opts?.skipErrorHandler) throw error;
      if (error.response) {
        // Axios 的错误
        // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
        if (error.response.status === 401) {
          // 排除 登录、登录回调
          if (isLoginPath() || isSessionExpiredPath()) {
            return;
          }
          const query = parse(history.location.search);
          const { redirect_uri } = query as { redirect_uri: string };
          if (!isLoginPath() && !redirect_uri) {
            let settings: Record<string, string> = { pathname: SESSION_EXPIRED_PATH };
            const domain: string[] | string = window.location.href.split('/');
            if (window.location.href !== domain[0] + '//' + domain[2] + '/') {
              settings = {
                ...settings,
                search: stringify({
                  redirect_uri: window.location.href,
                }),
              };
            }
            const href = history.createHref(settings);
            history.push(href);
          }
          return;
        }
        const status = error.response.status;
        if (status === 502 || status === 503 || status === 504) {
          notification.error({
            message: `请求错误`,
            description: `服务暂时不可用，请稍后重试！`,
          });
          return;
        }
        notification.error({
          description: error.response.data.message,
          message: '请求错误',
        });
      } else if (error.request) {
        // 请求已经成功发起，但没有收到响应
        // \`error.request\` 在浏览器中是 XMLHttpRequest 的实例，
        // 而在node.js中是 http.ClientRequest 的实例
        Msg.error('None response! Please retry.').then();
      } else {
        // 发送请求时出了点问题
        Msg.error('Request error, please retry.').then();
      }
    },
  },
  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      // requestType==='form' ，或者requestType 为 undefined 更改 Accept 为 'application/json'
      if (config.requestType === 'form' || !config.requestType) {
        config.headers = { ...config.headers, Accept: 'application/json' };
      }
      return { ...config };
    },
  ],

  // 响应拦截器
  responseInterceptors: [
    (response) => {
      return response;
    },
    // mfa_setup_required 全局拦截：组织强制 MFA 时，任何受保护接口（OrgMfaEnforcementFilter）
    // 都会返回 403 + { error: 'mfa_setup_required', reason: 'org_policy' }，统一引导到绑定页。
    (response: any) => {
      try {
        const data = response?.data;
        if (
          response?.status === 403 &&
          data &&
          (data.error === 'mfa_setup_required' || data.status === 'mfa_setup_required')
        ) {
          const path = history.location.pathname;
          if (path !== '/mfa/setup' && path !== '/mfa/challenge') {
            const search = stringify({ redirect_uri: '/api/v1/jump' });
            window.location.replace(`/mfa/setup?${search}`);
          }
        }
      } catch (e) {
        // ignore guard failures
      }
      return response;
    },
  ],
};
