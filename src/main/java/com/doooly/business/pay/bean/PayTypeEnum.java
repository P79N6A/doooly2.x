package com.doooly.business.pay.bean;

/**
 * @Description: 支付方式枚举
 * @author: qing.zhang
 * @date: 2018-08-30
 */
public enum PayTypeEnum {
    DOOOLY(0, "dooolyPaymentService",0),//兜礼积分
    WEIXIN(1, "weixinPaymentService",3),//微信
    WEIXIN_DOOOLY(2, "dooolyWeixinPaymentService",3),//兜礼积分微信混合
    ALIPAY(6, "alipayPaymentService",6),//支付宝
    ALIPAY_DOOOLY(11, "alipayDooolyPaymentService",6);//兜礼积分支付宝混合

    private int code;//payment支付方式
    private String name;//对应支付处理类名称
    private int dooolyCode;//同步doooly支付方式使用

    PayTypeEnum(int code, String name, int dooolyCode) {
        this.code = code;
        this.name = name;
        this.dooolyCode = dooolyCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDooolyCode() {
        return dooolyCode;
    }

    public void setDooolyCode(int dooolyCode) {
        this.dooolyCode = dooolyCode;
    }

    public static String getNameByCode(int code){
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            if(payTypeEnum.getCode()==code){
                return payTypeEnum.name;
            }
        }
        return null;
    }

    public static int getDooolyCodeByCode(int code){
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            if(payTypeEnum.getCode()==code){
                return payTypeEnum.dooolyCode;
            }
        }
        return 0;
    }
}
