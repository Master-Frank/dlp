/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
type BaseCreateUserProps = {
  visible: boolean;
  organization?: { id: string | number; name: string };
  onCancel: () => void;
  onFinish: (success: boolean, continued: boolean) => void;
};
export type CreateUserProps = BaseCreateUserProps;
