package com.doooly.business.nexus;

import com.doooly.business.exwings.ExWingsUtils;
import com.doooly.common.constants.PropertiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

    private static final String PRIVATE_KEY_PATH = PropertiesHolder.getProperty("nexus_private_key");
    private static final String PUBLIC_KEY_PATH = PropertiesHolder.getProperty("nexus_public_key");

    public static final String jxcPublicKey = getKey(getRootPath() + "/WEB-INF/classes/" + PRIVATE_KEY_PATH);
    public static final String privateKey = getKey(getRootPath() + "/WEB-INF/classes/" + PUBLIC_KEY_PATH);

    private static String getKey(String filename)  {
        // Read key from file
        try {
            String strKeyPEM = "";
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                strKeyPEM += line + "\n";
            }
            br.close();
            return strKeyPEM;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getRootPath() {
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
