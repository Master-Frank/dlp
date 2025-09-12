/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Alert, Card } from 'antd';
import type { FC } from 'react';

import { PageContainer } from '@ant-design/pro-components';

const Workplace: FC = () => {
  return (
    <PageContainer>
      <Card>
        <Alert banner description={'欢迎使用 DLP 统一登录平台'} type={'success'} />
      </Card>
    </PageContainer>
  );
};

export default Workplace;
