--
-- ulp-portal - United Login Platform
-- Copyright (c) 2022-Present Frank Zhang
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- OIDC 集成测试种子数据
-- 两个应用：
--   test-app-id-1 / test-oidc-app / test-client      —— 正常 happy path、invalid client id、mismatched redirect
--   test-app-id-2 / test-oidc-expired / expired-client —— authorization_code_time_to_live=1ns，授权码立即过期
-- 一份 RSA 2048 keypair 同时挂在两个应用上（cert 行复制两份）
-- Hibernate Duration 列存纳秒；JSON 列 (client_auth_methods / auth_grant_types / response_types / grant_scopes)
-- 以 JSON 字符串写入；redirect_uris / post_logout_redirect_uris 在 schema 中是 TEXT 但实体侧带 @JdbcTypeCode(JSON)，
-- 也用 JSON 字符串。
-- 配合 @Sql 在每个测试方法的 Spring 事务内执行；@Transactional 测试结束回滚 → 互不污染。
-- 先 DELETE，幂等地处理同一 JVM 内多次执行。
--
-- 注意：PEM 写成单行 + \n 转义，不要写多行字面量。
-- 原因：Spring 的 ScriptUtils.splitSqlScript() 会把字面量内部的换行折叠成空格，
-- 把 -----BEGIN-----\nMIIE...\n----END----- 搞成 -----BEGIN----- MIIE... ----END-----，
-- MySQL 当场拒。改成单行后由 MySQL 自己 unescape \n，PEMParser 读到正确格式。

DELETE FROM ulp_app_cert        WHERE id_ IN ('test-cert-id-1', 'test-cert-id-2');
DELETE FROM ulp_app_oidc_config WHERE id_ IN ('test-oidc-cfg-1', 'test-oidc-cfg-2');
DELETE FROM ulp_app             WHERE id_ IN ('test-app-id-1', 'test-app-id-2');

INSERT INTO ulp_app (
    id_, name_, code_, client_id, client_secret, protocol_, type_, template_,
    init_login_type, authorization_type, is_configured, is_enabled,
    create_by, update_by
) VALUES
('test-app-id-1', 'Test OIDC App', 'test-oidc-app', 'test-client', 'test-secret',
 'oidc', 'standard', 'oidc', 'PORTAL_OR_APP', 'AUTHORIZATION', 1, 1,
 'test', 'test'),
('test-app-id-2', 'Test OIDC Expired', 'test-oidc-expired', 'expired-client', 'expired-secret',
 'oidc', 'standard', 'oidc', 'PORTAL_OR_APP', 'AUTHORIZATION', 1, 1,
 'test', 'test');

INSERT INTO ulp_app_oidc_config (
    id_, app_id,
    client_auth_methods, auth_grant_types, response_types, redirect_uris,
    post_logout_redirect_uris, grant_scopes,
    require_auth_consent, require_proof_key,
    token_endpoint_auth_signing_algorithm,
    refresh_token_time_to_live, authorization_code_time_to_live, device_code_time_to_live,
    access_token_time_to_live, id_token_time_to_live,
    id_token_signature_algorithm, access_token_format, reuse_refresh_token,
    create_by, update_by
) VALUES
('test-oidc-cfg-1', 'test-app-id-1',
 '["client_secret_basic"]', '["authorization_code"]', '["code"]', '["http://localhost/callback"]',
 '[]', '["openid","profile"]',
 false, false,
 'RS256',
 3600000000000, 300000000000, 300000000000,
 3600000000000, 3600000000000,
 'RS256', 'self-contained', false,
 'test', 'test'),
('test-oidc-cfg-2', 'test-app-id-2',
 '["client_secret_basic"]', '["authorization_code"]', '["code"]', '["http://localhost/callback"]',
 '[]', '["openid"]',
 false, false,
 'RS256',
 3600000000000, 1000000000, 300000000000,
 3600000000000, 3600000000000,
 'RS256', 'self-contained', false,
 'test', 'test');

