/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import {
  CaptFieldRef,
  ProForm,
  ProFormCaptcha,
  ProFormProps,
  ProFormText,
} from '@ant-design/pro-components';
import { LockOutlined, MobileOutlined } from '@ant-design/icons';
import { FormattedMessage } from '@@/exports';
import { App, Typography } from 'antd';
import { useIntl } from '@umijs/max';
import { forgetPasswordCode } from '@/pages/Login/service';
import Title from '@/components/Title';
import { useRef } from 'react';
import { phoneIsValidNumber, phoneParseNumber } from '@/utils/utils';

const { Paragraph } = Typography;
const Code = (props: ProFormProps) => {
  const intl = useIntl();
  const { message } = App.useApp();
  const captchaRef = useRef<CaptFieldRef>();
  const getSms = async (phone: string) => {
    if (phone) {
      captchaRef.current?.startTiming();
      const result = await forgetPasswordCode(phone);
      if (result.success) {
        message.success(intl.formatMessage({ id: 'pages.login.phone.get-opt-code.success' }));
        return;
      }
      setTimeout(async () => {
        message.error(result.message);
        captchaRef.current?.endTiming();
      }, 20);
      return;
    }
  };

  return (
    <>
      <Title size={'h1'} title={intl.formatMessage({ id: 'pages.login.forget-password' })} />
      <Paragraph>{intl.formatMessage({ id: 'pages.login.forget-password.desc' })}</Paragraph>
      <ProForm {...props}>
        <ProFormText
          fieldProps={{
            size: 'large',
            prefix: <MobileOutlined />,
            maxLength: 14,
          }}
          name="phone"
          placeholder={intl.formatMessage({
            id: 'pages.login.forget-password.phone.placeholder',
          })}
          validateTrigger={'onBlur'}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.forget-password.phone.required" />,
            },
            {
              validateTrigger: ['onBlur'],
              validator: async (_rule, value) => {
                if (!value) {
                  return Promise.resolve();
                }
                //校验是否为邮箱
                const emailReg = new RegExp(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/);
                const isEmail = emailReg.test(value);
                if (isEmail) {
                  return;
                }
                //解析手机号和区号并校验
                const phoneNumber = phoneParseNumber(value);
                const nationalNumber = phoneNumber.getNationalNumber();
                const countryCode = phoneNumber.getCountryCode();
                let isPhone: boolean = false;
                if (nationalNumber && countryCode) {
                  isPhone = await phoneIsValidNumber(
                    nationalNumber.toString(),
                    countryCode.toString(),
                  );
                }
                if (isPhone) {
                  return;
                }
                return Promise.reject<Error>(
                  new Error(
                    intl.formatMessage({
                      id: 'pages.login.recipient.invalid',
                    }),
                  ),
                );
              },
              message: <FormattedMessage id="pages.login.recipient.invalid" />,
            },
          ]}
        />
        <ProFormCaptcha
          fieldRef={captchaRef}
          fieldProps={{
            size: 'large',
            prefix: <LockOutlined className={'icon'} />,
          }}
          captchaProps={{
            size: 'large',
          }}
          placeholder={intl.formatMessage({
            id: 'pages.login.captcha.placeholder',
          })}
          captchaTextRender={(timing, count) => {
            if (timing) {
              return `${count} ${intl.formatMessage({
                id: 'pages.login.phone.captcha-second-text',
              })}`;
            }
            return intl.formatMessage({
              id: 'pages.login.phone.get-opt-code',
            });
          }}
          name="code"
          phoneName={'phone'}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.captcha.required" />,
            },
          ]}
          onGetCaptcha={getSms}
        />
      </ProForm>
    </>
  );
};
export default Code;
