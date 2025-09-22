package cn.topiam.employee.support.repository.page.domain;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;

/**
 * 分页模型类
 * 包含分页参数信息
 */
@Schema(description = "分页参数")
@ParameterObject
public class PageModel implements Serializable {
    private static final long serialVersionUID = 264581448374520031L;

    /**
     * 排序列表
     */
    @Parameter(description = "排序")
    private List<Sort> sorts;

    /**
     * 当前页
     */
    @Parameter(description = "当前页，默认第一页")
    private Integer current;

    /**
     * 过滤列表
     */
    @Parameter(description = "过滤")
    private List<Filter> filters;

    /**
     * 每页记录数
     */
    @Parameter(description = "每页记录，默认十条")
    private Integer pageSize;

   /**
    * 设置过滤列表
    *
    * @param filters 过滤列表
    */
   public void setFilters(List<Filter> filters) {
      this.filters = filters;
   }

   /**
    * 获取当前页
    *
    * @return 当前页
    */
   public int getCurrent() {
      if (this.current > 0) {
         return this.current - 1;
      } else {
         return this.current;
      }
   }

   /**
    * 检查是否可以比较
    *
    * @param object 对象
    * @return 是否可以比较
    */
   public boolean canEqual(Object object) {
      return object instanceof PageModel;
   }

