/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ prefixCls }) => ({
  expandedRow: {
    [`.${prefixCls}-table-expanded-row-fixed`]: {
      width: 'auto !important',
    },
  },
}));
export default useStyles;
