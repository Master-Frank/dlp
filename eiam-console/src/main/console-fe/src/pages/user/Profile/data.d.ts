/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 账户信息
 */
export type AccountInfo = {
  /** 用户ID */
  id: string;
  avatar: string;
  username: string;
  phone: string;
  access: string;
};

export interface GetBoundIdpList {
  code: string;
  name: string;
  type: string;
  category: string;
  bound: boolean;
  idpId: string;
}

/**
 * 账户菜单类型
 */
export enum AccountSettingsStateKey {
  base = 'base',
  security = 'security',
}
