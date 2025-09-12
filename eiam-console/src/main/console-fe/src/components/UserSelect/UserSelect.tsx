/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { batchGetUser, getUserList } from '@/services/account';
import { Select, SelectProps, Spin } from 'antd';
import { debounce } from 'lodash';
import { useMemo, useRef, useState } from 'react';
import useStyle from './style';
import classNames from 'classnames';
import UserAvatar from '@/components/UserAvatar';
import { useAsyncEffect } from 'ahooks';

const prefixCls = 'topiam-user-select';
interface UserData {
  id: string;
  username: string;
  fullName: string;
  avatar: string;
  orgDisplayPath: string;
}
export type UserSelectProps = Omit<SelectProps, 'onSearch' | 'options' | 'popupClassName'>;
let currentValue: string | undefined;

const fetchUserList = async (username: string | undefined): Promise<UserData[]> => {
  currentValue = username;
  const { data } = await getUserList({ username: username, current: 1, pageSize: 20 });
  if (currentValue === username) {
    return data?.map((item) => {
      return {
        id: item.id,
        username: `${item.username}`,
        fullName: `${item.fullName}`,
        avatar: `${item.avatar}`,
        orgDisplayPath: `${item.orgDisplayPath}`,
      } as UserData;
    }) as UserData[];
  }
  return [];
};

const UserSelect = (props: UserSelectProps) => {
  const { wrapSSR, hashId } = useStyle(prefixCls);

  const [data, setData] = useState<UserData[]>([]);
  const [fetching, setFetching] = useState(false);
  const fetchRef = useRef(0);

  const searchUserList = (search?: string | undefined) => {
    fetchRef.current += 1;
    const fetchId = fetchRef.current;
    setData([]);
    setFetching(true);

    fetchUserList(search).then((result) => {
      if (fetchId !== fetchRef.current) {
        // for fetch callback order
        return;
      }
      setData(result);
      setFetching(false);
    });
  };
  const debounceFetcher = useMemo(() => {
    return debounce(searchUserList, 600);
  }, []);
  const handleSearch = async (search: string) => {
    if (search) {
      debounceFetcher(search);
    } else {
      setData([]);
    }
  };

  const options = data.map((d: UserData) => {
    return {
      fullName: d.fullName,
      label: (
        <div
          style={{
            display: 'inline-flex',
            alignItems: 'center',
          }}
        >
          <div
            style={{
              marginRight: '0.5rem',
            }}
          >
            <UserAvatar avatar={d.avatar} username={d.fullName} size={'default'} />
          </div>
          <div
            style={{
              display: 'block',
            }}
          >
            <span>{d.fullName}</span>
            <br />
            <span>{d.username}</span>
          </div>
        </div>
      ),
      value: d.id,
    };
  });

  useAsyncEffect(async () => {
    if (props?.value && props.value.length >= 0) {
      const { success, result } = await batchGetUser(props?.value);
      if (success) {
        setData(result);
      }
    }
  }, []);

  return wrapSSR(
    <Select
      showSearch
      defaultActiveFirstOption={false}
      suffixIcon={null}
      filterOption={false}
      onSearch={handleSearch}
      optionLabelProp="fullName"
      allowClear
      notFoundContent={fetching ? <Spin size="small" /> : null}
      options={options}
      onFocus={() => {
        searchUserList();
      }}
      onBlur={() => {
        setData([]);
      }}
      {...props}
      popupClassName={classNames(`${prefixCls}-popup`, hashId)}
    />,
  );
};

export default UserSelect;
