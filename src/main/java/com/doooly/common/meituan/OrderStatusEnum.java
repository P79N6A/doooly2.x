package com.doooly.common.meituan;

/**
 * Created by wanghai on 2018/12/19.
 * 订单状态枚举
 */
public enum OrderStatusEnum {

    NEW(10,"新建"),
    PAYED(20, "已支付"),
    /**
     * 锁定状态，一般只存在几毫秒，如果停留在该状态，则是系统出现异常需要人工干预。
     */
    REFUNDEDING(30, "退款中"),
    PARTLY_REFUNDED(31, "已部分退款"),
    FULLY_REFUNDED(32, "已全额退款");

    private int code;

    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
