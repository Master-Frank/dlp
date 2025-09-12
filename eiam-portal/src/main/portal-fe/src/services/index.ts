/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@@/plugin-request/request';

/**
 * 获取登录公钥
 */
export async function getLoginEncryptSecret(): Promise<API.ApiResult<API.EncryptSecret>> {
  return request(`/api/v1/public_secret?type=login`);
}

/**
 * 获取加密公钥
 */
export async function getEncryptSecret(): Promise<API.ApiResult<API.EncryptSecret>> {
  return request(`/api/v1/public_secret?type=encrypt`);
}

/**
 * 退出登录
 */
export async function outLogin() {
  return request('/api/v1/logout', {
    method: 'post',
  });
}

/**
 * 获取当前用户
 */
export async function getCurrentUser(): Promise<API.ApiResult<API.CurrentUser>> {
  return request<API.ApiResult<API.CurrentUser>>('/api/v1/session/current_user', {
    responseType: 'json',
  });
}

/**
 * 获取当前session状态
 */
export async function getCurrentStatus(): Promise<API.ApiResult<API.CurrentStatus>> {
  return request<API.ApiResult<API.CurrentStatus>>('/api/v1/session/current_status', {
    skipErrorHandler: true,
  });
}
