package com.doooly.common.meituan;

/**
 * Created by Administrator on 2018/12/17.
 */

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Author: chenzhenfeng@meituan.com
 * Date: 17/12/9 下午6:15
 * Description:
 */
public class EncryptUtil {
    private EncryptUtil() {

    }
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);
    private static final String ALGORITHM_HMAC_SHA1 = "HmacSHA1";
    private static final String BASE_NUMBER = "0123456789";

    // md5加密
    public static String md5(String plainText) {
        if (StringUtils.isBlank(plainText)) {
            return null;
        } else {
            return DigestUtils.md5Hex(plainText);
        }
    }

    // sha1加密
    public static String sha1(String plainText) {
        if (StringUtils.isBlank(plainText)) {
            return null;
        } else {
            return DigestUtils.sha1Hex(plainText);
        }
    }

    // base64
    public static String base64(String input) {
        return Base64.encodeBase64String(input.getBytes());
    }

    // HMAC_SHA1加密
    public static String hmacSha1(String plainText, String secretKey) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(secretKey), ALGORITHM_HMAC_SHA1);
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM_HMAC_SHA1);
            mac.init(secretKeySpec);
            return Base64.encodeBase64String(mac.doFinal(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(plainText)));
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // 生成AES秘钥
    public static String generateAESSecretKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom());
            return Base64.encodeBase64String(kgen.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("AesCypher.genKey NoSuchAlgorithmException", e);
            return null;
        }
    }

    // AES加密
    public static String aesEncrypt(String originText, String secret) throws Exception {
        AesCypher cypher = new AesCypher(secret);
        return cypher.encrypt(originText);
    }

    // AES解密
    public static String aesDecrypt(String encryptedText, String secret) throws Exception{
        AesCypher cypher = new AesCypher(secret);
        return cypher.decrypt(encryptedText);
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(BASE_NUMBER.charAt(random.nextInt(BASE_NUMBER.length())));
        }
        return sb.toString();
    }

    // 内部aes类
    static class AesCypher {
        private static final Logger LOGGER = LoggerFactory.getLogger(AesCypher.class);

        private static String DEFAULT_SECRET = "hqXj7NwyKYaL_Lc5TlCSzA==";
        private byte[] linebreak;
        private SecretKey key;
        private Cipher cipher;
        private Base64 coder;

        public AesCypher(String secret) {
            this.linebreak = new byte[0];

            try {
                this.coder = new Base64(32, this.linebreak, true);
                byte[] secrets = this.coder.decode(secret);
                // 转换为AES专用密钥
                this.key = new SecretKeySpec(secrets, "AES");
                // 创建密码器，算法/工作模式/补码方式 提供商
                this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            } catch (Exception e) {
                LOGGER.error("AesCypher.genKey NoSuchAlgorithmException", e);
            }

        }

        public AesCypher() {
            this(DEFAULT_SECRET);
        }

        public synchronized String encrypt(String plainText) throws Exception {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
            byte[] cipherText = this.cipher.doFinal(plainText.getBytes());
            return new String(this.coder.encode(cipherText));
        }

        public synchronized String decrypt(String codedText) throws Exception {
            byte[] encypted = this.coder.decode(codedText.getBytes());
            this.cipher.init(Cipher.DECRYPT_MODE, this.key);
            byte[] decrypted = this.cipher.doFinal(encypted);
            return new String(decrypted, "UTF-8");
        }
    }

    public static void main(String[] args) throws Exception {
        String plainText = "";
        // aes解密 和 解密
        String aesKey = EncryptUtil.generateAESSecretKey();
        System.out.println("AES秘钥: " + aesKey);
        String aesEncrypted = EncryptUtil.aesEncrypt(plainText, aesKey);
        System.out.println("AES密文: " + aesEncrypted);
        System.out.println("AES明文: " + EncryptUtil.aesDecrypt(aesEncrypted, aesKey));
    }
}
