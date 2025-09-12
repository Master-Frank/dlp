/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { getChildOrganization, getSearchOrganizationTree } from '@/services/account';
import type { DataNode } from '@/utils/tree';
import { getTreeAllKeys, updateTreeData } from '@/utils/tree';
import { DownOutlined } from '@ant-design/icons';
import { Empty, Input, Spin, Tree } from 'antd';
import type { Key } from 'react';
import React, { useState } from 'react';
import useStyle from './style';
import classnames from 'classnames';
import { useIntl } from '@umijs/max';

const prefixCls = 'account-organization';
const { Search } = Input;

/**
 * 搜索 Tree props
 */
export type SearchTreeProps = {
  handleTitleRender: (node: DataNode) => React.ReactNode;
  onSearchChange: (search: boolean) => void;
  onSelect: (id: string | number, name: string) => void;
};

export default (props: SearchTreeProps) => {
  const { styles } = useStyle(prefixCls);
  const { handleTitleRender, onSearchChange, onSelect } = props;
  const [searchOrganizationData, setSearchOrganizationData] = useState<DataNode[] | any>([]);
  // 展开节点
  const [expandedKeys, setExpandedKeys] = useState<Key[]>();
  const [loadedKeys, setLoadedKeys] = useState<Key[]>();
  const [selectedKeys, setSelectedKeys] = useState<Key[]>();
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);
  const [search, setSearch] = useState<boolean>(false);
  const intl = useIntl();
  /**
   * 搜索组织机构树
   *
   * @param keyWord
   */
  const searchOrganizationTree = async (
    keyWord: string,
  ): Promise<AccountAPI.SearchOrganizationTree[]> => {
    // 查询子节点
    const { success, result } = await getSearchOrganizationTree(keyWord);
    if (success) {
      setSearchOrganizationData(result);
      setAutoExpandParent(true);
      //展开和keyword有关元素
      setExpandedKeys(getTreeAllKeys(result));
    }
    return Promise.resolve(result);
  };

  const loadData = async (key: any) => {
    setLoading(true);
    // 查询子节点
    const childResult = await getChildOrganization(key);
    if (childResult.success) {
      setSearchOrganizationData((origin: DataNode[]) =>
        updateTreeData(origin, key, childResult.result),
      );
    }
    setLoading(false);
    return Promise.resolve();
  };

  return (
    <div className={styles}>
      <div className={classnames(`${prefixCls}`)}>
        <Spin spinning={loading}>
          <Search
            allowClear
            placeholder={intl.formatMessage({
              id: 'pages.account.user_list.organization.search_tree.search.placeholder',
            })}
            onSearch={async (value) => {
              if (value) {
                setLoading(true);
                setSearch(true);
                onSearchChange(true);
                await searchOrganizationTree(value);
                setLoading(false);
                return;
              }
              setSearch(false);
              onSearchChange(false);
            }}
          />
          {search && searchOrganizationData?.length > 0 ? (
            <div className={classnames(`${prefixCls}-tree`)}>
              <Tree<DataNode>
                blockNode
                fieldNames={{ key: 'id', title: 'name' }}
                titleRender={handleTitleRender}
                treeData={searchOrganizationData}
                loadData={(treeNode) => loadData(treeNode.key)}
                showLine={{ showLeafIcon: false }}
                switcherIcon={<DownOutlined />}
                selectedKeys={selectedKeys}
                loadedKeys={loadedKeys}
                onLoad={setLoadedKeys}
                onSelect={(keys_, { node }) => {
                  setSelectedKeys(keys_);
                  // @ts-ignore
                  onSelect(node.key, node?.name);
                }}
                onExpand={(keys) => {
                  setExpandedKeys(keys);
                  setAutoExpandParent(false);
                }}
                expandedKeys={expandedKeys}
                autoExpandParent={autoExpandParent}
              />
            </div>
          ) : (
            search &&
            !loading &&
            searchOrganizationData?.length === 0 && (
              <Empty
                image="https://gw.alipayobjects.com/zos/antfincdn/ZHrcdLPrvN/empty.svg"
                imageStyle={{
                  height: 60,
                }}
              />
            )
          )}
        </Spin>
      </div>
    </div>
  );
};
