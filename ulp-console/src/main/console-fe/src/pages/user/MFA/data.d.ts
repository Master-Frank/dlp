/*
 * ulp-console - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** /api/v1/mfa/bind/prepare 返回 */
export type PrepareBindResult = {
  otpAuthUri: string;
  secretBase32: string;
};

/** /api/v1/mfa/bind/confirm 返回 */
export type ConfirmBindResult = {
  backupCodes: string[];
};

/** /api/v1/mfa/challenge 返回 — backup-code 成功时附带剩余信息 */
export type ChallengeResult = {
  backup_codes_remaining?: number;
  regenerate_backup_codes_warning?: boolean;
  regenerate_backup_codes_required?: boolean;
};
