/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ prefixCls, token, css }, props) => {
  const antCls = `.${prefixCls}`;
  const prefixClassName = `.${props}`;
  return css`
    ${prefixClassName} {
      &-content {
        margin-inline-start: 15px;

        & > div {
          margin-inline-start: 0;
        }
      }

      &-item-content {
        flex: none;
        width: 100%;
      }

      ${antCls}-avatar > img {
        object-fit: fill;
      }

      ${antCls}-card-head {
        border-bottom: none;
      }

      ${antCls}-card-head-title {
        padding: 24px 0;
        line-height: 32px;
      }

      ${antCls}-card-extra {
        padding: 24px 0;
      }

      ${antCls}-list-item-action {
        margin-inline-start: 15px;
      }

      ${antCls}-pro-list ${antCls}-pro-list-row {
        padding-left: 10px;
        padding-right: 10px;
      }

      ${antCls}-pro-list ${antCls}-pro-list-row-content {
        flex: 0;
        margin: 0;
      }

      ${antCls}-list-item {
        padding-left: 10px;
        padding-right: 10px;
      }

      ${antCls}-pro-list-row-content {
        flex: 0;
        margin: 0;
      }
    }

    @media (max-width: ${token.screenXS}px) {
      ${prefixClassName} {
        &-content {
          display: none;
          margin-inline-start: 0;

          & > div {
            margin-inline-start: 0;
          }
        }

        &-item-content {
          flex: 0;
          width: 100%;
        }
      }
    }
    @media (max-width: ${token.screenSM}px) {
      ${prefixClassName} {
        &-content {
          & > div {
            &:last-child {
              top: 0;
              width: 100%;
            }
          }
        }
      }
    }
    @media (max-width: ${token.screenLG}) and (min-width: ${token.screenMD}) {
      ${prefixClassName} {
        &-content {
          & > div {
            &:last-child {
              top: 0;
              width: 100%;
            }
          }
        }
      }
    }
    @media (max-width: ${token.screenXL}px) {
      ${prefixClassName} {
        &-content {
          & > div {
            margin-inline-start: 24px;

            &:last-child {
              top: 0;
            }
          }
        }
      }
    }
    @media (max-width: 1400px) {
      ${prefixClassName} {
        &-content {
          & > div {
            &:last-child {
              top: 0;
            }
          }
        }
      }
    }
  `;
});

export default useStyles;
