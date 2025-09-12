/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import type React from 'react';
import type { DataNode as DataNode_ } from 'antd/es/tree';

export type DataNode = {
  id: string;
  name: string;
  parentId: string;
  children: DataNode[];
} & DataNode_;

/**
 * 更新树
 *
 * @param list
 * @param key
 * @param children
 * @param disabledId
 */
export function updateTreeData(
  list: DataNode[],
  key: React.Key,
  children: DataNode[] | any[] = [],
  disabledId?: string,
): DataNode[] {
  return list.map((node) => {
    const disabled = node.id === disabledId;
    const child = children.map((e) => {
      if (e.id === disabledId) {
        return {
          ...e,
          isLeaf: true,
          disabled: true,
        };
      }
      return e;
    });
    if (node.id === key) {
      return {
        ...node,
        disabled,
        isLeaf: false,
        children: disabled ? [] : child,
      };
    }
    if (node.children) {
      return {
        ...node,
        disabled,
        children: disabled ? [] : updateTreeData(node.children, key, children, disabledId),
      };
    }
    return node;
  });
}

/**
 * 获取Tree所有节点ID
 *
 * @param list
 */
export function getTreeAllKeys(list: DataNode[] | any): React.Key[] {
  const keys: React.Key[] = [];
  list.forEach((node: { id: React.Key; children: DataNode[] }): React.Key[] => {
    keys.push(node.id);
    if (node.children) {
      keys.push(...getTreeAllKeys(node.children));
    }
    return keys;
  });
  return keys;
}
