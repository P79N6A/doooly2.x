package com.doooly.common.meituan;

import com.business.common.constant.ConnectorConstants;

import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/12/14.
 */
public class MeituanConstants {


    private static ResourceBundle bundle = ConnectorConstants.bundle;

    private static String MERCHANT_INTERFACE_ROOT_URL = bundle.getString("merchant.interface.root.url");

    public static final int ENT_ID = 27898;

    public static final String TOKEN = "3Q2NCPLGM2-TK";

    public static final String MSIGN = "m2yMTbYMScVBJk7W54xUkw==";

    public static final String KEY = "cs1CKRonxDhBZKoi1W0x4g==";

    public static final String APP_KEY = "3Q2NCPEYQW";

    public static final String VERSION = "sqtV1";

    public static final String URL_ADD_STAFF = "https://api-sqt.meituan.com/openapi/staffInfo/addStaff";

    public static final String URL_UPDATE_STAFF = "https://api-sqt.meituan.com/openapi/staffInfo/updateStaff";

    public static final String URL_QUERY_STAFF = "https://api-sqt.meituan.com/openapi/staffInfo/staffGetSerialIdentity";

    public static final String URL_DEL_STAFF = "https://api-sqt.meituan.com/openapi/staffInfo/deleteStaff";

    public static final String URL_QUERY_ORDER = "https://api-sqt.meituan.com/openapi/channelOrder/channelOrderQueryByOrderSn";

    public static final String URL_QUERY_ORDER_BY_TIME_RANGE = "https://api-sqt.meituan.com/openapi/channelOrder/channelOrderQueryByTimeRange";

    public static final String URL_QUERY_ORDER_BY_CHANNEL_ORDER_ID = "https://api-sqt.meituan.com/openapi/channelOrder/channelOrderQueryByChannelOrderId";

    public static final String BUSINESS_ID_MEITUAN = "TEST_bb841e7227asehtmeituan";

    public static final int SYN_ORDER_PAGESIZE = 100;

    public static final String ORDER_SYN_URL = MERCHANT_INTERFACE_ROOT_URL + "checkTransactionCompletion";

    public static final String DataUserSyncUrl = MERCHANT_INTERFACE_ROOT_URL + "memberDataSynchronizationByPage";



    public final static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI7GfP9Zfqai1UFcEP/9t0CZj4wO47aU7kkk" +
            "SE+uL69CDb83m+kX1/Nn0W3AFuaLrXx/Ny6ZbO1Gu9ziP+9IwMR/pPkwhh6FpFY2domp0h84Wpp7XZxB6xcYAUkiE2XygkEuuDqyqHBhxSUyfXPznmDDC3" +
            "l+FvQgt5TxPTh7O3lnAgMBAAECgYA6NWQ6uuLuzw5Aomdv5qGynaivglaGVru7aCZvDeX0/uoZ3nMbGhR58QaqRxlPDv1A96Cox/Zn2mG3ESrdxHyKQL" +
            "90g+r4gNWZONc/KPLsG14nvjAlxhQ558p5N9x5IRpmq8r2r9XJoGJDk5rqO3dSyEUGuCHP9Aqqu2TD9Za8SQJBAOznLQ7nmEKR6R5xqe9uuQR1KoI12gb" +
            "oU8K4X3j3S2WD2CBJPEf0luuRSsBPokdtZdKbEr7c6IZxlDSHvBhZVT0CQQCaSNs6S7XL0swAPlxx1D777hGpiV9P42J+7j7roo6Gj0oxiHJLMeyLu4hz" +
            "jdCJ0+zeS0sh6HEC7ER+qnlE29tzAkB6SeNCfF5mjrdNldLo27j6ChlFWdMQGcGTFGWEJfNvlZ1tHSDW6/Uz6K4zk2frgxc6nf4RNCt7qwmcDC0WTJbpAk" +
            "EAiwllJx/bcRdCaGXKgXo4WGiu2g3GKwRLWv/xDACuWG0A+6pu9XzEIxiZWylN6Sdmqt1HlAMY9P1erJeMOZW4KQJBAJsipPM/Y/YUhRTnl4sZazamtEriOtu" +
            "1B6JzWKJ1frYU3ZSBkeYMekadalz/ANVz1FYHhfTcQ7Ti7gIGlcBr48w=";

    public final static String appKey = "3Q2NCPEYQW";

    public final static String app_id = "3LNVWQWLTB";

    public final static int entId = 27898;

    public final static String appSecret = "IJwEr21n7T4PwTOISdhSL25D88f56cOW";

    public final static String exhaust_key = "3RO5KBIQ81";

    public final static String meituan_access_url = "https://m-sqt.meituan.com/open/commonaccess/access";

    public final static String token = "3QTJRJD37N-TK";

    public final static String version = "1.0";

    public final static String sign = "Sef0aEZcvqbJq0CQ/FABzQ==";

    public final static String url_meituan_pay_notify = "https://api-sqt.meituan.com/openapi/payNotify/third";

    public final static String url_meituan_pay_notify_doooly = "http://test2.doooly.com/Doooly/jersey/meituan/payNotify";

    public final static String meituan_bussinesss_id = "9486";

    public final static String meituan_bussinesss_serial = "TEST_bb841e7227asehtmeituan";

}
