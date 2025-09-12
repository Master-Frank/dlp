/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { SortOrder } from 'antd/es/table/interface';
import { RequestData } from '@ant-design/pro-components';
import { request } from '@@/exports';
import { filterParamConverter, sortParamConverter } from '@/utils/utils';

/**
 * 获取身份源平台列表
 */
export async function getIdentityProviderList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AccountAPI.ListIdentitySource>> {
  return request<API.ApiResult<AccountAPI.ListIdentitySource>>('/api/v1/identity_source/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AccountAPI.ListIdentitySource>) => {
    const data: RequestData<AccountAPI.ListIdentitySource> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 创建身份源
 */
export async function createIdentitySource(
  params: Record<string, any>,
): Promise<API.ApiResult<{ id: string }>> {
  return request(`/api/v1/identity_source/create`, {
    data: params,
    method: 'POST',
    requestType: 'json',
  });
}

/**
 * 启用身份源
 */
export async function enableIdentitySource(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/identity_source/enable/${id}`, { method: 'PUT' });
}

/**
 * 禁用身份源
 */
export async function disableIdentitySource(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/identity_source/disable/${id}`, { method: 'PUT' });
}
