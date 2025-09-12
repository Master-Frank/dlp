/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import React from 'react';
import { ProFormSelect, ProFormSelectProps } from '@ant-design/pro-components';
import { PhoneAreaCode } from './data.d';

export default (props: Omit<ProFormSelectProps<PhoneAreaCode>, 'request'>) => {
  const { initialValue = '+86' } = props;

  return (
    <ProFormSelect
      {...props}
      initialValue={initialValue}
      request={async () => {
        return [{ value: '+86', label: `+86 中国` }];
      }}
    />
  );
};
