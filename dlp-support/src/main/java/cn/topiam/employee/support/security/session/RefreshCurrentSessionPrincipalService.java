package cn.topiam.employee.support.security.session;

import cn.topiam.employee.support.security.userdetails.UserDetails;

/**
 * 刷新当前会话主体服务接口
 * 定义获取当前会话主体的方法
 */
public interface RefreshCurrentSessionPrincipalService {
   
   /**
    * 根据会话ID获取主体信息
    *
    * @param sessionId 会话ID
    * @return 主体信息
    */
   UserDetails getPrincipal(String sessionId);
}