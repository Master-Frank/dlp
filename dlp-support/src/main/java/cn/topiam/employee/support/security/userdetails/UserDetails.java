package cn.topiam.employee.support.security.userdetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import lombok.Generated;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

/**
 * 用户详情类
 * 扩展Spring Security的User类，包含更多用户相关信息
 */
public class UserDetails extends User {
   /**
    * 手机号是否已验证
    */
   private Boolean phoneVerified;
   
   /**
    * 最后更新密码时间
    */
   private LocalDateTime lastUpdatePasswordTime;
   
   /**
    * 更新时间
    */
   private LocalDateTime updateTime;
   
   /**
    * 全名
    */
   private String fullName;
   
   /**
    * 邮箱
    */
   private final String email;
   
   /**
    * 邮箱是否已验证
    */
   private Boolean emailVerified;
   
   /**
    * 昵称
    */
   private String nickName;
   
   /**
    * 用户ID
    */
   private final String id;
   
   /**
    * 序列化版本号
    */
   private static final long serialVersionUID = 8227098865368453321L;
   
   /**
    * 手机区号
    */
   private String phoneAreaCode;
   
   /**
    * 是否需要修改密码
    */
   private Boolean needChangePassword;
   
   /**
    * 应用列表
    */
   private Collection<Application> applications;
   
   /**
    * 用户类型
    */
   private UserType userType;
   
   /**
    * 组织列表
    */
   private Collection<Organization> organizations;
   
   /**
    * 员工编号
    */
   private String employeeNumber;
   
   /**
    * 头像
    */
   private String avatar;
   
   /**
    * 手机号
    */
   private String phone;
   
   /**
    * 数据来源
    */
   private DataOrigin dataOrigin;
   
   /**
    * 过期日期
    */
   private LocalDate expireDate;
   
   /**
    * 组列表
    */
   private Collection<Group> groups;
   
   /**
    * 外部ID
    */
   private String externalId;
   
   /**
    * 用户名
    */
   private final String username;

   /**
    * 设置邮箱
    *
    * @param email 邮箱
    */
   @Generated
   public void setEmail(String email) {
      this.email = email;
   }

   /**
    * 设置用户类型
    *
    * @param userType 用户类型
    */
   @Generated
   public void setUserType(UserType userType) {
      this.userType = userType;
   }

