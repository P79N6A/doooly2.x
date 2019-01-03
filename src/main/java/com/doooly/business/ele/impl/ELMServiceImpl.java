package com.doooly.business.ele.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.MD5Util;
import com.doooly.common.elm.ELMConstants;
import com.doooly.common.elm.SignUtils;
import com.doooly.common.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 饿了么实现
 * @author: qing.zhang
 * @date: 2019-01-03
 */
@Service
public class ELMServiceImpl implements ELMServiceI{

    @Autowired
    private ConfigDictServiceI configDictServiceI;

    /** 推送信息
     * {
     "bNo": "业务编码",
     "uNo": "用户编码",
     "orderNo": "订单号",
     "orderNo98": "订单号(98开头)",
     "totalFee": "总费用",
     "status": "订单状态"
     }
     */
    @Override
    public ResultModel orderAmountPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        String consumerNo = httpServletRequest.getHeader("consumerNo");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE, ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        String elmConsumerSecret = eleConfig.getString("consumerSecret");
        Boolean flag = SignUtils.validParam(consumerNo,timeStamp,sign,eleConfig);
        if(!flag){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }
        //创建doooly订单
        String uNo = obj.getString("uNo");
        String orderNo = obj.getString("orderNo");
        String orderNo98 = obj.getString("orderNo98");
        String totalFee = obj.getString("totalFee");
        String status = obj.getString("status");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpClientUtil.HEADER_CONTENT_TYPE, HttpClientUtil.HEADER_CONTENT_TYPE_JSON);
        headerMap.put("consumerNo",consumerNo);
        headerMap.put("timeStamp", String.valueOf(DateUtils.getNowTime()));
        headerMap.put("sign", MD5Util.MD5Encode(sign + elmConsumerSecret + String.valueOf(DateUtils.getNowTime()), ELMConstants.CHARSET));

        Map<String, String> paramMap = new HashMap<>();


        return null;
    }
}
