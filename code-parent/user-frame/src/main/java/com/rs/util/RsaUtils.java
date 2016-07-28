package com.rs.util;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

public abstract class RsaUtils
{
  public static final String ALGORITHM = "RSA";
  public static final String ALGORITHM_FULL = "RSA/ECB/PKCS1Padding";
  public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
  public static final int DEFAULT_KEY_SIZE = 2048;
  
  public static Map.Entry<PrivateKey, PublicKey> genKeyPair()
  {
    return genKeyPair(2048);
  }
  
  public static Map.Entry<PrivateKey, PublicKey> genKeyPair(int keySize)
  {
    try
    {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
      keyPairGen.initialize(keySize);
      KeyPair keyPair = keyPairGen.generateKeyPair();
      
      return new ImmutablePair(keyPair.getPrivate(), keyPair.getPublic());
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static Map.Entry<String, String> genKeyPairStr()
  {
    return genKeyPairStr(2048);
  }
  
  public static Map.Entry<String, String> genKeyPairStr(int keySize)
  {
    Map.Entry<PrivateKey, PublicKey> p = genKeyPair(keySize);
    
    return new ImmutablePair(fromPrivateKey((PrivateKey)p.getKey()), fromPublicKey((PublicKey)p.getValue()));
  }
  
  public static String fromPrivateKey(PrivateKey key)
  {
    return Base64.encodeBase64String(key.getEncoded());
  }
  
  public static String fromPublicKey(PublicKey key)
  {
    return Base64.encodeBase64String(key.getEncoded());
  }
  
  public static PrivateKey toPrivateKey(String key)
  {
    byte[] keyBytes = Base64.decodeBase64(key);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    try
    {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePrivate(pkcs8KeySpec);
    }
    catch (NoSuchAlgorithmException|InvalidKeySpecException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static PublicKey toPublicKey(String key)
  {
    byte[] keyBytes = Base64.decodeBase64(key);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    try
    {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(x509KeySpec);
    }
    catch (NoSuchAlgorithmException|InvalidKeySpecException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static String fromXmlPublicKey(String key)
  {
    return fromXmlPublicKey(toPublicKey(key));
  }
  
  public static String fromXmlPublicKey(PublicKey key)
  {
    RSAPublicKey pubKey = (RSAPublicKey)key;
    StringBuilder sb = new StringBuilder();
    sb.append("<RSAKeyValue>");
    sb.append("<Modulus>")
      .append(Base64.encodeBase64String(removeMsZero(pubKey.getModulus()
      .toByteArray()))).append("</Modulus>");
    sb.append("<Exponent>")
      .append(Base64.encodeBase64String(removeMsZero(pubKey.getPublicExponent()
      .toByteArray()))).append("</Exponent>");
    sb.append("</RSAKeyValue>");
    return sb.toString();
  }
  
  private static byte[] removeMsZero(byte[] data)
  {
    int len = data.length;
    byte[] data1;
    if (data[0] == 0)
    {
      data1 = new byte[data.length - 1];
      System.arraycopy(data, 1, data1, 0, len - 1);
    }
    else
    {
      data1 = data;
    }
    return data1;
  }
  
  public static byte[] decrypt(byte[] encryptedData, Key key)
  {
    return decrypt(encryptedData, key, 2048);
  }
  
  public static byte[] decrypt(byte[] encryptedData, Key key, int keySize)
  {
    int maxDecryptBlock = keySize / 8;
    ByteArrayOutputStream out = null;
    try
    {
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(2, key);
      int inputLen = encryptedData.length;
      out = new ByteArrayOutputStream();
      int offSet = 0;
      
      int i = 0;
      while (inputLen - offSet > 0)
      {
        byte[] cache;
        if (inputLen - offSet > maxDecryptBlock) {
          cache = cipher.doFinal(encryptedData, offSet, maxDecryptBlock);
        } else {
          cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
        }
        out.write(cache, 0, cache.length);
        i++;
        offSet = i * maxDecryptBlock;
      }
      return out.toByteArray();
    }
    catch (NoSuchAlgorithmException|IllegalBlockSizeException|BadPaddingException|NoSuchPaddingException|InvalidKeyException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      IOUtils.closeQuietly(out);
    }
  }
  
  public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
  {
    return decrypt(encryptedData, toPrivateKey(privateKey));
  }
  
  public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey, int keySize)
  {
    return decrypt(encryptedData, toPrivateKey(privateKey), keySize);
  }
  
  public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey, int keySize)
  {
    return decrypt(encryptedData, toPublicKey(publicKey), keySize);
  }
  
  public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
  {
    return decrypt(encryptedData, toPublicKey(publicKey));
  }
  
  public static byte[] encrypt(byte[] data, Key key)
  {
    return encrypt(data, key, 2048);
  }
  
  public static byte[] encrypt(byte[] data, Key key, int keySize)
  {
    int maxEncryptBlock = keySize / 8 - 11;
    ByteArrayOutputStream out = null;
    try
    {
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(1, key);
      int inputLen = data.length;
      out = new ByteArrayOutputStream();
      int offSet = 0;
      
      int i = 0;
      while (inputLen - offSet > 0)
      {
        byte[] cache;
        if (inputLen - offSet > maxEncryptBlock) {
          cache = cipher.doFinal(data, offSet, maxEncryptBlock);
        } else {
          cache = cipher.doFinal(data, offSet, inputLen - offSet);
        }
        out.write(cache, 0, cache.length);
        i++;
        offSet = i * maxEncryptBlock;
      }
      return out.toByteArray();
    }
    catch (NoSuchAlgorithmException|IllegalBlockSizeException|BadPaddingException|NoSuchPaddingException|InvalidKeyException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      IOUtils.closeQuietly(out);
    }
  }
  
  public static byte[] encryptByPublicKey(byte[] data, String publicKey)
  {
    return encrypt(data, toPublicKey(publicKey));
  }
  
  public static byte[] encryptByPublicKey(byte[] data, String publicKey, int keySize)
  {
    return encrypt(data, toPublicKey(publicKey), keySize);
  }
  
  public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
  {
    return encrypt(data, toPrivateKey(privateKey));
  }
  
  public static byte[] encryptByPrivateKey(byte[] data, String privateKey, int keySize)
  {
    return encrypt(data, toPrivateKey(privateKey), 2048);
  }
  
  public static String sign(byte[] data, PrivateKey privateKey)
  {
    try
    {
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initSign(privateKey);
      signature.update(data);
      return Base64.encodeBase64String(signature.sign());
    }
    catch (NoSuchAlgorithmException|InvalidKeyException|SignatureException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static String sign(byte[] data, String privateKey)
  {
    return sign(data, toPrivateKey(privateKey));
  }
  
  public static boolean verifySign(byte[] data, PublicKey publicKey, String sign)
  {
    try
    {
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initVerify(publicKey);
      signature.update(data);
      return signature.verify(Base64.decodeBase64(sign));
    }
    catch (NoSuchAlgorithmException|InvalidKeyException|SignatureException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static boolean verifySign(byte[] data, String publicKey, String sign)
  {
    return verifySign(data, toPublicKey(publicKey), sign);
  }
}
