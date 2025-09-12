/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProFormText } from '@ant-design/pro-components';
import CallbackUrl from './CallbackUrl';
import { useIntl } from '@umijs/max';

/**
 * 微信扫码登录
 *
 * @constructor
 */
const WeChatScanCode = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'appId']}
        label="AppId"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_id.extra',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_id.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        name={['config', 'appSecret']}
        label="AppSecret"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_secret.extra',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_secret.placeholder',
        })}
        fieldProps={{ autoComplete: 'new-password' }}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};
export default WeChatScanCode;
