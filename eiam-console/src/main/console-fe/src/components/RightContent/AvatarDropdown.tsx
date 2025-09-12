/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';
import { history, useIntl, useModel } from '@umijs/max';
import { Spin } from 'antd';
import React, { useCallback } from 'react';
import { flushSync } from 'react-dom';
import HeaderDropdown from '../HeaderDropdown';
import { isLoginPath, LOGIN_PATH } from '@/utils/utils';
import { outLogin } from '@/services';
import queryString from 'query-string';
import { createStyles } from 'antd-style';
import { ItemType } from 'antd/es/menu/hooks/useItems';

export type GlobalHeaderRightProps = {
  menu?: boolean;
  children?: React.ReactNode;
};

const useStyle = createStyles(({ token }) => ({
  main: {
    display: 'flex',
    height: '48px',
    marginLeft: 'auto',
    overflow: 'hidden',
    alignItems: 'center',
    padding: '0 8px',
    cursor: 'pointer',
    borderRadius: token.borderRadius,
    '&:hover': {
      backgroundColor: token.colorBgTextHover,
    },
  },
}));

export const AvatarName = () => {
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState || {};
  return <span>{currentUser?.username}</span>;
};

export const AvatarDropdown: React.FC<GlobalHeaderRightProps> = ({ children }) => {
  const intl = useIntl();

  const { styles } = useStyle();

  /**
   * 退出登录
   */
  const loginOut = async () => {
    await outLogin();
    const query = queryString.parse(history.location.search);
    const { redirect_uri } = query as { redirect_uri: string };

    if (!isLoginPath() && !redirect_uri) {
      let settings: Record<string, string> = { pathname: LOGIN_PATH };
      settings = {
        ...settings,
        search: queryString.stringify({ redirect_uri: window.location.href }),
      };
      const href = history.createHref(settings);
      window.location.replace(href);
    }
  };

  const { initialState, setInitialState } = useModel('@@initialState');

  const onMenuClick = useCallback(
    async (event: { key: string }) => {
      const { key } = event;
      if (key === 'logout' && initialState) {
        flushSync(() => {
          setInitialState({ ...initialState, currentUser: undefined });
          loginOut();
        });
        return;
      }
      history.push(`/user/${key}`);
    },
    [initialState, setInitialState],
  );

  const loading = (
    <span className={styles.main}>
      <Spin
        size="small"
        style={{
          marginLeft: 8,
          marginRight: 8,
        }}
      />
    </span>
  );

  if (!initialState) {
    return loading;
  }

  const { currentUser } = initialState;

  if (!currentUser || !currentUser.username) {
    return loading;
  }

  const menuItems: ItemType[] = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: intl.formatMessage({ id: 'components.right_content.profile' }),
    },
    {
      type: 'divider',
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: intl.formatMessage({ id: 'components.right_content.logout' }),
    },
  ];

  return (
    <HeaderDropdown
      menu={{
        selectedKeys: [],
        onClick: onMenuClick,
        items: menuItems,
      }}
    >
      {children}
    </HeaderDropdown>
  );
};

export default AvatarDropdown;
