package com.doooly.business.nexus;

import com.doooly.business.exwings.ExWingsUtils;
import com.doooly.business.utils.RSA;
import com.doooly.common.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static com.doooly.business.utils.RSA.getPrivateKey;
import static com.doooly.business.utils.RSA.getPublicKey;

/**
 * Created by WANG on 2018/7/20.
 */
public class NexusUtil {

    private static Logger logger = LoggerFactory.getLogger(NexusUtil.class);


    private static final String APPSECRECT = "90586B08844AB181D6878FF9DDF118C9";


    //兜礼私钥
//    private static final String PRIVATE_KEY_PATH = PropertiesHolder.getProperty("mobike_private_key");
//    private static final RSAPrivateKey privKey = getPrivateKey(getRootPath() + "/WEB-INF/classes/" + PRIVATE_KEY_PATH);
//    //集享公钥
//    private static final String PUBLIC_KEY_PATH = PropertiesHolder.getProperty("mobike_public_key");
//    private static final RSAPublicKey publicKey = getPublicKey(getRootPath() + "/WEB-INF/classes/" + PUBLIC_KEY_PATH);



    private static final RSAPrivateKey privKey = getPrivateKey("C:\\idea_space\\master\\doooly\\src\\main\\resources\\nexus\\test\\rsa_private_key.pem");
    private static final RSAPublicKey publicKey = getPublicKey("C:\\idea_space\\master\\doooly\\src\\main\\resources\\nexus\\test\\nexus_public_key.pem");

    public static String encryptByPublicKey(String rawText){
        logger.info("rawText = {}",rawText);
        try {
            return RSA.encryptByPublicKey(rawText, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String data) {
        try {
            logger.info("data={}", data);
            return RSA.sign(privKey, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createNonce(SortedMap<String, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        parameters.put("appSecret",key);
        Set es = parameters.entrySet();
        // 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            sb.append("&");
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v);
            }
        }
        String deeplink = "";
        if (sb.length() > 0) {
            deeplink = sb.substring(1, sb.length());
        }
        //sb.append("key=" + key);
        logger.info("deeplink = {}", deeplink);
        String sign = MD5Utils.encode(deeplink);
        return sign;
    }


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

}
