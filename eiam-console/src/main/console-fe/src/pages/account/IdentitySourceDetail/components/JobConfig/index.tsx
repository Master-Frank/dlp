/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import {
  ProCard,
  ProFormCheckbox,
  ProFormDependency,
  ProFormDigit,
  ProFormRadio,
  ProFormTimePicker,
} from '@ant-design/pro-components';
import { Alert, Divider } from 'antd';
import { useEffect } from 'react';
import { JobMode } from '../../constant';
import { useIntl } from '@umijs/max';

export default (props: { configured: boolean }) => {
  const { configured } = props;
  const intl = useIntl();
  useEffect(() => {}, [configured]);
  return (
    <>
      <Alert
        type={'info'}
        banner
        showIcon
        description={intl.formatMessage({
          id: 'pages.account.identity_source_detail.job_config.alert.description',
        })}
      />
      <br />
      <ProCard bordered headerBordered>
        <ProFormCheckbox.Group
          name={['jobConfig', 'dayOfWeek']}
          options={[
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.0',
              }),
              value: 'always',
            },
          ]}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.rule.0.message',
              }),
            },
          ]}
        />
        <Divider />
        <ProFormCheckbox.Group
          name={['jobConfig', 'dayOfWeek']}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.rule.0.message',
              }),
            },
          ]}
          options={[
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.1',
              }),
              value: 'monday',
            },
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.2',
              }),
              value: 'tuesday',
            },
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.3',
              }),
              value: 'wednesday',
            },
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.4',
              }),
              value: 'thursday',
            },
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.5',
              }),
              value: 'friday',
            },
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.6',
              }),
              value: 'saturday',
            },
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.day_of_week.options.7',
              }),
              value: 'sunday',
            },
          ]}
        />
        <Divider />
        <ProFormRadio.Group
          name={['jobConfig', 'mode']}
          options={[
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.mode.options.0',
              }),
              value: JobMode.period,
            },
            {
              label: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.mode.options.1',
              }),
              value: JobMode.timed,
            },
          ]}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.account.identity_source_detail.job_config.mode.rule.0.message',
              }),
            },
          ]}
        />
        <ProFormDependency name={['jobConfig', 'mode']}>
          {({ jobConfig }) => {
            return jobConfig?.mode === JobMode.period ? (
              <ProFormDigit
                min={1}
                max={24}
                name={['jobConfig', 'interval']}
                width={'xs'}
                addonBefore={intl.formatMessage({
                  id: 'pages.account.identity_source_detail.job_config.interval.addon_before',
                })}
                addonWarpStyle={{
                  flexWrap: 'nowrap',
                }}
                addonAfter={intl.formatMessage({
                  id: 'pages.account.identity_source_detail.job_config.interval.addon_after',
                })}
                rules={[
                  {
                    required: true,
                    message: intl.formatMessage({
                      id: 'pages.account.identity_source_detail.job_config.interval.rule.0.message',
                    }),
                  },
                ]}
              />
            ) : jobConfig?.mode === JobMode.timed ? (
              <ProFormTimePicker
                addonBefore={intl.formatMessage({
                  id: 'pages.account.identity_source_detail.job_config.time.addon_before',
                })}
                width={'xs'}
                name={['jobConfig', 'time']}
                rules={[
                  {
                    required: true,
                    message: intl.formatMessage({
                      id: 'pages.account.identity_source_detail.job_config.time.addon_before.rule.0.message',
                    }),
                  },
                ]}
              />
            ) : (
              <></>
            );
          }}
        </ProFormDependency>
      </ProCard>
    </>
  );
};
