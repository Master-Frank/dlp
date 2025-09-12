/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ token, prefixCls }) => {
  const antCls = `.${prefixCls}`;
  return {
    main: {
      [`.user-group-remark`]: {
        boxSizing: 'border-box',
        width: '100%',
        margin: '0 !important',
        color: '#00000073',
        fontSize: '14px',
      },
      [`.user-group-detail`]: {
        color: `${token.colorLink} !important`,
      },
      [`${antCls}-avatar`]: {
        width: '32px !important',
        height: '32px !important',
        marginRight: '10px',
      },
      [`${antCls}-avatar-circle`]: {
        verticalAlign: 'middle',
        backgroundColor: token.colorPrimary,
      },
      [`${antCls}-card`]: {
        '&-meta-avatar': {
          paddingRight: '5px !important',
        },
      },
      [`${antCls}-card-meta-description`]: {
        marginTop: '15px',
        marginBottom: '15px',
      },
    },
  };
});

export default useStyle;
