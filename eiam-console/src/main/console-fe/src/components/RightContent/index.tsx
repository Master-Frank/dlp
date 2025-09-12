/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { SelectLang, useModel } from '@umijs/max';
import React from 'react';
import Avatar from './AvatarDropdown';
import { Helmet } from '@@/exports';
import About from '../About';
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ token }) => ({
  main: {
    display: 'flex',
    height: '48px',
    marginLeft: 'auto',
    overflow: 'hidden',
    gap: 8,
  },
  action: {
    display: 'flex',
    float: 'right',
    height: '48px',
    marginLeft: 'auto',
    overflow: 'hidden',
    cursor: 'pointer',
    padding: '0 12px',
    borderRadius: token.borderRadius,
    '&:hover': {
      backgroundColor: token.colorBgTextHover,
    },
  },
}));

const GlobalHeaderRight: React.FC = () => {
  const { styles } = useStyle();

  const { initialState } = useModel('@@initialState');

  if (!initialState || !initialState.settings) {
    return null;
  }

  return (
    <div className={styles.main}>
      <Helmet>
        <link rel="icon" href={'/favicon.ico'} />
      </Helmet>
      <About />
      <SelectLang className={styles.action} />
      <Avatar />
    </div>
  );
};
export default GlobalHeaderRight;
