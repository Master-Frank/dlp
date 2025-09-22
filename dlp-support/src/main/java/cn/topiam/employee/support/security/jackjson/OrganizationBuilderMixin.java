package cn.topiam.employee.support.security.jackjson;

import cn.topiam.employee.support.security.userdetails.Organization;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织构建器Mixin类
 * 用于扩展组织构建器功能
 */
public class OrganizationBuilderMixin {
   
   /**
    * 组织列表
    */
   private List<Organization> organizations = new ArrayList<>();

   /**
    * 添加组织
    * 
    * @param organization 组织
    * @return 组织构建器Mixin
    */
   public OrganizationBuilderMixin addOrganization(Organization organization) {
      this.organizations.add(organization);
      return this;
   }

   /**
    * 获取组织列表
    * 
    * @return 组织列表
    */
   public List<Organization> getOrganizations() {
      return this.organizations;
   }
}