/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ prefixCls, token }) => ({
  expirationDate: {
    [`.${prefixCls}-form-item-control-input-content`]: {
      display: 'flex',
      alignItems: 'center',
    },
    marginBottom: '0 !important',
  },
  passwordLength: {
    marginBottom: ' 0px !important',
  },
  historyPasswordCheck: {
    marginBottom: ' 0px !important',
  },
  historyPasswordCheckCount: {
    padding: '15px !important',
    backgroundColor: token.colorFillAlter,
  },
  passwordComplexity: {
    [`.${prefixCls}-pro-field-radio-vertical  .${prefixCls}-radio-wrapper`]: {
      display: 'flex',
      alignItems: 'center',
      height: '30px',
      marginBottom: ' 5px',
      lineHeight: '30px',
    },
  },
  userScopeList: {
    [`.${prefixCls}-pro-form-list-container`]: {
      width: '100%  !important',
    },
  },
  userListSpace: {
    width: '100%',
    [`.${prefixCls}-space-item:last-child`]: {
      width: '100%',
    },
  },
  userCheckbox: {
    marginBottom: '10px !important',
  },
  excludeCheckboxEnable: {
    marginBottom: '10px !important',
    marginTop: '10px !important',
  },
  excludeCheckboxDisable: {
    marginTop: '10px !important',
  },
  excludeUserItem: {
    marginBottom: '0 !important',
  },
}));

export default useStyles;
