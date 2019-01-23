package com.doooly.common.elm;

/**
 * @Description: 饿了吗常量类
 * @author: qing.zhang
 * @date: 2019-01-03
 */
public class ELMConstants {
    //配置表饿了吗type和key
    public final static String ELM_DICT_TYPE = "MERCHANT_PARAM";
    public final static String ELM_DICT_KEY = "ELM";
    public static final int REQUEST_EXPIRY_TIME = 600;//饿了么时间戳超时时间单位秒
    public static final String CHARSET = "UTF-8";//饿了么时间戳超时时间单位秒
    public static final String ELM_BUSINESS_ID = "Test_wwnsdfsteierlema";

    public static final String ELM_URL = "https://enterprise-open-api.alta.elenet.me";
    public static final String QUERY_ORDER = "/v1/openapi/order/get";
    public static final String ELM_ADD_METHOD = "/v1/openapi/employee/add";

    public static final String ELM_ORDER_PREFIX = "ELM_ORDER_PREFIX:%s";

    //饿了么MsgCode add by paul
    public final static String ELM_RESULT_SUCCESS = "1";  //成功处理
    public final static String ELM_RESULT_FAIL = "2";     //处理失败
    public static final String CHECK_PARAM_SIGN_ERROR = "参数验签失败";
    //DEV
    public final static String ELM_APP_ID = "b393552e850e4f8bbf1cfe209e09042c";
    public final static String ELM_MERCHANT_NO = "Doooly_Elm";
    //兜里收款账户
    public final static String DOOOLY_FINANCIAL_ACCOUNT = "9624121301145727303249";

}
