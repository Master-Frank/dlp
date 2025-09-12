/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Button } from 'antd';
import useStyle from './style';
import { useIntl } from '@@/exports';

const prefixCls = 'topiam-forget-password';

const Success = ({ close }: { close: () => void }) => {
  const { styles } = useStyle(prefixCls);
  const intl = useIntl();
  return (
    <div className={styles.main}>
      <div className={`${prefixCls}-success`}>
        <div className={`${prefixCls}-success-title`}>
          {intl.formatMessage({ id: 'pages.login.forget-password.success' })}
        </div>
        <div className={`${prefixCls}-success-desc`}>
          {intl.formatMessage({ id: 'pages.login.forget-password.success.desc' })}
        </div>
        <Button
          type="primary"
          className={`${prefixCls}-success-button`}
          size={'large'}
          onClick={() => close()}
        >
          {intl.formatMessage({ id: 'app.return' })}
        </Button>
      </div>
    </div>
  );
};
export default Success;
