/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls, token }, props) => {
  const antCls = `.${prefixCls}`;
  const prefixClassName = `.${props}`;
  return {
    main: {
      [`${prefixClassName}`]: {
        [`&-content`]: {
          marginInlineStart: '15px',
          '& > div': {
            marginInlineStart: '0',
          },
        },
        [`&-item-content`]: {
          display: 'block',
          flex: 'none',
          width: '100%',
        },
        [`${antCls}-avatar > img`]: {
          objectFit: 'fill',
        },
        [`${antCls}-card-head`]: {
          borderBottom: 'none',
        },
        [`${antCls}-card-head-title`]: {
          padding: '24px 0',
          lineHeight: '32px',
        },
        [`${antCls}-card-extra`]: {
          padding: '24px 0',
        },
        [`${antCls}-list-pagination`]: {
          marginTop: '24px',
        },
        [`${antCls}-avatar-lg`]: {
          width: '48px',
          height: '48px',
          lineHeight: '48px',
        },
        [`${antCls}-list-item-action`]: {
          marginInlineStart: '15px',
        },
        [`${antCls}-pro-list ${antCls}-pro-list-row-content`]: {
          flex: 0,
          margin: '0',
        },
        [`${antCls}-pro-list ${antCls}-pro-list-row`]: {
          paddingLeft: '10px',
          paddingRight: '10px',
        },
        [`${antCls}-list-item`]: {
          paddingLeft: '10px',
          paddingRight: '10px',
        },
        [`${antCls}-pro-list-row-content`]: {
          flex: 0,
          margin: '0',
        },
      },
      [`@media  (max-width: ${token.screenXS}px)`]: {
        [`${prefixClassName}`]: {
          [`&-content`]: {
            marginInlineStart: '0',
            '& > div': {
              marginInlineStart: '0',
            },
          },
          [`&-item-content`]: {
            display: 'block',
            flex: 'none',
            width: '100%',
          },
        },
      },
      [`@media  (max-width: ${token.screenSM}px)`]: {
        [`${prefixClassName}`]: {},
      },
      [`@media  (max-width: ${token.screenMD}px)`]: {
        [`${prefixClassName}`]: {
          [`&-content`]: {
            '& > div': {
              display: 'block',
              '&:last-child': {
                top: '0',
                width: '100%',
              },
            },
          },
        },
      },
      [`@media  (max-width: ${token.screenLG}px) and @media (min-width: ${token.screenMD}px)`]: {
        [`${prefixClassName}`]: {
          [`&-content`]: {
            '& > div': {
              display: 'block',
              '&:last-child': {
                top: '0',
                width: '100%',
              },
            },
          },
        },
      },
      [`@media  (max-width: ${token.screenXL}px)`]: {
        [`${prefixClassName}`]: {
          [`&-content`]: {
            '& > div': {
              marginInlineStart: '24px',
              '&:last-child': {
                top: '0',
              },
            },
          },
        },
      },
      [`@media  (max-width: 1400px)`]: {
        [`${prefixClassName}`]: {
          [`&-content`]: {
            '& > div': {
              '&:last-child': {
                top: '0',
              },
            },
          },
        },
      },
    },
  };
});

export default useStyle;
