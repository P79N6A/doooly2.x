package com.doooly.common.elm;


/**
 * @Description: 饿了么支付状态
 * @author: paul
 * @date: 2019-01-21
 */
public enum PayStatusEnum {

    PayTypeNotPay("NOTPAY", "未支付"),
    PayTypeSuccess("SUCCESS", "支付成功"),
    PayTypeRefund("REFUND", "转入退款"),
    PayTypeClosed("CLOSED", "已关闭");

    protected String code;

    protected String name;

    PayStatusEnum(String code, String name ) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getPayTypeByCode(String code) {
        for (PayStatusEnum payTypeEnum : PayStatusEnum.values()) {
            if (payTypeEnum.getCode().equals(code)) {
                return payTypeEnum.getName();
            }
        }
        return null;
    }
}

