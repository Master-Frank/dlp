/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@umijs/max';

/**
 * 创建应用分组
 */
export async function createAppGroup(
  params: Record<string, string>,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/group/create`, {
    method: 'POST',
    data: params,
    requestType: 'json',
  });
}

/**
 * 获取应用分组
 */
export async function getAppGroup(id: string): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/group/get/${id}`, {
    method: 'GET',
  });
}

/**
 * 修改应用分组
 */
export async function updateAppGroup(
  params: Record<string, string>,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/group/update`, {
    method: 'PUT',
    data: params,
    requestType: 'json',
  });
}

/**
 * Remove App Group
 */
export async function removeAppGroup(id: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/app/group/delete/${id}`, {
    method: 'DELETE',
  });
}
