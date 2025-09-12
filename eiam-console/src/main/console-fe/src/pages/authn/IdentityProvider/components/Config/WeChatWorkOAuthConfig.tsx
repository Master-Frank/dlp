/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProFormText } from '@ant-design/pro-components';
import { Input } from 'antd';
import CallbackUrl from './CallbackUrl';
import { useIntl } from '@umijs/max';

/**
 * 企业微信认证
 *
 * @constructor
 */
const WeWorkScanCode = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'corpId']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.corp_id',
        })}
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.corp_id.extra',
        })}
      >
        <Input
          autoComplete="off"
          placeholder={intl.formatMessage({
            id: 'pages.authn.identity_provider.config.wework_scan_code.corp_id.placeholder',
          })}
        />
      </ProFormText>
      <ProFormText
        name={['config', 'agentId']}
        label="AgentId"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.agent_id.extra',
        })}
      >
        <Input
          autoComplete="off"
          placeholder={intl.formatMessage({
            id: 'pages.authn.identity_provider.config.wework_scan_code.agent_id.placeholder',
          })}
        />
      </ProFormText>
      <ProFormText.Password
        name={['config', 'appSecret']}
        label="Secret"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.app_secret.extra',
        })}
        fieldProps={{ autoComplete: 'new-password' }}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.app_secret.placeholder',
        })}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};

export default WeWorkScanCode;
