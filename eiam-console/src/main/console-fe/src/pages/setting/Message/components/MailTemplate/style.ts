/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ token, prefixCls }, props) => {
  const antCls = `.${prefixCls}`;
  const prefix = `${props}`;
  return {
    main: {
      [`.${prefix}`]: {
        [`&-title`]: {
          marginTop: 0,
          marginBottom: '0',
          fontSize: '16px',
        },
        [`${antCls}-avatar > img`]: {
          objectFit: 'fill',
        },
        [`${antCls}-card-head`]: {
          borderBottom: 'none',
        },
        [`${antCls}-card-head-title`]: {
          padding: '24px 0',
          lineHeight: '32px',
        },
        [`${antCls}-card-extra`]: {
          padding: '24px 0',
        },
        [`${antCls}-list-pagination`]: {
          marginTop: '24px',
        },
        [`${antCls}-avatar-lg`]: {
          width: '48px',
          height: '48px',
          lineHeight: '48px',
        },
        [`${antCls}-list-item-action`]: {
          marginInlineStart: '15px',
        },
      },
      [`@media  (max-width: ${token.screenXS}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenSM}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenMD}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenLG}px) and @media (min-width: ${token.screenMD}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenXL}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: 1400px)`]: {
        [`.${prefixCls}-list`]: {},
      },
    },
  };
});
export default useStyle;
