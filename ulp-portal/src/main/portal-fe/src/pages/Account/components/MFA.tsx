/*
 * ulp-portal - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { history, useIntl, useModel } from '@umijs/max';
import { useAsyncEffect } from 'ahooks';
import { Alert, Form, Input, List, Modal, Skeleton, Tag, message } from 'antd';
import { useState } from 'react';
import { unbind } from '../../MFA/service';

const MfaView = () => {
  const intl = useIntl();
  const { initialState, setInitialState } = useModel('@@initialState');
  const [loading, setLoading] = useState<boolean>(true);
  const [refresh, setRefresh] = useState<boolean>(false);
  const [unbindOpen, setUnbindOpen] = useState<boolean>(false);
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [form] = Form.useForm<{ otp: string }>();

  useAsyncEffect(async () => {
    if (initialState?.currentUser) {
      setLoading(false);
    }
  }, [initialState]);

  useAsyncEffect(async () => {
    if (refresh) {
      setLoading(true);
      const currentUser = await initialState?.fetchUserInfo?.();
      await setInitialState((s: any) => ({ ...s, currentUser }));
      setRefresh(false);
      setLoading(false);
    }
  }, [refresh]);

  const bound = !!initialState?.currentUser?.totpBind;

  const onBindClick = () => {
    history.push('/mfa/setup');
  };

  const onUnbindOk = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      const otp = (values.otp || '').replace(/\s+/g, '');
      const { success, status, message: msg } = await unbind(otp);
      if (success) {
        message.success(intl.formatMessage({ id: 'pages.account.mfa.unbind.success' }));
        setUnbindOpen(false);
        form.resetFields();
        setRefresh(true);
        return;
      }
      if (status === 'unbind_blocked_by_org_policy') {
        message.error(intl.formatMessage({ id: 'pages.account.mfa.unbind.blocked_by_org' }));
        return;
      }
      if (status === 'locked_out') {
        message.error(intl.formatMessage({ id: 'pages.mfa.locked_out' }));
        return;
      }
      message.error(msg || intl.formatMessage({ id: 'pages.mfa.challenge.invalid_otp' }));
    } finally {
      setSubmitting(false);
    }
  };

  const data = [
    {
      title: intl.formatMessage({ id: 'pages.account.menu.mfa' }),
      description: bound
        ? intl.formatMessage({ id: 'pages.account.mfa.status.enabled' })
        : intl.formatMessage({ id: 'pages.account.mfa.status.disabled' }),
      tag: bound ? (
        <Tag color="success">
          {intl.formatMessage({ id: 'pages.account.mfa.status.enabled' })}
        </Tag>
      ) : (
        <Tag>{intl.formatMessage({ id: 'pages.account.mfa.status.disabled' })}</Tag>
      ),
      actions: [
        bound ? (
          <a key="unbind" onClick={() => setUnbindOpen(true)}>
            {intl.formatMessage({ id: 'pages.account.mfa.unbind' })}
          </a>
        ) : (
          <a key="bind" onClick={onBindClick}>
            {intl.formatMessage({ id: 'pages.account.mfa.bind' })}
          </a>
        ),
      ],
    },
  ];

  return (
    <Skeleton loading={loading} paragraph={{ rows: 4 }}>
      <Alert
        type="info"
        showIcon
        message={intl.formatMessage({ id: 'pages.account.mfa.desc' })}
        style={{ marginBottom: 16 }}
      />
      <List
        itemLayout="horizontal"
        dataSource={data}
        renderItem={(item) => (
          <List.Item actions={item.actions}>
            <List.Item.Meta
              title={
                <span>
                  {item.title} {item.tag}
                </span>
              }
              description={item.description}
            />
          </List.Item>
        )}
      />
      <Modal
        title={intl.formatMessage({ id: 'pages.account.mfa.unbind' })}
        open={unbindOpen}
        confirmLoading={submitting}
        onOk={onUnbindOk}
        onCancel={() => {
          setUnbindOpen(false);
          form.resetFields();
        }}
        okButtonProps={{ danger: true }}
      >
        <Alert
          type="warning"
          showIcon
          message={intl.formatMessage({ id: 'pages.account.mfa.unbind.confirm' })}
          style={{ marginBottom: 16 }}
        />
        <Form form={form} layout="vertical">
          <Form.Item
            name="otp"
            label={intl.formatMessage({ id: 'pages.account.mfa.unbind.otp.placeholder' })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({ id: 'pages.mfa.setup.otp.required' }),
              },
              {
                pattern: /^\d{6}$/,
                message: intl.formatMessage({ id: 'pages.mfa.setup.otp.format' }),
              },
            ]}
          >
            <Input
              autoFocus
              autoComplete="one-time-code"
              maxLength={6}
              inputMode="numeric"
              placeholder="123456"
            />
          </Form.Item>
        </Form>
      </Modal>
    </Skeleton>
  );
};

export default MfaView;
