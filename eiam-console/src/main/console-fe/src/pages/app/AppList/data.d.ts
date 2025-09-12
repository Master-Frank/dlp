/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 应用列表
 */
export type AppList = {
  id: string;
  name: string;
  icon?: string;
  protocol: string;
  enabled: boolean;
  type: string;
  template: string;
  remark: string;
};
