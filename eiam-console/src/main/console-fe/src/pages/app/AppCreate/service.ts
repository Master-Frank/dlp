/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@umijs/max';
import { ListTemplate } from './data.d';
import { RequestData } from '@ant-design/pro-components';
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import { SortOrder } from 'antd/es/table/interface';

/**
 * 获取应用模板列表
 */
export async function getAppTemplateList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<ListTemplate>> {
  return request<API.ApiResult<ListTemplate>>('/api/v1/app/template/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<ListTemplate>) => {
    const data: RequestData<ListTemplate> = {
      data: result ? result?.result : [],
      success: result?.success,
    };
    return Promise.resolve(data);
  });
}

/**
 * Create Application
 */
export async function createApp(
  params: Record<string, string>,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/create`, {
    method: 'POST',
    data: params,
    requestType: 'json',
  });
}
