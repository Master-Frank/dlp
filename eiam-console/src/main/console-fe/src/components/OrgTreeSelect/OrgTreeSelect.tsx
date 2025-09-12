/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getChildOrganization, getRootOrganization } from '@/services/account';
import { useAsyncEffect } from 'ahooks';
import { TreeSelect } from 'antd';
import type { TreeSelectProps } from 'antd/es/tree-select';
import { useState } from 'react';
import { DataNode, updateTreeData } from '@/utils/tree';

const { SHOW_ALL } = TreeSelect;

export type OrganizationTreeSelectProps = Omit<
  TreeSelectProps,
  'loadData' | 'treeData' | 'loading'
>;

const OrgTreeSelect = (props?: OrganizationTreeSelectProps) => {
  const [loading, setLoading] = useState<boolean>(false);
  // 组织机构树
  const [organizationData, setOrganizationData] = useState<DataNode[] | any>([]);
  useAsyncEffect(async () => {
    setLoading(true);
    const { success, result } =
      (await getRootOrganization().finally(() => {
        setLoading(false);
      })) || {};
    if (success && result) {
      setOrganizationData([result]);
    }
  }, []);

  /**
   * 加载数据
   * @param key
   */
  const loadData = async (key: any) => {
    setLoading(true);
    // 查询子节点
    const childResult = await getChildOrganization(key).finally(() => {
      setLoading(false);
    });
    if (childResult?.success) {
      setOrganizationData((origin: DataNode[]) => updateTreeData(origin, key, childResult.result));
    }
    return Promise.resolve();
  };

  return (
    <TreeSelect
      fieldNames={{ value: 'id', label: 'name' }}
      loadData={(treeNode) => loadData(treeNode.key)}
      loading={loading}
      treeData={organizationData}
      treeCheckable={true}
      showCheckedStrategy={SHOW_ALL}
      treeCheckStrictly={true}
      {...props}
    />
  );
};

export default OrgTreeSelect;
