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
        name={['config', 'endpoint']}
        label={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.endpoint`,
        })}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.endpoint.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.endpoint.rule.0.message`,
            }),
          },
        ]}
      />
      <ProFormText
        name={['config', 'domain']}
        label={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.domain`,
        })}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.domain.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.domain.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'accessKeyId']}
        label="AccessKeyId"
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.access_key_id.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.access_key_id.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'new-password',
        }}
      />
      <ProFormText.Password
        name={['config', 'secretAccessKey']}
        label="SecretAccessKey"
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.secret_access_key.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.secret_access_key.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'bucket']}
        label={'Bucket'}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.bucket.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.bucket.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'region']}
        label={'Region'}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.region.placeholder`,
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};
