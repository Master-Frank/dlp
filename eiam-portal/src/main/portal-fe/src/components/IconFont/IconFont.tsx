/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import classnames from 'classnames';
import useStyle from './style';

type IconFontProps = {
  name: string;
};
const prefixCls = 'topiam-icon';

/**
 * Icon Font
 * https://www.iconfont.cn/help/detail?helptype=code
 */
export default (props: IconFontProps) => {
  const { name } = props;
  const { wrapSSR, hashId } = useStyle(prefixCls);

  return wrapSSR(
    <svg className={classnames(`${prefixCls}`, hashId)} aria-hidden="true">
      <use xlinkHref={`#${name}`} />
    </svg>,
  );
};
