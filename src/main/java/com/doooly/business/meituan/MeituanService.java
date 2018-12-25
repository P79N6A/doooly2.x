package com.doooly.business.meituan;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.OrderMsg;

import java.util.Map;

/**
 * Created by Administrator on 2018/12/13.
 */
public interface MeituanService {

    String easyLogin(String entToken,String staffNo,String staffPhoneNo) throws Exception;

    String getSiginature(Map<String,Object> map) throws Exception;

    String convertMapToUrl(Map<String,Object> map);

    String convertMapToUrlEncode(Map<String, Object> paramMap);

    OrderMsg createOrderMeituan(JSONObject json);

}
