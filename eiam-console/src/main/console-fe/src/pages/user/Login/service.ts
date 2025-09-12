/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@umijs/max';

/**
 * 获取公钥
 */
export async function getLoginPublicSecret(): Promise<API.ApiResult<API.EncryptPublicSecret>> {
  return request(`/api/v1/public_secret?type=login`);
}

/**
 * 账户登录
 *
 * @param params
 */
export async function accountLogin(params: API.LoginParamsType) {
  return request<API.ApiResult<string>>('/api/v1/login', {
    method: 'POST',
    params,
    requestType: 'form',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}
