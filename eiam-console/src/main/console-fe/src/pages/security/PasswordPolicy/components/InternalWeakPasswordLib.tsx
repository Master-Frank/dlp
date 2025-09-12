/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getWeakPasswordLib } from '../service';
import { useAsyncEffect } from 'ahooks';
import { List, Modal, Spin } from 'antd';
import * as React from 'react';
import { useState } from 'react';
import { useIntl } from '@umijs/max';

export type InternalWeakCipherProps = {
  visible: boolean;
  onCancel?: (e: React.MouseEvent<HTMLElement>) => void;
};
export default (props: InternalWeakCipherProps) => {
  const { visible, onCancel } = props;
  const [data, setData] = useState<string[]>();
  const [loading, setLoading] = useState<boolean>(true);
  const intl = useIntl();
  /** useAsyncEffect */
  useAsyncEffect(async () => {
    if (visible) {
      setLoading(true);
      const { success, result } = await getWeakPasswordLib();
      if (success && result) {
        const libraries = result?.map((value) => {
          return value.value;
        });
        setData(libraries);
      }
      setLoading(false);
    }
  }, [visible]);
  return (
    <Modal
      open={visible}
      title={intl.formatMessage({
        id: 'pages.setting.security.password_policy.weak_password_checking.password_library',
      })}
      footer={false}
      onCancel={onCancel}
      destroyOnClose
    >
      <Spin spinning={loading}>
        <List
          size="small"
          bordered={false}
          dataSource={data}
          pagination={{ simple: true }}
          renderItem={(item) => <List.Item>{item}</List.Item>}
        />
      </Spin>
    </Modal>
  );
};
