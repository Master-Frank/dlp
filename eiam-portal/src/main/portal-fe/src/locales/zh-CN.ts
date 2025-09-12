/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import component from './zh-CN/component';
import menu from './zh-CN/menu';
import pages from './zh-CN/pages';

export default {
  'nav_bar.lang': '语言',
  'layout.user.link.help': '帮助',
  'layout.user.link.privacy': '隐私',
  'layout.user.link.terms': '条款',
  'app.notification.rule.0.message': '提示',
  /* TopIAM 自定义 */
  'app.option': '操作',
  'app.create': '新增',
  'app.create_child': '新增下级',
  'app.update': '修改',
  'app.delete': '删除',
  'app.batch_delete': '批量删除',
  'app.import': '导入',
  'app.export': '导出',
  'app.save': '保存',
  'app.delete_confirm': '您确定要删除吗？',
  'app.enable_confirm': '您确定要启用吗？',
  'app.disable_confirm': '您确定要禁用吗？',
  'app.delete_success': '删除成功',
  'app.create_success': '新增成功',
  'app.update_success': '修改成功',
  'app.operation_success': '操作成功',
  'app.cancel': '取消',
  'app.confirm': '确认',
  'app.success': '成功',
  'app.fail': '失败',
  'app.enable': '启用',
  'app.disable': '禁用',
  'app.selected': '已选',
  'app.item': '项',
  'app.deselect': '取消选择',
  'app.yes': '是',
  'app.no': '否',
  'app.unknown': '未知',
  'app.please_choose': '请选择',
  'app.please_enter': '请输入',
  'app.required_field': '此项为必填项',
  'app.send_successfully': '发送成功',
  'app.start_time': '开始时间',
  'app.end_time': '结束时间',
  'app.return': '返回',
  'app.system-notification': '系统通知',
  ...menu,
  ...component,
  ...pages,
};
