/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import type { GenerateStyle, ProAliasToken } from '@ant-design/pro-components';
import { useStyle as useAntdStyle } from '@ant-design/pro-components';
import { ConfigProvider } from 'antd';
import { useContext } from 'react';

const { ConfigContext } = ConfigProvider;

interface IconFontComponentToken extends ProAliasToken {
  antCls: string;
  prefixCls: string;
}

const genActionsStyle: GenerateStyle<IconFontComponentToken> = (token) => {
  const { prefixCls } = token;

  return {
    [`${prefixCls}`]: {
      width: '1.5em',
      height: '1.5em',
      overflow: 'hidden',
      fontSize: '21px',
      verticalAlign: '-0.15em',
      fill: 'currentColor',
      display: 'inherit',
    },
  };
};

export default function useStyle(prefixCls?: string) {
  const { getPrefixCls } = useContext(ConfigContext || ConfigProvider.ConfigContext);
  const antCls = `.${getPrefixCls()}`;

  return useAntdStyle('IconFontComponent', (token) => {
    const iconFontComponentToken: IconFontComponentToken = {
      ...token,
      prefixCls: `.${prefixCls}`,
      antCls,
    };

    return [genActionsStyle(iconFontComponentToken)];
  });
}
