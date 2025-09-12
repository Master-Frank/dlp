/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@umijs/max';
import { BasicSettingConfig, SecurityDefensePolicyConfig } from './data.d';

/**
 * 获取基础配置
 */
export async function getBasicSettingConfig(): Promise<API.ApiResult<BasicSettingConfig>> {
  return request('/api/v1/setting/security/basic/config');
}

/**
 * 保存基础配置
 */
export async function saveBasicSettingConfig(
  params: BasicSettingConfig,
): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/setting/security/basic/save', {
    method: 'POST',
    data: { ...params },
    requestType: 'form',
  });
}

/**
 * 获取内容安全策略配置
 */
export async function getSecurityDefensePolicyConfig(): Promise<
  API.ApiResult<SecurityDefensePolicyConfig>
> {
  return request('/api/v1/setting/security/defense_policy/config');
}

/**
 * 保存内容安全策略配置
 */
export async function saveSecurityDefensePolicyConfig(
  params: SecurityDefensePolicyConfig,
): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/setting/security/defense_policy/save', {
    method: 'POST',
    data: { ...params },
    requestType: 'form',
  });
}
