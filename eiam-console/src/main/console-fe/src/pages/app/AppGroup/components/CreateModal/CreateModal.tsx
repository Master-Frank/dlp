/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ModalForm, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { Form, Spin } from 'antd';
import React, { useState } from 'react';
import { useIntl } from '@@/exports';
import { random } from '@/utils/utils';
import { useAsyncEffect } from 'ahooks';

export default (props: {
  open: boolean;
  onFinish: (formData: Record<string, string>) => Promise<boolean | void>;
  onCancel: (e: React.MouseEvent<HTMLButtonElement>) => void;
}) => {
  const { open, onCancel, onFinish } = props;
  const [form] = Form.useForm();
  const intl = useIntl();
  const [loading, setLoading] = useState<boolean>(false);

  useAsyncEffect(async () => {
    if (open) {
      form.setFieldsValue({ code: random(9) });
    }
  }, [open]);

  return (
    <ModalForm
      title={intl.formatMessage({ id: 'pages.app_group.create.modal_form.title' })}
      form={form}
      open={open}
      labelCol={{ span: 4 }}
      wrapperCol={{ span: 20 }}
      width={'500px'}
      layout={'horizontal'}
      labelAlign={'right'}
      preserve={false}
      autoFocusFirstInput
      modalProps={{
        maskClosable: true,
        destroyOnClose: true,
        onCancel: onCancel,
      }}
      onFinish={async (values: Record<string, string>) => {
        setLoading(true);
        await onFinish(values).finally(() => {
          setLoading(false);
        });
      }}
    >
      <Spin spinning={loading}>
        <ProFormText
          label={intl.formatMessage({ id: 'pages.app_group.modal_form.name' })}
          name="name"
          placeholder={intl.formatMessage({ id: 'pages.app_group.modal_form.name.placeholder' })}
          fieldProps={{
            maxLength: 8,
          }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.app_group.modal_form.name.rule.0.message',
              }),
            },
          ]}
        />
        <ProFormText
          label={intl.formatMessage({ id: 'pages.app_group.modal_form.code' })}
          placeholder={intl.formatMessage({ id: 'pages.app_group.modal_form.code.placeholder' })}
          name="code"
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.app_group.modal_form.code.rule.0.message',
              }),
            },
          ]}
        />
        <ProFormTextArea
          label={intl.formatMessage({ id: 'pages.app_group.modal_form.remark' })}
          name="remark"
          fieldProps={{
            placeholder: intl.formatMessage({
              id: 'pages.app_group.modal_form.remark.placeholder',
            }),
            rows: 2,
            maxLength: 20,
            autoComplete: 'off',
            showCount: true,
          }}
        />
      </Spin>
    </ModalForm>
  );
};
