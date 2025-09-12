/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 认证源
 */
export type ListIdentityProvider = {
  id: string;
  name: string;
  desc: string;
  remark: string;
  type: any;
  enabled: boolean;
  displayed: boolean;
};

/**
 * 认证源详情
 */
export type GetIdentityProvider = {
  type: string;
  redirectUri: string;
  config: any;
};
