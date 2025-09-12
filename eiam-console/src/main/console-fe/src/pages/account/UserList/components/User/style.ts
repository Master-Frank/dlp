/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ css, prefixCls }) => {
  return css`
    .${prefixCls}-pro-table-list-toolbar-right {
      flex: none;
    }

    .${prefixCls}-pro-table-list-toolbar-title {
      width: 150px;
      height: 100%;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  `;
});

export default useStyle;
