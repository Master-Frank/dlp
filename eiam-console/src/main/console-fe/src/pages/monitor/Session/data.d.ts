/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
export interface GeoLocation {
  ip: string;
  countryName: string;
  countryCode: string;
  provinceName: string;
  provinceCode: string;
  cityName: string;
  cityCode: string;
}

export interface UserAgent {
  browser: string;
  browserType: string;
  browserMajorVersion: string;
  deviceType: string;
  platform: string;
  platformVersion: string;
}

/**
 * SessionList
 */
export type SessionList = {
  id: string;
  username: string;
  geoLocation: GeoLocation;
  userAgent: UserAgent;
  loginTime: string;
  lastRequest: string;
  sessionId: string;
};
