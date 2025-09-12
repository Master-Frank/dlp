/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { history } from '@@/core/history';
import { parse } from 'querystring';

export const goto = (jump?: boolean) => {
  if (!history) return;
  const query = parse(history.location.search);
  if (!jump) {
    const { redirect_uri } = query as { redirect_uri: string };
    window.location.replace(redirect_uri || '/');
    return;
  }
  //跳转
  window.location.replace('/api/v1/jump');
};
