/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProFormText } from '@ant-design/pro-components';
import CallbackUrl from './CallbackUrl';
import { useIntl } from '@umijs/max';

/**
 * AliPay Oauth 登录
 *
 * @constructor
 */
const QqOauthConfig = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'appId']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_id',
        })}
        rules={[{ required: true }]}
        fieldProps={{ autoComplete: 'off' }}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_id.placeholder',
        })}
      />
      <ProFormText.Password
        rules={[{ required: true }]}
        name={['config', 'appPrivateKey']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_private_key',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_private_key.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        rules={[{ required: true }]}
        name={['config', 'alipayPublicKey']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.alipay_public_key',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.alipay_public_key.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};
export default QqOauthConfig;
