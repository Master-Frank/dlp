/*
 * ulp-console - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { request } from '@umijs/max';

/** 组织 MFA 强制策略响应 */
export type OrgMfaPolicyResult = {
  orgId: string;
  mfaEnforced: boolean;
  changed: boolean;
};

/**
 * 切换组织 MFA 强制策略
 */
export async function setOrgMfaPolicy(
  orgId: string,
  mfaEnforced: boolean,
): Promise<API.ApiResult<OrgMfaPolicyResult>> {
  return request<API.ApiResult<OrgMfaPolicyResult>>(
    `/api/v1/admin/organizations/${orgId}/mfa-policy`,
    {
      method: 'POST',
      data: { mfaEnforced },
      skipErrorHandler: true,
    },
  ).catch(({ response: { data } }) => data);
}

/**
 * 管理员重置普通用户的 MFA 绑定
 */
export async function resetUserMfa(userId: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/admin/users/${userId}/reset-mfa`, {
    method: 'POST',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => data);
}

/**
 * 重置管理员的 MFA 绑定
 */
export async function resetAdminMfa(adminId: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/admin/administrators/${adminId}/reset-mfa`, {
    method: 'POST',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => data);
}
