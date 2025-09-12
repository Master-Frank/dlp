/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProFormText } from '@ant-design/pro-components';
import { Typography } from 'antd';
import { useIntl } from '@umijs/max';

const { Paragraph } = Typography;

export default () => {
  const intl = useIntl();

  return (
    <ProFormText
      label={intl.formatMessage({
        id: 'pages.authn.identity_provider.config.callback_url',
      })}
      name={'redirectUri'}
      proFieldProps={{
        render: (value: string) => {
          return (
            value && (
              <Paragraph copyable={{ text: value }} style={{ marginBottom: '0' }}>
                <span
                  dangerouslySetInnerHTML={{
                    __html: `<span>${value}</span>`,
                  }}
                />
              </Paragraph>
            )
          );
        },
      }}
      readonly
      fieldProps={{ autoComplete: 'off' }}
    />
  );
};
