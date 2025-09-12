/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import type { RequestData } from '@ant-design/pro-components';
import type { SortOrder } from 'antd/es/table/interface';
import { request } from '@umijs/max';
import { AppGroupList, AppList } from './data.d';

/**
 * 获取应用列表
 */
export async function queryAppList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AppList>> {
  const { result, success } = await request<API.ApiResult<AppList>>('/api/v1/app/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  });
  return {
    data: result?.list ? result?.list : [],
    success: success,
    total: result?.pagination ? result?.pagination.total : 0,
  };
}

/**
 * 获取应用分组
 */
export async function getAppGroupList(): Promise<API.ApiResult<AppGroupList>> {
  return request<API.ApiResult<any>>('/api/v1/app/group_list', {});
}
