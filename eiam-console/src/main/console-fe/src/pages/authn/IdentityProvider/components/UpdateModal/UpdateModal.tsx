/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getIdentityProvider } from '../../service';
import {
  ModalForm,
  ProFormDependency,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Skeleton, Spin } from 'antd';
import { useForm } from 'antd/es/form/Form';
import { useState } from 'react';
import { DRAWER_FORM_ITEM_LAYOUT } from '../../constant';
import Config from '../Config';
import { useIntl } from '@umijs/max';

export type CreateDrawerProps = {
  visible?: boolean;
  id: string;
  onCancel: () => void;
  afterClose: () => void;
  onFinish: (values: Record<string, string>) => Promise<void>;
};
export default (props: CreateDrawerProps) => {
  const { visible, id, onCancel, onFinish, afterClose } = props;
  const [form] = useForm();
  const [loading, setLoading] = useState(false);
  const [updateLoading, setUpdateLoading] = useState<boolean>(false);
  const intl = useIntl();

  useAsyncEffect(async () => {
    if (visible) {
      setLoading(true);
      const { success, result } = await getIdentityProvider(id);
      if (success && result) {
        form?.setFieldsValue({ ...result });
        setLoading(false);
      }
    }
  }, [visible, id]);

  return (
    <ModalForm
      title={intl.formatMessage({
        id: 'pages.authn.identity_provider.update_modal_title',
      })}
      width={580}
      layout={'horizontal'}
      {...DRAWER_FORM_ITEM_LAYOUT}
      modalProps={{
        forceRender: true,
        onCancel: () => {
          onCancel();
          form?.resetFields();
        },
        destroyOnClose: true,
        afterClose: () => {
          if (afterClose) {
            afterClose();
          }
        },
      }}
      form={form}
      scrollToFirstError
      onFinish={async (values: Record<string, string>) => {
        setUpdateLoading(true);
        await onFinish(values).finally(() => {
          setUpdateLoading(false);
        });
      }}
      open={visible}
    >
      <Skeleton loading={loading} active={true}>
        <Spin spinning={updateLoading}>
          <ProFormText name={'id'} hidden />
          <ProFormText name={'type'} hidden />
          <ProFormText
            label={intl.formatMessage({
              id: 'pages.authn.identity_provider.create_modal.form.name',
            })}
            name="name"
            placeholder={intl.formatMessage({
              id: 'pages.authn.identity_provider.create_modal.form.name.placeholder',
            })}
            rules={[
              {
                message: intl.formatMessage({
                  id: 'pages.authn.identity_provider.create_modal.form.name.rule.0.message',
                }),
                required: true,
              },
            ]}
            fieldProps={{
              autoComplete: 'off',
            }}
          />
          <ProFormDependency name={['type']}>
            {({ type }) => {
              return <Config type={type} isCreate={false} />;
            }}
          </ProFormDependency>
          <ProFormTextArea
            name="remark"
            fieldProps={{ rows: 2, maxLength: 20, showCount: false }}
            placeholder={intl.formatMessage({
              id: 'pages.authn.identity_provider.create_modal.form.remark.placeholder',
            })}
            label={intl.formatMessage({
              id: 'pages.authn.identity_provider.create_modal.form.remark',
            })}
          />
        </Spin>
      </Skeleton>
    </ModalForm>
  );
};
