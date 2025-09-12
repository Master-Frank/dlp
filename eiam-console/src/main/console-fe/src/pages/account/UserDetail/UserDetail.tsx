/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { history } from '@@/core/history';

import { PageContainer } from '@ant-design/pro-components';
import { useMount } from 'ahooks';
import { App, Button } from 'antd';
import React, { useState } from 'react';
import LoginAudit from './components/LoginAudit';
import UserInfo from './components/UserInfo';
import { UserDetailTabs } from './constant';
import queryString from 'query-string';
import { useLocation } from '@umijs/max';
import { useIntl } from '@@/exports';
import { ExclamationCircleFilled } from '@ant-design/icons';
import { removeUser } from '@/services/account';

export default () => {
  const location = useLocation();
  const intl = useIntl();
  const useApp = App.useApp();
  const query = queryString.parse(location.search);
  const { id } = query as { id: string };
  const { type } = query as {
    type: UserDetailTabs;
  };
  const [tabActiveKey, setTabActiveKey] = useState<string>();

  const goUserList = () => {
    history.push(`/account/user`);
  };

  useMount(() => {
    if (!id) {
      useApp.message
        .warning(intl.formatMessage({ id: 'pages.account.user_detail.user_info.not_selected' }))
        .then();
      goUserList();
      return;
    }
    if (!type || !UserDetailTabs[type]) {
      setTabActiveKey(UserDetailTabs.user_info);
      history.replace({
        pathname: location.pathname,
        search: queryString.stringify({ type: UserDetailTabs.user_info, id: id }),
      });
      return;
    }
    setTabActiveKey(type);
  });

  return (
    <PageContainer
      onBack={() => {
        goUserList();
      }}
      tabList={[
        {
          key: UserDetailTabs.user_info,
          tab: intl.formatMessage({ id: 'pages.account.user_detail.tabs.user_info' }),
        },
        {
          key: UserDetailTabs.login_audit,
          tab: intl.formatMessage({ id: 'pages.account.user_detail.tabs.login_audit' }),
        },
      ]}
      extra={[
        <Button
          key="delete"
          type="primary"
          danger
          onClick={() => {
            const confirmed = useApp.modal.error({
              centered: true,
              title: intl.formatMessage({
                id: 'pages.account.user_detail.extra.delete.confirm_title',
              }),
              icon: <ExclamationCircleFilled />,
              content: intl.formatMessage({
                id: 'pages.account.user_detail.extra.delete.confirm_content',
              }),
              okText: intl.formatMessage({ id: 'app.confirm' }),
              okType: 'danger',
              okCancel: true,
              cancelText: intl.formatMessage({ id: 'app.cancel' }),
              onOk: async () => {
                const { success } = await removeUser(id);
                if (success) {
                  useApp.message.success(intl.formatMessage({ id: 'app.operation_success' }));
                  confirmed.destroy();
                  goUserList();
                }
              },
            });
          }}
        >
          {intl.formatMessage({ id: 'pages.account.user_detail.extra.delete' })}
        </Button>,
      ]}
      tabActiveKey={tabActiveKey}
      onTabChange={(key: string) => {
        setTabActiveKey(key);
        history.replace({
          pathname: location.pathname,
          search: queryString.stringify({ type: key, id: id }),
        });
      }}
    >
      {/*用户信息*/}
      {UserDetailTabs.user_info === tabActiveKey && <UserInfo userId={id} />}
      {/*登录日志*/}
      {UserDetailTabs.login_audit === tabActiveKey && <LoginAudit userId={id} />}
    </PageContainer>
  );
};