   public static String decryptString(Object object) {
      int var1 = (3 ^ 5) << 3 ^ 4;
      int var2 = (3 ^ 5) << 4 ^ (3 ^ 5) << (3 & 5);
      int var3 = 5 << 4 ^ 2 << 2 ^ 3;
      String str = (String) object;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (3 >> 1);
      char[] result = chars;
      int var4 = var3;
      int var5 = index;

      for (int var6 = var2; var5 >= 0; var5 = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char) (charValue ^ var6);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ var4);
      }

      return new String(result);
   }

   /**
    * 获取排序列表
    *
    * @return 排序列表
    */
   public List<Sort> getSorts() {
      return this.sorts;
   }

   @Override
   public String toString() {
      return "PageModel(current=" + this.getCurrent() + ", pageSize=" + this.getPageSize() + ", sorts=" + String.valueOf(this.getSorts()) + ", filters=" + String.valueOf(this.getFilters()) + ")";
   }

   /**
    * 获取每页记录数
    *
    * @return 每页记录数
    */
   public Integer getPageSize() {
      return this.pageSize;
   }

   /**
    * 获取过滤列表
    *
    * @return 过滤列表
    */
   public List<Filter> getFilters() {
      return this.filters;
   }

   /**
    * 默认构造函数
    */
   public PageModel() {
      super();
      this.current = 1;
      this.pageSize = 10;
      this.sorts = new ArrayList<>();
      this.filters = new ArrayList<>();
   }

   /**
    * 设置排序列表
    *
    * @param sorts 排序列表
    */
   public void setSorts(List<Sort> sorts) {
      this.sorts = sorts;
   }

   /**
    * 设置每页记录数
    *
    * @param pageSize 每页记录数
    */
   public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
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
   public int hashCode() {
      int result = 59;
      result = result * 59 + this.getCurrent();
      Integer pageSize = this.getPageSize();
      result = result * 59 + (pageSize == null ? 43 : pageSize.hashCode());
      List<Sort> sorts = this.getSorts();
      result = result * 59 + (sorts == null ? 43 : sorts.hashCode());
      List<Filter> filters = this.getFilters();
      return result * 59 + (filters == null ? 43 : filters.hashCode());
   }

   @Override
   public boolean equals(Object object) {
      PageModel that = (PageModel) object;
      if (that == this) {
         return true;
      } else if (!(that instanceof PageModel)) {
         return false;
      } else if (!that.canEqual(this)) {
         return false;
      } else if (this.getCurrent() != that.getCurrent()) {
         return false;
      } else {
         Integer thisPageSize = this.getPageSize();
         Integer thatPageSize = that.getPageSize();
         if (thisPageSize == null) {
            if (thatPageSize != null) {
               return false;
            }
         } else if (!thisPageSize.equals(thatPageSize)) {
            return false;
         }

         List<Sort> thisSorts = this.getSorts();
         List<Sort> thatSorts = that.getSorts();
         if (thisSorts == null) {
            if (thatSorts != null) {
               return false;
            }
         } else if (!thisSorts.equals(thatSorts)) {
            return false;
         }

         List<Filter> thisFilters = this.getFilters();
         List<Filter> thatFilters = that.getFilters();
         if (thisFilters == null) {
            if (thatFilters != null) {
               return false;
            }
         } else if (!thisFilters.equals(thatFilters)) {
            return false;
         }

         return true;
      }
   }

   /**
    * 排序内部类
    */
   @Schema(description = "排序参数")
   @ParameterObject
   public static class Sort {
      /**
       * 排序列名
       */
      @Parameter(description = "需要排序的字段")
      private String sorter;

      /**
       * 是否正序
       */
      @Parameter(description = "是否正序排列，默认 true")
      private Boolean asc;

      @Override
      public String toString() {
         return "PageModel.Sort(sorter=" + this.getSorter() + ", asc=" + this.getAsc() + ")";
      }

      /**
       * 获取是否正序
       *
       * @return 是否正序
       */
      public Boolean getAsc() {
         return this.asc;
      }

      /**
       * 设置排序列名
       *
       * @param sorter 排序列名
       */
      public void setSorter(String sorter) {
         this.sorter = sorter;
      }

      /**
       * 检查是否可以比较
       *
       * @param object 对象
       * @return 是否可以比较
       */
      public boolean canEqual(Object object) {
         return object instanceof Sort;
      }

      /**
       * 默认构造函数
       */
      public Sort() {
         super();
         this.asc = Boolean.TRUE;
      }

      /**
       * 设置是否正序
       *
       * @param asc 是否正序
       */
      public void setAsc(Boolean asc) {
         this.asc = asc;
      }

      @Override
      public int hashCode() {
         int result = 59;
         Boolean asc = this.getAsc();
         result = result * 59 + (asc == null ? 43 : asc.hashCode());
         String sorter = this.getSorter();
         return result * 59 + (sorter == null ? 43 : sorter.hashCode());
      }

      /**
       * 获取排序列名
       *
       * @return 排序列名
       */
      public String getSorter() {
         return this.sorter;
      }

      @Override
      public boolean equals(Object object) {
         Sort that = (Sort) object;
         if (that == this) {
            return true;
         } else if (!(that instanceof Sort)) {
            return false;
         } else if (!that.canEqual(this)) {
            return false;
         } else {
            Boolean thatAsc = that.getAsc();
            Boolean thisAsc = this.getAsc();
            if (thisAsc == null) {
               if (thatAsc != null) {
                  return false;
               }
            } else if (!thisAsc.equals(thatAsc)) {
               return false;
            }

            String thatSorter = that.getSorter();
            String thisSorter = this.getSorter();
            if (thisSorter == null) {
               if (thatSorter != null) {
                  return false;
               }
            } else if (!thisSorter.equals(thatSorter)) {
               return false;
            }

            return true;
         }
      }
   }

   /**
    * 过滤内部类
    */
   @Schema(description = "过滤参数")
   @ParameterObject
   public static class Filter {
      /**
       * 过滤列名
       */
      @Parameter(description = "需要过滤的字段")
      private String sorter;

      /**
       * 检查是否可以比较
       *
       * @param object 对象
       * @return 是否可以比较
       */
      public boolean canEqual(Object object) {
         return object instanceof Filter;
      }

      @Override
      public String toString() {
         return "PageModel.Filter(sorter=" + this.getSorter() + ")";
      }

      /**
       * 设置过滤列名
       *
       * @param sorter 过滤列名
       */
      public void setSorter(String sorter) {
         this.sorter = sorter;
      }

      @Override
      public boolean equals(Object object) {
         Filter that = (Filter) object;
         if (that == this) {
            return true;
         } else if (!(that instanceof Filter)) {
            return false;
         } else if (!that.canEqual(this)) {
            return false;
         } else {
            String thatSorter = that.getSorter();
            String thisSorter = this.getSorter();
            if (thisSorter == null) {
               if (thatSorter != null) {
                  return false;
               }
            } else if (!thisSorter.equals(thatSorter)) {
               return false;
            }

            return true;
         }
      }

      @Override
      public int hashCode() {
         int result = 59;
         String sorter = this.getSorter();
         return result * 59 + (sorter == null ? 43 : sorter.hashCode());
      }

      /**
       * 获取过滤列名
       *
       * @return 过滤列名
       */
      public String getSorter() {
         return this.sorter;
      }
   }
}