-- 同一对 PEM 喂给两个应用（测试场景下不需要为每个 app 单独生成密钥）
INSERT INTO ulp_app_cert (
    id_, app_id, using_type, key_long, sign_algo, private_key, public_key,
    create_by, update_by
) VALUES
('test-cert-id-1', 'test-app-id-1', 'oidc_jwk', 2048, 'RSA',
 '-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD9pNauPVXka2q5\n5KmNPkf6dLSpx7CYdUD0OIg8paSt2DZt5oBtkxgc+E0GWGLqt/DmM0XaW0xH4l4v\n1lsF6a2ZugWisQQEi87qgZMCivK9rffqTfZZU1dyMf4a9UcOhWi4ZJEo8ZEn1mdz\ngAXGPVUOWp19iGncc1mIhhi9c1bpAtoy9BbVUsN6uBKOsDmt6UtH4QSisIh2z32k\nORvy2uUn2m0m637SOFacz8E3WIYKtJqKNuT2U1RkPtD1VvCqDrM+A/siJyZ1M9aC\nLu1YT0yylc1Qvqq+evRYx8JVaJWoqCcjdxjAiTNRXvt6BNKRgcXKKwQYBLlrendJ\nDasGhVgpAgMBAAECggEAAS56MAoEhd8Auns1KtLlkwYlvHfmoRKEbLwnLqYkY17D\nQ9A2h1wk2Sddyifm/7op4X6ku94f7Q1MnEXauxyHWh9urN8iJPMcRzAhlc9Seb2k\nhCHxwfaEYk7XOiZWmtvA/OvorQjGtklrxl2sLowFAt87RiqN+LCNCW4Q0drGfBPM\nkzmmtjcx0YlGpD45tuQJkvS4lKvcbQlDWudhp6Hcm/cV6i0tbN2g0At/uaXNxiuh\nwfoXIOFLtd3wqaCIF3onw+9PlLTxk5DsZHuihvCG92YTRyH4UhDMlhq4MwX1a02w\niD+lxdq3VLsNcBoDO7cVM3be131KQinEJph/KowFcQKBgQD/xWcDsW/vYgmpH6w1\nGFrHuSkbeHUFoUoHZHMTPNfLhv30Ris4PUbiQkxCD4zQkC/NOePu/saNUVhYHZSJ\nwxefBFnjUHr/F1Hewse4u0qE9vcg1xuPI3qX58BSSfenbMODjdnUmdZo7MLmtFD9\njFmXEKVz/VKO04zbgmpou5PIkQKBgQD93vLn23cv3O0Df/fKNUM23xNrwN6YjDMz\ng8/EkWHB+JrjFUrUVEK/kooeSSNv5rMfMKgf0Glgz1de92QpeLv30F0J7u5V9696\n9Aid0qy+AxIR3oIzBRTZC8NdrDQ4MLD2O5CZpVzKFpVNI8zqzlEYxzpTmiYPzKF9\n+9TT6vuiGQKBgQC5TtI18MaCj2skZ1gjF8Qd098ekgVm0NaLyJE/LOPEB8fSxUvm\n8S58G0CY1B9XtD+N1xV3QIumM3toS/YkYX6prUNa2CJk0wZz+HcvNjLlZvDhkDfd\nWv0lNbk3ZXPSj5CPraRWziZz2qXS9G2BZcA7HMpi4PSBmnABUdm6i7ykoQKBgHDe\nO15ry1yjO1jP/wmOjpiJqye/8vcddfIUSz4YaL8FWU9WexNVduuXKgL2/2NTzRUz\n27txPDiHVk/pa0Wo4OD3aTXuXVYpLYJblq0cKiK8WL9LDtXCD5fDzBMMaZcFxtdi\nehJlW5CZY72NCiDmo1WB1eOvZ/akQrQxT1j8Yu2ZAoGAM3r6G7siOC31rcdXOSTO\nIElnTU5jZVdo9Zqk2XlUEC5S0olqMsV2niCMznL8oGgmy62NG6Nhuli7AUM4Zjig\nHh9YQRsfvsLJ1DE1LGfgbzT9P9qUD9F3YiaFJpSAy74qm4stS7keUuLdR5I298hU\n/fqIEBL5awwXWxEQPUCuTPU=\n-----END PRIVATE KEY-----\n',
 '-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA/aTWrj1V5GtqueSpjT5H\n+nS0qcewmHVA9DiIPKWkrdg2beaAbZMYHPhNBlhi6rfw5jNF2ltMR+JeL9ZbBemt\nmboForEEBIvO6oGTAoryva336k32WVNXcjH+GvVHDoVouGSRKPGRJ9Znc4AFxj1V\nDlqdfYhp3HNZiIYYvXNW6QLaMvQW1VLDergSjrA5relLR+EEorCIds99pDkb8trl\nJ9ptJut+0jhWnM/BN1iGCrSaijbk9lNUZD7Q9Vbwqg6zPgP7IicmdTPWgi7tWE9M\nspXNUL6qvnr0WMfCVWiVqKgnI3cYwIkzUV77egTSkYHFyisEGAS5a3p3SQ2rBoVY\nKQIDAQAB\n-----END PUBLIC KEY-----\n',
 'test', 'test'),
