/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Typography } from 'antd';

const { Text } = Typography;

const EllipsisMiddle = (props: { suffixCount: number; text: string }) => {
  const { suffixCount, text } = props;
  const start = text.slice(0, text.length - suffixCount).trim();
  const suffix = text.slice(-suffixCount).trim();
  return (
    <Text style={{ maxWidth: '100%' }} ellipsis={{ suffix, tooltip: true }} copyable={!!start}>
      {start ? start : '-'}
    </Text>
  );
};
export default EllipsisMiddle;
