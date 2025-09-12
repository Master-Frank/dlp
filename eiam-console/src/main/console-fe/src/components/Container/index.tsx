/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import classnames from 'classnames';
import useStyle from './style';

type IProps = {
  children: any;
  maxWidth?: number | string;
};
const prefixCls = 'topiam-container';

export const Container = (props: IProps) => {
  const { children = null, maxWidth = 1000 } = props;
  const { wrapSSR, hashId } = useStyle(prefixCls);
  return wrapSSR(
    <div className={classnames(`${prefixCls}`, hashId)} style={{ maxWidth }}>
      {children}
    </div>,
  );
};
