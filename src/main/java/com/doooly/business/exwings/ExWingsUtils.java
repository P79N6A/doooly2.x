package com.doooly.business.exwings;

import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PropertiesHolder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * Created by WANG on 2018/7/11.
 */
public class ExWingsUtils {

    private static Logger logger = LoggerFactory.getLogger(ExWingsUtils.class);

    private static final String PRIVATE_KEY_PATH = PropertiesHolder.getProperty("mobike_private_key");
    private static final String PUBLIC_KEY_PATH = PropertiesHolder.getProperty("mobike_public_key");
    public static final String RECHARGE_URL = PropertiesHolder.getProperty("mobike_recharge_url");
    private static final String appKey = PropertiesHolder.getProperty("mobike_appkey");

//    private static final RSAPrivateKey privKey = getPrivateKey("C:\\idea_space\\master\\doooly\\src\\main\\resources\\exwings\\test\\rsa_private_key_pkcs8.pem");
//    private static final RSAPublicKey publicKey = getPublicKey("C:\\idea_space\\master\\doooly\\src\\main\\resources\\exwings\\test\\exwigns_mobike_public_key_2048.pem");
//    private static final String appKey = "6811517d994514d96df1b95df460fa42";
//    public static final String BALANCE_URL = "http://47.93.89.6/mobike/balance.php";
//    public static final String RECHARGE_URL = "http://47.93.89.6/mobike/recharge_v2.php";

    private static final RSAPrivateKey privKey = getPrivateKey(getRootPath() + "/WEB-INF/classes/" + PRIVATE_KEY_PATH);
    private static final RSAPublicKey publicKey = getPublicKey(getRootPath() + "/WEB-INF/classes/" + PUBLIC_KEY_PATH);

    public static final String BALANCE_URL = "http://47.93.89.6/mobike/balance.php";

    public static String getRootPath() {
        String classPath = ExWingsUtils.class.getClassLoader().getResource("/").getPath();
        String rootPath = "";
        //windows下
        if ("\\".equals(File.separator)) {
            rootPath = classPath.substring(1, classPath.indexOf("/WEB-INF"));
            rootPath = rootPath.replace("%20", " ");
        }
        //linux下
        if ("/".equals(File.separator)) {
            rootPath = classPath.substring(0, classPath.indexOf("/WEB-INF"));
        }
        return rootPath;
    }

    private static String getKey(String filename) throws IOException {
        // Read key from file
        String strKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM += line + "\n";
        }
        br.close();
        return strKeyPEM;
    }

    public static RSAPrivateKey getPrivateKey(String filename) {
        try {
            logger.info("privateKey = " + filename);
            String privateKeyPEM = getKey(filename);
            return getPrivateKeyFromString(privateKeyPEM);
        } catch (Exception e) {
            logger.error("getPrivateKey e = {}", e);
            e.printStackTrace();
        }
        return null;
    }

    public static RSAPrivateKey getPrivateKeyFromString(String key) throws IOException, GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
        return privKey;
    }

    public static RSAPublicKey getPublicKey(String filename) {
        try {
            logger.info("publicKey = " + filename);
            String publicKeyPEM = getKey(filename);
            return getPublicKeyFromString(publicKeyPEM);
        } catch (Exception e) {
            logger.error("getPublicKey e = {}", e);
            e.printStackTrace();
        }
        return null;
    }

    public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }

    public static String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sign = Signature.getInstance("SHA1withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(sign.sign()), "UTF-8");
    }

    public static boolean verify(PublicKey publicKey, String message, String signature) throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA1withRSA");
        sign.initVerify(publicKey);
        sign.update(message.getBytes("UTF-8"));
        return sign.verify(Base64.decodeBase64(signature.getBytes("UTF-8")));
    }

    public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes("UTF-8")));
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), "UTF-8");
    }

    public static String getTs() {
        return (System.currentTimeMillis() / 1000) + "";
    }

    public static boolean exwingsVerify(String signature, Object... args) {
        try {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                message.append(args[i]);
            }
            message.append(appKey);
            Signature sign = Signature.getInstance("SHA1withRSA");
            sign.initVerify(publicKey);
            sign.update(message.toString().getBytes("UTF-8"));
            return sign.verify(Base64.decodeBase64(signature.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String exwingsSign(Object... args) {
        try {
            StringBuilder sbf = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                sbf.append(args[i]);
            }
            sbf.append(appKey);
            logger.info("signStr={},", sbf.toString());
            return sign(privKey, sbf.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //交易流水号的唯一标识，英文字符和数字混排，长度14位，YYMMDD+8位数字，不可为空
    public static String getOrderId() {
        String dateStr = DateUtils.formatDate(new Date(), "yyMMdd");
        return dateStr + (int) ((Math.random() * 9 + 1) * 10000000);
    }
}
