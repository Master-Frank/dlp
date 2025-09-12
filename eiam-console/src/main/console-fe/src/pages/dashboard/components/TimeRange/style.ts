/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls, token }) => {
  const antCls = `.${prefixCls}`;
  return {
    main: {
      ['.sales-extra-wrap']: {
        [`.sales-extra`]: {
          display: 'inline-block',
          marginRight: '24px',
          a: {
            marginLeft: '24px',
            color: token.colorText,
            ['&:hover']: {
              color: token.colorPrimary,
            },
          },
        },
        [`${antCls}-picker-range`]: {
          width: '270px',
        },
      },
      [`@media  (max-width: ${token.screenXXL}px)`]: {
        [`.sales-extra-wrap`]: {
          [`${antCls}-picker-range`]: {
            display: 'none',
          },
        },
      },
      [`@media  (max-width: ${token.screenXL}px)`]: {
        [`.sales-extra-wrap`]: {
          [`.sales-extra`]: {
            display: 'none',
          },
        },
      },
    },
  };
});
export default useStyle;
