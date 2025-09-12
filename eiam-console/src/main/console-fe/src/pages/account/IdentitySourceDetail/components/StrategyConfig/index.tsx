/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getChildOrganization, getRootOrganization } from '@/services/account';
import { updateTreeData } from '@/utils/tree';
import { ProFormItem, ProFormSwitch, ProFormText } from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Divider, TreeSelect } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from '@umijs/max';

export default (props: { configured: boolean }) => {
  const { configured } = props;
  useEffect(() => {}, [configured]);
  // 组织机构树
  const [organizationData, setOrganizationData] = useState<any[]>([]);
  const intl = useIntl();

  /**
   * 获取根组织数据
   */
  const getRootOrganizationData = async () => {
    // 查询根节点
    const { success, result } = (await getRootOrganization()) || {};
    if (success && result) {
      setOrganizationData([result]);
    }
  };

  /**
   * 加载数据
   * @param key
   */
  const loadData = async (key: any) => {
    // 查询子节点
    const childResult = await getChildOrganization(key);
    if (childResult.success) {
      setOrganizationData((origin) => updateTreeData(origin, key, childResult.result));
    }
    return Promise.resolve();
  };

  useAsyncEffect(async () => {
    await getRootOrganizationData();
  }, []);
  const targetExtra = (
    <span>
      <>
        {intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.target_extra.0',
        })}
      </>
      <br />
      <>
        {intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.target_extra.1',
        })}
      </>{' '}
    </span>
  );
  return (
    <>
      <Divider orientation="left" plain>
        {intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.organization_policies',
        })}
      </Divider>
      <ProFormItem
        name={['strategyConfig', 'organization', 'targetId']}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.organization_policies.target_id',
        })}
        extra={targetExtra}
      >
        <TreeSelect
          placeholder={intl.formatMessage({
            id: 'pages.account.identity_source_detail.strategy_config.organization_policies.target_id.placeholder',
          })}
          allowClear
          labelInValue
          loadData={(i) => {
            return loadData(i.id);
          }}
          treeData={organizationData}
          treeNodeFilterProp={'name'}
          fieldNames={{ label: 'name', value: 'id' }}
        />
      </ProFormItem>
      <Divider orientation="left" plain>
        {intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.user_policies',
        })}
      </Divider>
      <ProFormText.Password
        name={['strategyConfig', 'user', 'defaultPassword']}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.user_policies.default_password',
        })}
        extra={intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.user_policies.default_password.extra',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.user_policies.default_password.placeholder',
        })}
        fieldProps={{
          autoComplete: 'new-password',
        }}
      />
      <ProFormSwitch
        name={['strategyConfig', 'user', 'enabled']}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.user_policies.enabled',
        })}
        initialValue={true}
        extra={
          <>
            <span>
              {intl.formatMessage({
                id: 'pages.account.identity_source_detail.strategy_config.user_policies.enabled.extra.0',
              })}
            </span>
            <br />
            <span style={{ color: 'red' }}>
              {intl.formatMessage({
                id: 'pages.account.identity_source_detail.strategy_config.user_policies.enabled.extra.1',
              })}
            </span>
          </>
        }
      />
      <ProFormSwitch
        name={['strategyConfig', 'user', 'emailNotify']}
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.user_policies.email_notify',
        })}
        initialValue={true}
        extra={intl.formatMessage({
          id: 'pages.account.identity_source_detail.strategy_config.user_policies.email_notify.extra',
        })}
      />
    </>
  );
};
