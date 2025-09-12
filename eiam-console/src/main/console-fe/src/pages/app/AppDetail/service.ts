/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@@/exports';
import { GetApp } from './data.d';

/**
 * Get Application
 */
export async function getApp(id: string): Promise<API.ApiResult<GetApp>> {
  return request<API.ApiResult<GetApp>>(`/api/v1/app/get/${id}`, {
    method: 'GET',
  });
}

/**
 * Update Application
 */
export async function updateApp(params: Record<string, string>): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/app/update`, {
    method: 'PUT',
    data: params,
    requestType: 'json',
  });
}

/**
 * create Account
 */
export async function createAccount(
  params: Record<string, string>,
): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>('/api/v1/app/account/create', {
    method: 'POST',
    requestType: 'json',
    data: params,
  });
}

/**
 * remove Account
 */
export async function removeAccount(id: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/app/account/delete/${id}`, {
    method: 'DELETE',
  });
}

/**
 *  设置账号为默认
 */
export async function updateAppAccountActivateDefault(id: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/app/account/activate_default/${id}`, {
    method: 'PUT',
  });
}

/**
 *  取消账号为默认
 */
export async function updateAppAccountDeactivateDefault(
  id: string,
): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/app/account/deactivate_default/${id}`, {
    method: 'PUT',
  });
}

/**
 * create App AccessPolicy
 */
export async function createAppAccessPolicy(
  params: Record<string, string>,
): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>('/api/v1/app/access_policy/create', {
    method: 'POST',
    requestType: 'json',
    data: params,
  });
}

/**
 * Enable AppAccessPolicy
 */
export async function enableAppAccessPolicy(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/app/access_policy/enable/${id}`, {
    method: 'PUT',
  });
}

/**
 * Disable AppAccessPolicy
 */
export async function disableAppAccessPolicy(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/app/access_policy/disable/${id}`, {
    method: 'PUT',
  });
}

/**
 * Get  Config
 */
export async function getAppConfig(id: string): Promise<API.ApiResult<Record<string, any>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/get/config/${id}`, {
    method: 'GET',
  });
}

/**
 * Save  Config
 */
export async function saveAppConfig(
  values: Record<string, any>,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/save/config`, {
    method: 'PUT',
    data: values,
  });
}

/**
 * Get Cert List
 */
export async function getCertList(
  appId: string,
  usingType?: string,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/cert/list`, {
    method: 'GET',
    params: { appId, usingType },
  });
}
