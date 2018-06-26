package com.doooly.business.shanghaibank.utils;

/**
 * 数据签名工具类
 */
public class SignUtil {
    /**
     * 返回签名内容和base64编码公钥证书
     *
     * @param data 原始数据
     * @param pfxPath 证书路径，绝对路径，/tmp/xxx.pfx
     * @param pfxPwd 证书密钥密码
     * @return signInfo数据[0]加签之后的数据[1]公钥base64编码
     */
    public static String[] signData(String data, String pfxPath, String pfxPwd) {
        String[] signInfo = new String[2];
        com.koalii.crypto.SignUtil signUtil = new com.koalii.crypto.SignUtil();
        try {
            signUtil.initSignCertAndKey(pfxPath, pfxPwd);
            //签名结果Base64编码
            String signData = signUtil.signData(data.getBytes());
//        	String signData ="";
            signData = signData.replaceAll("\r", "");
            signData = signData.replaceAll("\n", "");
            //获得签名证书的Base64编码
            String signCert = signUtil.getEncodedSignCert();
//            String signCert = "";
            signCert = signCert.replaceAll("\r", "");
            signCert = signCert.replaceAll("\n", "");
            signInfo[0] = signData;
            signInfo[1] = signCert;
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败",e);
        }
        return signInfo;
    }
}