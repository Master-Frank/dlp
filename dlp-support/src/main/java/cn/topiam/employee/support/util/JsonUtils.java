package cn.topiam.employee.support.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class JsonUtils {
   // $FF: synthetic field
   private static final ObjectMapper 1_1_1_ce = new ObjectMapper();

   public static <T> T readValue(String a, TypeReference<T> a) {
      String var10000 = a;
      TypeReference var3 = a;
      TypeReference a = var10000;

      try {
         return (T)(hasText(a) ? 1_1_1_ce.readValue(a, var3) : null);
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static <T> T readValue(byte[] a, Class<T> a) throws JsonUtilException {
      byte[] var10000 = a;
      Class var3 = a;
      Class a = var10000;

      try {
         return (T)(a != null && ((Object[])a).length > 0 ? 1_1_1_ce.readValue((byte[])a, var3) : null);
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static Map<String, Object> getNodeAsMap(JsonNode a) {
      return (Map)1_1_1_ce.convertValue(a, Map.class);
   }

   public static <T> T readValue(String a, Class<T> a) throws JsonUtilException {
      String var10000 = a;
      Class var3 = a;
      Class a = var10000;

      try {
         return (T)(hasText(a) ? 1_1_1_ce.readValue(a, var3) : null);
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static Date getNodeAsDate(JsonNode a, String a) {
      JsonNode var10000 = a;
      String var4 = a;
      String a = var10000;
      JsonNode var5;
      long var2 = (var5 = ((JsonNode)a).get(var4)) == null ? -1L : var5.asLong(-1L);
      return var2 == -1L ? null : new Date(var2);
   }

   public static boolean getNodeAsBoolean(JsonNode a, String a, boolean a) {
      JsonNode var10000 = a;
      String var3 = a;
      String a = var10000;
      JsonNode var4;
      return (boolean)((var4 = ((JsonNode)a).get(var3)) == null ? a : var4.asBoolean((boolean)a));
   }

   public static String writeValueAsString(Object a) throws JsonUtilException {
      Object var1 = a;

      try {
         return 1_1_1_ce.writeValueAsString(var1);
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static JsonNode readTree(JsonParser a) {
      JsonParser var1 = a;

      try {
         return (JsonNode)1_1_1_ce.readTree(var1);
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static byte[] writeValueAsBytes(Object a) throws JsonUtilException {
      Object var1 = a;

      try {
         return 1_1_1_ce.writeValueAsBytes(var1);
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static int getNodeAsInt(JsonNode a, String a, int a) {
      JsonNode var10000 = a;
      String var3 = a;
      String a = var10000;
      JsonNode var4;
      return (var4 = ((JsonNode)a).get(var3)) == null ? a : var4.asInt(a);
   }

   public static String getNodeAsString(JsonNode a, String a, String a) {
      JsonNode var10000 = a;
      String var3 = a;
      String a = var10000;
      JsonNode var4;
      return (var4 = ((JsonNode)a).get(var3)) == null ? a : var4.asText(a);
   }

   public static <T> T readValue(byte[] a, TypeReference<T> a) {
      byte[] var10000 = a;
      TypeReference var3 = a;
      TypeReference a = var10000;

      try {
         return (T)(a != null && ((Object[])a).length > 0 ? 1_1_1_ce.readValue((byte[])a, var3) : null);
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static JsonNode readTree(String a) {
      String var1 = a;

      try {
         return hasText(var1) ? 1_1_1_ce.readTree(var1) : null;
      } catch (IOException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static boolean hasText(CharSequence a) {
      CharSequence var2 = a;
      if (!hasLength(a)) {
         int var5 = 3 & 4;
         boolean var8 = true;
         return (boolean)var5;
      } else {
         int var1 = a.length();
         int var10000 = 3 & 4;
         int var10002 = 1;

         for(CharSequence var3 = var10000; var10000 < var1; var10000 = var3) {
            if (!Character.isWhitespace(var2.charAt(var3))) {
               var10000 = 5 >> 2;
               var10002 = 5 >> 2;
               return (boolean)var10000;
            }

            ++var3;
         }

         var10002 = 1;
         return false;
      }
   }

   public static <T> T convertValue(Object a, Class<T> a) throws JsonUtilException {
      Object var10000 = a;
      Class var3 = a;
      Class a = (Class)var10000;

      try {
         return (T)(a == null ? null : 1_1_1_ce.convertValue(a, var3));
      } catch (IllegalArgumentException var2) {
         throw new JsonUtilException(var2);
      }
   }

   public static String serializeExcludingProperties(Object a, String... a) {
      Object var8 = writeValueAsString(a);
      Object var9 = (Map)readValue(var8, new TypeReference<Map<String, Object>>() {
      });
      String[] var10;
      int var6 = (var10 = a).length;
      int var10000 = 3 & 4;
      int var10002 = 1;

      for(int var4 = var10000; var10000 < var6; var10000 = var4) {
         String var5;
         if ((var5 = var10[var4]).contains(".")) {
            String var10001 = PhoneUtils.1_1_1_ce("\u001e<");
            boolean var10004 = true;
            String[] var3 = var5.split(var10001, 2);
            var10002 = 2 & 5;
            var10004 = true;
            if (var9.containsKey(var3[var10002])) {
               int var10003 = 3 >> 2;
               int var10005 = 1;
               Object var7 = var9.get(var3[var10003]);
               var10002 = 3 & 4;
               var10004 = true;
               var10001 = var3[var10002];
               var10003 = 2 ^ 3;
               var10005 = 2 ^ 3;
               String[] var15 = new String[var10003];
               var10005 = 1;
               var10005 = 3 & 4;
               int var10007 = 1;
               var10007 = 3 >> 1;
               int var10009 = 3 >> 1;
               var15[var10005] = var3[var10007];
               var9.put(var10001, readValue(serializeExcludingProperties(var7, var15), new TypeReference<Map<String, Object>>() {
               }));
            }
         } else {
            var9.remove(var5);
         }

         ++var4;
      }

      return writeValueAsString(var9);
   }

   public static boolean hasLength(CharSequence a) {
      if (a != null && !a.isEmpty()) {
         int var10000 = 3 >> 1;
         int var1 = 3 >> 1;
         return (boolean)var10000;
      } else {
         boolean var10002 = true;
         return false;
      }
   }

   public static class JsonUtilException extends RuntimeException {
      // $FF: synthetic field
      private static final long 1_1_1_ce = -4804245225960963421L;

      public JsonUtilException(Throwable a) {
         JsonUtilException var10000 = a;
         Throwable var2 = a;
         Throwable a = var10000;
         a.<init>(var2);
      }
   }
}
