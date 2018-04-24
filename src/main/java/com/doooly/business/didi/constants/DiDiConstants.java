package com.doooly.business.didi.constants;

import java.util.ResourceBundle;

/**
 * @Description: 滴滴配置相关常量
 * @author: qing.zhang
 * @date: 2018-03-06
 */
public class DiDiConstants {

    private static ResourceBundle didiBundle = ResourceBundle.getBundle("didi");
    public static final String CHARSET_UTF_8 = "UTF-8";

    //申请应用时分配的AppKey
    public static final String DIDI_CLIENTID = didiBundle.getString("didi.clientId");
    //申请应用时分配的AppSecret
    public static final String DIDI_CLIENTSECRET = didiBundle.getString("didi.clientSecret");
    //请求的类型，填写client_credentials
    public static final String DIDI_GRANTTYPE = didiBundle.getString("didi.grantType");
    //sign_key不要参与参数传递,只参与生成sign
    public static final String DIDI_SIGNKEY = didiBundle.getString("didi.signKey");
    //企业ID
    public static final String DIDI_COMPANYID = didiBundle.getString("didi.companyId");
    //管理员手机号
    public static final String DIDI_ADMIN_PHONE = didiBundle.getString("didi.admin.phone");
    //数据开始位置
    public static final String DIDI_QUERY_OFFSET = didiBundle.getString("didi.query.offset");
    //数据条数（最大100条）
    public static final String DIDI_QUERY_LENGTH = didiBundle.getString("didi.query.length");
    //accessToken 缓存key
    public static final String DIDI_ACCESS_TOKEN = didiBundle.getString("didi.access.token");
    //滴滴商户id
    public static final String DIDI_BUSINESS_ID = didiBundle.getString("didi.business.id");
    //滴滴商户id
    public static final String DIDI_ORDER_QUERY_SWITCH = didiBundle.getString("didi.order.query.switch");

    // 获取授权access_token的URL
    public static final String AUTHORIZE_URL = didiBundle.getString("authorize.url");
    // 查询用户
    public static final String MEMBER_GET_URL = didiBundle.getString("member.get.url");
    // 新增用户
    public static final String MEMBER_ADD_URL = didiBundle.getString("member.add.url");
    // 修改用户
    public static final String MEMBER_EDIT_URL = didiBundle.getString("member.edit.url");
    // 删除用户
    public static final String MEMBER_DELETE_URL = didiBundle.getString("member.delete.url");
    // 订单查询
    public static final String ORDER_GET_URL = didiBundle.getString("order.get.url");
    // 订单详情查询
    public static final String ORDER_DETAIL_URL = didiBundle.getString("order.detail.url");
    // 企业信息查询
    public static final String COMPANY_GET_URL = didiBundle.getString("company.get.url");
    // 用车制度查询
    public static final String REGULATION_URL = didiBundle.getString("regulation.url");
    // 成本中心新增
    public static final String BUDGETCENTER_ADD_URL = didiBundle.getString("budgetcenter.add.url");
    // 成本中心查询
    public static final String BUDGETCENTER_GET_URL = didiBundle.getString("budgetcenter.get.url");
    // 成本中心修改
    public static final String BUDGETCENTER_EDIT_URL = didiBundle.getString("budgetcenter.edit.url");
}
