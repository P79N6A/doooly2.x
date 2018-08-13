package com.doooly.business.nexus;

import com.doooly.business.exwings.ExWingsUtils;
import com.doooly.common.constants.PropertiesHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    //
    private static  final  Map<String,String> retMsg = new HashMap<String,String>();
    private static  final  Map<String,String> retStatus = new HashMap<String,String>();

    static {
        retMsg.put("00","成功");
        retMsg.put("28","会员数据不存在");
        retMsg.put("29","卡号与会员 ID 不匹配");
        retMsg.put("30","该会员已经存在");
        retMsg.put("31","卡片已挂失");
        retMsg.put("32","格式不对");
        retMsg.put("33","卡号不存在");
        retMsg.put("34","动态码已过期");
        retMsg.put("35","无效的动态码");
        retMsg.put("98","商品信息异常");
        retMsg.put("99","系统异常");
        retMsg.put("B8","参数不完整");
        retMsg.put("A1","调用 API 异常");
        retMsg.put("A2","验签失败错误码 描述");
        retMsg.put("A3","加签失败");
        retMsg.put("A4","执行器不存在");
        retMsg.put("A5","APPID 不存在");
        retMsg.put("A6","解密失败");
        retMsg.put("A7","接口未授权");
        retMsg.put("A8","验证签名失败");
        retMsg.put("A9","数据库操作失败");
        retMsg.put("B9","调交易平台失败");
        retMsg.put("CD","余额不足");

        //ret status
        retStatus.put("95","订单号重复");
        retStatus.put("96","内部异常");
        retStatus.put("97","报文格式或签名校验失败");
        retStatus.put("98","找不到原交易");
        retStatus.put("99","交易参数异常");
    }




    //返回码信息
    public static String getRetMsg(String msgCode) {
        if(!StringUtils.isEmpty(msgCode)){
            return retMsg.get(msgCode);
        }
        return msgCode;
    }


    //处理码信息
    public static String getRetStatus(String status) {
        if(!StringUtils.isEmpty(status)){
            return retStatus.get(status);
        }
        return status;
    }



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
