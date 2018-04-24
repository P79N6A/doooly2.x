package com.doooly.business.oneNumber.service.impl;

import com.doooly.business.oneNumber.service.OneNumberServiceI;
import com.doooly.business.utils.MD5Util;
import com.doooly.dao.reachad.AdBusinessExpandInfoDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * @Description: 1号通接口
 * @author: qing.zhang
 * @date: 2017-08-28
 */
@Service
public class OneNumberService implements OneNumberServiceI{

    private static Logger logger = Logger.getLogger(OneNumberService.class);

    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;
    @Autowired
    private AdUserDao adUserDao;

    @Override
    public MessageDataBean getTargetUrl(String userId, String businessId,String targetUrl) throws UnsupportedEncodingException {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        AdUser adUser = adUserDao.getById(userId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        long timestamp = new Date().getTime() / 1000;
        String resultUrl;
        switch (adBusinessExpandInfo.getType()){
            //兜礼提供对接方式
            case 0:
                resultUrl = getDooolyUrl(targetUrl, adUser, adBusinessExpandInfo, timestamp);
                break;
            //内购网专属
            case 1:
                resultUrl = getNeiGouUrl(targetUrl, adUser, adBusinessExpandInfo, timestamp);
                break;
            //酷邀贷专属
            case 2:
                resultUrl = getKuYaoDaiUrl(targetUrl, adUser, adBusinessExpandInfo);
                break;
            default:
                resultUrl = "";
                break;
        }

        logger.info("1号通生成的结果链接:"+resultUrl);
        map.put("resultUrl",resultUrl);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

    /**
     * 酷邀贷专属
     * @param targetUrl 目标地址
     * @param adUser 用户信息
     * @param adBusinessExpandInfo 商户扩展信息
     * @return 跳转链接
     */
    private String getKuYaoDaiUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo) {
        String resultUrl;
        String str = "telNo=" + adUser.getTelephone()+
                "&source=" + adBusinessExpandInfo.getShopId();
        if(StringUtils.isNotBlank(targetUrl) && !"null".equals(targetUrl)){
            str += "&surl=" + targetUrl;
        }
        if(adBusinessExpandInfo.getBusinessUrl().contains("?")){
            resultUrl = adBusinessExpandInfo.getBusinessUrl()+ "&" + str ;
        }else {
            resultUrl = adBusinessExpandInfo.getBusinessUrl()+ "?" + str ;
        }
        return resultUrl;
    }


    /**
     * 内购网链接获取
     * @param targetUrl 目标地址
     * @param adUser 用户信息
     * @param adBusinessExpandInfo 商户扩展信息
     * @param timestamp 时间戳
     * @return
     * @throws UnsupportedEncodingException 编码异常
     */
    private String getNeiGouUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo, long timestamp) throws UnsupportedEncodingException {
        String resultUrl;//加密字符串需要shopKey
        String str = "client_id=" + adBusinessExpandInfo.getShopId()+
                "&external_user_id=" + adUser.getCardNumber()+
                "&name=" + adUser.getName()+
                "&time="+ timestamp;
        if(StringUtils.isNotBlank(targetUrl) && !"null".equals(targetUrl)){
            str += "&surl=" + targetUrl;
        }
        String[] params = StringUtils.split(str, "&");
        Arrays.sort(params);
        String param = StringUtils.join(params, "&")+adBusinessExpandInfo.getShopKey();
        String sign = MD5Util.MD5Encode(param, "UTF-8");
        if(adBusinessExpandInfo.getBusinessUrl().contains("?")){
            resultUrl = adBusinessExpandInfo.getBusinessUrl()+ "&" + str + "&sign=" + sign;
        }else {
            resultUrl = adBusinessExpandInfo.getBusinessUrl()+ "?" + str + "&sign=" + sign;
        }
        return resultUrl;
    }

    /**
     *
     * 跳转链接获取
     * @param targetUrl 目标地址
     * @param adUser 用户信息
     * @param adBusinessExpandInfo 商户扩展信息
     * @param timestamp 时间戳
     * @return
     * @throws UnsupportedEncodingException 编码异常
     */
    private String getDooolyUrl(String targetUrl, AdUser adUser, AdBusinessExpandInfo adBusinessExpandInfo, long timestamp) throws UnsupportedEncodingException {
        String resultUrl;//加密字符串需要shopKey
        String str = "shopId=" + adBusinessExpandInfo.getShopId()+
                "&shopKey=" + adBusinessExpandInfo.getShopKey()+
                "&mobile=" + adUser.getTelephone()+
                "&cardNumber=" + adUser.getCardNumber()+
                "&actionTime="+ timestamp;
        String sign = MD5Util.MD5Encode(str, "UTF-8");
        String encode = "";
        if(StringUtils.isNotBlank(targetUrl)){
            encode =  URLEncoder.encode(targetUrl, "UTF-8");
        }
        targetUrl = "&targetUrl="+ encode;
        //拼接访问地址不传shopKey
        String str1 = "shopId=" + adBusinessExpandInfo.getShopId()+
                "&mobile=" + adUser.getTelephone()+
                "&cardNumber=" + adUser.getCardNumber()+
                "&actionTime="+ timestamp;

        if(adBusinessExpandInfo.getBusinessUrl().contains("?")){
            resultUrl = adBusinessExpandInfo.getBusinessUrl() + "&" + str1 + "&sign=" + sign + targetUrl;
        }else {
            resultUrl = adBusinessExpandInfo.getBusinessUrl() + "?" + str1 + "&sign=" + sign + targetUrl;
        }
        return resultUrl;
    }
}
