/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
// @ts-ignore
/* eslint-disable */
// API 更新时间：
// API 唯一标识：
import * as account from './account';
import * as app from './app';
import * as user from './user';
import { request } from '@@/exports';

export default {
  account,
  app,
  user,
};

/**
 * 退出登录
 */
export async function outLogin() {
  return request('/api/v1/logout', {
    method: 'POST',
  });
}

/**
 * 获取加密公钥
 */
export async function getEncryptSecret(): Promise<API.ApiResult<API.EncryptSecret>> {
  return request(`/api/v1/public_secret?type=encrypt`);
}


/**
 * 获取当前session状态
 */
export async function getCurrentStatus(): Promise<API.ApiResult<API.CurrentStatus>> {
  return request<API.ApiResult<API.CurrentStatus>>('/api/v1/session/current_status', {
    skipErrorHandler: true,
  });
}
