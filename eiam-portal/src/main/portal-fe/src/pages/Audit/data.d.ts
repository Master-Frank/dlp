/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 审计列表
 */
export type AuditList = {
  id: string;
  username: string;
  context: string;
  targets: Record<string, any>;
  userId: string;
  userAgent: Record<string, string>;
  geoLocation: Record<string, string>;
  eventType: string;
  eventResult: string;
  eventTime: string;
  eventStatus: 'success' | 'fail';
};
/**
 * 审计类型分组
 */
export type AuditTypeGroup = {
  name: string;
  code: string;
  types: AuditType[];
};

/**
 * 审计类型
 */
export type AuditType = {
  name: string;
  code: string;
};

export enum UserType {
  admin = 'admin',
  user = 'user',
}

export enum EventStatus {
  success = 'success',
  fail = 'fail',
}
