/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { StatisticCard } from '@ant-design/pro-components';
import { useState } from 'react';
import { useAsyncEffect } from 'ahooks';
import { history } from '@@/core/history';
import {
  AppstoreOutlined,
  RocketOutlined,
  SafetyCertificateOutlined,
  TeamOutlined,
} from '@ant-design/icons';
import { Col, Row } from 'antd';
import { getAnalysisOverview } from '../../service';
import { useIntl } from '@umijs/max';

const topColResponsiveProps = {
  xs: 24,
  sm: 12,
  md: 12,
  lg: 12,
  xl: 6,
  style: { marginBottom: 24 },
};
export default () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [data, setData] = useState<DashboardAPI.OverviewResult>();
  const intl = useIntl();

  useAsyncEffect(async () => {
    setLoading(true);
    const { result, success } = await getAnalysisOverview();
    if (success) {
      setData(result);
    }
    setLoading(false);
  }, []);

  return (
    <Row gutter={24}>
      <Col {...topColResponsiveProps}>
        <StatisticCard
          loading={loading}
          statistic={{
            title: intl.formatMessage({ id: 'pages.dashboard.analysis.overview.today_auth.count' }),
            tip: intl.formatMessage({ id: 'pages.dashboard.analysis.overview.today_auth.count' }),
            value: data?.todayAuthnCount,
            icon: (
              <SafetyCertificateOutlined
                style={{
                  width: 46,
                  height: 46,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderRadius: 21,
                  color: '#1890FF',
                  backgroundColor: 'rgba(0,145,255,0.10)',
                  fontSize: 20,
                }}
              />
            ),
          }}
        />
      </Col>
      <Col {...topColResponsiveProps}>
        <StatisticCard
          loading={loading}
          statistic={{
            title: intl.formatMessage({
              id: 'pages.dashboard.analysis.overview.auth_source.count',
            }),
            tip: intl.formatMessage({ id: 'pages.dashboard.analysis.overview.auth_source.count' }),
            value: data?.idpCount,
            valueRender: (node) => {
              return (
                <a
                  onClick={() => {
                    history.replace(`/authn/identity_provider`);
                  }}
                >
                  {node}
                </a>
              );
            },
            icon: (
              <RocketOutlined
                style={{
                  width: 46,
                  height: 46,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderRadius: 21,
                  color: '#1890FF',
                  backgroundColor: 'rgba(0,145,255,0.10)',
                  fontSize: 20,
                }}
              />
            ),
          }}
        />
      </Col>
      <Col {...topColResponsiveProps}>
        <StatisticCard
          loading={loading}
          statistic={{
            title: intl.formatMessage({ id: 'pages.dashboard.analysis.overview.user.count' }),
            tip: intl.formatMessage({ id: 'pages.dashboard.analysis.overview.user.count' }),
            value: data?.userCount,
            valueRender: (node) => {
              return (
                <a
                  onClick={() => {
                    history.replace(`/account/user`);
                  }}
                >
                  {node}
                </a>
              );
            },
            icon: (
              <TeamOutlined
                style={{
                  width: 46,
                  height: 46,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderRadius: 21,
                  color: '#1890FF',
                  backgroundColor: 'rgba(0,145,255,0.10)',
                  fontSize: 20,
                }}
              />
            ),
          }}
        />
      </Col>
      <Col {...topColResponsiveProps}>
        <StatisticCard
          loading={loading}
          statistic={{
            title: intl.formatMessage({ id: 'pages.dashboard.analysis.overview.app.count' }),
            tip: intl.formatMessage({ id: 'pages.dashboard.analysis.overview.app.count' }),
            value: data?.appCount,
            valueRender: (node) => {
              return (
                <a
                  onClick={() => {
                    history.replace(`/app`);
                  }}
                >
                  {node}
                </a>
              );
            },
            icon: (
              <AppstoreOutlined
                style={{
                  width: 46,
                  height: 46,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderRadius: 21,
                  color: '#1890FF',
                  backgroundColor: 'rgba(0,145,255,0.10)',
                  fontSize: 20,
                }}
              />
            ),
          }}
        />
      </Col>
    </Row>
  );
};
