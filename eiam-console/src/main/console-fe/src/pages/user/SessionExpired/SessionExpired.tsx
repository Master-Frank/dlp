/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { LOGIN_PATH } from '@/utils/utils';
import { history } from '@@/core/history';
import { useIntl, useLocation, useModel } from '@umijs/max';
import { useMount } from 'ahooks';
import { App } from 'antd';
import queryString from 'query-string';

export default () => {
  const { setInitialState } = useModel('@@initialState');
  const location = useLocation();
  const intl = useIntl();
  const { modal } = App.useApp();
  useMount(async () => {
    modal.warning({
      title: intl.formatMessage({ id: 'pages.session-expired.title' }),
      content: intl.formatMessage({ id: 'pages.session-expired.content' }),
      okText: intl.formatMessage({ id: 'pages.session-expired.okText' }),
      okType: 'danger',
      centered: false,
      maskClosable: false,
      okCancel: false,
      onOk: async () => {
        await setInitialState((s: any) => ({ ...s, currentUser: undefined }));
        const query = queryString.parse(location.search);
        const { redirect_uri } = query as { redirect_uri: string };
        let settings: Record<string, string> = { pathname: LOGIN_PATH };
        const domain: string[] | string = redirect_uri && redirect_uri.split('/');
        if (redirect_uri && redirect_uri !== domain[0] + '//' + domain[2] + '/') {
          settings = {
            ...settings,
            search: queryString.stringify({
              redirect_uri: redirect_uri,
            }),
          };
        }
        const href = history.createHref(settings);
        window.location.replace(href);
      },
    });
  });
  return <></>;
};