   /**
    * 构造函数
    *
    * @param id 用户ID
    * @param username 用户名
    * @param password 密码
    * @param userType 用户类型
    * @param enabled 是否启用
    * @param accountNonExpired 账户是否未过期
    * @param credentialsNonExpired 凭据是否未过期
    * @param accountNonLocked 账户是否未锁定
    * @param authorities 权限集合
    */
   public UserDetails(String id, String username, String password, UserType userType, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
      super(username, Objects.isNull(password) ? "" : password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
      this.id = id;
      this.username = username;
      this.userType = userType;
      this.groups = Collections.unmodifiableSet(new HashSet<>());
      this.organizations = Collections.unmodifiableSet(new HashSet<>());
      this.applications = Collections.unmodifiableSet(new HashSet<>());
   }

   /**
    * 获取头像
    *
    * @return 头像
    */
   @JsonProperty("avatar")
   public String getAvatar() {
      return this.avatar;
   }

   /**
    * 获取员工编号
    *
    * @return 员工编号
    */
   @JsonProperty("employeeNumber")
   public String getEmployeeNumber() {
      return this.employeeNumber;
   }

   /**
    * 获取是否需要修改密码
    *
    * @return 是否需要修改密码
    */
   @JsonProperty("needChangePassword")
   public Boolean getNeedChangePassword() {
      return this.needChangePassword;
   }

   /**
    * 获取用户ID
    *
    * @return 用户ID
    */
   @JsonProperty("id")
   public String getId() {
      return this.id;
   }

   /**
    * 计算哈希值
    *
    * @return 哈希值
    */
   @Override
   public int hashCode() {
      Object[] values = new Object[2];
      values[0] = super.hashCode();
      values[1] = this.email;
      return Objects.hash(values);
   }

   /**
    * 获取手机号
    *
    * @return 手机号
    */
   @JsonProperty("phone")
   public String getPhone() {
      return this.phone;
   }

   /**
    * 设置全名
    *
    * @param fullName 全名
    */
   @Generated
   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   /**
    * 设置组列表
    *
    * @param groups 组列表
    */
   public void setGroups(Collection<Group> groups) {
      Assert.notNull(groups, "组列表不能为空");
      this.groups = Collections.unmodifiableSet(new HashSet<>(groups));
   }

   /**
    * 设置员工编号
    *
    * @param employeeNumber 员工编号
    */
   @Generated
   public void setEmployeeNumber(String employeeNumber) {
      this.employeeNumber = employeeNumber;
   }

   /**
    * 设置组织列表
    *
    * @param organizations 组织列表
    */
   public void setOrganizations(Collection<Organization> organizations) {
      Assert.notNull(organizations, "组织列表不能为空");
      this.organizations = Collections.unmodifiableSet(new HashSet<>(organizations));
   }

   /**
    * 获取更新时间
    *
    * @return 更新时间
    */
   @JsonProperty("updateTime")
   public LocalDateTime getUpdateTime() {
      return this.updateTime;
   }

   /**
    * 设置手机号是否已验证
    *
    * @param phoneVerified 手机号是否已验证
    */
   @Generated
   public void setPhoneVerified(Boolean phoneVerified) {
      this.phoneVerified = phoneVerified;
   }

   /**
    * 设置是否需要修改密码
    *
    * @param needChangePassword 是否需要修改密码
    */
   @Generated
   public void setNeedChangePassword(Boolean needChangePassword) {
      this.needChangePassword = needChangePassword;
   }

   /**
    * 设置头像
    *
    * @param avatar 头像
    */
   @Generated
   public void setAvatar(String avatar) {
      this.avatar = avatar;
   }

   /**
    * 获取手机号是否已验证
    *
    * @return 手机号是否已验证
    */
   @JsonProperty("phoneVerified")
   public Boolean getPhoneVerified() {
      return this.phoneVerified;
   }

   /**
    * 获取用户名
    *
    * @return 用户名
    */
   @JsonProperty("username")
   public String getUsername() {
      return this.username;
   }

   /**
    * 设置手机区号
    *
    * @param phoneAreaCode 手机区号
    */
   @Generated
   public void setPhoneAreaCode(String phoneAreaCode) {
      this.phoneAreaCode = phoneAreaCode;
   }

   /**
    * 获取全名
    *
    * @return 全名
    */
   @JsonProperty("fullName")
   public String getFullName() {
      return this.fullName;
   }

   /**
    * 获取昵称
    *
    * @return 昵称
    */
   @JsonProperty("nickName")
   public String getNickName() {
      return this.nickName;
   }

   /**
    * 获取外部ID
    *
    * @return 外部ID
    */
   @JsonProperty("externalId")
   public String getExternalId() {
      return this.externalId;
   }

   /**
    * 获取过期日期
    *
    * @return 过期日期
    */
   @JsonProperty("expireDate")
   public LocalDate getExpireDate() {
      return this.expireDate;
   }

   /**
    * 设置邮箱是否已验证
    *
    * @param emailVerified 邮箱是否已验证
    */
   @Generated
   public void setEmailVerified(Boolean emailVerified) {
      this.emailVerified = emailVerified;
   }

   /**
    * 获取应用列表
    *
    * @return 应用列表
    */
   @JsonProperty("applications")
   public Collection<Application> getApplications() {
      return CollectionUtils.isEmpty(this.applications) ? CollectionUtils.emptyCollection() : this.applications;
   }

   /**
    * 设置昵称
    *
    * @param nickName 昵称
    */
   @Generated
   public void setNickName(String nickName) {
      this.nickName = nickName;
   }

   /**
    * 设置应用列表
    *
    * @param applications 应用列表
    */
   public void setApplications(Collection<Application> applications) {
      Assert.notNull(applications, "应用列表不能为空");
      this.applications = Collections.unmodifiableSet(new HashSet<>(applications));
   }

   /**
    * 获取邮箱是否已验证
    *
    * @return 邮箱是否已验证
    */
   @JsonProperty("emailVerified")
   public Boolean getEmailVerified() {
      return this.emailVerified;
   }

   /**
    * 转换为字符串
    *
    * @return 用户名
    */
   @Override
   public String toString() {
      return this.username;
   }

   /**
    * 获取数据来源
    *
    * @return 数据来源
    */
   @JsonProperty("dataOrigin")
   public DataOrigin getDataOrigin() {
      return this.dataOrigin;
   }

   /**
    * 设置外部ID
    *
    * @param externalId 外部ID
    */
   @Generated
   public void setExternalId(String externalId) {
      this.externalId = externalId;
   }

   /**
    * 比较对象是否相等
    *
    * @param other 其他对象
    * @return 是否相等
    */
   @Override
   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         if (!super.equals(other)) {
            return false;
         } else {
            UserDetails userDetails = (UserDetails) other;
            return StringUtils.equals(this.username, userDetails.getUsername());
         }
      } else {
         return false;
      }
   }

   /**
    * 构造函数
    *
    * @param id 用户ID
    * @param username 用户名
    * @param userType 用户类型
    * @param enabled 是否启用
    * @param accountNonExpired 账户是否未过期
    * @param credentialsNonExpired 凭据是否未过期
    * @param accountNonLocked 账户是否未锁定
    * @param authorities 权限集合
    */
   public UserDetails(String id, String username, UserType userType, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
      super(username, "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
      this.id = id;
      this.username = username;
      this.userType = userType;
      this.groups = Collections.unmodifiableSet(new HashSet<>());
      this.organizations = Collections.unmodifiableSet(new HashSet<>());
      this.applications = Collections.unmodifiableSet(new HashSet<>());
   }

   /**
    * 获取组列表
    *
    * @return 组列表
    */
   @JsonProperty("groups")
   public Collection<Group> getGroups() {
      return CollectionUtils.isEmpty(this.groups) ? CollectionUtils.emptyCollection() : this.groups;
   }

   /**
    * 设置过期日期
    *
    * @param expireDate 过期日期
    */
   @Generated
   public void setExpireDate(LocalDate expireDate) {
      this.expireDate = expireDate;
   }

   /**
    * 获取最后更新密码时间
    *
    * @return 最后更新密码时间
    */
   @JsonProperty("lastUpdatePasswordTime")
   public LocalDateTime getLastUpdatePasswordTime() {
      return this.lastUpdatePasswordTime;
   }

   /**
    * 获取邮箱
    *
    * @return 邮箱
    */
   @JsonProperty("email")
   public String getEmail() {
      return this.email;
   }

   /**
    * 设置手机号
    *
    * @param phone 手机号
    */
   @Generated
   public void setPhone(String phone) {
      this.phone = phone;
   }

   /**
    * 获取手机区号
    *
    * @return 手机区号
    */
   @JsonProperty("phoneAreaCode")
   public String getPhoneAreaCode() {
      return this.phoneAreaCode;
   }

   /**
    * 获取用户类型
    *
    * @return 用户类型
    */
   @JsonProperty("userType")
   public UserType getUserType() {
      return this.userType;
   }

   /**
    * 获取组织列表
    *
    * @return 组织列表
    */
   @JsonProperty("organizations")
   public Collection<Organization> getOrganizations() {
      return CollectionUtils.isEmpty(this.organizations) ? CollectionUtils.emptyCollection() : this.organizations;
   }

   /**
    * 设置最后更新密码时间
    *
    * @param lastUpdatePasswordTime 最后更新密码时间
    */
   @Generated
   public void setLastUpdatePasswordTime(LocalDateTime lastUpdatePasswordTime) {
      this.lastUpdatePasswordTime = lastUpdatePasswordTime;
   }

   /**
    * 设置数据来源
    *
    * @param dataOrigin 数据来源
    */
   @Generated
   public void setDataOrigin(DataOrigin dataOrigin) {
      this.dataOrigin = dataOrigin;
   }

   /**
    * 设置更新时间
    *
    * @param updateTime 更新时间
    */
   @Generated
   public void setUpdateTime(LocalDateTime updateTime) {
      this.updateTime = updateTime;
   }
}
