package com.doooly.common.meituan;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by liujinxin on 2017/12/27.
 * api 对称加密算法
 */
public class SimpleStringCypher {

    private static String DEFAULT_SECRET = "0vno63ufgaght892";
    private byte[] linebreak = {}; // Remove Base64 encoder default linebreak
    private SecretKey key;
    private Cipher cipher;
    private Base64 coder;

    public SimpleStringCypher(String secret) {
        try {

            coder = new Base64(32, linebreak, true);

            byte[] secrets = coder.decode(secret);
            key = new SecretKeySpec(secrets, "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SimpleStringCypher() {
        this(DEFAULT_SECRET);
    }

    public synchronized String encrypt(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return new String(coder.encode(cipherText));
    }

    public synchronized String decrypt(String codedText) throws Exception {
        byte[] encypted = coder.decode(codedText.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(encypted);
        return new String(decrypted, "UTF-8");
    }
}
