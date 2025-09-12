/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { PageContainer } from '@ant-design/pro-components';
import React, { useState } from 'react';
import { useIntl } from '@umijs/max';
import { TabType } from './constant';
import DefensePolicy from './components/DefensePolicy';
import Basic from './components/Basic';

export default () => {
  const [tabActiveKey, setTabActiveKey] = useState<string>(TabType.basic);
  const intl = useIntl();

  return (
    <PageContainer
      tabActiveKey={tabActiveKey}
      onTabChange={(key) => {
        setTabActiveKey(key);
      }}
      tabList={[
        {
          key: TabType.basic,
          tab: intl.formatMessage({ id: 'pages.setting.basic_setting' }),
        },
        {
          key: TabType.defense_policy,
          tab: intl.formatMessage({ id: 'pages.setting.security.defense_policy' }),
        },
      ]}
    >
      {tabActiveKey === TabType.basic && <Basic />}
      {tabActiveKey === TabType.defense_policy && <DefensePolicy />}
    </PageContainer>
  );
};
