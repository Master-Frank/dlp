/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { GridContent } from '@ant-design/pro-components';
import { Col, Row } from 'antd';
import Overview from './components/Overview';
import HotAuthnProvider from './components/HotAuthnProvider';
import AuthnZone from './components/AuthnZone';
import AppVisitRank from './components/AppVisitRank';
import AuthnQuantity from './components/AuthnQuantity';

const Dashboard = () => {
  return (
    <GridContent style={{ height: '100%' }}>
      {/*概述*/}
      <Overview />
      <Row gutter={[24, 24]}>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*授权数量*/}
          <AuthnQuantity />
        </Col>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*访问量统计*/}
          <AppVisitRank />
        </Col>
      </Row>
      <Row gutter={[24, 24]}>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*热点提供商*/}
          <HotAuthnProvider />
        </Col>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*认证区域*/}
          <AuthnZone />
        </Col>
      </Row>
    </GridContent>
  );
};
export default () => {
  return <Dashboard />;
};
