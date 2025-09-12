/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
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
