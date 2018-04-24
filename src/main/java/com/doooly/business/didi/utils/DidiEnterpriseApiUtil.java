package com.doooly.business.didi.utils;

import com.alibaba.fastjson.JSONObject;
import com.didi.DiDiConnector;
import com.doooly.business.didi.constants.DiDiConstants;
import com.doooly.common.util.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.doooly.business.didi.constants.DiDiConstants.AUTHORIZE_URL;
import static com.doooly.business.didi.constants.DiDiConstants.MEMBER_GET_URL;

/**
 * @className: DidiEnterpriseApiUtil
 * @description:
 * @author: wangchenyu
 * @date: 2018-02-01 11:30
 */
@Component
public class DidiEnterpriseApiUtil {

    private static final Logger logger = LoggerFactory.getLogger(DidiEnterpriseApiUtil.class);

    @Autowired
    private StringRedisTemplate redis;

    /**
     * 获取滴滴access_token
     *
     * @return
     * @throws Exception
     */
    public String getDidiAccessToken() {
        String didiAccessToken = redis.opsForValue().get(DiDiConstants.DIDI_ACCESS_TOKEN);
        if (StringUtils.isBlank(didiAccessToken) || didiAccessToken.equals("null")) {
            //调用滴滴授权接口，获取access_token，这里需要加锁
            didiAccessToken = initDidiAccessToken();
        }
        return didiAccessToken;
    }

    public synchronized String initDidiAccessToken() {
        try {
            // 调用滴滴授权接口
            String didiAccessToken = getAccessToken();
            if (StringUtils.isBlank(didiAccessToken) || didiAccessToken.equals("null") ) {
                // 双重保险，这里再请求一次，获取滴滴授权的access_token
                didiAccessToken = DiDiConnector.getDidiAccessToken();
            }
            if(didiAccessToken!=null){
                redis.opsForValue().set(DiDiConstants.DIDI_ACCESS_TOKEN, didiAccessToken, 25, TimeUnit.MINUTES);
            }
            return didiAccessToken;
        } catch (Exception e) {
            logger.error("获取滴滴授权access_token出错", e);
            return null;
        }
    }


