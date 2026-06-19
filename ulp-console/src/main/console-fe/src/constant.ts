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
/**
 * 提供商
 */
export enum IdentitySourceProvider {
  dingtalk = 'dingtalk',
  feishu = 'feishu',
}
export enum RESULT_STATE {
  //数字签名错误
  EX900005 = 'EX900005',
  //成功
  SUCCESS = '200',
}

/**
 * 短信提供商
 */
export enum SMS_PROVIDER {
  ALIYUN = 'aliyun',
  QI_NIU = 'qiniu',
  TENCENT = 'tencent',
}

export enum GEO_IP_PROVIDER {
  MAXMIND = 'maxmind',
  DEFAULT = 'default',
}

export enum EMAIL_PROVIDER {
  CUSTOMIZE = 'customize',
  ALIYUN = 'aliyun',
  TENCENT = 'tencent',
  NET_EASE = 'netease',
}

/**
 * App Protocol
 */
export enum AppProtocolType {
  oidc = 'OIDC',
  jwt = 'JWT',
  form = 'FORM',
}

export enum PolicyEffectType {
  ALLOW = 'ALLOW',
  DENY = 'DENY',
}

export enum AccessPolicyType {
  ROLE = 'ROLE',
  RESOURCE = 'RESOURCE',
  USER = 'USER',
  ORGANIZATION = 'ORGANIZATION',
  USER_GROUP = 'USER_GROUP',
}

export enum ParamCheckType {
  PHONE = 'PHONE',
  NAME = 'NAME',
  USERNAME = 'USERNAME',
  EMAIL = 'EMAIL',
  CODE = 'CODE',
}

export enum SESSION_STATUS {
  REQUIRE_RESET_PASSWORD = 'require_reset_password',
  MFA_REQUIRED = 'mfa_required',
  MFA_SETUP_REQUIRED = 'mfa_setup_required',
}
