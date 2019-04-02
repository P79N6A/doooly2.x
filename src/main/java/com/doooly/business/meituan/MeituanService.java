package com.doooly.business.meituan;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.meituan.MeituanProductTypeEnum;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.reachad.AdUser;

import java.util.Map;

/**
 * Created by Administrator on 2018/12/13.
 */
public interface MeituanService {

    String easyLogin(String entToken, String staffNo, String staffPhoneNo,Map<String,Object> riskParam,MeituanProductTypeEnum productTypeEnum) throws Exception;

    String getSiginature(Map<String,Object> map) throws Exception;

    String convertMapToUrl(Map<String,Object> map);

    String convertMapToUrlEncode(Map<String, Object> paramMap);

    OrderMsg createOrderMeituan(JSONObject json);

}
