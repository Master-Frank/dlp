/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ prefixCls }) => {
  return {
    step_form: {
      [`.${prefixCls}-form-item`]: {
        [`.${prefixCls}-form-item-control-input`]: {
          width: '100%',
        },
      },
    },
  };
});

export default useStyles;
