/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { PasswordPolicyConfig, WeakPasswordLib } from './data.d';
import { request } from '@umijs/max';

/**
 * 查询系统弱密码库
 */
export async function getWeakPasswordLib(): Promise<API.ApiResult<WeakPasswordLib[]>> {
  return request('/api/v1/setting/security/password_policy/weak_password_lib');
}

/**
 * 获取密码策略配置
 */
export async function getPasswordPolicyConfig(): Promise<API.ApiResult<PasswordPolicyConfig>> {
  return request('/api/v1/setting/security/password_policy/config');
}

/**
 * 保存密码策略配置
 */
export async function savePasswordPolicyConfig(
  params: Record<string, any>,
): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/setting/security/password_policy/save', {
    method: 'POST',
    data: { ...params },
    requestType: 'form',
  });
}
