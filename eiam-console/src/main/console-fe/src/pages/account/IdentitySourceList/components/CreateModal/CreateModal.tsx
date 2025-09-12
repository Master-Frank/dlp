/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import React, { useRef, useState } from 'react';
import { ModalForm, ProFormSelect, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { Avatar, FormInstance, Space, Spin } from 'antd';
import { useIntl } from '@umijs/max';
import { IdentitySourceProvider } from '@/constant';
import { ICON_LIST } from '@/components/IconFont/constant';

type CreateModelProps = {
  visible: boolean;
  onClose: () => void;
  onFinish: (formData: Record<string, string>) => Promise<boolean | void>;
};
export default (props: CreateModelProps) => {
  const form = useRef<FormInstance>();
  const intl = useIntl();
  const { visible, onClose, onFinish } = props;
  const [loading, setLoading] = useState(false);
  return (
    <>
      <ModalForm
        title={intl.formatMessage({ id: 'pages.account.identity_source_list.create_modal' })}
        width="500px"
        layout={'horizontal'}
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 20 }}
        onFinish={async (values: Record<string, string>) => {
          setLoading(true);
          await onFinish(values).finally(() => {
            setLoading(false);
          });
        }}
        modalProps={{
          destroyOnClose: true,
          onCancel: () => {
            form.current?.resetFields();
            onClose();
          },
        }}
        open={visible}
      >
        <Spin spinning={loading}>
          <ProFormSelect
            name="provider"
            label={intl.formatMessage({
              id: 'pages.account.identity_source_list.create_modal.provider',
            })}
            fieldProps={{
              placeholder: intl.formatMessage({
                id: 'pages.account.identity_source_list.create_modal.provider.placeholder',
              }),
              optionRender: ({ data }) => {
                return (
                  <Space>
                    <Avatar shape={'square'} src={data?.icon} />
                    {data.label}
                  </Space>
                );
              },
            }}
            options={[
              {
                value: IdentitySourceProvider.dingtalk,
                label: intl.formatMessage({
                  id: 'pages.account.identity_source_list.create_modal.provider.placeholder.options.dingtalk',
                }),
                icon: ICON_LIST[IdentitySourceProvider.dingtalk],
              },
              {
                value: IdentitySourceProvider.feishu,
                label: intl.formatMessage({
                  id: 'pages.account.identity_source_list.create_modal.provider.placeholder.options.feishu',
                }),
                icon: ICON_LIST[IdentitySourceProvider.feishu],
              },
            ]}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.account.identity_source_list.create_modal.provider.placeholder.rule.0.message',
                }),
              },
            ]}
          />
          <ProFormText
            name="name"
            label={intl.formatMessage({
              id: 'pages.account.identity_source_list.create_modal.name',
            })}
            placeholder={intl.formatMessage({
              id: 'pages.account.identity_source_list.create_modal.name.placeholder',
            })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.account.identity_source_list.create_modal.name.rule.0.message',
                }),
              },
            ]}
          />
          <ProFormTextArea
            name="remark"
            label={intl.formatMessage({
              id: 'pages.account.identity_source_list.create_modal.remark',
            })}
            placeholder={intl.formatMessage({
              id: 'pages.account.identity_source_list.create_modal.remark.placeholder',
            })}
            fieldProps={{ rows: 2, maxLength: 20 }}
          />
        </Spin>
      </ModalForm>
    </>
  );
};
