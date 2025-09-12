/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * ConfigTabs
 */
export enum ConfigTabs {
  //应用配置
  app_config = 'app_config',
  //协议
  protocol_config = 'protocol_config',
  //访问授权
  access_policy = 'access_policy',
  //登录访问
  login_access = 'login_access',
  //应用账户
  app_account = 'app_account',
}

/**
 * SSO 授权范围
 */
export enum SsoScope {
  //手动授权
  AUTHORIZATION = 'authorization',
  //全员可访问
  ALL_ACCESS = 'all_access',
}

/**
 * 证书使用类型
 */
export enum CertUsingType {
  JWT_ENCRYPT = 'jwt_encrypt',
}

/**
 * 表单加密类型
 */
export enum FormEncryptType {
  /**aes*/
  aes = 'aes',
  /**base64*/
  base64 = 'base64',
  /**md5*/
  md5 = 'md5',
}
