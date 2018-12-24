package com.doooly.business.oneNumber.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import com.doooly.business.utils.RSAEncryptUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.business.common.util.EncryptDecryptUtil;
import com.doooly.business.oneNumber.service.OneNumberServiceI;
import com.doooly.business.utils.MD5Util;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.reachad.AdBusinessExpandInfoDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdUser;

/**
 * @Description: 1号通接口
 * @author: qing.zhang
 * @date: 2017-08-28
 */
@Service
public class OneNumberService implements OneNumberServiceI {

    private static Logger logger = Logger.getLogger(OneNumberService.class);
    private static ResourceBundle appBundle = ResourceBundle.getBundle("prop/sctcd");

    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;
    @Autowired
    private AdUserDao adUserDao;

    @Override
    public MessageDataBean getTargetUrl(String userId, String businessId, String targetUrl)
            throws UnsupportedEncodingException {
        MessageDataBean messageDataBean = new MessageDataBean();
        Map<String, Object> map = new HashMap<>();
        AdUser adUser = adUserDao.getById(Integer.valueOf(userId));
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        long timestamp = new Date().getTime() / 1000;
        String resultUrl = null;
        switch (adBusinessExpandInfo.getType()) {
            // 兜礼提供对接方式
            case 0:
                resultUrl = getDooolyUrl(targetUrl, adUser, adBusinessExpandInfo, timestamp);
                break;
            // 内购网专属
            case 1:
                resultUrl = getNeiGouUrl(targetUrl, adUser, adBusinessExpandInfo, timestamp);
                break;
            // 酷邀贷专属
            case 2:
                map = getKuYaoDaiUrl(targetUrl, adUser, adBusinessExpandInfo);
                break;
            // 酷屏专属
            case 3:
                resultUrl = getKuPingUrl(targetUrl, adUser, adBusinessExpandInfo, timestamp);
                break;
            // 微医专属
            case 4:
                resultUrl = getWeiYiUrl(targetUrl, adUser, adBusinessExpandInfo);
                break;
            // 一嗨租车专属
            case 5:
                resultUrl = getYiHaiUrl(targetUrl, adUser, adBusinessExpandInfo);
                break;
            default:
                resultUrl = "";
                break;
        }
        logger.info("1号通生成的结果链接:" + resultUrl);
        if (map.isEmpty()) {
            map.put("resultUrl", resultUrl);
        }
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

    /**
     * 一嗨租车专属连接
     *
     * @param targetUrl
     * @param adUser
     * @param adBusinessExpandInfo
     * @return
     */
    private String getYiHaiUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo) {

        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("appId", adBusinessExpandInfo.getShopId());
        msgMap.put("appSecret", adBusinessExpandInfo.getShopKey());
        msgMap.put("openId", "/fastorder/hospital");
        msgMap.put("IdType", "0");

        JSONObject jsonResponse = HttpClientUtil.httpPost("https://m.1hai.cn/MediaPlatform/Hybrid/PreAuthCode", new JSONObject(msgMap));
        if ("0".equals(jsonResponse.getString("Errcode"))) {
            String preAuthCode = jsonResponse.getString("PreAuthCode");
            String url = adBusinessExpandInfo.getBusinessUrl();
            return String.format("%s?appId=%s&preAuthCode=%s", url, adBusinessExpandInfo.getShopId(), preAuthCode);
        } else {
            return null;
        }
    }

