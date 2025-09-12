/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { ProFormText } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

/**
 * 阿里云
 */
const AliCloud = () => {
  const intl = useIntl();
  return (
    <>
      <ProFormText
        name="accessKeyId"
        label="AccessKey ID"
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.aliyun.access_key_id.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.aliyun.access_key_id.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        name="accessKeySecret"
        label="AccessKey Secret"
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.aliyun.access_key_secret.rule.0.message',
            }),
          },
        ]}
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.aliyun.access_key_secret.placeholder',
        })}
        fieldProps={{
          autoComplete: 'new-password',
          iconRender: (value) => {
            return value ? <EyeTwoTone /> : <EyeInvisibleOutlined />;
          },
        }}
      />
      <ProFormText
        name="signName"
        label={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.aliyun.sign_name',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.aliyun.sign_name.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.aliyun.sign_name.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};

export default AliCloud;
