/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ParamCheckType } from '@/constant';
import { request } from '@@/plugin-request/request';
import { GetBoundIdpList } from './data.d';

/**
 * 准备修改手机号
 *
 * @param encrypt
 */
export async function prepareChangePhone(encrypt: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/account/prepare_change_phone`, {
    data: { encrypt: encrypt },
    method: 'POST',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

export async function getBoundIdpList(): Promise<API.ApiResult<GetBoundIdpList[]>> {
  return request(`/api/v1/account/bound_idp`, {
    method: 'GET',
  });
}

export async function unbindIdp(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/account/unbind_idp/${id}`, {
    method: 'DELETE',
  });
}

/**
 * 更换手机
 *
 * @param data
 */
export async function changePhone(data: Record<string, string>): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/account/change_phone`, {
    data: data,
    method: 'PUT',
  });
}

/**
 * 准备更换邮箱
 *
 * @param encrypt
 */
export async function prepareChangeEmail(encrypt: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/account/prepare_change_email`, {
    data: { encrypt: encrypt },
    method: 'POST',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 更换邮箱
 *
 * @param data
 */
export async function changeEmail(data: Record<string, string>): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/account/change_email`, {
    data: data,
    method: 'PUT',
  });
}

/**
 * 更换密码
 *
 * @param encrypt
 */
export async function changePassword(encrypt: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/account/change_password`, {
    data: { encrypt: encrypt },
    method: 'PUT',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 更改基础信息
 *
 * @param encrypt
 */
export async function changeBaseInfo(encrypt: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/account/change_info`, {
    data: { encrypt: encrypt },
    method: 'PUT',
  });
}

/**
 * 验证用户信息
 *
 * @param type
 * @param value
 * @param id
 */
export async function userParamCheck(
  type: ParamCheckType,
  value: string,
  id?: string,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/user/param_check`, {
    params: { id, type, value },
    method: 'GET',
  });
}

/**
 * 准备修改账户密码
 */
export async function prepareChangePassword(encrypt: string): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/account/prepare_change_password', {
    method: 'POST',
    data: { encrypt: encrypt },
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}
