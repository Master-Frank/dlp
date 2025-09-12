/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import type { ProFormInstance } from '@ant-design/pro-components';
import { ProFormText } from '@ant-design/pro-components';
import { App, Button } from 'antd';
import type { RefObject } from 'react';
import React, { useCallback, useEffect, useImperativeHandle } from 'react';
import { BASIC_CONFIG_FROM_PARAM } from '../../constant';
import type { BasicConfigInstance } from '../BasicConfig';
import { useIntl } from '@umijs/max';
import { IdentitySourceProvider } from '@/constant';

export interface DingTalkConfigProps {
  configured: boolean;
  onConfigValidator: (config: Record<string, string>) => Promise<boolean> | void;
  basicConfigRef: RefObject<BasicConfigInstance | undefined>;
  formRef?: React.RefObject<ProFormInstance<Record<string, string>> | undefined>;
}
const DingTalkConfig = (props: DingTalkConfigProps) => {
  const { configured, onConfigValidator, formRef, basicConfigRef } = props;
  useEffect(() => {}, [configured]);
  const intl = useIntl();
  const { message } = App.useApp();

  /**
   * 验证连接
   */
  const configValidator = useCallback(async (): Promise<boolean> => {
    const current = formRef?.current;
    try {
      // 判断参数是否存在
      await current?.validateFields([
        BASIC_CONFIG_FROM_PARAM.corpId,
        BASIC_CONFIG_FROM_PARAM.appKey,
        BASIC_CONFIG_FROM_PARAM.appSecret,
      ]);
      //调用接口验证
      const result = await onConfigValidator({
        provider: IdentitySourceProvider.dingtalk,
        corpId: current?.getFieldValue(BASIC_CONFIG_FROM_PARAM.corpId),
        appKey: current?.getFieldValue(BASIC_CONFIG_FROM_PARAM.appKey),
        appSecret: current?.getFieldValue(BASIC_CONFIG_FROM_PARAM.appSecret),
      });
      if (!result) {
        current?.setFields([
          {
            name: BASIC_CONFIG_FROM_PARAM.corpId,
            errors: [
              intl.formatMessage({ id: 'pages.account.identity_source_detail.common.test_fail' }),
            ],
          },
        ]);
        current?.setFields([
          {
            name: BASIC_CONFIG_FROM_PARAM.appKey,
            errors: [
              intl.formatMessage({ id: 'pages.account.identity_source_detail.common.test_fail' }),
            ],
          },
        ]);
        current?.setFields([
          {
            name: BASIC_CONFIG_FROM_PARAM.appSecret,
            errors: [
              intl.formatMessage({ id: 'pages.account.identity_source_detail.common.test_fail' }),
            ],
          },
        ]);
        return false;
      }
      message.success(
        intl.formatMessage({ id: 'pages.account.identity_source_detail.common.test_succeed' }),
      );
      return true;
    } catch (e) {
      return false;
    }
  }, [formRef, onConfigValidator]);

  useImperativeHandle(basicConfigRef, () => {
    return { configValidator: configValidator };
  }, [configValidator]);
  return (
    <>
      <ProFormText
        name={BASIC_CONFIG_FROM_PARAM.corpId}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.corp_id',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.corp_id.placeholder',
        })}
        extra={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.corp_id.extra',
        })}
      />
      <ProFormText
        name={BASIC_CONFIG_FROM_PARAM.appKey}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.app_key',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.app_key.placeholder',
        })}
        extra={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.app_key.extra',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.account.identity_source_detail.basic_config.ding_talk.app_key.rule.0.message',
            }),
          },
        ]}
      />
      <ProFormText.Password
        name={BASIC_CONFIG_FROM_PARAM.appSecret}
        label={intl.formatMessage({ id: 'pages.account.identity_source_detail.common.app_secret' })}
        placeholder={intl.formatMessage({
          id: 'pages.account.identity_source_detail.common.app_secret.placeholder',
        })}
        extra={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.app_secret.extra',
        })}
        fieldProps={{ autoComplete: 'new-password' }}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.account.identity_source_detail.common.app_secret.rule.0.message',
            }),
          },
        ]}
        addonWarpStyle={{
          flexWrap: 'nowrap',
        }}
        addonAfter={
          <Button type={'default'} onClick={configValidator}>
            {intl.formatMessage({ id: 'pages.account.identity_source_detail.common.test_connect' })}
          </Button>
        }
      />
      {/*回调事件*/}
      <ProFormText.Password
        name={['basicConfig', 'aesKey']}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.aes_key',
        })}
        extra={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.aes_key.extra',
        })}
        fieldProps={{ autoComplete: 'new-password' }}
      />
      <ProFormText.Password
        name={['basicConfig', 'token']}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.token',
        })}
        extra={intl.formatMessage({
          id: 'pages.account.identity_source_detail.basic_config.ding_talk.token.extra',
        })}
        fieldProps={{ autoComplete: 'new-password' }}
      />
    </>
  );
};
export default DingTalkConfig;
