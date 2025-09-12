/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { ProFormText } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

/**
 * 七牛
 */
const QiNiu = () => {
  const intl = useIntl();
  return (
    <>
      <ProFormText
        name="accessKey"
        label="AccessKey"
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.qi_niu.access_key.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.qi_niu.access_key.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        name="secretKey"
        label="SecretKey"
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.qi_niu.secret_key.rule.0.message',
            }),
          },
        ]}
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.qi_niu.secret_key.placeholder',
        })}
        fieldProps={{
          autoComplete: 'new-password',
          iconRender: (value) => {
            return value ? <EyeTwoTone /> : <EyeInvisibleOutlined />;
          },
        }}
      />
    </>
  );
};

export default QiNiu;
