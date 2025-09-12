/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ token, css, prefixCls }, prop) => {
  const prefixClassName = prop as string;
  return css`
    .${prefixClassName} {
      border: none;
      border-radius: ${token.borderRadius};

      &-item-card {
        .${prefixCls}-card-body {
          padding: 16px !important;
          background: #f7f8fa;
          border-radius: ${token.borderRadius}px;
        }
      }

      &-item-content-wrapper {
        display: flex;
      }

      &-item-avatar {
        width: 54px;
        height: 54px;
        border-radius: 4px;
        margin-right: 5px;

        & .${prefixCls}-avatar {
          width: 54px !important;
          height: 54px !important;
          display: flex;
          align-items: center;
          border-radius: 4px;

          > img {
            height: auto;
          }
        }
      }

      &-item-content {
        display: flex;
        flex-direction: column;
        font-size: 16px;
        min-width: 0;

        &-title {
          font-weight: 500;
          color: #293350;
          margin-top: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        &-type {
          color: #9fabcb;
          font-size: 13px;
          margin-top: 4px;
        }
        &-desc {
          margin-top: 12px;
        }
      }
    }
  `;
});
export default useStyles;
