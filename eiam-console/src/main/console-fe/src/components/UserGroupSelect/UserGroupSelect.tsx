/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getUserGroupList } from '@/services/account';
import type { SelectProps } from 'antd';
import { Select, Spin } from 'antd';
import { useState } from 'react';
import { useAsyncEffect } from 'ahooks';
import { SortOrder } from 'antd/es/table/interface';
import { RequestData } from '@ant-design/pro-components';

const { Option } = Select;

interface UserData {
  label: string;
  value: string;
}
export type UserGroupSelectProps<ValueType = UserData> = Omit<
  SelectProps<ValueType | ValueType[]>,
  'onSearch'
>;

async function getAllUserGroupList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AccountAPI.ListUserGroup>> {
  let pageSize = 100,
    current = 1;
  // 存储所有数据的数组
  let result: RequestData<AccountAPI.ListUserGroup> = {
    data: [],
    success: false,
    total: undefined,
  };

  while (true) {
    // 调用分页接口
    const { success, data, total } = await getUserGroupList(
      { current, pageSize, ...params },
      sort,
      filter,
    );
    if (success && data) {
      // 如果当前页没有数据，表示已经加载完全部数据，退出循环
      if (data?.length === 0) {
        break;
      }
      result = { data: result.data?.concat(data), success: success, total: total };
      // 增加当前页码
      if (total && total <= pageSize * current) {
        break;
      } else {
        current = current + 1;
      }
    }
  }

  return result;
}

const UserGroupSelect = (props: UserGroupSelectProps) => {
  const [data, setData] = useState<AccountAPI.ListUserGroup[]>([]);
  const [fetching, setFetching] = useState(false);

  useAsyncEffect(async () => {
    setFetching(true);
    const { success, data } = await getAllUserGroupList({}, {}, {}).finally(() => {
      setFetching(false);
    });
    if (success && data) {
      setData(data);
    }
  }, []);

  return (
    <Select
      showSearch
      defaultActiveFirstOption={false}
      suffixIcon={null}
      allowClear
      filterOption={(input, option) => {
        return ((option?.label as string) ?? '').toLowerCase().includes(input.toLowerCase());
      }}
      notFoundContent={fetching ? <Spin size="small" /> : null}
      {...props}
    >
      {data.map((d: AccountAPI.ListUserGroup) => (
        <Option key={d.id} value={d.id} label={d.name}>
          {d.name}
        </Option>
      ))}
    </Select>
  );
};

export default UserGroupSelect;
