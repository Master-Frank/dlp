package cn.topiam.employee.support.security.password.weak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 默认弱密码库实现类
 * 从资源文件加载弱密码词库
 */
public class DefaultPasswordWeakLibImpl implements PasswordWeakLib {
   /**
    * 弱密码词库文件路径
    */
   private static final String WEAK_PASSWORD_FILE = "/dictionaries/10k-most-common.txt";
   
   /**
    * 日志记录器
    */
   private static final Logger logger = LoggerFactory.getLogger(DefaultPasswordWeakLibImpl.class);
   
   /**
    * 弱密码映射表
    */
   private final Map<String, Boolean> weakPasswordMap;

   /**
    * 检查单词是否为弱密码
    *
    * @param word 待检查的单词
    * @return 是否为弱密码
    */
   @Override
   public Boolean wordExists(String word) {
      synchronized(this.weakPasswordMap) {
         return Boolean.TRUE.equals(this.weakPasswordMap.get(word));
      }
   }

   /**
    * 构造函数
    * 初始化弱密码映射表并加载弱密码词库
    */
   public DefaultPasswordWeakLibImpl() {
      this.weakPasswordMap = new HashMap<>(16);
      this.loadWeakPasswords();
   }

   /**
    * 加载弱密码词库
    */
   private void loadWeakPasswords() {
      try {
         logger.debug("正在加载弱密码词库");
         this.loadWeakPasswords(this.getClass().getResourceAsStream("/dictionaries/10k-most-common.txt"));
      } catch (Exception e) {
         logger.error("加载弱密码词库失败", e);
      }
   }

   /**
    * 从输入流加载弱密码
    *
    * @param inputStream 输入流
    * @throws IOException IO异常
    */
   private void loadWeakPasswords(InputStream inputStream) throws IOException {
      if (Objects.nonNull(inputStream)) {
         HashMap<String, Boolean> newWeakPasswords = new HashMap<>(16);
         HashSet<String> existingPasswords = new HashSet<>();
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

         String word;
         while((word = reader.readLine()) != null) {
            if (this.wordExists(word)) {
               existingPasswords.add(word);
            } else {
               newWeakPasswords.put(word, Boolean.TRUE);
            }
         }

         synchronized(this.weakPasswordMap) {
            this.weakPasswordMap.keySet().retainAll(existingPasswords);
            this.weakPasswordMap.putAll(newWeakPasswords);
         }
      }
   }

   /**
    * 获取弱密码词列表
    *
    * @return 弱密码词列表
    */
   @Override
   public List<String> getWordList() {
      return this.weakPasswordMap.keySet().stream().toList();
   }
}