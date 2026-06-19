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

DELETE FROM ulp_app_cert        WHERE id_ = 'test-ropc-cert';
DELETE FROM ulp_app_oidc_config WHERE id_ = 'test-ropc-cfg';
DELETE FROM ulp_app             WHERE id_ = 'test-ropc-app-id';

INSERT INTO ulp_app (
    id_, name_, code_, client_id, client_secret, protocol_, type_, template_,
    init_login_type, authorization_type, is_configured, is_enabled,
    create_by, update_by
) VALUES
('test-ropc-app-id', 'Test ROPC App', 'test-ropc-app', 'ropc-client', 'ropc-secret',
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
('test-ropc-cfg', 'test-ropc-app-id',
 '["client_secret_basic"]', '["password","refresh_token"]', '["code"]', '[]',
 '[]', '["openid"]',
 false, false,
 'RS256',
 3600000000000, 300000000000, 300000000000,
 3600000000000, 3600000000000,
 'RS256', 'self-contained', false,
 'test', 'test');

INSERT INTO ulp_app_cert (
    id_, app_id, using_type, key_long, sign_algo, private_key, public_key,
    create_by, update_by
) VALUES
('test-ropc-cert', 'test-ropc-app-id', 'oidc_jwk', 2048, 'RSA',
 '-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD9pNauPVXka2q5\n5KmNPkf6dLSpx7CYdUD0OIg8paSt2DZt5oBtkxgc+E0GWGLqt/DmM0XaW0xH4l4v\n1lsF6a2ZugWisQQEi87qgZMCivK9rffqTfZZU1dyMf4a9UcOhWi4ZJEo8ZEn1mdz\ngAXGPVUOWp19iGncc1mIhhi9c1bpAtoy9BbVUsN6uBKOsDmt6UtH4QSisIh2z32k\nORvy2uUn2m0m637SOFacz8E3WIYKtJqKNuT2U1RkPtD1VvCqDrM+A/siJyZ1M9aC\nLu1YT0yylc1Qvqq+evRYx8JVaJWoqCcjdxjAiTNRXvt6BNKRgcXKKwQYBLlrendJ\nDasGhVgpAgMBAAECggEAAS56MAoEhd8Auns1KtLlkwYlvHfmoRKEbLwnLqYkY17D\nQ9A2h1wk2Sddyifm/7op4X6ku94f7Q1MnEXauxyHWh9urN8iJPMcRzAhlc9Seb2k\nhCHxwfaEYk7XOiZWmtvA/OvorQjGtklrxl2sLowFAt87RiqN+LCNCW4Q0drGfBPM\nkzmmtjcx0YlGpD45tuQJkvS4lKvcbQlDWudhp6Hcm/cV6i0tbN2g0At/uaXNxiuh\nwfoXIOFLtd3wqaCIF3onw+9PlLTxk5DsZHuihvCG92YTRyH4UhDMlhq4MwX1a02w\niD+lxdq3VLsNcBoDO7cVM3be131KQinEJph/KowFcQKBgQD/xWcDsW/vYgmpH6w1\nGFrHuSkbeHUFoUoHZHMTPNfLhv30Ris4PUbiQkxCD4zQkC/NOePu/saNUVhYHZSJ\nwxefBFnjUHr/F1Hewse4u0qE9vcg1xuPI3qX58BSSfenbMODjdnUmdZo7MLmtFD9\njFmXEKVz/VKO04zbgmpou5PIkQKBgQD93vLn23cv3O0Df/fKNUM23xNrwN6YjDMz\ng8/EkWHB+JrjFUrUVEK/kooeSSNv5rMfMKgf0Glgz1de92QpeLv30F0J7u5V9696\n9Aid0qy+AxIR3oIzBRTZC8NdrDQ4MLD2O5CZpVzKFpVNI8zqzlEYxzpTmiYPzKF9\n+9TT6vuiGQKBgQC5TtI18MaCj2skZ1gjF8Qd098ekgVm0NaLyJE/LOPEB8fSxUvm\n8S58G0CY1B9XtD+N1xV3QIumM3toS/YkYX6prUNa2CJk0wZz+HcvNjLlZvDhkDfd\nWv0lNbk3ZXPSj5CPraRWziZz2qXS9G2BZcA7HMpi4PSBmnABUdm6i7ykoQKBgHDe\nO15ry1yjO1jP/wmOjpiJqye/8vcddfIUSz4YaL8FWU9WexNVduuXKgL2/2NTzRUz\n27txPDiHVk/pa0Wo4OD3aTXuXVYpLYJblq0cKiK8WL9LDtXCD5fDzBMMaZcFxtdi\nehJlW5CZY72NCiDmo1WB1eOvZ/akQrQxT1j8Yu2ZAoGAM3r6G7siOC31rcdXOSTO\nIElnTU5jZVdo9Zqk2XlUEC5S0olqMsV2niCMznL8oGgmy62NG6Nhuli7AUM4Zjig\nHh9YQRsfvsLJ1DE1LGfgbzT9P9qUD9F3YiaFJpSAy74qm4stS7keUuLdR5I298hU\n/fqIEBL5awwXWxEQPUCuTPU=\n-----END PRIVATE KEY-----\n',
 '-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA/aTWrj1V5GtqueSpjT5H\n+nS0qcewmHVA9DiIPKWkrdg2beaAbZMYHPhNBlhi6rfw5jNF2ltMR+JeL9ZbBemt\nmboForEEBIvO6oGTAoryva336k32WVNXcjH+GvVHDoVouGSRKPGRJ9Znc4AFxj1V\nDlqdfYhp3HNZiIYYvXNW6QLaMvQW1VLDergSjrA5relLR+EEorCIds99pDkb8trl\nJ9ptJut+0jhWnM/BN1iGCrSaijbk9lNUZD7Q9Vbwqg6zPgP7IicmdTPWgi7tWE9M\nspXNUL6qvnr0WMfCVWiVqKgnI3cYwIkzUV77egTSkYHFyisEGAS5a3p3SQ2rBoVY\nKQIDAQAB\n-----END PUBLIC KEY-----\n',
 'test', 'test');
