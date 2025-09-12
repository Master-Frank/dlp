/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getUserListNotInGroup } from '@/services/account';
import type { ProColumns } from '@ant-design/pro-components';
import { DrawerForm, ProTable } from '@ant-design/pro-components';

import { Alert, App, Space, Table, Tooltip } from 'antd';
import type { Key } from 'antd/es/table/interface';
import React, { useState } from 'react';
import useStyle from './style';
import classnames from 'classnames';
import Avatar from '@/components/UserAvatar';
import { useIntl } from '@umijs/max';

const prefixCls = 'user-group-add-member';

interface ModalProps {
  groupId: string;
  visible: boolean;
  onCancel: () => void;
  onFinish: (userIds: Key[]) => Promise<boolean>;
}

/**
 * 添加成员
 *
 * @param props
 * @constructor
 */
export default (props: ModalProps) => {
  const { groupId, onFinish, onCancel, visible } = props;
  // 目标选中键
  const [targetKeys, setTargetKeys] = useState<Key[]>([]);
  const { styles } = useStyle(prefixCls);
  const intl = useIntl();
  const { message } = App.useApp();
  /**
   * 列
   */
  const columns: ProColumns<AccountAPI.ListUser>[] = [
    {
      dataIndex: 'username',
      title: intl.formatMessage({ id: 'pages.account.user_group_detail.common.username' }),
      width: 130,
      fixed: 'left',
      filterSearch: true,
      ellipsis: true,
      render: (_dom, record) => (
        <Space>
          <Avatar avatar={record.avatar} username={record.username} />
          <Tooltip title={record.username}>
            <span>{record?.username}</span>
          </Tooltip>
        </Space>
      ),
    },
    {
      title: intl.formatMessage({
        id: 'pages.account.user_group_detail.add_member.columns.full_name',
      }),
      dataIndex: 'fullName',
      align: 'center',
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_group_detail.common.phone' }),
      dataIndex: 'phone',
      align: 'center',
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_group_detail.common.org_display_path' }),
      dataIndex: 'orgDisplayPath',
      ellipsis: true,
    },
  ];

  return (
    <DrawerForm
      open={visible}
      preserve={false}
      title={intl.formatMessage({
        id: 'pages.account.user_group_detail.add_member.drawer_form.title',
      })}
      width={650}
      drawerProps={{
        destroyOnClose: true,
        maskClosable: true,
        onClose: () => {
          setTargetKeys([]);
          onCancel();
        },
      }}
      className={styles.main}
      onFinish={async () => {
        if (targetKeys.length > 0) {
          const result = await onFinish(targetKeys);
          if (result) {
            setTargetKeys([]);
          }
          return result;
        }
        message.warning(
          intl.formatMessage({
            id: 'pages.account.user_group_detail.add_member.drawer_form.on_finish.message',
          }),
        );
        return false;
      }}
    >
      <div className={`${prefixCls}`}>
        <Alert
          banner
          type={'info'}
          message={intl.formatMessage({
            id: 'pages.account.user_group_detail.add_member.alert.message',
          })}
        />
        <ProTable
          className={classnames(`${prefixCls}-table`)}
          rowSelection={{
            selections: [Table.SELECTION_ALL, Table.SELECTION_INVERT],
            selectedRowKeys: targetKeys,
            onChange: (selectedRowKeys) => {
              setTargetKeys(selectedRowKeys);
            },
          }}
          scroll={{ x: 600 }}
          search={false}
          options={{
            setting: true,
            density: false,
            search: {
              placeholder: intl.formatMessage({
                id: 'pages.account.user_group_detail.add_member.pro_table.options.0.search.placeholder',
              }),
              name: 'keyword',
            },
          }}
          params={{ id: groupId }}
          tableAlertRender={false}
          columns={columns}
          rowKey="id"
          pagination={{ defaultPageSize: 10, showQuickJumper: false, showPrevNextJumpers: false }}
          request={getUserListNotInGroup}
        />
      </div>
    </DrawerForm>
  );
};
