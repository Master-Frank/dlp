/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { createUserGroup, getUserGroupList, removeUserGroup } from '@/services/account';
import { history } from '@@/core/history';
import { PlusOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, type ProColumns, ProTable } from '@ant-design/pro-components';
import { App, Button, Space, Table } from 'antd';
import React, { useRef, useState } from 'react';
import AddUserGroup from './components/AddUserGroup';
import { useIntl } from '@umijs/max';

/**
 * onDetailClick
 * @param id
 */
const onDetailClick = (id: string) => {
  history.push(`/account/user-group/detail?id=${id}&type=member`);
};

const UserGroupList = () => {
  const intl = useIntl();
  const { message, modal } = App.useApp();
  const actionRef = useRef<ActionType>();
  const [addUserGroupVisible, setAddUserGroupVisible] = useState<boolean>(false);
  const [loading, setLoading] = useState(false);

  const columns: ProColumns<AccountAPI.ListUserGroup>[] = [
    {
      title: intl.formatMessage({
        id: 'pages.account.user_group_list.column.name',
      }),
      dataIndex: 'name',
      ellipsis: true,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({
        id: 'pages.account.user_group_list.column.code',
      }),
      dataIndex: 'code',
      search: false,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_group_list.column.remark' }),
      dataIndex: 'remark',
      search: false,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({
        id: 'pages.account.user_group_list.column.create_time',
      }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      align: 'center',
      search: false,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({
        id: 'pages.account.user_group_list.column.option',
      }),
      valueType: 'option',
      key: 'option',
      width: 120,
      align: 'center',
      render: (_text, record) => {
        return (
          <Space>
            <a
              key={'detail'}
              onClick={() => {
                onDetailClick(record.id);
              }}
            >
              {intl.formatMessage({ id: 'app.detail' })}
            </a>
            <a
              key="remove"
              style={{
                color: 'red',
              }}
              onClick={() => {
                modal.error({
                  title: intl.formatMessage({
                    id: 'pages.account.user_group_list.column.option.delete_title',
                  }),
                  content: intl.formatMessage({
                    id: 'pages.account.user_group_list.column.option.delete_content',
                  }),
                  okText: intl.formatMessage({ id: 'app.confirm' }),
                  okType: 'primary',
                  cancelText: intl.formatMessage({ id: 'app.cancel' }),
                  centered: true,
                  okCancel: true,
                  onOk: async () => {
                    setLoading(true);
                    const { success } = await removeUserGroup(record.id)
                      .catch(({ response: { data } }) => {
                        return data;
                      })
                      .finally(() => {
                        setLoading(false);
                      });
                    if (success) {
                      message.success(intl.formatMessage({ id: 'app.operation_success' }));
                      actionRef.current?.reload();
                      return;
                    }
                  },
                });
              }}
            >
              {intl.formatMessage({ id: 'app.delete' })}
            </a>
          </Space>
        );
      },
    },
  ];

  return (
    <PageContainer content={intl.formatMessage({ id: 'pages.account.user_group_list.desc' })}>
      <ProTable<AccountAPI.ListUserGroup>
        actionRef={actionRef}
        columns={columns}
        pagination={{
          size: 'small',
          defaultPageSize: 10,
          showSizeChanger: false,
        }}
        rowKey="id"
        toolBarRender={() => {
          return [
            <Button
              key={'create'}
              type={'primary'}
              onClick={() => {
                setAddUserGroupVisible(true);
              }}
            >
              <PlusOutlined />
              {intl.formatMessage({
                id: 'pages.account.user_group_list.create_button',
              })}
            </Button>,
          ];
        }}
        loading={loading}
        onLoadingChange={(loading) => {
          if (typeof loading === 'boolean') {
            setLoading(loading);
          }
        }}
        form={{
          // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
          syncToUrl: (values, type) => {
            if (type === 'get') {
              return {
                ...values,
              };
            }
            return values;
          },
        }}
        request={(params, sort, filter) => {
          return getUserGroupList(params, sort, filter);
        }}
        rowSelection={{
          // 自定义选择项参考: https://ant.design/components/table-cn/#components-table-demo-row-selection-custom
          // 注释该行则默认不显示下拉选项
          selections: [Table.SELECTION_ALL, Table.SELECTION_INVERT],
        }}
      />
      {/*新增用户组*/}
      <AddUserGroup
        visible={addUserGroupVisible}
        onFinish={async (values: AccountAPI.BaseUserGroup) => {
          try {
            let success: boolean;
            const result = await createUserGroup(values);
            success = result.success;
            actionRef.current?.reload();
            if (success) {
              message.success(intl.formatMessage({ id: 'app.operation_success' }));
              setAddUserGroupVisible(false);
              return true;
            }
            return false;
          } catch (e) {
            return false;
          }
        }}
        onCancel={() => {
          setAddUserGroupVisible(false);
        }}
      />
    </PageContainer>
  );
};
export default UserGroupList;
