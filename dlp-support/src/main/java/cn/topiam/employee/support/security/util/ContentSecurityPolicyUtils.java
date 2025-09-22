package cn.topiam.employee.support.security.util;

import com.shapesecurity.salvation2.Policy;

public final class ContentSecurityPolicyUtils {
   public static Policy parse(String a) {
      return Policy.parseSerializedCSP(a, (ax, axx, axxx, axxxx) -> {
         Policy.Severity var10000 = ax;
         int var4 = axxxx;
         int a = (int)var10000;
         throw new ContentSecurityPolicyPolicyException(new PolicyError(a, axx, axxx, var4));
      });
   }

   public static final class PolicyError extends Record {
      // $FF: synthetic field
      private final int G;
      // $FF: synthetic field
      private final Policy.Severity D;
      // $FF: synthetic field
      private final String b;
      // $FF: synthetic field
      private final int 1_1_1_ce;

      public boolean equals(Object a) {
         PolicyError var10000 = a;
         Object var2 = a;
         a = var10000;
         if (a == var2) {
            int var6 = 3 & 5;
            int var9 = 3 & 5;
            return (boolean)var6;
         } else if (var2 != null && a.getClass() == var2.getClass()) {
            PolicyError var3 = (PolicyError)var2;
            if (((PolicyError)a).G == var3.G && ((PolicyError)a).1_1_1_ce == var3.1_1_1_ce && ((PolicyError)a).D == var3.D && ((PolicyError)a).b.equals(var3.b)) {
               int var5 = 3 & 5;
               int var8 = 3 & 5;
               return (boolean)var5;
            } else {
               boolean var7 = true;
               return false;
            }
         } else {
            boolean var10002 = true;
            return false;
         }
      }

      public final int hashCode() {
         return a.hashCode<invokedynamic>(a);
      }

      public int valueIndex() {
         return a.1_1_1_ce;
      }

      public Policy.Severity severity() {
         return a.D;
      }

      public String message() {
         return a.b;
      }

      public String toString() {
         String var10000 = a.D.name();
         return "(" + var10000 + ") " + a.b + " at directive " + a.G + " at value " + a.1_1_1_ce;
      }

      public PolicyError(Policy.Severity a, String a, int a, int a) {
         PolicyError var10000 = a;
         int var5 = a;
         int a = (int)var10000;
         a.<init>();
         a.D = a;
         a.b = a;
         a.G = a;
         a.1_1_1_ce = var5;
      }

      public int directiveIndex() {
         return a.G;
      }
   }

   public static class ContentSecurityPolicyPolicyException extends RuntimeException {
      // $FF: synthetic field
      private final PolicyError 1_1_1_ce;

      public ContentSecurityPolicyPolicyException(PolicyError a) {
         ContentSecurityPolicyPolicyException var10000 = a;
         PolicyError var2 = a;
         PolicyError a = var10000;
         a.<init>();
         a.1_1_1_ce = var2;
      }

      public PolicyError getError() {
         return a.1_1_1_ce;
      }
   }
}
