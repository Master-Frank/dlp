/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { DataNode } from 'antd/es/tree';

export type CreateOrganizationFormProps<T> = {
  visible: boolean;
  onCancel?: () => void;
  onFinish: (formData: Partial<T>) => Promise<boolean | void>;
  currentNode?: DataNode | any;
};
