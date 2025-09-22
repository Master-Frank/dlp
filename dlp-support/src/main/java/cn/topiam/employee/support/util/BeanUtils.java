package cn.topiam.employee.support.util;

import cn.topiam.employee.support.context.ApplicationContextService;
import com.google.common.collect.Maps;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class BeanUtils extends org.springframework.beans.BeanUtils {
   public static void merge(Object source, Object target, @Nullable String... ignoreProperties) throws BeansException {
      Object var10000 = source;
      String[] var3 = ignoreProperties;
      String[] ignoreProperties = (String[])var10000;
      merge(ignoreProperties, target, (Class)null, var3);
   }

   public static void merge(Object a, Object a) throws BeansException {
      Object var10000 = a;
      a = a;
      a = var10000;
      merge(a, a, (Class)null, (String[])null);
   }

   public static <T> Map<String, Object> beanToMap(T a) {
      HashMap var1 = Maps.newHashMap();
      if (a != null) {
         Iterator var3;
         BeanMap var4;
         Iterator var10000 = var3 = (var4 = BeanMap.create(a)).keySet().iterator();

         while(var10000.hasNext()) {
            var10000 = var3;
            a = var3.next();
            var1.put(a.toString(), var4.get(a));
         }
      }

      return var1;
   }

   public static <T> T mapToBean(Map<String, Object> a, Class<T> a) {
      Map var10000 = a;
      Class var4 = a;
      Class a = var10000;

      try {
         int var10001 = 2 & 5;
         boolean var10003 = true;
         Class[] var8 = new Class[var10001];
         var10003 = true;
         Constructor var7 = var4.getDeclaredConstructor(var8);
         int var9 = 3 & 4;
         var10003 = true;
         Object[] var10 = new Object[var9];
         var10003 = true;
         var5 = var7.newInstance(var10);
      } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException var3) {
         throw new RuntimeException(var3);
      }

      BeanMap var2 = BeanMap.create(var5);
      var2.putAll(a);
      return (T)var5;
   }

   public static void merge(Object source, Object target, @Nullable Class<?> editable, @Nullable String... ignoreProperties) throws BeansException {
      Class editable = (Class)source;
      Assert.notNull(source, ApplicationContextService.1_1_1_ce("\u001d6;+-<n4;*:y 6:y,<n7;5\""));
      Assert.notNull(target, VersionUtils.1_1_1_ce("\u0004I\"O5\\pE%[$\b>G$\b2MpF%D<"));
      Class var4 = target.getClass();
      if (Objects.nonNull(editable)) {
         if (!editable.isInstance(target)) {
            String var30 = target.getClass().getName();
            throw new IllegalArgumentException("Target class [" + var30 + "] not assignable to Editable class [" + editable.getName() + "]");
         }

         var4 = editable;
      }

      Object var11 = getPropertyDescriptors(var4);
      List var18 = Objects.nonNull(ignoreProperties) ? Arrays.asList(ignoreProperties) : null;
      String[] var17 = var11;
      int var5 = var11.length;
      int var19 = 3 & 4;
      int var10002 = 1;

      for(int var6 = var19; var19 < var5; var19 = var6) {
         PropertyDescriptor var7;
         Method var8;
         if (!Objects.nonNull(var8 = (var7 = var17[var6]).getWriteMethod()) || !Objects.isNull(var18) && var18.contains(var7.getName())) {
            var19 = 0;
            var10002 = (boolean)1;
         } else {
            var19 = 1;
            var10002 = (boolean)1;
         }

         Object var12 = (boolean)var19;
         PropertyDescriptor var13;
         Method var14;
         if (var12 && (var13 = getPropertyDescriptor(editable.getClass(), var7.getName())) != null && Objects.nonNull(var14 = var13.getReadMethod())) {
            Class[] var21 = var8.getParameterTypes();
            int var10001 = 3 & 4;
            int var10003 = 1;
            if (ClassUtils.isAssignable(var21[var10001], var14.getReturnType())) {
               try {
                  if (!Modifier.isPublic(var14.getDeclaringClass().getModifiers())) {
                     var10001 = 3 >> 1;
                     var10003 = 3 >> 1;
                     var14.setAccessible((boolean)var10001);
                  }

                  var10002 = 3 & 4;
                  int var10004 = 1;
                  Object[] var27 = new Object[var10002];
                  var10004 = (boolean)1;
                  Object var15 = var14.invoke(editable, var27);
                  if (!Modifier.isPublic(var8.getDeclaringClass().getModifiers())) {
                     var10001 = 2 ^ 3;
                     var10003 = 2 ^ 3;
                     var8.setAccessible((boolean)var10001);
                  }

                  if (Objects.nonNull(var15)) {
                     int var28 = 2 ^ 3;
                     var10004 = 2 ^ 3;
                     Object[] var29 = new Object[var28];
                     var10004 = 1;
                     var10004 = 3 >> 2;
                     boolean var10006 = true;
                     var29[var10004] = var15;
                     var8.invoke(target, var29);
                  }
               } catch (Throwable var9) {
                  throw new FatalBeanException("Could not copy property '" + var7.getName() + "' from source to target", var9);
               }
            }
         }

         ++var6;
      }

   }
}
