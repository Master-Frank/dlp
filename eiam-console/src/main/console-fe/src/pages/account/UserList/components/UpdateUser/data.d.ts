/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
export type UpdateFormProps = {
  id: string;
  visible: boolean;
  onCancel: () => void;
  afterClose: () => void;
  onFinish: (success: boolean) => void;
};
