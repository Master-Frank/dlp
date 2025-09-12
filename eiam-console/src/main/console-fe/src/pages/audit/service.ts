/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import type { RequestData } from '@ant-design/pro-components';
import type { SortOrder } from 'antd/es/table/interface';
import { request } from '@umijs/max';
import { AuditList, AuditTypeGroup } from './data.d';

/**
 * 获取审计列表
 */
export async function getAuditList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AuditList>> {
  return request<API.ApiResult<AuditList>>(`/api/v1/audit/list`, {
    params: {
      ...params,
      startEventTime: params.eventTime && params.eventTime[0],
      endEventTime: params.eventTime && params.eventTime[1],
      ...sortParamConverter(sort),
      ...filterParamConverter(filter),
    },
  }).then((result: API.ApiResult<AuditList>) => {
    const data: RequestData<AuditList> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 查询审计字典
 */
export async function getAuditTypes(userType: string): Promise<API.ApiResult<AuditTypeGroup>> {
  return request(`/api/v1/audit/types/${userType}`);
}
