/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import type { GenerateStyle, ProAliasToken } from '@ant-design/pro-components';
import { useStyle as useAntdStyle } from '@ant-design/pro-components';
import { ConfigProvider } from 'antd';
import { useContext } from 'react';

const { ConfigContext } = ConfigProvider;

interface TitleComponentToken extends ProAliasToken {
  antCls: string;
  prefixCls: string;
}

const genActionsStyle: GenerateStyle<TitleComponentToken> = (token) => {
  const { prefixCls } = token;

  return {
    [`${prefixCls}`]: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginBottom: '24px',
      '&-title': {
        display: 'flex',
        alignItems: 'center',
        height: '36px',
        color: '#181818',
        fontSize: '30px',
        lineHeight: '36px',
        '&-back-btn': {
          marginInlineStart: '8px',
        },
      },
    },
  };
};

export default function useStyle(prefixCls?: string) {
  const { getPrefixCls } = useContext(ConfigContext || ConfigProvider.ConfigContext);
  const antCls = `.${getPrefixCls()}`;

  return useAntdStyle('TitleComponent', (token) => {
    const titleComponentToken: TitleComponentToken = {
      ...token,
      prefixCls: `.${prefixCls}`,
      antCls,
    };

    return [genActionsStyle(titleComponentToken)];
  });
}
