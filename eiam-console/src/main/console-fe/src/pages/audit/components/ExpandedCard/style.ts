/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ token, prefixCls }) => ({
  expandedCard: {
    display: 'flex',
    paddingLeft: '10px',
    [`.${prefixCls}-table-expanded-row-fixed`]: {
      width: 'auto !important',
    },
  },
  expandedCardEachLine: {
    display: 'flex',
    marginBottom: '14px',
  },
  expandedCardLineUser: {
    flex: 1,
    marginRight: '24px',
  },
  expandedCardLineEvent: {
    flex: 1,
    marginRight: '24px',
  },
  expandedCardLineGeo: {
    flex: 1,
    marginRight: '24px',
  },
  expandedCardLabel: {
    display: 'block',
    minWidth: '80px',
    color: token.colorTextLabel,
    fontSize: '14px',
    [`.${prefixCls}-collapse-header`]: {
      paddingTop: '0 !important',
      paddingBottom: '0 !important',
    },
    [`.${prefixCls}-collapse-header-text`]: {
      color: token.colorTextLabel,
    },
  },
  expandedCardContent: {
    display: 'block',
    flexWrap: 'wrap',
    overflowX: 'auto',
    color: token.colorText,
    fontSize: '14px',
  },
  expandedCardTarget: {
    flex: 1,
  },
  expandedCardPanel: {
    fontSize: '14px',
    [`.${prefixCls}-collapse-header`]: {
      paddingTop: '0 !important',
      paddingBottom: '0 !important',
    },
    [`.${prefixCls}-collapse-header-text`]: {
      color: token.colorTextLabel,
    },
    [`.${prefixCls}-collapse-content>.${prefixCls}-collapse-content-box`]: {
      paddingTop: '6px  !important',
      paddingBottom: '6px  !important',
    },
  },
  expandedCardPanelContent: {
    padding: '0px 24px  !important',
    [`p`]: {
      marginTop: '0px !important',
      marginBottom: '6px !important',
    },
  },
}));

export default useStyles;
