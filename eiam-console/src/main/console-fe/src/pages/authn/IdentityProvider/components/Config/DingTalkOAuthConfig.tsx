/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProFormText } from '@ant-design/pro-components';
import CallbackUrl from './CallbackUrl';
import { useIntl } from '@umijs/max';

/**
 * 钉钉Oauth登录
 *
 * @constructor
 */
const DingTalkOauth = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'appKey']}
        label="AppKey"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.ding_talk_oauth.app_id.extra',
        })}
        fieldProps={{ autoComplete: 'off' }}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.ding_talk_oauth.app_id.placeholder',
        })}
      />
      <ProFormText.Password
        rules={[{ required: true }]}
        name={['config', 'appSecret']}
        label="AppSecret"
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.ding_talk_oauth.app_id.extra',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.ding_talk_oauth.app_secret.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};
export default DingTalkOauth;
