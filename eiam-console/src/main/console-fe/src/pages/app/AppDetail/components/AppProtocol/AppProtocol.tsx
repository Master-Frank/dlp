/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { AppProtocolType } from '@/constant';

import { useAsyncEffect } from 'ahooks';
import { Skeleton } from 'antd';
import { useState } from 'react';
import FromConfig from './FromProtocolConfig';
import JwtConfig from './JwtProtocolConfig';
import OidcConfig from './OidcProtocolConfig';
import { GetApp } from '../../data.d';
import { useModel } from '@@/exports';

export default () => {
  const { app } = useModel('app.AppDetail.model');
  const [loading, setLoading] = useState<boolean>(true);
  useAsyncEffect(async () => {
    setLoading(true);

    setLoading(false);
  }, [app]);

  const ComponentByKey = ({ key, app }: { key: string; app: GetApp }) => {
    const components = {
      [AppProtocolType.jwt]: JwtConfig,
      [AppProtocolType.oidc]: OidcConfig,
      [AppProtocolType.form]: FromConfig,
    };
    const Component = components[key];
    return <Component app={app} />;
  };
  return (
    <Skeleton loading={loading} active={true} paragraph={{ rows: 5 }}>
      {app && ComponentByKey({ key: app?.protocol, app: app })}
    </Skeleton>
  );
};
