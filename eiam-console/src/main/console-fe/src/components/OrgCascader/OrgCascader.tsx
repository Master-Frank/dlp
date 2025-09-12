/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getChildOrganization, getRootOrganization } from '@/services/account';
import { useAsyncEffect } from 'ahooks';
import { Cascader } from 'antd';
import type { CascaderProps, DefaultOptionType } from 'antd/es/cascader';
import { useState } from 'react';

export type OrganizationCascaderProps = Omit<
  CascaderProps<DefaultOptionType>,
  'options' | 'loadData' | 'fieldNames' | 'changeOnSelect' | 'onChange'
>;

const OrgCascader = (props?: OrganizationCascaderProps) => {
  const [options, setOptions] = useState<any>();
  const [loading, setLoading] = useState<boolean>(false);

  useAsyncEffect(async () => {
    setLoading(true);
    const { success, result } =
      (await getRootOrganization().finally(() => {
        setLoading(false);
      })) || {};
    if (success && result) {
      setOptions([result]);
    }
  }, []);

  const loadData = async (selectedOptions: string | any[]) => {
    setLoading(true);
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true; // load options lazily
    // 查询子节点
    const { success, result } = await getChildOrganization(targetOption.id).finally(() => {
      setLoading(false);
    });
    if (success && result) {
      targetOption.children = [...result];
      setOptions([...options]);
    }
    targetOption.loading = false;
  };

  return (
    <Cascader
      fieldNames={{ value: 'id', label: 'name' }}
      options={options}
      loadData={loadData}
      loading={loading}
      changeOnSelect
      showCheckedStrategy={'SHOW_CHILD'}
      {...props}
    />
  );
};

export default OrgCascader;
