/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { request } from '@@/plugin-request/request';
import type { RequestData } from '@ant-design/pro-components';
import { SessionList } from './data.d';

/**
 * 获取在线用户列表
 */
export async function getSessionList(
  params: Record<string, string>,
): Promise<RequestData<SessionList>> {
  return request<API.ApiResult<SessionList>>('/api/v1/session/list', {
    method: 'GET',
    params,
  }).then((result) => {
    const data: RequestData<SessionList> = {
      data: result?.result ? result?.result : [],
      success: result?.success,
    };
    return Promise.resolve(data);
  });
}

/**
 * 下线服务
 */
export async function removeSessions(sessionIds: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>('/api/v1/session/remove', {
    method: 'DELETE',
    params: { sessionIds },
  });
}