('test-cert-id-2', 'test-app-id-2', 'oidc_jwk', 2048, 'RSA',
 '-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD9pNauPVXka2q5\n5KmNPkf6dLSpx7CYdUD0OIg8paSt2DZt5oBtkxgc+E0GWGLqt/DmM0XaW0xH4l4v\n1lsF6a2ZugWisQQEi87qgZMCivK9rffqTfZZU1dyMf4a9UcOhWi4ZJEo8ZEn1mdz\ngAXGPVUOWp19iGncc1mIhhi9c1bpAtoy9BbVUsN6uBKOsDmt6UtH4QSisIh2z32k\nORvy2uUn2m0m637SOFacz8E3WIYKtJqKNuT2U1RkPtD1VvCqDrM+A/siJyZ1M9aC\nLu1YT0yylc1Qvqq+evRYx8JVaJWoqCcjdxjAiTNRXvt6BNKRgcXKKwQYBLlrendJ\nDasGhVgpAgMBAAECggEAAS56MAoEhd8Auns1KtLlkwYlvHfmoRKEbLwnLqYkY17D\nQ9A2h1wk2Sddyifm/7op4X6ku94f7Q1MnEXauxyHWh9urN8iJPMcRzAhlc9Seb2k\nhCHxwfaEYk7XOiZWmtvA/OvorQjGtklrxl2sLowFAt87RiqN+LCNCW4Q0drGfBPM\nkzmmtjcx0YlGpD45tuQJkvS4lKvcbQlDWudhp6Hcm/cV6i0tbN2g0At/uaXNxiuh\nwfoXIOFLtd3wqaCIF3onw+9PlLTxk5DsZHuihvCG92YTRyH4UhDMlhq4MwX1a02w\niD+lxdq3VLsNcBoDO7cVM3be131KQinEJph/KowFcQKBgQD/xWcDsW/vYgmpH6w1\nGFrHuSkbeHUFoUoHZHMTPNfLhv30Ris4PUbiQkxCD4zQkC/NOePu/saNUVhYHZSJ\nwxefBFnjUHr/F1Hewse4u0qE9vcg1xuPI3qX58BSSfenbMODjdnUmdZo7MLmtFD9\njFmXEKVz/VKO04zbgmpou5PIkQKBgQD93vLn23cv3O0Df/fKNUM23xNrwN6YjDMz\ng8/EkWHB+JrjFUrUVEK/kooeSSNv5rMfMKgf0Glgz1de92QpeLv30F0J7u5V9696\n9Aid0qy+AxIR3oIzBRTZC8NdrDQ4MLD2O5CZpVzKFpVNI8zqzlEYxzpTmiYPzKF9\n+9TT6vuiGQKBgQC5TtI18MaCj2skZ1gjF8Qd098ekgVm0NaLyJE/LOPEB8fSxUvm\n8S58G0CY1B9XtD+N1xV3QIumM3toS/YkYX6prUNa2CJk0wZz+HcvNjLlZvDhkDfd\nWv0lNbk3ZXPSj5CPraRWziZz2qXS9G2BZcA7HMpi4PSBmnABUdm6i7ykoQKBgHDe\nO15ry1yjO1jP/wmOjpiJqye/8vcddfIUSz4YaL8FWU9WexNVduuXKgL2/2NTzRUz\n27txPDiHVk/pa0Wo4OD3aTXuXVYpLYJblq0cKiK8WL9LDtXCD5fDzBMMaZcFxtdi\nehJlW5CZY72NCiDmo1WB1eOvZ/akQrQxT1j8Yu2ZAoGAM3r6G7siOC31rcdXOSTO\nIElnTU5jZVdo9Zqk2XlUEC5S0olqMsV2niCMznL8oGgmy62NG6Nhuli7AUM4Zjig\nHh9YQRsfvsLJ1DE1LGfgbzT9P9qUD9F3YiaFJpSAy74qm4stS7keUuLdR5I298hU\n/fqIEBL5awwXWxEQPUCuTPU=\n-----END PRIVATE KEY-----\n',
 '-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA/aTWrj1V5GtqueSpjT5H\n+nS0qcewmHVA9DiIPKWkrdg2beaAbZMYHPhNBlhi6rfw5jNF2ltMR+JeL9ZbBemt\nmboForEEBIvO6oGTAoryva336k32WVNXcjH+GvVHDoVouGSRKPGRJ9Znc4AFxj1V\nDlqdfYhp3HNZiIYYvXNW6QLaMvQW1VLDergSjrA5relLR+EEorCIds99pDkb8trl\nJ9ptJut+0jhWnM/BN1iGCrSaijbk9lNUZD7Q9Vbwqg6zPgP7IicmdTPWgi7tWE9M\nspXNUL6qvnr0WMfCVWiVqKgnI3cYwIkzUV77egTSkYHFyisEGAS5a3p3SQ2rBoVY\nKQIDAQAB\n-----END PUBLIC KEY-----\n',
 'test', 'test');
