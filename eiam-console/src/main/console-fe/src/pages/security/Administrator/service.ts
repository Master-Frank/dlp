/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { RequestData } from '@ant-design/pro-components';
import { request } from '@@/exports';
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import { SortOrder } from 'antd/es/table/interface';
import { AdministratorList } from './data';

/**
 * 查询管理员列表
 */
export async function getAdministratorList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AdministratorList>> {
  return request<API.ApiResult<AdministratorList>>('/api/v1/setting/administrator/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AdministratorList>) => {
    const data: RequestData<AdministratorList> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 新增管理员
 *
 * @param params
 */
export async function createAdministrator(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/administrator/create`, {
    data: params,
    method: 'POST',
  });
}

/**
 * 更新管理员
 *
 * @param params
 */
export async function updateAdministrator(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/administrator/update`, {
    data: { ...params },
    method: 'PUT',
  });
}

/**
 * Remove Administrator
 */
export async function deleteAdministrator(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/administrator/delete/${id}`, {
    method: 'DELETE',
  });
}

/**
 * Get Administrator Details
 */
export async function getAdministrator(id: string): Promise<API.ApiResult<Record<string, string>>> {
  return request(`/api/v1/setting/administrator/get/${id}`, {
    method: 'GET',
  });
}

/**
 * Enable Administrator
 */
export async function enableAdministrator(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/administrator/enable/${id}`, {
    method: 'PUT',
  });
}

/**
 * Disable Administrator
 */
export async function disableAdministrator(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/administrator/disable/${id}`, {
    method: 'PUT',
  });
}

/**
 * 重置管理员密码
 *
 * @param id
 * @param password
 */
export async function resetAdministratorPassword(
  id: string,
  password: string,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/administrator/reset_password`, {
    params: { id, password },
    requestType: 'form',
    method: 'PUT',
  });
}

/**
 * 验证管理员用户信息
 *
 * @param type
 * @param value
 * @param id
 */
export async function administratorParamCheck(
  type: string,
  value: string,
  id?: string,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/administrator/param_check`, {
    params: { id, type, value },
    method: 'GET',
  });
}
