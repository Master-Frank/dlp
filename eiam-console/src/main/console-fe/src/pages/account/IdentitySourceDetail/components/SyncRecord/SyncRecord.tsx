/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getIdentitySourceSyncRecordList } from '../../service';
import {
  CheckCircleOutlined,
  CloseCircleOutlined,
  ExclamationCircleOutlined,
} from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { Drawer, Tag } from 'antd';
import React, { useRef } from 'react';
import { useIntl } from '@umijs/max';
import { createStyles } from 'antd-style';

interface SyncRecordProps {
  syncHistoryId: string;
  open: boolean;
  onClose: (e: React.MouseEvent | React.KeyboardEvent) => void;
  objectType: string;
}

const useStyle = createStyles(({ prefixCls }) => {
  const antCls = `.${prefixCls}`;

  return {
    main: {
      [`${antCls}-pro-card ${antCls}-pro-card-body`]: {
        padding: '24px 0px 0px',
      },
    },
  };
});

export default (props: SyncRecordProps) => {
  const { syncHistoryId, open, onClose, objectType } = props;
  const actionRef = useRef<ActionType>();
  const intl = useIntl();

  const columns: ProColumns<AccountAPI.ListIdentitySourceSyncRecord>[] = [
    {
      title: intl.formatMessage({ id: 'pages.account.identity_source_detail.common.action_type' }),
      dataIndex: 'actionType',
      align: 'center',
      width: 80,
      ellipsis: true,
      valueType: 'select',
      valueEnum: {
        insert: { text: intl.formatMessage({ id: 'app.create' }) },
        update: { text: intl.formatMessage({ id: 'app.update' }) },
        delete: { text: intl.formatMessage({ id: 'app.delete' }) },
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.account.identity_source_detail.common.object_id' }),
      dataIndex: 'objectId',
      ellipsis: true,
      search: false,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.identity_source_detail.common.object_name' }),
      dataIndex: 'objectName',
      ellipsis: true,
      search: false,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.identity_source_detail.common.desc' }),
      dataIndex: 'desc',
      ellipsis: true,
      search: false,
    },
    {
      title: intl.formatMessage({
        id: 'pages.account.identity_source_detail.sync_record.columns.status',
      }),
      dataIndex: 'status',
      align: 'center',
      valueType: 'select',
      width: 80,
      valueEnum: {
        success: { text: intl.formatMessage({ id: 'app.success' }) },
        fail: { text: intl.formatMessage({ id: 'app.fail' }) },
        skip: { text: intl.formatMessage({ id: 'app.skip' }) },
      },
      renderText: (text: any) => (
        <>
          {text === 'success' && (
            <Tag icon={<CheckCircleOutlined />} color="#87d068">
              {intl.formatMessage({ id: 'app.success' })}
            </Tag>
          )}
          {text === 'fail' && (
            <Tag icon={<CloseCircleOutlined />} color="#e54545">
              {intl.formatMessage({ id: 'app.fail' })}
            </Tag>
          )}
          {text === 'skip' && (
            <Tag icon={<ExclamationCircleOutlined />} color="#faad14">
              {intl.formatMessage({ id: 'app.skip' })}
            </Tag>
          )}
        </>
      ),
    },
  ];

  const { styles } = useStyle();

  return (
    <Drawer
      open={open}
      title={intl.formatMessage({
        id: 'pages.account.identity_source_detail.sync_record.drawer.title',
      })}
      onClose={onClose}
      width={630}
      destroyOnClose
      styles={{
        body: {
          paddingTop: 0,
        },
      }}
    >
      <ProTable<AccountAPI.ListIdentitySourceSyncRecord>
        actionRef={actionRef}
        className={styles.main}
        columns={columns}
        search={{ filterType: 'light' }}
        params={{ syncHistoryId, objectType }}
        request={getIdentitySourceSyncRecordList}
        rowKey={'id'}
        pagination={{ defaultPageSize: 10, showQuickJumper: false }}
      />
    </Drawer>
  );
};
