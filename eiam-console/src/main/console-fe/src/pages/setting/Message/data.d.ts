/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 邮件列表
 */
export type EmailTemplateList = {
  /** 名称 */
  name: string;
  /** 编码 */
  code: string;
  /** 内容 */
  content: string;
  /** 自定义*/
  custom: boolean;
  /** 描述 */
  description: string;
};

/**
 * 邮件详情
 */
export type GetEmailTemplate = {
  /** 名称 */
  name: string;
  /** 模板 */
  theme: string;
  /** 内容 */
  content: string;
  /** 描述 */
  desc: string;
  /** 自定义 */
  custom: boolean;
};

/**
 * 短信列表
 */
export type SmsTemplateList = {
  /** 名称 */
  name: string;
  /** 类型 */
  type: string;
  /** 内容 */
  content: string;
};
