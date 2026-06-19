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
import { history } from '@@/core/history';
import { GridContent, PageContainer } from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Menu } from 'antd';
import type { ItemType } from 'antd/es/menu/hooks/useItems';
import { useLayoutEffect, useRef, useState } from 'react';
import BaseView from './components/Base';
import MfaView from './components/MFA';
import SecurityView from './components/Security';
import { AccountSettingsStateKey } from './data.d';
import classnames from 'classnames';
import useStyle from './style';
import queryString from 'query-string';
import { useIntl, useLocation } from '@umijs/max';

const prefixCls = 'account';

type AccountSettingState = {
  mode: 'inline' | 'horizontal';
  selectKey: AccountSettingsStateKey;
};

const AccountSettings = () => {
  const { wrapSSR, hashId } = useStyle(prefixCls);
  const location = useLocation();
  const query = queryString.parse(location.search);
  const { type } = query as { type: AccountSettingsStateKey };
  const intl = useIntl();
  const [initConfig, setInitConfig] = useState<AccountSettingState>({
    mode: 'inline',
    selectKey: AccountSettingsStateKey.base,
  });

  useAsyncEffect(async () => {
    if (!type || !AccountSettingsStateKey[type]) {
      setInitConfig({ ...initConfig, selectKey: AccountSettingsStateKey.base });
      history.replace({
        pathname: location.pathname,
        search: queryString.stringify({ type: AccountSettingsStateKey.base }),
      });
      return;
    }
    setInitConfig({ ...initConfig, selectKey: type });
  }, [type]);

  const menu: ItemType[] = [
    {
      key: AccountSettingsStateKey.base,
      label: intl.formatMessage({
        id: 'page.user.profile.menu.base',
      }),
    },
    {
      key: AccountSettingsStateKey.security,
      label: intl.formatMessage({
        id: 'page.user.profile.menu.security',
      }),
    },
    {
      key: AccountSettingsStateKey.mfa,
      label: intl.formatMessage({
        id: 'page.user.profile.menu.mfa',
      }),
    },
  ];

  const dom = useRef<HTMLDivElement>();

  const resize = () => {
    requestAnimationFrame(() => {
      if (!dom.current) {
        return;
      }
      let mode: 'inline' | 'horizontal' = 'inline';
      const { offsetWidth } = dom.current;
      if (dom.current.offsetWidth < 641 && offsetWidth > 400) {
        mode = 'horizontal';
      }
      if (window.innerWidth < 768 && offsetWidth > 400) {
        mode = 'horizontal';
      }
      setInitConfig({ ...initConfig, selectKey: type, mode: mode as AccountSettingState['mode'] });
    });
  };

  useLayoutEffect(() => {
    if (dom.current) {
      window.addEventListener('resize', resize);
      resize();
    }
    return () => {
      window.removeEventListener('resize', resize);
    };
  }, [type]);

  const renderChildren = () => {
    const { selectKey } = initConfig;
    switch (selectKey) {
      case AccountSettingsStateKey.base:
        return <BaseView />;
      case AccountSettingsStateKey.security:
        return <SecurityView />;
      case AccountSettingsStateKey.mfa:
        return <MfaView />;
      default:
        return null;
    }
  };

  return wrapSSR(
    <PageContainer className={classnames(`${prefixCls}`, hashId)}>
      <GridContent>
        <div
          className={classnames(`${prefixCls}-main`, hashId)}
          ref={(ref) => {
            if (ref) {
              dom.current = ref;
            }
          }}
        >
          <div className={classnames(`${prefixCls}-left`, hashId)}>
            <Menu
              mode={initConfig.mode}
              selectedKeys={[initConfig.selectKey]}
              onClick={({ key }) => {
                setInitConfig({
                  ...initConfig,
                  selectKey: key as AccountSettingsStateKey,
                });
                history.replace({
                  pathname: location.pathname,
                  search: queryString.stringify({ type: key }),
                });
              }}
              items={menu}
            />
          </div>
          <div className={classnames(`${prefixCls}-right`, hashId)}>
            <div className={classnames(`${prefixCls}-right-title`, hashId)}>
              {menu.map((i: any) => {
                if (i?.key === initConfig.selectKey) {
                  return <div key={i}>{i.label}</div>;
                }
                return undefined;
              })}
            </div>
            {renderChildren()}
          </div>
        </div>
      </GridContent>
    </PageContainer>,
  );
};
export default AccountSettings;
