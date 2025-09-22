package cn.topiam.employee.support.security.password.weak;

import java.util.List;

/**
 * 弱密码库接口
 * 定义弱密码库的基本操作方法
 */
public interface PasswordWeakLib {
   
   /**
    * 获取弱密码词列表
    *
    * @return 弱密码词列表
    */
   List<String> getWordList();

   /**
    * 检查单词是否存在于弱密码库中
    *
    * @param word 待检查的单词
    * @return 是否存在
    */
   Boolean wordExists(String word);
}