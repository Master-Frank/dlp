/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { App, Image } from 'antd';
import React from 'react';
import { useAsyncEffect } from 'ahooks';

type IProps = {
  children: React.JSX.Element;
};

const PageContainer: React.FC<IProps> = (props) => {
  const { notification } = App.useApp();

  useAsyncEffect(async () => {
    notification.open({
      key: 'notification',
      message: '提示',
      duration: null,
      placement: 'bottomRight',
      description: (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <Image src={'/wecomqrcode.jpg'} width={180} preview={false} />
          <div>
            <span style={{ color: '#1890FF', textAlign: 'center', display: 'block' }}>
              如果使用中遇到问题，联系Charles
            </span>
          </div>
        </div>
      ),
      style: {
        width: 220,
        marginBottom: 0,
        padding: 15,
      },
    });
  }, []);
  return <>{props.children}</>;
};
export default PageContainer;
