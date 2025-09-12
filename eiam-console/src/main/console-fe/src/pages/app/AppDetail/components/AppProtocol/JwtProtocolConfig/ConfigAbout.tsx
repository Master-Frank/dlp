/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProForm, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { App, Form, Typography } from 'antd';
import { VerticalAlignBottomOutlined } from '@ant-design/icons';
import { getCertList } from '../../../service';
import { CertUsingType } from '@/pages/app/AppDetail/constant';
import { useIntl } from '@umijs/max';
import { createStyles } from 'antd-style';
import { ColProps } from 'antd/es/grid/col';
import copy from 'copy-to-clipboard';
import { CopyConfig } from 'antd/es/typography/Base';

const IDP_ENCRYPT_CERT = 'idpEncryptCert';

const useStyles = createStyles(({}) => ({
  config: {
    backgroundColor: '#f1f1f2',
  },
}));

/**
 * 配置信息
 *
 * @param props
 */
export default (props: {
  appId: string;
  protocolEndpoint: Record<string, string>;
  collapsed?: boolean;
  labelCol?: ColProps;
  wrapperCol?: ColProps;
}) => {
  const [configForm] = Form.useForm();
  const {
    protocolEndpoint,
    appId,
    labelCol = {
      span: 6,
    },
    wrapperCol = {
      span: 14,
    },
  } = props;
  const intl = useIntl();
  const { message } = App.useApp();
  const { styles } = useStyles();

  useAsyncEffect(async () => {
    configForm.setFieldsValue(protocolEndpoint);
  }, [appId, protocolEndpoint]);

  useAsyncEffect(async () => {
    //获取JWT签名证书
    const certResult = await getCertList(appId, CertUsingType.JWT_ENCRYPT);
    if (certResult.success && certResult.result) {
      certResult.result.forEach((value) => {
        if (value.usingType === CertUsingType.JWT_ENCRYPT) {
          configForm.setFieldsValue({ idpEncryptCert: value.cert });
        }
      });
    }
  }, [appId]);

  const copySuccess = async () => {
    message.success(intl.formatMessage({ id: 'custom.copy_success' }));
  };

  const copyable: CopyConfig = {
    text: configForm.getFieldValue(IDP_ENCRYPT_CERT),
    onCopy: () => {
      copySuccess().then();
    },
  };

  const downloadEncryptCert = () => {
    const value = configForm.getFieldValue(IDP_ENCRYPT_CERT);
    if (!value) {
      return;
    }
    const blob = new Blob([value], { type: 'application/x-x509-ca-cert' });
    const url = URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = appId + 'sign.cer';
    document.documentElement.appendChild(a);
    a.click();
    document.documentElement.removeChild(a);
  };

  return (
    <ProForm
      layout={'horizontal'}
      labelCol={labelCol}
      wrapperCol={wrapperCol}
      labelAlign={'right'}
      labelWrap
      submitter={false}
      form={configForm}
      className={styles.config}
    >
      <ProFormText
        label={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.jwt.config_about.idp_sso_endpoint',
        })}
        name={'idpSsoEndpoint'}
        extra={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.jwt.config_about.idp_sso_endpoint.extra',
        })}
        readonly
        proFieldProps={{
          render: (value: string) => {
            return value && <Typography.Text copyable>{value}</Typography.Text>;
          },
        }}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormTextArea
        label={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.jwt.config_about.idp_encrypt_cert',
        })}
        name={IDP_ENCRYPT_CERT}
        disabled
        fieldProps={{ autoComplete: 'off', rows: 3 }}
        extra={
          <div style={{ display: 'inline-block' }}>
            <div style={{ display: 'inline-block' }}>
              {intl.formatMessage({
                id: 'pages.app.config.detail.protocol_config.jwt.config_about.idp_encrypt_cert.extra.0',
              })}
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <Typography.Paragraph style={{ display: 'inline-block' }} copyable={copyable}>
                <a
                  onClick={async () => {
                    copy(configForm.getFieldValue(IDP_ENCRYPT_CERT));
                    await copySuccess();
                  }}
                >
                  {intl.formatMessage({
                    id: 'pages.app.config.detail.protocol_config.jwt.config_about.idp_encrypt_cert.extra.1',
                  })}
                </a>
              </Typography.Paragraph>
              <a onClick={downloadEncryptCert}>
                {intl.formatMessage({
                  id: 'pages.app.config.detail.protocol_config.jwt.config_about.idp_encrypt_cert.extra.2',
                })}
                <VerticalAlignBottomOutlined />
              </a>
            </div>
          </div>
        }
      />
    </ProForm>
  );
};
