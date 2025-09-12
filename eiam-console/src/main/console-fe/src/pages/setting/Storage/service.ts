/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@umijs/max';

/**
 * 获取存储配置
 */
export async function getStorageConfig(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/storage/config`);
}

/**
 * 保存存储配置
 *
 * @param params
 */
export async function saveStorageConfig(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/storage/save`, {
    data: { ...params },
    method: 'POST',
  });
}

/**
 * 禁用存储服务
 *
 */
export async function disableStorage(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/storage/disable`, {
    method: 'PUT',
  });
}

/**
 * 获取IP地理位置配置
 */
export async function getGeoIpConfig(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/geo_ip/config`);
}

/**
 * 保存IP地理位置配置
 *
 * @param params
 */
export async function saveGeoIpConfig(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/geo_ip/save`, {
    data: { ...params },
    method: 'POST',
  });
}

/**
 * 关闭地理位置服务启用/禁用
 *
 */
export async function disableGeoIp(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/geo_ip/disable`, {
    method: 'PUT',
  });
}
