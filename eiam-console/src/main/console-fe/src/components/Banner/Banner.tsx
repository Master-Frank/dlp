/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { Alert } from 'antd';
import React from 'react';
import Marquee from 'react-fast-marquee';

const message = () => {
  return (
    <>
      <span>如果使用中遇到问题，请联系Charles，你的支持将是我们前行的动力。
      </span>
    </>
  );
};
export default (props: { play?: boolean }) => {
  const { play } = props;
  return (
    <Alert
      style={{
        padding: 0,
        background:
          'repeating-linear-gradient(35deg, hsl(196 120% 85%), hsl(196 120% 85%) 20px, hsl(196 120% 95%) 10px, hsl(196 120% 95%) 40px)',
      }}
      message={
        <div
          style={{
            whiteSpace: 'nowrap', //强制文本在一行内输出
            overflow: 'hidden', //隐藏溢出部分
            textOverflow: 'ellipsis', //对溢出部分加上...
            textAlign: 'center',
            color: 'black',
          }}
        >
          {play ? (
            <Marquee pauseOnHover gradient={false}>
              {message()}
            </Marquee>
          ) : (
            message()
          )}
        </div>
      }
      showIcon={false}
      banner
      type={'info'}
    />
  );
};
