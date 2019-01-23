package com.doooly.common.constants;

import java.util.ResourceBundle;

/**
 * @Description: payment 项目参数
 * @author: qing.zhang
 * @date: 2018-08-09
 */
public class PaymentConstants {
	private static ResourceBundle payBundle = ResourceBundle.getBundle("prop/pointpay");

    // 支付授权token，唯一标识，放入缓存
    public static final String PAYMENT_ACCESS_TOKEN_KEY = payBundle.getString("payment.access.token.key");

	public static final String PAYMENT_HTTPS = payBundle.getString("payment.https");// payment路径

	public static final String PAYMENT_HTTPS_V2 = payBundle.getString("payment.https.V2");// payment路径

	public static final String PAYMENT_NOTIFY_URL = payBundle.getString("payment.notify.url");// 兜礼支付完成通知接口路径

	public static final String PAYMENT_NOTIFY_URL_V2 = payBundle.getString("payment.notify.url.v2");// 兜礼支付完成通知接口路径

	public static final String PAYMENT_REFUND_NOTIFY_URL = payBundle.getString("payment.refund.notify.url");// 兜礼退款完成通知接口路径

	public static final String AUTHORIZE_URL = PAYMENT_HTTPS+"/auth/authorize";// 授权接口地址

	public static final String UNIFIED_ORDER_URL = PAYMENT_HTTPS+"/mchpay/unifiedorder";// 支付下单接口

	public static final String UNIFIED_ORDER_URL_V2 = PAYMENT_HTTPS_V2+"/mchpay/unifiedorder";// 支付下单接口v2

	public static final String GET_PAYFROM_URL = PAYMENT_HTTPS+"/pay/getPayForm";// 确认支付接口

	public static final String GET_PAYFROM_URL_V2 = PAYMENT_HTTPS_V2+"/pay/getPayForm";// 确认支付接口v2

	public static final String INTEGRAL_PAY_URL = PAYMENT_HTTPS+"/pay/integralPay";// 积分现金支付

	public static final String INTEGRAL_PAY_URL_V2 =PAYMENT_HTTPS_V2+"/pay/integralPay";// 积分现金支付

	public static final String ORDER_QUERY_URL = PAYMENT_HTTPS+"/mchpay/orderquery";// 支付查询接口

	public static final String ORDER_REFUND_URL = PAYMENT_HTTPS+"/mchpay/applyRefund";// 兜礼支付退款接口

	public static final String ORDER_APPLY_REFUND_URL = PAYMENT_HTTPS+"/mchpay/refund";// 兜礼支付申请退款接口

}
