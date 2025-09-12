/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Modal } from 'antd';
import React from 'react';

export type MailTemplateBrowseProps = {
  /** 标题 */
  title: string | React.ReactNode;
  /** 是否显示 */
  visible: boolean;
  /** 内容 */
  content: string;
  /** 取消方法 */
  onCancel: () => void;
};
export default (props: MailTemplateBrowseProps) => {
  const { title, visible, onCancel, content } = props;
  return (
    <Modal
      title={title}
      open={visible}
      closable
      destroyOnClose
      width="800px"
      onCancel={onCancel}
      onOk={onCancel}
    >
      {/* eslint-disable-next-line react/no-danger */}
      <div dangerouslySetInnerHTML={{ __html: content }} />
    </Modal>
  );
};
