package com.doooly.common.elm;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.MD5Util;

/**
 * @Description: 饿了么加密验证工具类
 * @author: qing.zhang
 * @date: 2019-01-03
 */
public class SignUtils {


    public static Boolean validParam(String consumerNo, String timeStamp, String sign, JSONObject obj, JSONObject eleConfig) {
        String elmConsumerNO = eleConfig.getString("consumerNo");
        String elmConsumerSecret = eleConfig.getString("consumerSecret");
        //1,客户编码校验
        if(!consumerNo.equals(elmConsumerNO)){
            //传入和提供配置不一致
            return false;
        }
        //2,时间戳
        int nowTime = DateUtils.getNowTime();
        if(nowTime - Long.parseLong(timeStamp) > ELMConstants.REQUEST_EXPIRY_TIME){
            return false;
        }
        //3,加密sign
        String sign1 = MD5Util.MD5Encode(obj + elmConsumerSecret + timeStamp, ELMConstants.CHARSET);
        if(!sign.equals(sign1)){
            return false;
        }
        return true;
    }
}
