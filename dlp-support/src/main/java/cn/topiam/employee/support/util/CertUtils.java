package cn.topiam.employee.support.util;

import cn.topiam.employee.support.exception.TopIamException;
import cn.topiam.employee.support.repository.page.domain.ExampleRequest;
import cn.topiam.employee.support.security.jackjson.GrantedAuthorityMixin;
import jakarta.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import okhttp3.CertificatePinner;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertUtils {
   // $FF: synthetic field
   private static final Logger 1_1_1_ce = LoggerFactory.getLogger(CertUtils.class);

   public static byte[] getDer(String a) {
      return DatatypeConverter.parseBase64Binary(keyCleanup(a));
   }

   public static String isRoot(X509Certificate a) {
      return String.valueOf(a.getSubjectX500Principal().equals(a.getIssuerX500Principal()));
   }

   public static String getNotAfter(X509Certificate a) {
      return a.getNotAfter().toString();
   }

   public static String getIssuerDn(X509Certificate a) {
      X509Certificate var2 = a.getIssuerX500Principal().toString();
      int var10001 = GrantedAuthorityMixin.1_1_1_ce("DU:").length();
      boolean var10005 = true;
      return var2.substring(var10001, var2.indexOf(44));
   }

   public static PrivateKey readPrivateKey(String a, String a) throws IOException {
      String var2 = a;
      PEMParser var10000 = new PEMParser(new CharArrayReader(a.toCharArray()));
      String var6 = var10000.readObject();
      var10000.close();
      JcaPEMKeyConverter var3 = (new JcaPEMKeyConverter()).setProvider(ExampleRequest.1_1_1_ce("{r"));
      if (var6 == null) {
         throw new NullPointerException("Unable to decode PEM key:" + var2);
      } else {
         KeyPair var9;
         if (var6 instanceof PEMEncryptedKeyPair) {
            PEMEncryptedKeyPair var4 = (PEMEncryptedKeyPair)var6;
            PEMDecryptorProvider var5 = (new JcePEMDecryptorProviderBuilder()).build(a.toCharArray());
            var9 = var3.getKeyPair(var4.decryptKeyPair(var5));
         } else {
            if (var6 instanceof PrivateKeyInfo) {
               String var7 = (PrivateKeyInfo)var6;
               return var3.getPrivateKey(var7);
            }

            PEMKeyPair var8 = (PEMKeyPair)var6;
            var9 = var3.getKeyPair(var8);
         }

         return var9.getPrivate();
      }
   }

   public static String getPin(X509Certificate a) {
      return CertificatePinner.pin(a).substring(ExampleRequest.1_1_1_ce("BQP\u000b\u0004\u000f\u001e").length());
   }

   public static X509Certificate getCertificate(byte[] a) throws CertificateException {
      byte[] var1 = a;
      byte[] var2 = CertificateFactory.getInstance(GrantedAuthorityMixin.1_1_1_ce("_52+>"));
      return (X509Certificate)var2.generateCertificate(new ByteArrayInputStream(var1));
   }

   public static X500Name getX500Name(String a, String a, String a, String a, String a, String a) {
      String var6 = a;
      X500NameBuilder var7;
      (var7 = new X500NameBuilder(BCStrictStyle.INSTANCE)).addRDN(BCStyle.CN, var6);
      var7.addRDN(BCStyle.O, a);
      var7.addRDN(BCStyle.L, a);
      var7.addRDN(BCStyle.ST, a);
      var7.addRDN(BCStyle.C, a);
      var7.addRDN(BCStyle.OU, a);
      return var7.build();
   }

   public static String getCertificate(X500Name a, X500Name a, BigInteger a, Date a, Date a, PublicKey a, PrivateKey a) throws OperatorCreationException, CertificateException, IOException {
      X500Name var10000 = a;
      a = a;
      a = var10000;
      BigInteger var12 = new JcaX509v3CertificateBuilder(a, a, a, a, a, a);
      if (a == a) {
         int var10002 = 3 & 5;
         int var10004 = 3 & 5;
         X500Name var8 = new BasicConstraints(var10002);
         var10002 = 5 >> 3;
         var10004 = 1;
         var12.addExtension(Extension.basicConstraints, (boolean)var10002, var8);
      }

      X500Name var9 = (new JcaContentSignerBuilder(GrantedAuthorityMixin.1_1_1_ce("TSF)2-PRSSUHF"))).setProvider(ExampleRequest.1_1_1_ce("{r")).build(a);
      X500Name var10 = var12.build(var9);
      BigInteger var13 = new JcaX509CertificateConverter();
      var13.setProvider(GrantedAuthorityMixin.1_1_1_ce("YD"));
      return getCertificate((Certificate)var13.getCertificate(var10));
   }

   public static X509Certificate loadCertFromString(String a) {
      String var1 = a;

      try {
         Security.addProvider(new BouncyCastleProvider());
         String var4 = CertificateFactory.getInstance(GrantedAuthorityMixin.1_1_1_ce("_52+>"), ExampleRequest.1_1_1_ce("{r"));
         byte[] var2 = Base64.decode(keyCleanup(var1));
         ByteArrayInputStream var5 = new ByteArrayInputStream(var2);
         return (X509Certificate)var4.generateCertificate(var5);
      } catch (NoSuchProviderException | CertificateException var3) {
         1_1_1_ce.error(((GeneralSecurityException)var3).getMessage());
         throw new TopIamException(((GeneralSecurityException)var3).getMessage(), var3);
      }
   }

   public static String keyCleanup(String a) {
      return a.replaceAll(ExampleRequest.1_1_1_ce("\u0014\u001c\u0014\u001c\u0014s|vp\u007f\u0019\u0019\u0017\u001b\u0010\u001c\u0014\u001c\u0014\u001c"), "").replaceAll(GrantedAuthorityMixin.1_1_1_ce("*6*6*^I_'3)1.6*6*"), "").replaceAll(ExampleRequest.1_1_1_ce("eB"), "").trim();
   }

   public static PublicKey readPublicKey(String a, String a) throws IOException {
      String var2 = a;
      PEMParser var10000 = new PEMParser(new CharArrayReader(a.toCharArray()));
      String var6 = var10000.readObject();
      var10000.close();
      JcaPEMKeyConverter var3 = (new JcaPEMKeyConverter()).setProvider(GrantedAuthorityMixin.1_1_1_ce("YD"));
      if (var6 == null) {
         throw new NullPointerException("Unable to decode PEM key:" + var2);
      } else {
         KeyPair var9;
         if (var6 instanceof PEMEncryptedKeyPair) {
            PEMEncryptedKeyPair var4 = (PEMEncryptedKeyPair)var6;
            PEMDecryptorProvider var5 = (new JcePEMDecryptorProviderBuilder()).build(a.toCharArray());
            var9 = var3.getKeyPair(var4.decryptKeyPair(var5));
         } else {
            if (var6 instanceof SubjectPublicKeyInfo) {
               String var7 = (SubjectPublicKeyInfo)var6;
               return var3.getPublicKey(var7);
            }

            PEMKeyPair var8 = (PEMKeyPair)var6;
            var9 = var3.getKeyPair(var8);
         }

         return var9.getPublic();
      }
   }

   public static X509Certificate loadCertFromInputStream(InputStream a) throws Exception {
      InputStream var1 = a;

      try {
         return (X509Certificate)CertificateFactory.getInstance(GrantedAuthorityMixin.1_1_1_ce("_52+>")).generateCertificate(var1);
      } catch (CertificateException var2) {
         1_1_1_ce.error(ExampleRequest.1_1_1_ce("诰也旑泬请划］诸乗籂垺忼顊昖i\u0017\u0004\t\b"));
         throw new Exception(GrantedAuthorityMixin.1_1_1_ce("识乽旧泎证到＋诚乡籠垌忞顼昴_52+>"), var2);
      }
   }

   static {
      Security.addProvider(new BouncyCastleProvider());
   }

   public static String encodePem(Certificate a) {
      Certificate var1 = a;

      try {
         return toPem(ExampleRequest.1_1_1_ce("r|cmx\u007fxzpmt"), var1.getEncoded());
      } catch (CertificateEncodingException var2) {
         throw new TopIamException(GrantedAuthorityMixin.1_1_1_ce("诚乡缍砆弙帿"), var2);
      }
   }

   public static RSAPrivateKey getPrivateKey(byte[] a, String a) throws InvalidKeySpecException, NoSuchAlgorithmException {
      byte[] var3 = new PKCS8EncodedKeySpec(a);
      return (RSAPrivateKey)KeyFactory.getInstance(a).generatePrivate(var3);
   }

   public static String encodePem(PublicKey a) {
      return toPem(GrantedAuthorityMixin.1_1_1_ce("KRYKRD;L^^"), a.getEncoded());
   }

   public static String encodePem(PrivateKey a) {
      return toPem(ExampleRequest.1_1_1_ce("akxopmt\u0019z|h"), a.getEncoded());
   }

   public static String toPem(String a, byte[] a) {
      byte[] var9 = new PemObject(a, a);
      StringWriter var2 = new StringWriter();

      try {
         String var8 = new PemWriter(var2);

         try {
            var8.writeObject(var9);
         } catch (Throwable var6) {
            Throwable var10000;
            try {
               var8.close();
            } catch (Throwable var5) {
               var10000 = var6;
               var6.addSuppressed(var5);
               throw var10000;
            }

            var10000 = var6;
            throw var10000;
         }

         var8.close();
      } catch (IOException var7) {
         throw new RuntimeException(var7);
      }

      return var2.toString();
   }

   public static byte[] getDer(String a, String a, String a) {
      String var10000 = a;
      a = a;
      a = var10000;
      var10000 = a.split(a);
      int var10001 = 3 >> 2;
      boolean var10003 = true;
      var10000 = ((Object[])var10000)[var10001].split(a);
      var10001 = 3 ^ 3;
      var10003 = true;
      return getDer(((Object[])var10000)[var10001]);
   }

   public static String getCertificate(Certificate a) {
      return encodePem(a);
   }

   public static String getSubjectDn(X509Certificate a) {
      X509Certificate var2 = a.getSubjectX500Principal().toString();
      int var10001 = ExampleRequest.1_1_1_ce("rw\f").length();
      boolean var10005 = true;
      return var2.substring(var10001, var2.indexOf(44));
   }
}
