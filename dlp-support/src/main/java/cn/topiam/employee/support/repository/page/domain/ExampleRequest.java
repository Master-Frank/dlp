package cn.topiam.employee.support.repository.page.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;

/**
 * 示例请求类
 * 包含示例对象和分页请求信息
 *
 * @param <T> 泛型类型
 */
public class ExampleRequest<T> {
   
   /**
    * 分页请求
    */
   private PageRequest pageRequest;
   
   /**
    * 示例对象
    */
   private Example<T> example;

   public static String decryptString(Object object) {
      int var1 = (3 ^ 5) << 3 ^ 3 >> 1;
      int var2 = 3 << 3 ^ 5;
      int var3 = (2 ^ 5) << 3 ^ 4 ^ 5;
      String str = (String) object;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (3 >> 1);
      char[] result = chars;
      int var4 = var3;
      int var5 = index;

      for(int var6 = var1; var5 >= 0; var5 = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char)(charValue ^ var6);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char)(str.charAt(tempIndex2) ^ var4);
      }

      return new String(result);
   }

   @Override
   public boolean equals(Object object) {
      ExampleRequest that = (ExampleRequest)object;
      if (that == this) {
         return true;
      } else if (!(that instanceof ExampleRequest)) {
         return false;
      } else if (!that.canEqual(this)) {
         return false;
      } else {
         Example thatExample = that.getExample();
         Example thisExample = this.getExample();
         if (thisExample == null) {
            if (thatExample != null) {
               return false;
            }
         } else if (!thisExample.equals(thatExample)) {
            return false;
         }

         PageRequest thatPageRequest = that.getPageRequest();
         PageRequest thisPageRequest = this.getPageRequest();
         if (thisPageRequest == null) {
            if (thatPageRequest != null) {
               return false;
            }
         } else if (!thisPageRequest.equals(thatPageRequest)) {
            return false;
         }

         return true;
      }
   }

   /**
    * 检查是否可以比较
    *
    * @param object 对象
    * @return 是否可以比较
    */
   public boolean canEqual(Object object) {
      return object instanceof ExampleRequest;
   }

   @Override
   public int hashCode() {
      int result = 59;
      Example example = this.getExample();
      result = result * 59 + (example == null ? 43 : example.hashCode());
      PageRequest pageRequest = this.getPageRequest();
      return result * 59 + (pageRequest == null ? 43 : pageRequest.hashCode());
   }

   /**
    * 设置分页请求
    *
    * @param pageRequest 分页请求
    */
   public void setPageRequest(PageRequest pageRequest) {
      this.pageRequest = pageRequest;
   }

   @Override
   public String toString() {
      return "ExampleRequest(example=" + String.valueOf(this.getExample()) + ", pageRequest=" + String.valueOf(this.getPageRequest()) + ")";
   }

   /**
    * 获取示例对象
    *
    * @return 示例对象
    */
   public Example<T> getExample() {
      return this.example;
   }

   /**
    * 设置示例对象
    *
    * @param example 示例对象
    */
   public void setExample(Example<T> example) {
      this.example = example;
   }

   /**
    * 获取分页请求
    *
    * @return 分页请求
    */
   public PageRequest getPageRequest() {
      return this.pageRequest;
   }
}