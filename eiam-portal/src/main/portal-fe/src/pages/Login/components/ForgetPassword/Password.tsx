/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProForm, ProFormProps, ProFormText } from '@ant-design/pro-components';
import { LockTwoTone } from '@ant-design/icons';
import { FormattedMessage, useIntl } from '@@/exports';
import useStyle from './style';
import Title from '@/components/Title';
import { Typography } from 'antd';

const prefixCls = 'topiam-forget-password';
const { Paragraph } = Typography;

type PasswordProps = {
  phone: string;
};
const Password = ({ phone, ...rest }: PasswordProps & ProFormProps) => {
  const intl = useIntl();
  const { styles } = useStyle(prefixCls);
  return (
    <div className={styles.main}>
      <Title size={'h1'} title={intl.formatMessage({ id: 'pages.login.forget-password' })} />
      <Paragraph>{`${intl.formatMessage(
        { id: 'pages.login.forget-password.password.desc' },
        { phone: phone },
      )}`}</Paragraph>
      <ProForm {...rest}>
        <ProFormText.Password
          name="newPassword"
          fieldProps={{
            size: 'large',
            autoComplete: 'off',
            prefix: <LockTwoTone className={'icon'} />,
          }}
          placeholder={intl.formatMessage({
            id: 'pages.login.forget-password.password.new',
          })}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.password.required" />,
            },
          ]}
        />
        <ProFormText.Password
          name="passwordAgain"
          fieldProps={{
            size: 'large',
            autoComplete: 'off',
            prefix: <LockTwoTone className={'icon'} />,
          }}
          placeholder={intl.formatMessage({
            id: 'pages.login.forget-password.password.confirm',
          })}
          validateTrigger={'onBlur'}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.password.required" />,
            },
            {
              validator: (_, value, callback) => {
                const password = rest?.formRef?.current?.getFieldValue('newPassword');
                if (password && password !== value) {
                  callback(
                    intl.formatMessage({
                      id: 'pages.login.forget-password.inconsistency',
                    }),
                  );
                }
                callback();
              },
              message: <FormattedMessage id="pages.login.forget-password.inconsistency" />,
            },
          ]}
        />
      </ProForm>
    </div>
  );
};
export default Password;
