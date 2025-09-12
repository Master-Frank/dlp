/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Alert, AlertProps } from 'antd';
import React from 'react';
import { omit } from 'lodash';
import { createStyles } from 'antd-style';

export interface Props extends Omit<AlertProps, 'type'> {
  type?: 'success' | 'info' | 'warning' | 'error' | 'grey';
}

const useStyles = createStyles(({ prefixCls }) => {
  return {
    greyIcon: {
      [`.${prefixCls}-alert-icon`]: {
        color: 'rgb(107, 119, 133) !important',
      },
    },
  };
});
export default (props: Props) => {
  const { styles } = useStyles();

  if (props.type === 'grey') {
    return (
      <Alert
        {...omit(props, 'type')}
        style={{ backgroundColor: '#f1f1f2', border: '1px solid #f1f1f2', ...props.style }}
        className={styles.greyIcon}
      />
    );
  }
  return <Alert type={props.type} {...omit(props, 'type')} />;
};
