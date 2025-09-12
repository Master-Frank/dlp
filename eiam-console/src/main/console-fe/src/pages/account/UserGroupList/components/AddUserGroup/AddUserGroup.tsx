/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { random } from '@/utils/utils';
import { ModalForm, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { Spin } from 'antd';
import { useForm } from 'antd/es/form/Form';
import * as React from 'react';
import { useState } from 'react';
import { useIntl } from '@umijs/max';
import { useAsyncEffect } from 'ahooks';

const layout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 20 },
};
export default (props: {
  visible: boolean;
  onFinish: (formData: AccountAPI.CreateUserGroup) => Promise<boolean | void>;
  onCancel: (e: React.MouseEvent<HTMLElement>) => void;
}) => {
  const [form] = useForm();
  const { visible, onFinish, onCancel } = props;
  const [loading, setLoading] = useState<boolean>(false);
  const intl = useIntl();

  useAsyncEffect(async () => {
    if (visible) {
      form.setFieldsValue({ code: random(9) });
    }
  }, [visible]);

  return (
    <ModalForm<AccountAPI.CreateUserGroup>
      title={intl.formatMessage({ id: 'pages.account.user_group_list.form.title' })}
      form={form}
      scrollToFirstError
      {...layout}
      layout={'horizontal'}
      labelAlign={'right'}
      preserve={false}
      width="500px"
      open={visible}
      modalProps={{
        maskClosable: true,
        destroyOnClose: true,
        onCancel: onCancel,
      }}
      onFinish={async (values: AccountAPI.BaseUserGroup) => {
        setLoading(true);
        await onFinish(values).finally(() => {
          setLoading(false);
        });
      }}
    >
      <Spin spinning={loading}>
        <ProFormText
          name="name"
          label={intl.formatMessage({ id: 'pages.account.user_group_list.form.name' })}
          placeholder={intl.formatMessage({
            id: 'pages.account.user_group_list.form.name.placeholder',
          })}
          fieldProps={{
            maxLength: 8,
          }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.account.user_group_list.form.name.rule.0.message',
              }),
            },
          ]}
        />
        <ProFormText
          name="code"
          label={intl.formatMessage({ id: 'pages.account.user_group_list.form.code' })}
          placeholder={intl.formatMessage({
            id: 'pages.account.user_group_list.form.code.placeholder',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.account.user_group_list.form.code.rule.0.message',
              }),
            },
            {
              pattern: new RegExp('^[A-Za-z0-9_-]*$'),
              message: intl.formatMessage({
                id: 'pages.account.user_group_list.form.code.rule.1.message',
              }),
            },
          ]}
        />
        <ProFormTextArea
          name="remark"
          fieldProps={{ rows: 2, maxLength: 20, showCount: true }}
          placeholder={intl.formatMessage({
            id: 'pages.account.user_group_list.form.remark.placeholder',
          })}
          label={intl.formatMessage({ id: 'pages.account.user_group_list.form.remark' })}
        />
      </Spin>
    </ModalForm>
  );
};
