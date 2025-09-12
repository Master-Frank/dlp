/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls }) => {
  const antCls = `.${prefixCls}`;
  return {
    descriptionRemark: {
      [`${antCls}-descriptions-item-container ${antCls}-space-item`]: {
        span: {
          padding: '0 !important',
        },
      },
    },
  };
});

export default useStyle;
