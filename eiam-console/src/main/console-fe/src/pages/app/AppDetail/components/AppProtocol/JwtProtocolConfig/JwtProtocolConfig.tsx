/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getAppConfig, saveAppConfig } from '../../../service';
import { useAsyncEffect } from 'ahooks';
import { App, Form, Spin } from 'antd';
import React, { useState } from 'react';
import {
  FooterToolbar,
  ProCard,
  ProForm,
  ProFormDigit,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import ConfigAbout from './ConfigAbout';
import { omit } from 'lodash';
import { useIntl } from '@umijs/max';
import { GetApp } from '../../../data.d';
import Alert from '@/components/Alert';

const layout = {
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 14,
  },
};
export default (props: { app: GetApp | Record<string, any> }) => {
  const { app } = props;
  const { id, template } = app;
  const [form] = Form.useForm();
  const [loading, setLoading] = useState<boolean>(true);
  const [protocolEndpoint, setProtocolEndpoint] = useState<Record<string, string>>({});
  const intl = useIntl();
  const { message } = App.useApp();

  const getConfig = async () => {
    setLoading(true);
    const { result, success } = await getAppConfig(id);
    if (success && result) {
      form.setFieldsValue({ ...omit(result, 'protocolEndpoint'), appId: id });
      //设置Endpoint相关
      setProtocolEndpoint(result.protocolEndpoint);
    }
    setLoading(false);
  };

  useAsyncEffect(async () => {
    await getConfig();
  }, []);

  return (
    <Spin spinning={loading}>
      <ProForm
        layout={'horizontal'}
        labelAlign={'right'}
        {...layout}
        form={form}
        scrollToFirstError
        onFinish={async (values) => {
          setLoading(true);
          const { success } = await saveAppConfig({
            id,
            template,
            config: omit(values, 'id', 'template'),
          }).finally(() => {
            setLoading(false);
          });
          if (success) {
            message.success(intl.formatMessage({ id: 'app.save_success' }));
            await getConfig();
            return true;
          }
          message.error(intl.formatMessage({ id: 'app.save_fail' }));
          return false;
        }}
        submitter={{
          render: (_, dom) => {
            return <FooterToolbar>{dom.map((item) => item)}</FooterToolbar>;
          },
        }}
      >
        <ProCard bordered>
          <Alert
            showIcon={true}
            banner
            type={'grey'}
            message={
              <span>
                {intl.formatMessage({ id: 'app.issue' })}
                {intl.formatMessage({ id: 'app.disposition' })}{' '}
                <a
                  target={'_blank'}
                  href={'https://eiam.topiam.cn/docs/portal/jwt/overview'}
                  rel="noreferrer"
                >
                  {intl.formatMessage({
                    id: 'pages.app.config.detail.protocol_config.jwt',
                  })}
                </a>{' '}
                。
              </span>
            }
          />
          <br />
          <ProFormText
            label={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.redirect_url',
            })}
            name={'redirectUrl'}
            fieldProps={{ allowClear: false }}
            extra={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.redirect_url.extra',
            })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.redirect_url.rule.0.message',
                }),
              },
              {
                type: 'url',
                message: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.redirect_url.rule.1.message',
                }),
              },
            ]}
          />
          <ProFormText
            label={'target link url'}
            name={'targetLinkUrl'}
            fieldProps={{ allowClear: false }}
            extra={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.target_link_url.extra',
            })}
            rules={[
              {
                type: 'url',
                message: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.target_link_url.rule.0.message',
                }),
              },
            ]}
          />

          <ProFormRadio.Group
            name={'bindingType'}
            label={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.binding_type',
            })}
            initialValue={['post']}
            extra={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.binding_type.extra',
            })}
            options={[
              { value: 'post', label: 'POST' },
              { value: 'get', label: 'GET' },
            ]}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.binding_type.rule.0.message',
                }),
              },
            ]}
          />
          <ProFormSelect
            label={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.idtoken_subject_type',
            })}
            name={'idTokenSubjectType'}
            options={[
              {
                value: 'user_id',
                label: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.idtoken_subject_type.option.0',
                }),
              },
              {
                value: 'app_user',
                label: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.idtoken_subject_type.option.1',
                }),
              },
            ]}
            fieldProps={{ allowClear: false }}
            extra={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.idtoken_subject_type.extra',
            })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.idtoken_subject_type.rule.0.message',
                }),
              },
            ]}
          />
          <ProFormDigit
            name={'idTokenTimeToLive'}
            label={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.idtoken_time_to_live',
            })}
            addonAfter={'秒'}
            max={84600}
            min={1}
            extra={intl.formatMessage({
              id: 'pages.app.config.detail.protocol_config.jwt.idtoken_time_to_live.extra',
            })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.idtoken_time_to_live.rule.0.message',
                }),
              },
            ]}
          />
        </ProCard>
      </ProForm>
      <br />
      <ProCard
        title={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.jwt.config_about',
        })}
        bordered
        collapsible
        headerBordered
      >
        <ConfigAbout appId={app.id} protocolEndpoint={protocolEndpoint} {...layout} />
      </ProCard>
    </Spin>
  );
};
