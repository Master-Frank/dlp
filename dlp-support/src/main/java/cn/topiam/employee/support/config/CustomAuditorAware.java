package cn.topiam.employee.support.config;

import cn.topiam.employee.support.security.util.SecurityUtils;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

/**
 * 自定义审计员感知器
 * 用于获取当前审计员（用户ID）
 */
public final class CustomAuditorAware implements AuditorAware<String> {
   
   /**
    * 获取当前审计员
    * 
    * @return 当前审计员的Optional包装
    */
   @NonNull
   public Optional<String> getCurrentAuditor() {
      return Optional.ofNullable(SecurityUtils.getCurrentUserId());
   }
}