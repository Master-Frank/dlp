/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 服务异常状态
 */
export enum ServerExceptionStatus {
  /**密码验证失败错误 */
  PASSWORD_VALIDATED_FAIL_ERROR = 'password_validated_fail_error',
  /**无效的 MFA 代码错误 */
  INVALID_MFA_CODE_ERROR = 'invalid_mfa_code_error',
  /**MFA 未发现秘密错误 */
  BIND_MFA_NOT_FOUND_SECRET_ERROR = 'bind_mfa_not_found_secret_error',
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
  /**旧密码 */
  OLD_PASSWORD = 'oldPassword',
  /**新密码 */
  NEW_PASSWORD = 'newPassword',
  /**验证码*/
  VERIFY_CODE = 'verifyCode',
  CHANNEL = 'channel',
}
