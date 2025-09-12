/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { ProFormText } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

export default () => {
  const intl = useIntl();
  return (
    <>
      <ProFormText
        name={['config', 'accessKey']}
        label="AccessKey"
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.minio.access_key.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.minio.access_key.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        name={['config', 'secretKey']}
        label="SecretKey"
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.minio.secret_key.rule.0.message',
            }),
          },
        ]}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.minio.secret_key.placeholder',
        })}
        fieldProps={{
          autoComplete: 'new-password',
          iconRender: (value) => {
            return value ? <EyeTwoTone /> : <EyeInvisibleOutlined />;
          },
        }}
      />
      <ProFormText
        name={['config', 'domain']}
        label={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.minio.domain`,
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.minio.domain.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.minio.domain.rule.0.message',
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'off',
        }}
      />
      <ProFormText
        name={['config', 'endpoint']}
        label={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.minio.endpoint`,
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.minio.endpoint.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.minio.endpoint.rule.0.message',
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'off',
        }}
      />
      <ProFormText
        name={['config', 'bucket']}
        label={'Bucket'}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.minio.bucket.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.minio.bucket.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};
