/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Dropdown } from 'antd';
import type { DropDownProps } from 'antd/es/dropdown';
import React from 'react';
import classNames from 'classnames';
import { createStyles } from 'antd-style';

export type HeaderDropdownProps = {
  overlayClassName?: string;
  placement?: 'bottomLeft' | 'bottomRight' | 'topLeft' | 'topCenter' | 'topRight' | 'bottomCenter';
} & Omit<DropDownProps, 'overlay'>;

const useStyle = createStyles(({ token }) => {
  return {
    main: {
      [`@media screen and (max-width: ${token.screenXS})`]: {
        width: '100%',
      },
    },
  };
});

const HeaderDropdown: React.FC<HeaderDropdownProps> = ({ overlayClassName: cls, ...restProps }) => {
  const { styles } = useStyle();
  return <Dropdown overlayClassName={classNames(styles.main, cls)} {...restProps} />;
};

export default HeaderDropdown;