    /**
     * 微医专属连接
     *
     * @param targetUrl
     * @param adUser
     * @param adBusinessExpandInfo
     * @return
     */
    private String getWeiYiUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo) {

        Map<String, String> msgMap = new HashMap<String, String>();
        msgMap.put("mobile", adUser.getTelephone());
        msgMap.put("ext_user_id", adUser.getCardNumber());
        msgMap.put("target", "/fastorder/hospital");
        msgMap.put("name", adUser.getName());

        String json = JSONObject.toJSONString(msgMap);
        String url = adBusinessExpandInfo.getBusinessUrl();
        String appScert = adBusinessExpandInfo.getShopKey();

        String encryptUrl = EncryptDecryptUtil.encryptByDES(json, appScert);

        return String.format("%s?token=%s", url, encryptUrl);
    }

    /**
     * 酷屏专属
     *
     * @param targetUrl
     * @param adUser
     * @param adBusinessExpandInfo
     * @param timestamp
     * @return
     */
    private String getKuPingUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo,
                                long timestamp) {

        String url = adBusinessExpandInfo.getBusinessUrl();
        String appKey = adBusinessExpandInfo.getShopId();
        String customerUserId = adUser.getCardNumber();
        String customerUserPhone = adUser.getTelephone();
        String appScert = adBusinessExpandInfo.getShopKey();

        String str = appKey + customerUserId + customerUserPhone + timestamp + appScert;
        String sign = MD5Util.MD5Encode(str, "UTF-8");

        return String.format("%s?appKey=%s&customerUserId=%s&customerUserPhone=%s&timestamp=%s&sign=%s", url, appKey,
                customerUserId, customerUserPhone, timestamp, sign);
    }

    /**
     * 酷邀贷专属
     *
     * @param targetUrl            目标地址
     * @param adUser               用户信息
     * @param adBusinessExpandInfo 商户扩展信息
     * @return 跳转链接
     */
    private Map<String, Object> getKuYaoDaiUrl(String targetUrl, AdUser adUser,
                                               AdBusinessExpandInfo adBusinessExpandInfo) {
        Map<String, Object> res = new HashMap<>();
        String resultUrl;
        String mobile = null;
        String dev_pub_key = appBundle.getString("ONE_NUM_DEV_PUB_KEY");
        String prd_pub_key = appBundle.getString("ONE_NUM_PRD_PUB_KEY");
        logger.info("------------ dev_pub_key :"  + dev_pub_key);
        logger.info("------------ prd_pub_key :"  + prd_pub_key);
        String str = RSAEncryptUtil.encryptByRSAPubKey(Base64.decodeBase64(dev_pub_key), adUser.getTelephone());
        try {
            mobile = URLEncoder.encode(str, "UTF-8");// 进行编码传输
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        res.put("headerKey", appBundle.getString("ONE_NUM_HEADER_KEY"));
        res.put("headerValue", appBundle.getString("ONE_NUM_HEADER_VALUE"));
        res.put("telNo", mobile);
        res.put("channelCode", adBusinessExpandInfo.getShopId());
        res.put("type", adBusinessExpandInfo.getType()); //前端需要字段，判断是否是新接口，酷邀贷专属
        res.put("resultUrl", adBusinessExpandInfo.getBusinessUrl());
        return res;
    }

    /**
     * 内购网链接获取
     *
     * @param targetUrl            目标地址
     * @param adUser               用户信息
     * @param adBusinessExpandInfo 商户扩展信息
     * @param timestamp            时间戳
     * @return
     * @throws UnsupportedEncodingException 编码异常
     */
    private String getNeiGouUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo,
                                long timestamp) throws UnsupportedEncodingException {
        String resultUrl;// 加密字符串需要shopKey
        String str = "client_id=" + adBusinessExpandInfo.getShopId() + "&external_user_id=" + adUser.getCardNumber()
                + "&name=" + adUser.getName() + "&time=" + timestamp;
        if (StringUtils.isNotBlank(targetUrl) && !"null".equals(targetUrl)) {
            str += "&surl=" + targetUrl;
        }
        String[] params = StringUtils.split(str, "&");
        Arrays.sort(params);
        String param = StringUtils.join(params, "&") + adBusinessExpandInfo.getShopKey();
        String sign = MD5Util.MD5Encode(param, "UTF-8");
        if (adBusinessExpandInfo.getBusinessUrl().contains("?")) {
            resultUrl = adBusinessExpandInfo.getBusinessUrl() + "&" + str + "&sign=" + sign;
        } else {
            resultUrl = adBusinessExpandInfo.getBusinessUrl() + "?" + str + "&sign=" + sign;
        }
        return resultUrl;
    }

    /**
     * 跳转链接获取
     *
     * @param targetUrl            目标地址
     * @param adUser               用户信息
     * @param adBusinessExpandInfo 商户扩展信息
     * @param timestamp            时间戳
     * @return
     * @throws UnsupportedEncodingException 编码异常
     */
    private String getDooolyUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo,
                                long timestamp) throws UnsupportedEncodingException {
        String resultUrl;// 加密字符串需要shopKey
        String str = "shopId=" + adBusinessExpandInfo.getShopId() + "&shopKey=" + adBusinessExpandInfo.getShopKey()
                + "&mobile=" + adUser.getTelephone() + "&cardNumber=" + adUser.getCardNumber() + "&actionTime="
                + timestamp;
        String sign = MD5Util.MD5Encode(str, "UTF-8");
        String encode = "";
        if (StringUtils.isNotBlank(targetUrl)) {
            encode = URLEncoder.encode(targetUrl, "UTF-8");
        }
        targetUrl = "&targetUrl=" + encode;
        // 拼接访问地址不传shopKey
        String str1 = "shopId=" + adBusinessExpandInfo.getShopId() + "&mobile=" + adUser.getTelephone() + "&cardNumber="
                + adUser.getCardNumber() + "&actionTime=" + timestamp;

        if (adBusinessExpandInfo.getBusinessUrl().contains("?")) {
            resultUrl = adBusinessExpandInfo.getBusinessUrl() + "&" + str1 + "&sign=" + sign + targetUrl;
        } else {
            resultUrl = adBusinessExpandInfo.getBusinessUrl() + "?" + str1 + "&sign=" + sign + targetUrl;
        }
        return resultUrl;
    }
}
