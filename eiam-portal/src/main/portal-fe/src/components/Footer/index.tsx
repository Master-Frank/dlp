/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
// import { GithubOutlined } from '@ant-design/icons';
import type { FooterProps } from '@ant-design/pro-components';
import { DefaultFooter } from '@ant-design/pro-components';

const currentYear = new Date().getFullYear();

export default (props: FooterProps) => (
  <DefaultFooter
    style={{ backgroundColor: 'transparent' }}
    copyright={`Copyright 2005-${currentYear} Charles有限公司`}
    links={[
      {
        key: 'website',
        title: '官方网站',
        href: 'https://baidu.com',
        blankTarget: true,
      },
      // {
      //   key: 'github',
      //   title: <GithubOutlined />,
      //   href: 'https://github.com/',
      //   blankTarget: true,
      // },
      // {
      //   key: 'docs',
      //   title: '使用文档',
      //   href: 'https://eiam.topiam.cn/docs/introduction/overview',
      //   blankTarget: true,
      // },
    ]}
    {...props}
  />
);
