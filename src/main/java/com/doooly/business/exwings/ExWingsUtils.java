package com.doooly.business.exwings;

import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.RSA;
import com.doooly.common.constants.PropertiesHolder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static com.doooly.business.utils.RSA.getPrivateKey;
import static com.doooly.business.utils.RSA.getPublicKey;

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
            return RSA.sign(privKey, sbf.toString());
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
