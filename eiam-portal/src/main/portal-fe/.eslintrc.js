/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
module.exports = {
  extends: [require.resolve('@umijs/max/eslint')],
  globals: {
    page: true,
    REACT_APP_ENV: true,
  },
};
