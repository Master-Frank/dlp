/*
 * ulp-console - United Login Platform
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
import { Alert, Button, Card, Form, Input, Space, Tabs, Typography, message } from 'antd';
import { useState } from 'react';
import { Helmet, history, useIntl } from '@umijs/max';
import queryString from 'query-string';
import { challenge, challengeWithBackupCode } from './service';

const { Title, Paragraph } = Typography;

const MfaChallenge = () => {
  const intl = useIntl();
  const [activeKey, setActiveKey] = useState<'totp' | 'backup'>('totp');
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [otpForm] = Form.useForm<{ code: string }>();
  const [backupForm] = Form.useForm<{ backupCode: string }>();
  const [errorMsg, setErrorMsg] = useState<string>();

  const onSuccess = (extra?: { warn?: boolean; required?: boolean }) => {
    if (extra?.required) {
      message.warning(intl.formatMessage({ id: 'pages.mfa.challenge.backup.required.regen' }));
    } else if (extra?.warn) {
      message.warning(intl.formatMessage({ id: 'pages.mfa.challenge.backup.warn.regen' }));
    }
    const query = queryString.parse(history.location.search);
    const { redirect_uri } = query as { redirect_uri?: string };
    window.location.replace(redirect_uri || '/');
  };

  const mapStatusToMessage = (status?: string, fallback?: string): string => {
    switch (status) {
      case 'invalid_otp':
        return intl.formatMessage({ id: 'pages.mfa.challenge.invalid_otp' });
      case 'invalid_backup_code':
        return intl.formatMessage({ id: 'pages.mfa.challenge.invalid_backup_code' });
      case 'challenge_expired':
        return intl.formatMessage({ id: 'pages.mfa.challenge.expired' });
      case 'challenge_session_invalid':
      case 'subject_not_bound':
        return intl.formatMessage({ id: 'pages.mfa.challenge.session_invalid' });
      case 'locked_out':
        return intl.formatMessage({ id: 'pages.mfa.locked_out' });
      default:
        return fallback || intl.formatMessage({ id: 'pages.mfa.challenge.unknown' });
    }
  };

  const onTotpSubmit = async (values: { code: string }) => {
    setSubmitting(true);
    setErrorMsg(undefined);
    try {
      const code = (values.code || '').replace(/\s+/g, '');
      const { success, status, message: msg } = await challenge(code);
      if (success && status === 'ok') {
        onSuccess();
        return;
      }
      const text = mapStatusToMessage(status, msg);
      setErrorMsg(text);
      if (status === 'challenge_expired' || status === 'challenge_session_invalid') {
        setTimeout(() => history.replace('/login'), 1500);
      }
    } finally {
      setSubmitting(false);
    }
  };

  const onBackupSubmit = async (values: { backupCode: string }) => {
    setSubmitting(true);
    setErrorMsg(undefined);
    try {
      const raw = (values.backupCode || '').trim().toUpperCase();
      const { success, status, message: msg, result } = await challengeWithBackupCode(raw);
      if (success && status === 'ok') {
        onSuccess({
          warn: !!result?.regenerate_backup_codes_warning,
          required: !!result?.regenerate_backup_codes_required,
        });
        return;
      }
      const text = mapStatusToMessage(status, msg);
      setErrorMsg(text);
      if (status === 'challenge_expired' || status === 'challenge_session_invalid') {
        setTimeout(() => history.replace('/login'), 1500);
      }
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <Helmet>
        <title>{intl.formatMessage({ id: 'pages.mfa.challenge.title' })}</title>
      </Helmet>
      <div style={{ maxWidth: 480, margin: '80px auto', padding: '0 16px' }}>
        <Card>
          <Title level={3}>{intl.formatMessage({ id: 'pages.mfa.challenge.title' })}</Title>
          <Paragraph>{intl.formatMessage({ id: 'pages.mfa.challenge.subtitle' })}</Paragraph>
          {errorMsg && (
            <Alert
              type="error"
              showIcon
              message={errorMsg}
              style={{ marginBottom: 16 }}
              closable
              onClose={() => setErrorMsg(undefined)}
            />
          )}
          <Tabs
            activeKey={activeKey}
            onChange={(k) => {
              setActiveKey(k as 'totp' | 'backup');
              setErrorMsg(undefined);
            }}
            items={[
              {
                key: 'totp',
                label: intl.formatMessage({ id: 'pages.mfa.challenge.tab.totp' }),
                children: (
                  <Form form={otpForm} onFinish={onTotpSubmit} layout="vertical">
                    <Form.Item
                      name="code"
                      label={intl.formatMessage({ id: 'pages.mfa.challenge.totp.label' })}
                      rules={[
                        { required: true, message: intl.formatMessage({ id: 'pages.mfa.challenge.totp.required' }) },
                        { pattern: /^\d{6}$/, message: intl.formatMessage({ id: 'pages.mfa.challenge.totp.format' }) },
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
                    <Form.Item>
                      <Space style={{ width: '100%', justifyContent: 'space-between' }}>
                        <Button type="link" onClick={() => setActiveKey('backup')}>
                          {intl.formatMessage({ id: 'pages.mfa.challenge.use.backup' })}
                        </Button>
                        <Button type="primary" htmlType="submit" loading={submitting}>
                          {intl.formatMessage({ id: 'pages.mfa.challenge.submit' })}
                        </Button>
                      </Space>
                    </Form.Item>
                  </Form>
                ),
              },
              {
                key: 'backup',
                label: intl.formatMessage({ id: 'pages.mfa.challenge.tab.backup' }),
                children: (
                  <Form form={backupForm} onFinish={onBackupSubmit} layout="vertical">
                    <Form.Item
                      name="backupCode"
                      label={intl.formatMessage({ id: 'pages.mfa.challenge.backup.label' })}
                      rules={[
                        {
                          required: true,
                          message: intl.formatMessage({ id: 'pages.mfa.challenge.backup.required' }),
                        },
                      ]}
                    >
                      <Input
                        autoComplete="off"
                        placeholder="ABCD-EFGH"
                        style={{ textTransform: 'uppercase' }}
                      />
                    </Form.Item>
                    <Form.Item>
                      <Space style={{ width: '100%', justifyContent: 'space-between' }}>
                        <Button type="link" onClick={() => setActiveKey('totp')}>
                          {intl.formatMessage({ id: 'pages.mfa.challenge.use.totp' })}
                        </Button>
                        <Button type="primary" htmlType="submit" loading={submitting}>
                          {intl.formatMessage({ id: 'pages.mfa.challenge.submit' })}
                        </Button>
                      </Space>
                    </Form.Item>
                  </Form>
                ),
              },
            ]}
          />
        </Card>
      </div>
    </>
  );
};

export default MfaChallenge;
