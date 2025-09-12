/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls, token }, props) => {
  const antCls = `.${prefixCls}`;
  const prefix = `${props}`;
  return {
    main: {
      [`${antCls}-card-hoverable:hover`]: {
        borderLeft: '1px solid #52c41a',
      },
      [`.${prefix}-descriptions`]: {
        [`${antCls}-descriptions-small ${antCls}-descriptions-row > th, ${antCls}-descriptions-small ${antCls}-descriptions-row > td`]:
          {
            paddingBottom: '16px',
          },
      },
      [`.${prefix}-content`]: {
        width: '100%',
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        [`&-status`]: {
          width: '2%',
          height: '80px',
          borderRadius: '8px 0 0 8px',
          background: token.colorSuccess,
          marginRight: '10px',
        },
        [`&-title`]: {
          width: '63%',
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          [`&-avatar`]: {
            display: 'inline-flex',
            alignItems: 'center',
          },
          [`&-text`]: {
            display: 'inline-block',
            paddingLeft: '10px',
            textAlign: 'center',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            overflow: 'hidden',
            fontWeight: '600',
          },
        },
        [`&-operate`]: {
          width: '35%',
          textAlign: 'center',
          marginRight: '10px',
        },
      },
    },
  };
});

export default useStyle;
