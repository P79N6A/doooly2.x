package com.doooly.business.meituan.impl;

import com.doooly.business.meituan.MeituanService;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.RsaUtil;
import com.doooly.common.util.BeanMapUtil;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.entity.meituan.EasyLogin;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/13.
 */
@Service
public class MeituanServiceImpl implements MeituanService{

    @Override
    public String easyLogin(String entToken, String staffNo, String staffPhoneNo) throws Exception{
        EasyLogin easyLogin = new EasyLogin();
        easyLogin.setEntToken(entToken);
        easyLogin.setStaffNo(staffNo);
        easyLogin.setStaffPhoneNo(staffPhoneNo);
        Map<String,Object> paramMap = BeanMapUtil.transBean2Map(easyLogin);
        paramMap.remove("signature");
        paramMap =  BeanMapUtil.sortMapByKey(paramMap);
        String signature = getSiginature(paramMap);
        paramMap.put("signature",signature);
        paramMap =  BeanMapUtil.sortMapByKey(paramMap);
        String ret = MeituanConstants.meituan_access_url + convertMapToUrl(paramMap);
        return ret;
    }


    @Override
    public String getSiginature(Map<String, Object> paramMap) throws Exception{
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Object> entry : paramMap.entrySet()) {
            if (sb.length() == 0) {
                sb.append(entry.getKey() + "=" + entry.getValue());
            } else {
                sb.append("&").append(entry.getKey() + "=" + entry.getValue());
            }
        }
        System.out.println(new Gson().toJson(paramMap));
        System.out.println(sb.toString());
        RSAPrivateKey rsaPrivateKey = RsaUtil.loadPrivateKey(MeituanConstants.private_key);
        String signature = RsaUtil.sign(sb.toString().getBytes(),rsaPrivateKey);
        signature = URLEncoder.encode(signature,"utf-8");
        System.out.println(signature);
        return signature;
    }

    @Override
    public String convertMapToUrl(Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String,Object> entry : paramMap.entrySet()) {
            if (sb.length() == 1) {
                sb.append(entry.getKey() + "=" + entry.getValue());
            } else {
                sb.append("&").append(entry.getKey() + "=" + entry.getValue());
            }
        }
        return sb.toString();
    }
}
