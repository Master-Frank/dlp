/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls, token }, props) => {
  const antCls = `.${prefixCls}`;
  const { prefix, loginImg } = props as any;
  const prefixClassName = `.${prefix}`;
  return {
    main: {
      [`${prefixClassName}`]: {
        ['&-container']: {
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
          overflow: 'auto',
          backgroundColor: '#fff',
        },
        ['&-content']: {
          display: 'flex',
          flex: 'none !important',
          flexDirection: 'column',
          padding: '32px',
          maxWidth: '100%',
          backgroundColor: '#fff',
          borderRadius: token.borderRadius,
          boxShadow: '0px 0px 24px 0px rgba(0,0,0,0.1)',
          [`${antCls}-tabs-nav-list`]: {
            margin: 'auto',
            fontSize: '16px',
          },
        },
        ['&-title']: {
          textAlign: 'center',
        },
        ['&-main']: {
          width: '328px',
          marginTop: '50px',
        },
        ['&-footer']: {
          position: 'sticky',
          top: '100%',
          right: '0',
          bottom: '0',
          left: '0',
          [`${antCls}-pro-global-footer`]: {},
          [`${antCls}-pro-global-footer-list >a`]: {
            color: 'rgba(0,0,0,.45)',
            fontSize: '14px',
          },
          [`${antCls}-pro-global-footer-copyright`]: {
            color: 'rgba(0,0,0,.45)',
            fontSize: '14px',
          },
        },
      },
      [`@media screen and (min-width: ${token.screenMDMin}px)`]: {
        [`${prefixClassName}`]: {
          ['&-container']: {
            backgroundColor: '#f0f2f5',
            backgroundImage: `url(${(loginImg as string) || '/login-background.png'})`,
            backgroundRepeat: 'no-repeat',
            backgroundSize: 'cover',
          },
          ['&-content']: {
            minHeight: '500px',
          },
        },
      },
      [`@media screen and (max-width: ${token.screenMD}px)`]: {
        [`${prefixClassName}`]: {
          ['&-content']: {
            boxShadow: 'none',
          },
          ['&-footer']: {
            backgroundColor: '#ffffff',
          },
        },
      },
      [`@media screen and (max-width: ${token.screenSM}px)`]: {
        [`${prefixClassName}`]: {
          ['&-main']: {
            width: '95%',
            maxWidth: '328px',
          },
          ['&-footer']: {
            display: 'none',
          },
        },
      },
    },
  };
});
export default useStyle;
