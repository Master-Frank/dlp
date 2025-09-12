/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { FieldNames, ServerExceptionStatus } from '../constant';
import { changeEmail, prepareChangeEmail } from '../service';
import type { CaptFieldRef, ProFormInstance } from '@ant-design/pro-components';
import { ModalForm, ProFormCaptcha, ProFormText } from '@ant-design/pro-components';
import { App, Spin } from 'antd';
import { omit } from 'lodash';
import { useEffect, useRef, useState } from 'react';
import { useIntl } from '@@/exports';

export default (props: {
  visible: boolean;
  prefixCls: string;
  setVisible: (visible: boolean) => void;
  setRefresh: (visible: boolean) => void;
}) => {
  const intl = useIntl();
  const useApp = App.useApp();
  const { visible, setVisible, setRefresh } = props;
  const [loading, setLoading] = useState<boolean>(false);
  /**已发送验证码*/
  const [hasSendCaptcha, setHasSendCaptcha] = useState<boolean>(false);
  const captchaRef = useRef<CaptFieldRef>();
  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    setLoading(true);
    setLoading(false);
  }, [visible]);

  return (
    <>
      <ModalForm
        title={intl.formatMessage({ id: 'page.user.profile.bind.totp.form.update_email' })}
        width={'560px'}
        formRef={formRef}
        labelAlign={'right'}
        preserve={false}
        layout={'horizontal'}
        labelCol={{
          span: 4,
        }}
        wrapperCol={{
          span: 20,
        }}
        autoFocusFirstInput
        open={visible}
        modalProps={{
          destroyOnClose: true,
          maskClosable: false,
          onCancel: async () => {
            setVisible(false);
            setHasSendCaptcha(false);
          },
        }}
        onFinish={async (formData: Record<string, any>) => {
          if (!hasSendCaptcha) {
            useApp.message.error(
              intl.formatMessage({ id: 'page.user.profile.please_send_code.message' }),
            );
            return Promise.reject();
          }
          const { success } = await changeEmail(omit(formData, FieldNames.PASSWORD));
          if (success) {
            useApp.message.success(intl.formatMessage({ id: 'app.update_success' }));
            setVisible(false);
            setRefresh(true);
            setHasSendCaptcha(false);
            return Promise.resolve();
          }
          return Promise.reject();
        }}
      >
        <Spin spinning={loading}>
          <ProFormText.Password
            name={FieldNames.PASSWORD}
            label={intl.formatMessage({ id: 'page.user.profile.common.form.password' })}
            placeholder={intl.formatMessage({
              id: 'page.user.profile.common.form.password.placeholder',
            })}
            fieldProps={{ autoComplete: 'off' }}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'page.user.profile.common.form.password.rule.0',
                }),
              },
            ]}
          />
          <ProFormCaptcha
            name={FieldNames.EMAIL}
            placeholder={intl.formatMessage({
              id: 'page.user.profile.modify_email.form.email.placeholder',
            })}
            label={intl.formatMessage({ id: 'page.user.profile.modify_email.form.email' })}
            fieldRef={captchaRef}
            phoneName={FieldNames.EMAIL}
            fieldProps={{ autoComplete: 'off' }}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'page.user.profile.modify_email.form.email.rule.0',
                }),
              },
              {
                type: 'email',
                message: intl.formatMessage({
                  id: 'page.user.profile.modify_email.form.email.rule.1',
                }),
              },
            ]}
            onGetCaptcha={async (email) => {
              if (!(await formRef.current?.validateFields([FieldNames.PASSWORD]))) {
                return Promise.reject();
              }
              const { success, message, result, status } = await prepareChangeEmail({
                email: email,
                password: formRef.current?.getFieldValue(FieldNames.PASSWORD),
              });
              if (!success && status === ServerExceptionStatus.PASSWORD_VALIDATED_FAIL_ERROR) {
                formRef.current?.setFields([{ name: FieldNames.PASSWORD, errors: [`${message}`] }]);
                return Promise.reject();
              }
              if (success && result) {
                setHasSendCaptcha(true);
                useApp.message.success(intl.formatMessage({ id: 'app.send_successfully' }));
                return Promise.resolve();
              }
              useApp.message.error(message);
              captchaRef.current?.endTiming();
              return Promise.reject();
            }}
          />
          <ProFormText
            label={intl.formatMessage({ id: 'page.user.profile.common.form.code' })}
            placeholder={intl.formatMessage({
              id: 'page.user.profile.common.form.code.placeholder',
            })}
            name={FieldNames.OTP}
            fieldProps={{ autoComplete: 'off' }}
            rules={[
              {
                required: true,
                message: intl.formatMessage({ id: 'page.user.profile.common.form.code.rule.0' }),
              },
            ]}
          />
        </Spin>
      </ModalForm>
    </>
  );
};
