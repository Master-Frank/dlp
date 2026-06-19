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
import type {
  ChallengeResult,
  ConfirmBindResult,
  PrepareBindResult,
} from './data.d';

export async function prepareBind(): Promise<API.ApiResult<PrepareBindResult>> {
  return request<API.ApiResult<PrepareBindResult>>('/api/v1/mfa/bind/prepare', {
    method: 'POST',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => data);
}

export async function confirmBind(otp: string): Promise<API.ApiResult<ConfirmBindResult>> {
  return request<API.ApiResult<ConfirmBindResult>>('/api/v1/mfa/bind/confirm', {
    method: 'POST',
    data: { otp },
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => data);
}

export async function unbind(currentOtp: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>('/api/v1/mfa/unbind', {
    method: 'POST',
    data: { currentOtp },
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => data);
}

export async function challenge(code: string): Promise<API.ApiResult<ChallengeResult>> {
  return request<API.ApiResult<ChallengeResult>>('/api/v1/mfa/challenge', {
    method: 'POST',
    data: { code },
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => data);
}

export async function challengeWithBackupCode(
  backupCode: string,
): Promise<API.ApiResult<ChallengeResult>> {
  return request<API.ApiResult<ChallengeResult>>('/api/v1/mfa/challenge', {
    method: 'POST',
    data: { backupCode },
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => data);
}
