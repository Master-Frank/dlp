/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles((_, props) => {
  const prefixCls = `${props}`;
  return {
    main: {
      width: '328px',
      [`.${prefixCls}`]: {
        ['&-success-box']: {
          padding: '40px',
          textAlign: 'center',
        },
        ['&-success']: {
          width: '100%',
          textAlign: 'center',
          marginTop: '32px',
          ['&-button']: {
            width: '100%',
          },
          ['&-title']: {
            fontWeight: '600',
            fontSize: '24px',
            marginBottom: '24px',
          },
          ['&-desc']: {
            fontWeight: '400',
            fontSize: '14px',
            marginBottom: '32px',
            lineHeight: '20px',
            color: '#545968',
          },
        },
        ['&-back']: {
          width: '100%',
          marginBottom: '24px',
        },
        ['&-back-time']: {
          display: 'flex',
          justifyContent: 'center',
          fontSize: '14px',
          color: '#215ae5',
          cursor: 'pointer',
          marginTop: '16px',
          height: '20px',
        },
      },
    },
  };
});

export default useStyle;
