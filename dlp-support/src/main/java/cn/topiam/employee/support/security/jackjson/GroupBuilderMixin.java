package cn.topiam.employee.support.security.jackjson;

import cn.topiam.employee.support.security.userdetails.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * 组构建器Mixin类
 * 用于扩展组构建器功能
 */
public class GroupBuilderMixin {
   
   /**
    * 组列表
    */
   private List<Group> groups = new ArrayList<>();

   /**
    * 添加组
    * 
    * @param group 组
    * @return 组构建器Mixin
    */
   public GroupBuilderMixin addGroup(Group group) {
      this.groups.add(group);
      return this;
   }

   /**
    * 获取组列表
    * 
    * @return 组列表
    */
   public List<Group> getGroups() {
      return this.groups;
   }
}