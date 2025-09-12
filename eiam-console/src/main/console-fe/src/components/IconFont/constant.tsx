/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import IconFont from '@/components/IconFont';
import type { ReactNode } from 'react';

/**
 * ICON
 */
export const ICON_LIST = {
  password: <IconFont name="icon-password" />,
  dingtalk: <IconFont name="icon-dingtalk" />,
  feishu: <IconFont name="icon-feishu" />,
  wechat_work: <IconFont name="icon-qiyeweixin" />,
  wechat_oauth: <IconFont name="icon-weixin" />,
  dingtalk_oauth: <IconFont name="icon-dingtalk" />,
  wechatwork_oauth: <IconFont name="icon-qiyeweixin" />,
  feishu_oauth: <IconFont name="icon-feishu" />,
  qq_oauth: <IconFont name="icon-qq" />,
  gitee_oauth: <IconFont name="icon-gitee" />,
  weibo_oauth: <IconFont name="icon-weibo" />,
  github_oauth: <IconFont name="icon-github" />,
  google_oauth: <IconFont name="icon-google" />,
  alipay_oauth: <IconFont name="icon-alipay" />,
  app_jwt: <IconFont name="icon-jwt" />,
  oidc: <IconFont name="icon-openid" />,
  app_form: <IconFont name="icon-form" />,
} as Record<string, ReactNode>;
