package com.doooly.business.payment.constants;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-08-20
 */
public class PayConstants {
    /**支付状态，未支付*/
    public static final String PAY_STATUS_0 = "0";
    /**支付状态，支付成功*/
    public static final String PAY_STATUS_1 = "1";
    /**支付状态，支付失败*/
    public static final String PAY_STATUS_2 = "2";
    /**退款状态，未退款*/
    public final static String REFUND_STATUS_I = "i";
    /**退款状态，退款成功*/
    public final static String REFUND_STATUS_S = "s";
    /**退款状态，退款失败*/
    public final static String REFUND_STATUS_F = "f";
    /**支付方式，积分*/
    public static final int pay_type_0 = 0;
    /**支付状态，非积分*/
    public static final int pay_type_1 = 1;
    /**支付状态，现金*/
    public static final int pay_type_2 = 2;
    /**支付状态，微信*/
    public static final int pay_type_3 = 3;

}
