package com.doooly.common.elm;


/**
 * @Description: 饿了么订单状态
 * @author: qing.zhang
 * @date: 2019-01-03
 */
public enum OrderTypeEnum {

    OrderTypeEnum10(10, "订单已取消"),
    OrderTypeEnum15(15, "订单待支付"),
    OrderTypeEnum16(16, "订单支付失败"),
    OrderTypeEnum20(20, "订单等待餐厅确认"),
    OrderTypeEnum30(30, "餐厅已接单"),
    OrderTypeEnum35(35, "待配送"),
    OrderTypeEnum40(40, "待取餐"),
    OrderTypeEnum50(50, "配送员到店"),
    OrderTypeEnum60(60, "配送中"),
    OrderTypeEnum70(70, "确认送达"),
    OrderTypeEnum80(80, "配送异常"),
    OrderTypeEnum90(90, "完成订单"),
    OrderTypeEnum100(100, "申请退单"),
    OrderTypeEnum105(105, "退单失败"),
    OrderTypeEnum110(110, "退单成功");

    protected int code;

    protected String name;

    OrderTypeEnum(int code, String name ) {
        this.code = code;
        this.name = name;
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

    public static String getOrderTypeByCode(int code) {
        for (OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()) {
            if (orderTypeEnum.getCode() == code) {
                return orderTypeEnum.getName();
            }
        }
        return null;
    }

}
