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
import { Alert, Button, Card, Form, Input, Space, Spin, Steps, Typography, message } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { Helmet, history, useIntl } from '@umijs/max';
import QRCode from 'qrcode';
import queryString from 'query-string';
import { confirmBind, prepareBind } from './service';
import type { PrepareBindResult } from './data.d';

const { Title, Text, Paragraph } = Typography;

type Phase = 'prepare' | 'confirm' | 'backup';

const MfaSetup = () => {
  const intl = useIntl();
  const [phase, setPhase] = useState<Phase>('prepare');
  const [loading, setLoading] = useState<boolean>(true);
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [prep, setPrep] = useState<PrepareBindResult>();
  const [qrDataUrl, setQrDataUrl] = useState<string>();
  const [backupCodes, setBackupCodes] = useState<string[]>([]);
  const [form] = Form.useForm<{ otp: string }>();
  const acknowledgedRef = useRef<boolean>(false);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      const { success, result, message: msg } = await prepareBind();
      if (cancelled) return;
      if (success && result) {
        setPrep(result);
        try {
          const url = await QRCode.toDataURL(result.otpAuthUri, { width: 220, margin: 1 });
          if (!cancelled) setQrDataUrl(url);
        } catch (e) {
          if (!cancelled) message.error(intl.formatMessage({ id: 'pages.mfa.setup.qr.fail' }));
        }
        setPhase('confirm');
      } else {
        message.error(msg || intl.formatMessage({ id: 'pages.mfa.setup.prepare.fail' }));
      }
      setLoading(false);
    })();
    return () => {
      cancelled = true;
    };
  }, [intl]);

  const onConfirm = async (values: { otp: string }) => {
    setSubmitting(true);
    try {
      const otp = (values.otp || '').replace(/\s+/g, '');
      const { success, result, message: msg, status } = await confirmBind(otp);
      if (success && result?.backupCodes?.length) {
        setBackupCodes(result.backupCodes);
        setPhase('backup');
        return;
      }
      if (status === 'locked_out') {
        message.error(intl.formatMessage({ id: 'pages.mfa.locked_out' }));
        return;
      }
      message.error(msg || intl.formatMessage({ id: 'pages.mfa.setup.confirm.fail' }));
    } finally {
      setSubmitting(false);
    }
  };

  const downloadBackupCodes = () => {
    const lines = [
      intl.formatMessage({ id: 'pages.mfa.backup.file.header' }),
      '',
      ...backupCodes,
    ];
    const blob = new Blob([lines.join('\n')], { type: 'text/plain;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `ulp-mfa-backup-codes-${Date.now()}.txt`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  const finishSetup = () => {
    if (!acknowledgedRef.current) {
      message.warning(intl.formatMessage({ id: 'pages.mfa.backup.ack.required' }));
      return;
    }
    const query = queryString.parse(history.location.search);
    const { redirect_uri } = query as { redirect_uri?: string };
    window.location.replace(redirect_uri || '/user/profile?type=mfa');
  };

  return (
    <>
      <Helmet>
        <title>{intl.formatMessage({ id: 'pages.mfa.setup.title' })}</title>
      </Helmet>
      <div style={{ maxWidth: 720, margin: '40px auto', padding: '0 16px' }}>
        <Card>
          <Title level={3}>{intl.formatMessage({ id: 'pages.mfa.setup.title' })}</Title>
          <Steps
            size="small"
            current={phase === 'prepare' ? 0 : phase === 'confirm' ? 1 : 2}
            items={[
              { title: intl.formatMessage({ id: 'pages.mfa.setup.steps.scan' }) },
              { title: intl.formatMessage({ id: 'pages.mfa.setup.steps.verify' }) },
              { title: intl.formatMessage({ id: 'pages.mfa.setup.steps.backup' }) },
            ]}
            style={{ marginBottom: 24 }}
          />
          <Spin spinning={loading}>
            {phase !== 'backup' && prep && (
              <Space direction="vertical" size="large" style={{ width: '100%' }}>
                <Paragraph>{intl.formatMessage({ id: 'pages.mfa.setup.scan.tip' })}</Paragraph>
                <div style={{ textAlign: 'center' }}>
                  {qrDataUrl ? (
                    <img
                      src={qrDataUrl}
                      alt="QR"
                      style={{ width: 220, height: 220, border: '1px solid #f0f0f0' }}
                    />
                  ) : (
                    <Spin />
                  )}
                </div>
                <Alert
                  type="info"
                  showIcon
                  message={intl.formatMessage({ id: 'pages.mfa.setup.secret.label' })}
                  description={<Text code copyable>{prep.secretBase32}</Text>}
                />
                <Form form={form} onFinish={onConfirm} layout="vertical">
                  <Form.Item
                    name="otp"
                    label={intl.formatMessage({ id: 'pages.mfa.setup.otp.label' })}
                    rules={[
                      { required: true, message: intl.formatMessage({ id: 'pages.mfa.setup.otp.required' }) },
                      { pattern: /^\d{6}$/, message: intl.formatMessage({ id: 'pages.mfa.setup.otp.format' }) },
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
                    <Button type="primary" htmlType="submit" loading={submitting} block>
                      {intl.formatMessage({ id: 'pages.mfa.setup.confirm.submit' })}
                    </Button>
                  </Form.Item>
                </Form>
              </Space>
            )}
            {phase === 'backup' && (
              <Space direction="vertical" size="large" style={{ width: '100%' }}>
                <Alert
                  type="warning"
                  showIcon
                  message={intl.formatMessage({ id: 'pages.mfa.backup.warning.title' })}
                  description={intl.formatMessage({ id: 'pages.mfa.backup.warning.desc' })}
                />
                <Card size="small" type="inner">
                  <div
                    style={{
                      display: 'grid',
                      gridTemplateColumns: 'repeat(2, 1fr)',
                      gap: 8,
                      fontFamily: 'monospace',
                      fontSize: 14,
                    }}
                  >
                    {backupCodes.map((c) => (
                      <Text code copyable key={c}>
                        {c}
                      </Text>
                    ))}
                  </div>
                </Card>
                <Space>
                  <Button onClick={downloadBackupCodes}>
                    {intl.formatMessage({ id: 'pages.mfa.backup.download' })}
                  </Button>
                  <Button
                    onClick={() => {
                      acknowledgedRef.current = true;
                      message.success(intl.formatMessage({ id: 'pages.mfa.backup.ack.done' }));
                    }}
                  >
                    {intl.formatMessage({ id: 'pages.mfa.backup.ack' })}
                  </Button>
                  <Button type="primary" onClick={finishSetup}>
                    {intl.formatMessage({ id: 'pages.mfa.setup.finish' })}
                  </Button>
                </Space>
              </Space>
            )}
          </Spin>
        </Card>
      </div>
    </>
  );
};

export default MfaSetup;
