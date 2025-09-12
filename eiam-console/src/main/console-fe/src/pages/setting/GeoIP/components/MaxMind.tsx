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
      <ProFormText.Password
        name={['config', 'sessionKey']}
        label={intl.formatMessage({ id: 'pages.setting.geoip.maxmind.form_text.label' })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.geoip.maxmind.form_text.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.geoip.maxmind.form_text.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'new-password' }}
      />
    </>
  );
};
