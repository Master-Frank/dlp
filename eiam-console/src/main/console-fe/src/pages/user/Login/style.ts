/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ token, prefixCls }, prefix) => {
  const prefixClassName = `.${prefix}`;
  const antCls = `.${prefixCls}`;
  return {
    main: {
      // Ant Design Pro 的 LoginFormPage 组件默认采用左右布局，这里把左侧的样式去掉https://procomponents.ant.design/components/login-form#loginformpage
      '.ant-pro-form-login-page-notice': {
        display: 'none', // 隐藏左侧元素
      },
      [`${prefixClassName}`]: {
        backgroundColor: 'white',
        height: '100vh',
        border: '1px solid rgb(240, 240, 240)',
        [`${prefixClassName}-form-prefix-icon`]: {
          color: token.colorPrimary,
          fontSize: token.fontSize,
        },
        [`${antCls}-pro-form-login-page`]: {
          backgroundSize: 'cover',
        },
        [`${antCls}-pro-form-login-page-header`]: {
          height: 'auto',
        },
        [`${antCls}-pro-form-login-page-logo`]: {
          maxWidth: '200px',
          width: '100%',
          height: '100%',
          marginRight: 0,
          fontSize: '18px',
          lineHeight: '100%',
          textAlign: 'center',
          verticalAlign: 'top',
        },
        [`${antCls}-pro-form-login-page-desc`]: {
          marginTop: '25px',
          marginBottom: '40px',
          color: 'rgba(0, 0, 0, 0.45)',
          fontSize: '14px',
        },
        [`${antCls}-pro-form-login-page-container`]: {
          height: 'auto',
          [`${antCls}-pro-form-login-page-top`]: {
            marginTop: '25px',
          },
        },
        [`${antCls}-pro-form-login-page-main`]: {
          marginBottom: '80px',
        },
      },
    },
  };
});

export default useStyle;
