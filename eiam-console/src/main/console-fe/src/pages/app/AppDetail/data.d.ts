/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 应用信息
 */
export type GetApp = {
  id: string;
  type: string;
  name: string;
  icon: string;
  template: string;
  protocol: string;
  protocolName: string;
  clientId: string;
  clientSecret: string;
  //sso登录链接
  initLoginUrl: string;
  nameIdValueType: string;
  //授权范围
  authorizationType: string;
  enabled: boolean;
  remark: string;
  groupIds: string[];
};
