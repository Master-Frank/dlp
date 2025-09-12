/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { FormattedMessage, useIntl } from '@@/plugin-locale/localeExports';
import { LockTwoTone, UserOutlined } from '@ant-design/icons';
import { ProFormText } from '@ant-design/pro-components';
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ token }) => {
  return {
    main: {
      ['.icon']: {
        color: token.colorPrimary,
        fontSize: token.fontSize,
      },
    },
  };
});
export default () => {
  const intl = useIntl();
  const { styles } = useStyle();
  return (
    <div className={styles.main}>
      <ProFormText
        name="username"
        fieldProps={{
          size: 'large',
          prefix: <UserOutlined className={'icon'} />,
          autoComplete: 'off',
        }}
        placeholder={intl.formatMessage({
          id: 'pages.login.username.placeholder',
        })}
        rules={[
          {
            required: true,
            message: <FormattedMessage id="pages.login.username.required" />,
          },
        ]}
      />
      <ProFormText.Password
        name="password"
        fieldProps={{
          size: 'large',
          autoComplete: 'off',
          prefix: <LockTwoTone className={'icon'} />,
        }}
        placeholder={intl.formatMessage({
          id: 'pages.login.password.placeholder',
        })}
        rules={[
          {
            required: true,
            message: <FormattedMessage id="pages.login.password.required" />,
          },
        ]}
      />
    </div>
  );
};
