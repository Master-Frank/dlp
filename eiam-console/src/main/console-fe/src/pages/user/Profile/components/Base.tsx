/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { ProForm, ProFormText } from '@ant-design/pro-components';
import { App, Form, Skeleton } from 'antd';
import { useState } from 'react';

import { changeBaseInfo } from '../service';
import { useAsyncEffect } from 'ahooks';
import { useModel } from '@umijs/max';
import { useIntl } from '@@/exports';
import { createStyles } from 'antd-style';

const prefixCls = 'account-base';

const useStyles = createStyles(({ token }, props) => {
  const prefixClassName = `.${props}`;
  return {
    accountBase: {
      display: 'flex',
      paddingTop: '12px',
      [`@media screen and (max-width: ${token.screenMD}px)`]: {
        flexDirection: 'column-reverse',
      },
      [`${prefixClassName}-left`]: {
        minWidth: '224px',
        maxWidth: '448px',
      },
    },
  };
});

export const FORM_ITEM_LAYOUT = {
  labelCol: {
    span: 5,
  },
  wrapperCol: {
    span: 19,
  },
};

const BaseView = () => {
  const intl = useIntl();
  const useApp = App.useApp();
  const { styles, cx } = useStyles(prefixCls);
  const [loading, setLoading] = useState<boolean>();
  const { initialState, setInitialState } = useModel('@@initialState');

  useAsyncEffect(async () => {
    setLoading(true);
    if (initialState && initialState.currentUser) {
      setTimeout(async () => {
        setLoading(false);
      }, 500);
    }
  }, [initialState]);

  const handleFinish = async (values: Record<string, string>) => {
    const { success } = await changeBaseInfo({
      fullName: values.fullName,
      nickName: values.nickName,
    });
    if (success) {
      useApp.message.success(intl.formatMessage({ id: 'app.update_success' }));
      //获取当前用户信息
      const currentUser = await initialState?.fetchUserInfo?.();
      await setInitialState((s: any) => ({ ...s, currentUser: currentUser }));
    }
  };

  return (
    <div className={styles.accountBase}>
      {loading ? (
        <Skeleton paragraph={{ rows: 8 }} active />
      ) : (
        <>
          <div className={cx(`${prefixCls}-left`)}>
            <ProForm
              layout="horizontal"
              labelAlign={'left'}
              {...FORM_ITEM_LAYOUT}
              onFinish={handleFinish}
              submitter={{
                render: (_p, dom) => {
                  return <Form.Item wrapperCol={{ span: 19, offset: 5 }}>{dom}</Form.Item>;
                },
                searchConfig: {
                  submitText: intl.formatMessage({
                    id: 'page.user.profile.base.form.update_button',
                  }),
                },
                resetButtonProps: {
                  style: {
                    display: 'none',
                  },
                },
              }}
              initialValues={{
                ...initialState?.currentUser,
                phone: initialState?.currentUser?.phone?.split('-'),
              }}
              requiredMark={false}
            >
              <ProFormText
                width="md"
                name="accountId"
                readonly
                label={intl.formatMessage({ id: 'page.user.profile.base.form.account_id' })}
              />
              <ProFormText
                width="md"
                name="username"
                readonly
                label={intl.formatMessage({ id: 'page.user.profile.base.form.username' })}
              />
              <ProFormText
                width="md"
                name="email"
                readonly
                label={intl.formatMessage({ id: 'page.user.profile.base.form.email' })}
              />
              <ProFormText
                width="md"
                name="phone"
                readonly
                label={intl.formatMessage({ id: 'page.user.profile.base.form.phone' })}
              />
              <ProFormText
                width="md"
                name="fullName"
                label={intl.formatMessage({ id: 'page.user.profile.base.form.full_name' })}
                allowClear={false}
                rules={[
                  {
                    required: true,
                    message: intl.formatMessage({
                      id: 'page.user.profile.base.form.full_name.rule.0',
                    }),
                  },
                ]}
              />
              <ProFormText
                width="md"
                name="nickName"
                label={intl.formatMessage({ id: 'page.user.profile.base.form.nick_name' })}
                allowClear={false}
              />
            </ProForm>
          </div>
        </>
      )}
    </div>
  );
};

export default BaseView;
