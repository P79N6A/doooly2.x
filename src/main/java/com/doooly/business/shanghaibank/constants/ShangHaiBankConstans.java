package com.doooly.business.shanghaibank.constants;

import java.util.ResourceBundle;

/**
 * @Description: 上海银行常量类
 * @author: qing.zhang
 * @date: 2018-05-29
 */
public class ShangHaiBankConstans {

    private static ResourceBundle shangHaiBankBundle = ResourceBundle.getBundle("prop/shanghaibank");

    public static final String PRIBKEY = shangHaiBankBundle.getString("pribKey");// 私钥

    public static final String PUBKEY = shangHaiBankBundle.getString("pubKey");// 公钥

    public static final String PFXPATH = shangHaiBankBundle.getString("pfxPath");// 签名文件位置

    public static final String PFXPWD = shangHaiBankBundle.getString("pfxPwd");// 密码

    public static final String KEY = shangHaiBankBundle.getString("key");// key

    public static final String CUSTOMER = shangHaiBankBundle.getString("customer");// customer

    public static final Integer PORT = Integer.valueOf(shangHaiBankBundle.getString("port"));// 端口

    public static final String HOST = shangHaiBankBundle.getString("host");// ip

    public static final String SIGN_KEY = shangHaiBankBundle.getString("sign_key");// ip

    public static final String ENCODING = "UTF-8";

    public static final String VIRACCTNOTICE = "virAcctNotice";//接口service名称
}