    /**
     * 把Map中所有元素排序，并以&形式连接返回(类似格式为a=xxx&b=xxx&c=xxx...)
     */
    private String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String result = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                result = result + key + "=" + value;
            } else {
                result = result + key + "=" + value + "&";
            }
        }
        return result;
    }

    private String createURLEncoderLinkString(Map<String, String> params, String charset) throws UnsupportedEncodingException {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String result = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = URLEncoder.encode(params.get(key), charset);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                result = result + key + "=" + value;
            } else {
                result = result + key + "=" + value + "&";
            }
        }
        return result;
    }

    /**
     * 获取滴滴授权access_token
     * POST请求
     */
    public String getAccessToken() {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", DiDiConstants.DIDI_CLIENTID);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("client_secret", DiDiConstants.DIDI_CLIENTSECRET);
            params.put("grant_type", DiDiConstants.DIDI_GRANTTYPE);
            params.put("phone", DiDiConstants.DIDI_ADMIN_PHONE);
            HashMap<String, String> signMap = (HashMap<String, String>) params.clone();
            signMap.put("sign_key", DiDiConstants.DIDI_SIGNKEY);
            String sign = DigestUtils.md5Hex(createLinkString(signMap).getBytes(DiDiConstants.CHARSET_UTF_8));
            params.put("sign", sign);

            JSONObject requestParamsJSON = JSONObject.parseObject(JSONObject.toJSONString(params));
            String responseJSON = HttpClientUtil.sendPost(requestParamsJSON, AUTHORIZE_URL);
            JSONObject jsonObject = JSONObject.parseObject(responseJSON);
            return String.valueOf(jsonObject.get("access_token"));
        } catch (UnsupportedEncodingException e) {
            logger.error("获取滴滴授权access_token出错", e);
            return null;
        }
    }

    /**
     * 新增用户
     * POST请求
     */
    public String didiMemberAdd(String data) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", DiDiConstants.DIDI_CLIENTID);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("access_token", getDidiAccessToken());
            params.put("company_id", DiDiConstants.DIDI_COMPANYID);
            params.put("data", data);
            HashMap<String, String> signMap = (HashMap<String, String>) params.clone();
            signMap.put("sign_key", DiDiConstants.DIDI_SIGNKEY);
            String sign = DigestUtils.md5Hex(createLinkString(signMap).getBytes(DiDiConstants.CHARSET_UTF_8));
            params.put("sign", sign);

            JSONObject requestParamsJSON = JSONObject.parseObject(JSONObject.toJSONString(params));
            String responseJSON = HttpClientUtil.sendPost(requestParamsJSON, DiDiConstants.MEMBER_ADD_URL);
            return responseJSON;
        } catch (UnsupportedEncodingException e) {
            logger.error("新增用户出错", e);
            return null;
        }
    }

    /**
     * 查询用户
     * GET请求
     */
    public String didiMemberGet(String phone) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", DiDiConstants.DIDI_CLIENTID);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("access_token", getDidiAccessToken());
            params.put("company_id", DiDiConstants.DIDI_COMPANYID);
            params.put("phone", phone);
            params.put("offset", DiDiConstants.DIDI_QUERY_OFFSET);
            params.put("length", DiDiConstants.DIDI_QUERY_LENGTH);

            HashMap<String, String> signMap = (HashMap<String, String>) params.clone();
            signMap.put("sign_key", DiDiConstants.DIDI_SIGNKEY);
            String sign = DigestUtils.md5Hex(createLinkString(signMap).getBytes(DiDiConstants.CHARSET_UTF_8));
            params.put("sign", sign);

            String requestURL = MEMBER_GET_URL + "?" + createURLEncoderLinkString(params, DiDiConstants.CHARSET_UTF_8);
            String responseJSON = HttpClientUtil.httpsGet(requestURL);
            return responseJSON;
        } catch (UnsupportedEncodingException e) {
            logger.error("查询用户出错", e);
            return null;
        }
    }

    /**
     * 查询用车制度
     * GET请求
     */
    public String didiRegulationGet() {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", DiDiConstants.DIDI_CLIENTID);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("access_token", getDidiAccessToken());
            params.put("company_id", DiDiConstants.DIDI_COMPANYID);
            HashMap<String, String> signMap = (HashMap<String, String>) params.clone();
            signMap.put("sign_key", DiDiConstants.DIDI_SIGNKEY);
            String sign = DigestUtils.md5Hex(createLinkString(signMap).getBytes(DiDiConstants.CHARSET_UTF_8));
            params.put("sign", sign);

            JSONObject requestParamsJSON = JSONObject.parseObject(JSONObject.toJSONString(params));
            String requestURL = DiDiConstants.REGULATION_URL + "?" + createURLEncoderLinkString(params, DiDiConstants.CHARSET_UTF_8);
            String responseJSON = HttpClientUtil.httpsGet(requestURL);
            return responseJSON;
        } catch (UnsupportedEncodingException e) {
            logger.error("查询用车制度出错", e);
            return null;
        }
    }

    /**
     * 修改用户
     * POST请求
     */
    public String didiMemberEdit(String data, Long businessMemberId) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", DiDiConstants.DIDI_CLIENTID);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("access_token", getDidiAccessToken());
            params.put("company_id", DiDiConstants.DIDI_COMPANYID);
            params.put("member_id", String.valueOf(businessMemberId));
            params.put("data", data);

            HashMap<String, String> signMap = (HashMap<String, String>) params.clone();
            signMap.put("sign_key", DiDiConstants.DIDI_SIGNKEY);
            String sign = DigestUtils.md5Hex(createLinkString(signMap).getBytes(DiDiConstants.CHARSET_UTF_8));
            params.put("sign", sign);

            JSONObject requestParamsJSON = JSONObject.parseObject(JSONObject.toJSONString(params));
            String responseJSON = HttpClientUtil.sendPost(requestParamsJSON, DiDiConstants.MEMBER_EDIT_URL);
            return responseJSON;
        } catch (UnsupportedEncodingException e) {
            logger.error("修改用户出错", e);
            return null;
        }
    }

    /**
     * 新增成本中心
     *
     * @param name   名称
     * @param amount 总金额
     * @return
     */
    public String didiBudgetCenterAdd(String name, BigDecimal amount) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", DiDiConstants.DIDI_CLIENTID);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("access_token", getDidiAccessToken());
            params.put("company_id", DiDiConstants.DIDI_COMPANYID);
            params.put("name", name);
            params.put("type", "1");
            params.put("budget_cycle", "0");
            params.put("total_quota", String.valueOf(amount));

            HashMap<String, String> signMap = (HashMap<String, String>) params.clone();
            signMap.put("sign_key", DiDiConstants.DIDI_SIGNKEY);
            String sign = DigestUtils.md5Hex(createLinkString(signMap).getBytes(DiDiConstants.CHARSET_UTF_8));
            params.put("sign", sign);

            JSONObject requestParamsJSON = JSONObject.parseObject(JSONObject.toJSONString(params));
            String responseJSON = HttpClientUtil.sendPost(requestParamsJSON, DiDiConstants.BUDGETCENTER_ADD_URL);
            return responseJSON;
        } catch (UnsupportedEncodingException e) {
            logger.error("新增成本中心出错", e);
            return null;
        }
    }

    /**
     * 成本中心查询
     *
     * @param id
     * @param name 名称
     * @return
     */
    public String didiBudgetCenterGet(String id, String name) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", DiDiConstants.DIDI_CLIENTID);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("access_token", getDidiAccessToken());
            params.put("company_id", DiDiConstants.DIDI_COMPANYID);
            params.put("name", name);
            params.put("id", id);
            params.put("offset", DiDiConstants.DIDI_QUERY_OFFSET);
            params.put("length", DiDiConstants.DIDI_QUERY_LENGTH);

            HashMap<String, String> signMap = (HashMap<String, String>) params.clone();
            signMap.put("sign_key", DiDiConstants.DIDI_SIGNKEY);
            String sign = DigestUtils.md5Hex(createLinkString(signMap).getBytes(DiDiConstants.CHARSET_UTF_8));
            params.put("sign", sign);

            String requestURL = DiDiConstants.BUDGETCENTER_GET_URL + "?" + createURLEncoderLinkString(params, DiDiConstants.CHARSET_UTF_8);
            String responseJSON = HttpClientUtil.httpsGet(requestURL);
            return responseJSON;
        } catch (UnsupportedEncodingException e) {
            logger.error("查询成本中心出错", e);
            return null;
        }
    }
}
