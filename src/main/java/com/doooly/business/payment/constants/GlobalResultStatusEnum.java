package com.doooly.business.payment.constants;

/**
 * 定义枚举返回状态常量类
 * 自定义返回结果 10000开始
 * Created by liangjun on 2017/3/17.
 */
public enum GlobalResultStatusEnum {
    SUCCESS_OK(200, "成功"),
    SUCCESS(1000, "成功"),
    FAIL(1001, "失败"),
    BAD_REQUEST(400, "Bad Request!"),
    UNAUTHORIZED(401, "token无效，请重新登录"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    MEDIA_TYPE_NOT_SUPPORTED(415, "Unsupported Media Type"),
    /*系统异常提示*/
    INTERNAL_SERVICE_ERROR(500, "Internal Server Error"),
    INTERNAL_SERVICE_ERROR_1(500, "[服务器]运行时异常"),
    INTERNAL_SERVICE_ERROR_2(500, "[服务器]空值异常"),
    INTERNAL_SERVICE_ERROR_3(500, "[服务器]类型转换异常"),
    INTERNAL_SERVICE_ERROR_4(500, "[服务器]IO异常"),
    INTERNAL_SERVICE_ERROR_5(500, "[服务器]未知方法异常"),
    INTERNAL_SERVICE_ERROR_6(500, "[服务器]数组越界异常"),
    /*自定义提示,可重新setMsg消息内容*/
    RESULT_Ok(900, "OK"),
    RESULT_INFO(901, "提示！"),
    RESULT_WARN(902, "警告！"),
    RESULT_FAIL(903, "失败！"),
    RESULT_ERROR(904, "错误！"),
    RESULT_EXCEPTION(905, "异常！"),
    /*更新数据提示*/
    SAVE_DATA_FAIL(906, "数据插入失败！"),
    UPDATE_DATA_FAIL(907, "数据更新失败！"),
    DELETE_DATA_FAIL(908, "数据删除失败！"),
    SELECT_DATA_FAIL(909, "数据查询失败！"),
    SELECT_DATA_INFO(910, "还没有记录哦！"),
    /*版本*/
    API_VERSION_WARN(999, "此版本接口已停用，请更新最新版本"),
    SIGN_VALID_ERROR(1001, "系统错误"),
    PARAM_VALID_ERROR(1002, "参数错误"),
    /*支付结果*/
    PAY_STATUS_SUCCESS(1000, "支付成功"),
    PAY_STATUS_FAIL(1001, "支付失败"),
    PAY_STATUS_NON(1002, "未支付"),
    /*登录验证结果*/
    ONE_NUMBER_LOGIN_SUCCESS(1000, "免登陆成功"),
    ONE_NUMBER_LOGIN_FAIL(1001, "免登陆失败"),
    ONE_NUMBER_SIGN_ERROR(1002, "签名验证失败"),
    ONE_NUMBER_PARAM_EMPTY(1003, "参数为空"),
    ONE_NUMBER_USER_EMPTY(1004, "会员不存在"),
    ONE_NUMBER_LOGIN_TIMEOUT(1005, "登录超时"),
    /*退款结果*/
    REFUND_STATUS_SUCCESS(1000, "退款成功"),
    REFUND_STATUS_FAIL(1001, "退款失败"),
    REFUND_STATUS_NON(1002, "未退款"),
    ;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String info;

    GlobalResultStatusEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
