/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * Category
 */
export enum IdentityProviderCategory {
  social = 'social',
  enterprise = 'enterprise',
}

/**
 * 认证源提供商平台
 */
export enum IdentityProviderType {
  //企业
  wechatwork_oauth = 'wechatwork_oauth',
  feishu_oauth = 'feishu_oauth',
  dingtalk_oauth = 'dingtalk_oauth',
  //社交
  qq_oauth = 'qq_oauth',
  gitee_oauth = 'gitee_oauth',
  wechat_qr = 'wechat_oauth',
  github_oauth = 'github_oauth',
  alipay_oauth = 'alipay_oauth',
}

/**
 * 是否回调的提供商
 */
export const EXIST_CALLBACK = [
  IdentityProviderType.wechatwork_oauth,
  IdentityProviderType.dingtalk_oauth,
  IdentityProviderType.wechat_qr,
  IdentityProviderType.qq_oauth,
  IdentityProviderType.feishu_oauth,
  IdentityProviderType.feishu_oauth,
  IdentityProviderType.gitee_oauth,
  IdentityProviderType.github_oauth,
];

export const DRAWER_FORM_ITEM_LAYOUT = {
  labelCol: {
    span: 5,
  },
  wrapperCol: {
    span: 19,
  },
};
