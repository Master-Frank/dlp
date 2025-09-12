/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import { useState } from 'react';
import { GetApp } from './data.d';

export default function () {
  //APP 信息
  const [app, setApp] = useState<GetApp>();
  return {
    app: app,
    setApp: setApp,
  };
}
