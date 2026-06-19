/*
 * ulp-portal - United Login Platform
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
export enum RESULT_STATE {
  //数字签名错误
  EX900005 = 'EX900005',
  //验证码错误
  EX000102 = 'EX000102',
  //成功
  SUCCESS = '200',
}

/**
 * IDP_TYPE
 */
export enum IDP_TYPE {
  ACCOUNT = 'account',
  SMS = 'sms',
}

export enum SESSION_STATUS {
  require_bind_idp = 'require_bind_idp',
  mfa_required = 'mfa_required',
  mfa_setup_required = 'mfa_setup_required',
}

/**
 * 字段名称
 */
export enum FieldNames {
  /**密码 */
  PASSWORD = 'password',
  /**OTP */
  OTP = 'otp',
  /**手机号 */
  PHONE = 'phone',
  /**邮箱 */
  EMAIL = 'email',
  /**新密码 */
  NEW_PASSWORD = 'newPassword',
  /**验证码*/
  VERIFY_CODE = 'verifyCode',
  CHANNEL = 'channel',
}
