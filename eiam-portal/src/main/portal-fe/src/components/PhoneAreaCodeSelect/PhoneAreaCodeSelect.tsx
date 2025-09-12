/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Select, Spin } from 'antd';
import { useAsyncEffect } from 'ahooks';
import { useState } from 'react';
import { PhoneAreaCode } from './data.d';
import { BaseOptionType, DefaultOptionType } from 'rc-select/lib/Select';

const { Option } = Select;

export interface PhoneAreaCodeProps<
  ValueType = any,
  OptionType extends BaseOptionType = DefaultOptionType,
> {
  defaultValue?: string;
  onChange?: (value: ValueType, option: OptionType | OptionType[]) => void;
}
export default (props: PhoneAreaCodeProps) => {
  const { defaultValue = '+86', onChange } = props;
  const [list, setList] = useState<PhoneAreaCode[]>();
  const [loading, setLoading] = useState<boolean>(true);
  useAsyncEffect(async () => {
    setLoading(true);
    setList([
      {
        englishName: 'China',
        chineseName: '中国',
        countryCode: 'CN',
        phoneCode: '+86',
      },
    ]);
    setLoading(false);
  }, []);
  return (
    <Spin spinning={loading}>
      <Select
        showSearch
        defaultValue={defaultValue}
        style={{ minWidth: '100px' }}
        onChange={onChange}
      >
        {list?.map((value) => {
          return (
            <Option value={value.phoneCode} key={value.countryCode}>
              {`${value.phoneCode} ${value.chineseName}`}
            </Option>
          );
        })}
      </Select>
    </Spin>
  );
};
