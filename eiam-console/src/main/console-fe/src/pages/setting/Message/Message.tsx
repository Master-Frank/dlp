/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { history } from '@@/core/history';
import { PageContainer } from '@ant-design/pro-components';
import React, { useEffect, useState } from 'react';
import MailProvider from '../Message/components/MailProvider';
import MailTemplate from '../Message/components/MailTemplate';
import SmsProvider from '../Message/components/SmsProvider';
import { TabType } from './constant';
import queryString from 'query-string';
import { useLocation } from '@umijs/max';
import { useIntl } from '@@/exports';

export default (): React.ReactNode => {
  const [activeKey, setActiveKey] = useState<string>();
  const location = useLocation();
  const intl = useIntl();
  useEffect(() => {
    const query = queryString.parse(location.search);
    const { type } = query as {
      type: TabType;
    };
    if (!type || !TabType[type]) {
      setActiveKey(TabType.mail_template);
      history.replace({
        pathname: location.pathname,
        search: queryString.stringify({ type: TabType.mail_template }),
      });
      return;
    }
    setActiveKey(type);
  }, [activeKey]);
  return (
    <PageContainer
      content={intl.formatMessage({ id: 'pages.setting.message.desc' })}
      tabActiveKey={activeKey}
      onTabChange={(key: string) => {
        setActiveKey(key);
        history.replace({
          pathname: location.pathname,
          search: queryString.stringify({ type: key }),
        });
      }}
      tabList={[
        {
          tab: intl.formatMessage({ id: 'pages.setting.message.sms_template' }),
          key: TabType.mail_template,
        },
        {
          tab: intl.formatMessage({ id: 'pages.setting.message.mail' }),
          key: TabType.mail,
        },
        {
          tab: intl.formatMessage({ id: 'pages.setting.message.sms' }),
          key: TabType.sms,
        },
      ]}
    >
      {/*邮件*/}
      {activeKey === TabType.mail && <MailProvider visible={activeKey === TabType.mail} />}
      {/*邮件模版*/}
      {activeKey === TabType.mail_template && (
        <MailTemplate visible={activeKey === TabType.mail_template} />
      )}
      {/*短信*/}
      {activeKey === TabType.sms && <SmsProvider visible={activeKey === TabType.sms} />}
    </PageContainer>
  );
};
