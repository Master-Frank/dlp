package cn.topiam.employee.support.repository.page.domain;

import io.swagger.v3.oas.annotations.Parameter;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 * 包含分页数据和分页信息
 *
 * @param <T> 泛型类型
 */
public class Page<T> implements Serializable {
   private static final long serialVersionUID = 7151477498103713983L;
   
   /**
    * 数据列表
    */
   @Parameter(
      description = "数据集合"
   )
   private List<T> list;
   
   /**
    * 分页信息
    */
   @Parameter(
      description = "页数数据"
   )
   private Pagination pagination;

   /**
    * 获取数据列表
    *
    * @return 数据列表
    */
   public List<T> getList() {
      return this.list;
   }

   /**
    * 获取分页信息
    *
    * @return 分页信息
    */
   public Pagination getPagination() {
      return this.pagination;
   }

   /**
    * 设置分页信息
    *
    * @param pagination 分页信息
    */
   public void setPagination(Pagination pagination) {
      this.pagination = pagination;
   }

   @Override
   public String toString() {
      return "Page(list=" + String.valueOf(this.getList()) + ", pagination=" + String.valueOf(this.getPagination()) + ")";
   }

   /**
    * 检查是否可以比较
    *
    * @param object 对象
    * @return 是否可以比较
    */
   public boolean canEqual(Object object) {
      return object instanceof Page;
   }

   @Override
   public int hashCode() {
      int result = 59;
      List<T> list = this.getList();
      result = result * 59 + (list == null ? 43 : list.hashCode());
      Pagination pagination = this.getPagination();
      return result * 59 + (pagination == null ? 43 : pagination.hashCode());
   }

   @Override
   public boolean equals(Object object) {
      Page that = (Page)object;
      if (that == this) {
         return true;
      } else if (!(that instanceof Page)) {
         return false;
      } else if (!that.canEqual(this)) {
         return false;
      } else {
         List<T> thatList = that.getList();
         List<T> thisList = this.getList();
         if (thisList == null) {
            if (thatList != null) {
               return false;
            }
         } else if (!thisList.equals(thatList)) {
            return false;
         }

         Pagination thatPagination = that.getPagination();
         Pagination thisPagination = this.getPagination();
         if (thisPagination == null) {
            if (thatPagination != null) {
               return false;
            }
         } else if (!thisPagination.equals(thatPagination)) {
            return false;
         }

         return true;
      }
   }

   /**
    * 设置数据列表
    *
    * @param list 数据列表
    */
   public void setList(List<T> list) {
      this.list = list;
   }

   public static String decryptString(Object object) {
      int var1 = 5 << 3 ^ 2;
      int var2 = 5 << 4 ^ 2 << 2 ^ 5 >> 2;
      int var3 = 5 << 4 ^ (2 ^ 5) << (3 & 5);
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

   /**
    * 分页信息内部类
    */
   public static class Pagination implements Serializable {
      /**
       * 当前页
       */
      @Parameter(
         description = "当前页"
      )
      private Integer current;
      
      private static final long serialVersionUID = -580115170667261984L;
      
      /**
       * 总记录数
       */
      @Parameter(
         description = "总条数"
      )
      private Long total;
      
      /**
       * 总页数
       */
      @Parameter(
         description = "总页数"
      )
      private Integer totalPages;

      /**
       * 构造函数
       *
       * @param total 总记录数
       * @param totalPages 总页数
       * @param current 当前页
       */
      public Pagination(Long total, Integer totalPages, Integer current) {
         this.total = total;
         this.totalPages = totalPages;
         this.current = current;
      }

      /**
       * 设置总页数
       *
       * @param totalPages 总页数
       */
      public void setTotalPages(Integer totalPages) {
         this.totalPages = totalPages;
      }

      @Override
      public int hashCode() {
         int result = 59;
         Long total = this.getTotal();
         result = result * 59 + (total == null ? 43 : total.hashCode());
         Integer totalPages = this.getTotalPages();
         result = result * 59 + (totalPages == null ? 43 : totalPages.hashCode());
         Integer current = this.getCurrent();
         return result * 59 + (current == null ? 43 : current.hashCode());
      }

      /**
       * 获取总页数
       *
       * @return 总页数
       */
      public Integer getTotalPages() {
         return this.totalPages;
      }

      @Override
      public boolean equals(Object object) {
         Pagination that = (Pagination)object;
         if (that == this) {
            return true;
         } else if (!(that instanceof Pagination)) {
            return false;
         } else if (!that.canEqual(this)) {
            return false;
         } else {
            Long thatTotal = that.getTotal();
            Long thisTotal = this.getTotal();
            if (thisTotal == null) {
               if (thatTotal != null) {
                  return false;
               }
            } else if (!thisTotal.equals(thatTotal)) {
               return false;
            }

            Integer thatTotalPages = that.getTotalPages();
            Integer thisTotalPages = this.getTotalPages();
            if (thisTotalPages == null) {
               if (thatTotalPages != null) {
                  return false;
               }
            } else if (!thisTotalPages.equals(thatTotalPages)) {
               return false;
            }

            Integer thatCurrent = that.getCurrent();
            Integer thisCurrent = this.getCurrent();
            if (thisCurrent == null) {
               if (thatCurrent != null) {
                  return false;
               }
            } else if (!thisCurrent.equals(thatCurrent)) {
               return false;
            }

            return true;
         }
      }

      /**
       * 获取总记录数
       *
       * @return 总记录数
       */
      public Long getTotal() {
         return this.total;
      }

      /**
       * 设置总记录数
       *
       * @param total 总记录数
       */
      public void setTotal(Long total) {
         this.total = total;
      }

      /**
       * 设置当前页
       *
       * @param current 当前页
       */
      public void setCurrent(Integer current) {
         this.current = current;
      }

      @Override
      public String toString() {
         return "Page.Pagination(total=" + this.getTotal() + ", totalPages=" + this.getTotalPages() + ", current=" + this.getCurrent() + ")";
      }

      /**
       * 检查是否可以比较
       *
       * @param object 对象
       * @return 是否可以比较
       */
      public boolean canEqual(Object object) {
         return object instanceof Pagination;
      }

      /**
       * 创建分页信息构建器
       *
       * @return 分页信息构建器
       */
      public static PaginationBuilder builder() {
         return new PaginationBuilder();
      }

      /**
       * 获取当前页
       *
       * @return 当前页
       */
      public Integer getCurrent() {
         return this.current;
      }

      /**
       * 分页信息构建器
       */
      public static class PaginationBuilder {
         private Long total;
         private Integer totalPages;
         private Integer current;

         /**
          * 构建分页信息
          *
          * @return 分页信息
          */
         public Pagination build() {
            return new Pagination(this.total, this.totalPages, this.current);
         }

         /**
          * 设置总记录数
          *
          * @param total 总记录数
          * @return 分页信息构建器
          */
         public PaginationBuilder total(Long total) {
            this.total = total;
            return this;
         }

         @Override
         public String toString() {
            return "Page.Pagination.PaginationBuilder(total=" + this.total + ", totalPages=" + this.totalPages + ", current=" + this.current + ")";
         }

         /**
          * 设置当前页
          *
          * @param current 当前页
          * @return 分页信息构建器
          */
         public PaginationBuilder current(Integer current) {
            this.current = current;
            return this;
         }

         /**
          * 设置总页数
          *
          * @param totalPages 总页数
          * @return 分页信息构建器
          */
         public PaginationBuilder totalPages(Integer totalPages) {
            this.totalPages = totalPages;
            return this;
         }
      }
   }
}