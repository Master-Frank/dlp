/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
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
}
