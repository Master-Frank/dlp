/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProCard } from '@ant-design/pro-components';
import type { PieConfig } from '@ant-design/charts';
import { Pie } from '@ant-design/charts';
import { useState } from 'react';
import { useAsyncEffect } from 'ahooks';
import { getAuthnHotProvider } from '../../service';
import TimeRange from '../TimeRange';
import { Empty, Skeleton } from 'antd';
import { useIntl } from '@umijs/max';

export default () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [data, setData] = useState<Record<string, any>[]>([]);
  const intl = useIntl();

  const [rangePickerValue, setRangePickerValue] = useState<{
    startTime: string;
    endTime: string;
  }>();

  useAsyncEffect(async () => {
    if (rangePickerValue) {
      const { startTime, endTime } = rangePickerValue;
      setLoading(true);
      const { result, success } = await getAuthnHotProvider(startTime, endTime);
      if (success && result) {
        setData(result);
      }
      //延迟展示
      setTimeout(() => {
        setLoading(false);
      }, 90);
    }
  }, [rangePickerValue]);

  const hotAuthnProviderConfig: PieConfig = {
    height: 290,
    appendPadding: 10,
    data: data,
    angleField: 'count',
    colorField: 'name',
    radius: 1,
    innerRadius: 0.6,
    label: {
      type: 'inner',
      offset: '-50%',
      content: '{value}',
      style: {
        textAlign: 'center',
        fontSize: 14,
      },
    },
    statistic: {
      title: false,
      content: {
        style: {
          whiteSpace: 'pre-wrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
        },
        content: '',
      },
    },
    // 添加 中心统计文本 交互
    interactions: [
      {
        type: 'element-selected',
      },
      {
        type: 'element-active',
      },
      {
        type: 'pie-statistic-active',
      },
    ],
  };

  return (
    <ProCard
      title={intl.formatMessage({ id: 'pages.dashboard.analysis.hot_authn_provider.title' })}
      headerBordered
      extra={
        <TimeRange
          type={'today'}
          onChange={(startTime, endTime) => {
            setRangePickerValue({ startTime, endTime });
          }}
        />
      }
    >
      <Skeleton loading={loading} style={{ height: 290 }} active paragraph={{ rows: 7 }}>
        {data.length > 0 ? (
          <Pie {...hotAuthnProviderConfig} />
        ) : (
          <div style={{ height: 290, alignItems: 'center', display: 'grid' }}>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
          </div>
        )}
      </Skeleton>
    </ProCard>
  );
};
