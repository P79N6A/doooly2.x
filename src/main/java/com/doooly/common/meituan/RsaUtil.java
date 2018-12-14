package com.doooly.common.meituan;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author liujinxin
 *重要函数  私钥
 * 2017/9/26
 */
public class RsaUtil {



    public static final String ENCRYPT_ALGORTHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    public static void genKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ENCRYPT_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        System.out.println("公钥:"+ new  String( Base64.encodeBase64(publicKey.getEncoded())));
        System.out.println("私钥:" +new  String( Base64.encodeBase64(privateKey.getEncoded())));

    }



    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_ALGORTHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空", e);
        }
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空", e);
        }
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data
     *            //加密数据
     * @param privateKey
     *            //私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, RSAPrivateKey privateKey) throws Exception {

        // 用A方私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);

        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 用公钥验证签名
     *
     * @param data
     *            //加密数据
     * @param publicKey
     *            //公钥
     * @param sign
     *            //签名
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws Exception
     */
     static boolean validate(byte[] data, RSAPublicKey publicKey, String sign) throws Exception {

        Signature signature;
        signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);

        return signature.verify(Base64.decodeBase64(sign));

    }

    public static boolean validate(byte[] data, String publicKey, String sign) throws Exception {

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(loadPublicKey(publicKey));
        signature.update(data);

        return signature.verify(Base64.decodeBase64(sign));
    }
    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encryptedString,RSAPrivateKey privateKey)
        throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORTHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedData  = Base64.decodeBase64(encryptedString);
        int inputLen = encryptedData.length;
        byte[] decryptedData;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();
        } finally {
            out.close();
        }
        return new String(decryptedData, "UTF-8");
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据 base64格式
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String encryptedString, RSAPublicKey publicKey)
        throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORTHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] encryptedData = Base64.decodeBase64(encryptedString);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = null;
        byte[] decryptedData;
        try {
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();
        } finally {
            out.close();
        }

        return  new String(decryptedData, "UTF-8");
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String plainText, RSAPublicKey publicKey)
        throws Exception {
        byte[] data = plainText.getBytes("UTF-8");
        // 对数据加密
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORTHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = null;
        byte[] encryptedData;
        try {
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            encryptedData = out.toByteArray();
        } finally {
            out.close();
        }
        return  Base64.encodeBase64String(encryptedData);
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String plainText, RSAPrivateKey privateKey)
        throws Exception {
        byte[] data = plainText.getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORTHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = null;
        byte[] encryptedData;
        try {
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            encryptedData = out.toByteArray();
        } finally {
            out.close();
        }

        return Base64.encodeBase64String(encryptedData);
    }
}
