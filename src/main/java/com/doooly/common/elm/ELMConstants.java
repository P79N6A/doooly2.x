package com.doooly.common.elm;

import java.util.ResourceBundle;

/**
 * @Description: 饿了吗常量类
 * @author: qing.zhang
 * @date: 2019-01-03
 */
public class ELMConstants {

    private static ResourceBundle elmBundle = ResourceBundle.getBundle("prop/elm");

    //配置表饿了吗type和key
    public final static String ELM_DICT_TYPE = "MERCHANT_PARAM";
    public final static String ELM_DICT_KEY = "ELM";
    public static final int REQUEST_EXPIRY_TIME = 600;//饿了么时间戳超时时间单位秒
    public static final String CHARSET = "UTF-8";//饿了么时间戳超时时间单位秒
    public static final String ELM_BUSINESS_ID = elmBundle.getString("elm_business_id");

    public static final String ELM_URL = elmBundle.getString("elm_url");
    public static final String QUERY_ORDER = "/v1/openapi/order/get";
    public static final String ELM_ADD_METHOD = "/v1/openapi/employee/add";

    public static final String ELM_ORDER_PREFIX = "ELM_ORDER_PREFIX:%s";

    //饿了么MsgCode add by paul
    public final static int ELM_RESULT_SUCCESS = 1;  //成功处理
    public final static int ELM_RESULT_FAIL = 2;     //处理失败
    public static final String ELM_CHECK_PARAM_SIGN_ERROR = "参数验签失败";
    public static final String ELE_CREATE_ORDER_FAIL = "落单失败,请稍后在试";
    public static final String ELE_MERCHANT_NO_ERROR = "无效的商户号";
    public static final String ELE_MERCHANT_MOB_ERROR = "会员手机号或会员卡号为空";
    public static final String ELE_MERCHANT_AUTHORIZE_FAIL = "接口授权认证失败";
    public static final String ELE_QUERY_PAY_FAIL = "支付查询失败，请稍后在试";
    public static final String ELE_REFUND_AMOUNT_ERROR = "金额参数不是数字或退款金额为0";
    public static final String ELE_REFUND_OPERATION_FAIL = "退款操作失败，请稍后在试";
    public static final String ELE_REFUND_ORDER_ERROR = "无效的订单号";
    public static final String ELE_REFUND_RECORD_ERROR = "退款记录不存在";

    //DEV
    public final static String ELM_APP_ID = elmBundle.getString("elm_app_id");
    public final static String ELM_MERCHANT_NO = elmBundle.getString("elm_merchant_no");
    //兜礼收款账户
    public final static String DOOOLY_FINANCIAL_ACCOUNT = elmBundle.getString("doooly_financial_account");

    //生产兜礼收银台页面
    public final static String DOOOLY_FINANCIAL_PAY_URL = elmBundle.getString("doooly_financial_pay_url");

    //兜礼私钥
    public static final String ELM_PRIVATE_KEY = elmBundle.getString("elm_private_key");;
    public static final String ELM_PUBLIC_KEY = elmBundle.getString("elm_public_key");;

    //饿了么给的公钥
    public static final String ELM_GIAVE_PUBLIC_KEY = elmBundle.getString("elm_giave_public_key");;

}
