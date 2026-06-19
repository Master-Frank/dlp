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
export default {
  'pages.mfa.setup.title': '设置二次验证 (MFA)',
  'pages.mfa.setup.steps.scan': '扫描二维码',
  'pages.mfa.setup.steps.verify': '验证 OTP',
  'pages.mfa.setup.steps.backup': '保存备份码',
  'pages.mfa.setup.scan.tip':
    '请用 Google Authenticator / 1Password / Microsoft Authenticator 等 TOTP 工具扫描下方二维码，或手动输入密钥。',
  'pages.mfa.setup.secret.label': '手动输入密钥（Base32）',
  'pages.mfa.setup.otp.label': '输入认证器显示的 6 位动态码',
  'pages.mfa.setup.otp.required': '请输入动态码',
  'pages.mfa.setup.otp.format': '动态码必须是 6 位数字',
  'pages.mfa.setup.confirm.submit': '验证并启用 MFA',
  'pages.mfa.setup.confirm.fail': '动态码不正确，请重试',
  'pages.mfa.setup.prepare.fail': '初始化失败，请稍后再试',
  'pages.mfa.setup.qr.fail': '二维码生成失败，请使用下方密钥手动添加',
  'pages.mfa.setup.finish': '完成',
  'pages.mfa.backup.warning.title': '请妥善保存以下 10 个备份码',
  'pages.mfa.backup.warning.desc':
    '丢失认证器时，可使用任意一个备份码完成登录。备份码不会再次显示，离开此页前务必下载或抄录。',
  'pages.mfa.backup.download': '下载为 .txt',
  'pages.mfa.backup.ack': '我已保存',
  'pages.mfa.backup.ack.required': '请先确认已保存备份码',
  'pages.mfa.backup.ack.done': '已记录，您可以离开此页',
  'pages.mfa.backup.file.header': 'ULP MFA 备份码（每个码仅可使用一次，请妥善保管）',
  'pages.mfa.challenge.title': '二次验证',
  'pages.mfa.challenge.subtitle': '请输入认证器中的动态码完成登录',
  'pages.mfa.challenge.tab.totp': '动态码 (TOTP)',
  'pages.mfa.challenge.tab.backup': '备份码',
  'pages.mfa.challenge.totp.label': '6 位动态码',
  'pages.mfa.challenge.totp.required': '请输入 6 位动态码',
  'pages.mfa.challenge.totp.format': '动态码必须是 6 位数字',
  'pages.mfa.challenge.backup.label': '备份码',
  'pages.mfa.challenge.backup.required': '请输入备份码',
  'pages.mfa.challenge.use.backup': '使用备份码',
  'pages.mfa.challenge.use.totp': '使用动态码',
  'pages.mfa.challenge.submit': '验证',
  'pages.mfa.challenge.invalid_otp': '动态码不正确',
  'pages.mfa.challenge.invalid_backup_code': '备份码不正确或已使用',
  'pages.mfa.challenge.expired': '挑战已超时，请重新登录',
  'pages.mfa.challenge.session_invalid': '会话已失效，请重新登录',
  'pages.mfa.challenge.unknown': '验证失败，请稍后重试',
  'pages.mfa.challenge.backup.warn.regen': '备份码剩余不多，建议尽快重新生成',
  'pages.mfa.challenge.backup.required.regen': '备份码已用完，请尽快在账户设置中重新生成',
  'pages.mfa.locked_out': '尝试次数过多，账户已临时锁定，请稍后再试',
  'page.user.profile.menu.mfa': '二次验证 (MFA)',
  'page.user.profile.mfa.status.enabled': '已启用',
  'page.user.profile.mfa.status.disabled': '未启用',
  'page.user.profile.mfa.bind': '绑定 MFA',
  'page.user.profile.mfa.unbind': '解绑 MFA',
  'page.user.profile.mfa.unbind.confirm': '解绑后下次登录将不再要求 MFA。确定继续？',
  'page.user.profile.mfa.unbind.otp.placeholder': '请输入当前动态码以确认',
  'page.user.profile.mfa.unbind.success': '已解绑 MFA',
  'page.user.profile.mfa.unbind.blocked': '当前组织已强制启用 MFA，无法解绑',
  'page.user.profile.mfa.desc':
    '为账户启用基于时间的一次性密码（TOTP），可有效阻止账号被盗用后的非授权登录。管理员 MFA 为自愿开启。',
};
