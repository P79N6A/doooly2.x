package com.doooly.common.elm;


/**
 * @Description: 饿了么退款状态
 * @author: paul
 * @date: 2019-01-21
 */
public enum RefundStatusEnum {

    RefundTypeSuccess("SUCCESS", "退款成功"),
    RefundTypeFail("FAIL", "退款失败"),
    RefundTypeProcessing("PROCESSING", "退款中");

    protected String code;

    protected String name;

    RefundStatusEnum(String code, String name ) {
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

    public static String getRefundTypeByCode(String code) {
        for (RefundStatusEnum payTypeEnum : RefundStatusEnum.values()) {
            if (payTypeEnum.getCode().equals(code)) {
                return payTypeEnum.getName();
            }
        }
        return null;
    }
}

