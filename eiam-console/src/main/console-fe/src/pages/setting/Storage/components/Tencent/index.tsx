/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProFormText } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

export default () => {
  const intl = useIntl();
  return (
    <>
      <ProFormText
        name={['config', 'domain']}
        label={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.domain',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.domain.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.domain.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'appId']}
        label="AppId"
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.app_id.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.app_id.rule.0.message',
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'off',
        }}
      />
      <ProFormText
        name={['config', 'secretId']}
        label="SecretId"
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.secret_id.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.secret_id.rule.0.message',
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'new-password',
        }}
      />
      <ProFormText.Password
        name={['config', 'secretKey']}
        label="SecretKey"
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.secret_key.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.secret_key.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'region']}
        label={'Region'}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.region.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.region.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'bucket']}
        label={'Bucket'}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.bucket.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.bucket.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};
