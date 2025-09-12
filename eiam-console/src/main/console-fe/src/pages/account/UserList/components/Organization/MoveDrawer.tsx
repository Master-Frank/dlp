/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getChildOrganization, getRootOrganization } from '@/services/account';
import type { DataNode } from '@/utils/tree';
import { updateTreeData } from '@/utils/tree';
import { DownOutlined } from '@ant-design/icons';
import { DrawerForm } from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Alert, App, Spin, Tree } from 'antd';
import type { Key } from 'react';
import { useState } from 'react';
import { useIntl } from '@umijs/max';

const MoveDrawer = (props: {
  id: string;
  visible?: boolean;
  onCancel: () => void;
  onFinish: (keys: string) => Promise<boolean | void>;
}) => {
  const { id, visible, onCancel, onFinish } = props;
  const [loading, setLoading] = useState<boolean>(false);
  // 组织机构树
  const [organizationData, setOrganizationData] = useState<any[]>([]);
  // 展开节点
  const [expandedKeys, setExpandedKeys] = useState<Key[] | any>([]);
  const [loadedKeys, setLoadedKeys] = useState<Key[] | any>();
  const [selectedKeys, setSelectedKeys] = useState<Key[] | any>([]);
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(false);
  const intl = useIntl();
  const { message } = App.useApp();
  /**
   * 获取根组织数据
   */
  const getRootOrganizationData = async () => {
    // 查询根节点
    setLoading(true);
    const { success, result } = (await getRootOrganization()) || {};
    setLoading(false);
    if (success && result) {
      setOrganizationData([result]);
      setLoadedKeys([]);
      setExpandedKeys([result.id]);
      setSelectedKeys([result.id]);
      setAutoExpandParent(true);
    }
  };

  useAsyncEffect(async () => {
    if (visible) {
      await getRootOrganizationData();
    }
  }, [visible]);
  /**
   * 加载数据
   * @param key
   */
  const loadData = async (key: any) => {
    if (key === id) {
      return Promise.resolve();
    }
    setLoading(true);
    // 查询子节点
    const childResult = await getChildOrganization(key);
    if (childResult.success) {
      setOrganizationData((origin) => updateTreeData(origin, key, childResult.result, id));
    }
    setLoading(false);
    return Promise.resolve();
  };

  return (
    <DrawerForm
      preserve={false}
      drawerProps={{
        maskClosable: true,
        destroyOnClose: true,
        onClose: () => {
          setOrganizationData([]);
          onCancel();
        },
      }}
      onFinish={async () => {
        if (selectedKeys.length > 0) {
          return await onFinish(selectedKeys[0]);
        }
        message.warning(
          intl.formatMessage({
            id: 'pages.account.user_list.organization.move_drawer.on_finish.message',
          }),
        );
      }}
      title={intl.formatMessage({ id: 'pages.account.user_list.organization.move_drawer' })}
      width={530}
      open={visible}
    >
      <Alert
        banner
        type={'info'}
        message={intl.formatMessage({
          id: 'pages.account.user_list.organization.move_drawer.alert.message',
        })}
      />
      <br />
      <Spin spinning={loading}>
        <Tree<DataNode>
          blockNode
          fieldNames={{ key: 'id', title: 'name' }}
          showLine={{ showLeafIcon: false }}
          switcherIcon={<DownOutlined />}
          loadData={(treeNode) => loadData(treeNode.key)}
          loadedKeys={loadedKeys}
          onLoad={setLoadedKeys}
          selectedKeys={selectedKeys}
          treeData={organizationData}
          onExpand={(keys) => {
            setExpandedKeys(keys);
            setAutoExpandParent(false);
          }}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpandParent}
          onSelect={(key: Key[]) => {
            setSelectedKeys(key);
          }}
        />
      </Spin>
    </DrawerForm>
  );
};
export default MoveDrawer;
