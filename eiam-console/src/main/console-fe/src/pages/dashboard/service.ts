/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@@/exports';

/**
 * 获取概述
 */
export async function getAnalysisOverview(): Promise<API.ApiResult<DashboardAPI.OverviewResult>> {
  return request(`/api/v1/analysis/overview`);
}

/**
 * 获取认证数量
 */
export async function getAuthnQuantity(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AuthnQuantityResult>> {
  return request(`/api/v1/analysis/authn/quantity`, {
    params: { startTime, endTime },
  });
}

/**
 * 获取热点提供商
 */
export async function getAuthnHotProvider(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AuthnHotProviderResult>> {
  return request(`/api/v1/analysis/authn/hot_provider`, {
    params: { startTime, endTime },
  });
}

/**
 * 获取登录区域
 */
export async function getAuthnZone(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AuthnQuantityResult>> {
  return request(`/api/v1/analysis/authn/zone`, {
    params: { startTime, endTime },
  });
}

/**
 * 获取应用访问排名
 */
export async function getAppVisitRank(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AppVisitsRank>> {
  return request(`/api/v1/analysis/app/visit_rank`, {
    params: { startTime, endTime },
  });
}
