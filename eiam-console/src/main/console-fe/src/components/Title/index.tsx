/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Button } from 'antd';
import { history } from '@umijs/max';
import classnames from 'classnames';
import useStyle from './style';
import React from 'react';

const prefixCls = 'topiam-title-wrapper';
export type IProps = {
  title: string | React.ReactNode;
  aside?: any;
  size?: 'h1' | 'h2' | 'h3';
  hasBack?: boolean;
};

const fontsize = {
  h1: '30px',
  h2: '20px',
  h3: '16px',
};

export const Title = (props: IProps) => {
  const { aside = null, hasBack = false, title, size = 'h1' } = props;
  const { wrapSSR, hashId } = useStyle(prefixCls);

  const style = {
    fontSize: fontsize[size],
  };

  const onBack = () => {
    history.back();
  };
  return wrapSSR(
    <div className={classnames(`${prefixCls}`, hashId)}>
      <div className={classnames(`${prefixCls}-title`, hashId)} style={style}>
        <span>{title}</span>
        {hasBack && (
          <Button
            size="small"
            onClick={onBack}
            className={classnames(`${prefixCls}-title-back-btn`, hashId)}
          >
            返回
          </Button>
        )}
      </div>

      <div>{aside}</div>
    </div>,
  );
};
export default Title;
