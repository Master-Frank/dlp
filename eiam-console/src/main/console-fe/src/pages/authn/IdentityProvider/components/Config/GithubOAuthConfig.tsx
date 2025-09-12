/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProFormText } from '@ant-design/pro-components';
import CallbackUrl from './CallbackUrl';
import { useIntl } from '@umijs/max';

/**
 * Github Oauth 登录
 *
 * @constructor
 */
const QqOauthConfig = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'clientId']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.github_oauth.client_id',
        })}
        rules={[{ required: true }]}
        fieldProps={{ autoComplete: 'off' }}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.github_oauth.client_id.placeholder',
        })}
      />
      <ProFormText.Password
        rules={[{ required: true }]}
        name={['config', 'clientSecret']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.github_oauth.client_secret',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.github_oauth.client_secret.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};
export default QqOauthConfig;
