/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
export enum UserType {
  admin = 'admin',
  user = 'user',
}

export enum EventStatus {
  success = 'success',
  fail = 'fail',
}

/**
 * 审计列表
 */
export interface AuditList {
  id: string;
  username: string;
  context: string;
  targets: Record<string, any>;
  userId: string;
  userAgent: Record<string, string>;
  geoLocation: Record<string, string>;
  eventType: string;
  userType: 'admin' | 'user';
  eventResult: string;
  eventTime: string;
  eventStatus: 'success' | 'fail';
}

/**
 * 审计类型分组
 */
export interface AuditTypeGroup {
  name: string;
  code: string;
  types: AuditType[];
}

/**
 * 审计类型
 */
export interface AuditType {
  name: string;
  code: string;
}
