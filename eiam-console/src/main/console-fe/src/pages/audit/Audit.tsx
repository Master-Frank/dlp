/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { PageContainer } from '@ant-design/pro-components';
import { UserType } from './data.d';
import Admin from './Admin';
import { useState } from 'react';
import User from './User';
import { useIntl } from '@umijs/max';

export default () => {
  const intl = useIntl();
  const [tabActiveKey, setTabActiveKey] = useState<string>(UserType.user);
  return (
    <PageContainer
      tabActiveKey={tabActiveKey}
      onTabChange={(key) => {
        setTabActiveKey(key);
      }}
      tabList={[
        {
          key: UserType.user,
          tab: intl.formatMessage({ id: 'pages.audit.user' }),
        },
        {
          key: UserType.admin,
          tab: intl.formatMessage({ id: 'pages.audit.admin' }),
        },
      ]}
      content={intl.formatMessage({ id: 'pages.audit.desc' })}
    >
      {tabActiveKey === UserType.user && <User />}
      {tabActiveKey === UserType.admin && <Admin />}
    </PageContainer>
  );
};
