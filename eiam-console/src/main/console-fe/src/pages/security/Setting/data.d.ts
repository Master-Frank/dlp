/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
export type SecurityDefensePolicyConfig = {
  contentSecurityPolicy: string;
};

/**
 * 安全基础配置
 */
export type BasicSettingConfig = {
  frequentRegisterCheck: boolean;
  emailVerifiedDefault: boolean;
  sendWelcomeEmail: boolean;
  verifyOldEmail: boolean;
  verifyOldPhone: boolean;
};
