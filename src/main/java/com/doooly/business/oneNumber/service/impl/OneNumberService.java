package com.doooly.business.oneNumber.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.business.common.util.EncryptDecryptUtil;
import com.doooly.business.oneNumber.service.OneNumberServiceI;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.utils.MD5Util;
import com.doooly.business.utils.RSAEncryptUtil;
import com.doooly.common.elm.ELMConstants;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.reachad.AdBusinessExpandInfoDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.report.UserSynRecordDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.report.UserSynRecord;
import com.koalii.bc.util.encoders.Hex;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 1号通接口
 * @author: qing.zhang
 * @date: 2017-08-28
 */
@Service
public class OneNumberService implements OneNumberServiceI {


    private static Logger logger = Logger.getLogger(OneNumberService.class);

    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;
    @Autowired
    private AdUserDao adUserDao;
	@Autowired
	private UserSynRecordDao userSynRecordDao;

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
                resultUrl = getKuYaoDaiUrl(targetUrl, adUser, adBusinessExpandInfo);
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
        map.put("resultUrl", resultUrl);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

    @Override
    public MessageDataBean authorization(JSONObject json) {
        String clientId = json.getString("clientId");
        String mobile = json.getString("mobile");
        String successRedirectUrl = json.getString("successRedirectUrl");
        String failureRedirectUrl = json.getString("failureRedirectUrl");
        String actionTime = json.getString("actionTime");
        String sign = json.getString("sign");
        json.remove("sign");
        MessageDataBean messageDataBean = new MessageDataBean();
        Map<String, Object> map = new HashMap<>();
        AdUser userParam = new AdUser();
        userParam.setCardNumber(mobile);
        userParam.setTelephone(mobile);
        AdUser loginUser = adUserDao.getUserInfo(userParam);
        long timestamp = new Date().getTime() / 1000;
        AdBusinessExpandInfo queryObj = new AdBusinessExpandInfo();
        queryObj.setClientId(clientId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessExpandInfo(queryObj);
        String jsonToStr = MD5Util.getSortSignContent(json);
        //String availableSign = MD5Util.MD5Psw(jsonToStr);
        //logger.info(" -------------->> availableSign:" + availableSign);
        Integer code = validatePayInfoParam(clientId, mobile, successRedirectUrl, failureRedirectUrl, actionTime, sign, timestamp, loginUser, adBusinessExpandInfo, jsonToStr);
        if(code == GlobalResultStatusEnum.ONE_NUMBER_LOGIN_SUCCESS.getCode()){
            map.put("url",successRedirectUrl);
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setCode(MessageDataBean.success_mess);
        } else {
            map.put("url",failureRedirectUrl+"?code="+code);
            messageDataBean.setCode(String.valueOf(code));
        }
        messageDataBean.setData(map);
        return messageDataBean;
    }


    private Integer validatePayInfoParam(String clientId, String mobile, String successRedirectUrl,
                                         String failureRedirectUrl, String actionTime, String sign, long timestamp, AdUser loginUser, AdBusinessExpandInfo adBusinessExpandInfo, String jsonToStr) {
        if (StringUtils.isBlank(clientId) || StringUtils.isBlank(mobile)
                || StringUtils.isBlank(failureRedirectUrl)
                || StringUtils.isBlank(actionTime)
                || StringUtils.isBlank(sign)|| adBusinessExpandInfo==null) {
            return GlobalResultStatusEnum.ONE_NUMBER_PARAM_EMPTY.getCode();
        }
        if(((timestamp-Long.valueOf(actionTime))>180)){
            return GlobalResultStatusEnum.ONE_NUMBER_LOGIN_TIMEOUT.getCode();
        }
        if(loginUser == null){
            return GlobalResultStatusEnum.ONE_NUMBER_USER_EMPTY.getCode();
        }
        if(!MD5Util.MD5Encode(jsonToStr,"utf-8").equals(sign)){
            return GlobalResultStatusEnum.ONE_NUMBER_SIGN_ERROR.getCode();
        }
        return GlobalResultStatusEnum.ONE_NUMBER_LOGIN_SUCCESS.getCode();
    }


    /**
	 * 饿了么专属连接
	 * @param adUser
	 * @param adBusinessExpandInfo
	 * @return
	 */
	private String getElmUrl(AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo) {
		StringBuilder url = new StringBuilder();
		long timeStamp = new Date().getTime();
		String consumerNo = adBusinessExpandInfo.getShopId();
		String consumerSecret = adBusinessExpandInfo.getShopKey();
		JSONObject synJson = new JSONObject();
		synJson.put("userId", adUser.getId());
		synJson.put("businessId", adBusinessExpandInfo.getBusinessId());
		synJson.put("shopId", adBusinessExpandInfo.getShopId());
		synJson.put("shopKey", adBusinessExpandInfo.getShopKey());
		JSONObject jsonObject = addStaff(synJson);

		JSONObject json = new JSONObject();
		json.put("uNo", adUser.getTelephone());
		json.put("bNo", adUser.getCardNumber());
		String org = Hex.encode(json.toJSONString().getBytes()).toString();
		int type = 3;
		String sign = MD5Util.MD5Psw(org + consumerSecret + timeStamp);
		url.append("https://entu.ele.me?");
		url.append("consumerNo=" + consumerNo);
		url.append("&type=" + type);
		url.append("&timeStamp=" + timeStamp);
		url.append("&sign=" + sign);
		url.append("&type=" + type);
		return url.toString();
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
    private String getKuYaoDaiUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo) {
        String mobile = null;
        String rsaMobile = RSAEncryptUtil.encryptByRSAPubKey(Base64.decodeBase64(RSAEncryptUtil.ONE_NUM_PRD_PUB_KEY),
                adUser.getTelephone());
        try {
            mobile = URLEncoder.encode(rsaMobile, "UTF-8");// 进行编码传输
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String resultUrl = adBusinessExpandInfo.getBusinessUrl() + "?" + "mobile=" + mobile + "&channelCode="
                + adBusinessExpandInfo.getShopId();
        return resultUrl;
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
		public JSONObject addStaff(JSONObject json) {
			JSONObject resutlJson = new JSONObject();
			String userId = json.get("userId").toString();
			String businessId = json.get("businessId").toString();
			String consumerNo = json.get("shopId").toString();
			String consumerSecret = json.get("shopKey").toString();
			UserSynRecord userSynRecord = userSynRecordDao.findByUserIdAndBusinessId(userId, businessId);

			if (userSynRecord != null && "用户同步成功".equals(userSynRecord.getRemarks())) {
				resutlJson.put("code", "1000");
				resutlJson.put("message", "用户已同步");
			} else {
				AdUser adUser = adUserDao.get(userId);

				if (adUser != null) {
					JSONObject result = synStaff(adUser, consumerNo, consumerSecret);

					if ("200".equals(result.get("code").toString())) {
						UserSynRecord synRecord = new UserSynRecord();
						synRecord.setBussinessId(businessId);
						synRecord.setUserId(Integer.valueOf(userId));
						synRecord.setRemarks(result.get("code") + "用户同步成功");
						synRecord.setCreateDate(new Date());
						userSynRecordDao.insert(synRecord);
						resutlJson.put("code", "1000");
						resutlJson.put("message", "用户同步成功");
					}
				} else {
					resutlJson.put("code", "1001");
					resutlJson.put("message", "用户未找到");
				}
			}
			return resutlJson;
		}

		/**
		 * 向饿了么同步添加会员信息
		 * @param adUser
		 * @param consumerNo
		 * @param consumerSecret
		 * @return
		 */
		private JSONObject synStaff(AdUser adUser, String consumerNo, String consumerSecret){
			JSONObject resultJson = new JSONObject();
			Long timeStamp = new Date().getTime();
			// 整理参数
			String url = ELMConstants.ELM_URL + ELMConstants.ELM_ADD_METHOD;
			JSONObject userJson = new JSONObject();
			userJson.put("employeeNo", adUser.getCardNumber());
			userJson.put("name", adUser.getName());
			userJson.put("phoneNumber", adUser.getTelephone());

			String jsonStr = org.apache.commons.codec.binary.Hex.encodeHexString(userJson.toJSONString().getBytes());

			HashMap<String, String> headerMap = new HashMap<>();
			headerMap.put("consumerNo", consumerNo);
			headerMap.put("timeStamp", timeStamp.toString());
			headerMap.put("sign", MD5Util.MD5Encode(jsonStr + consumerSecret + timeStamp.toString(), "UTF-8"));
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("json", jsonStr);

			String resultStr = HttpClientUtil.doPost(url, headerMap, paramMap);
			resultJson = JSONObject.parseObject(resultStr);
			return resultJson;
		}
}

