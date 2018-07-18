package com.doooly.business.exwings.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.exwings.ExWingsService;
import com.doooly.business.exwings.ExWingsUtils;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.common.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.doooly.business.exwings.ExWingsUtils.BALANCE_URL;

/**
 * Created by WANG on 2018/7/11.
 */
@Service
public class ExWingsServiceImpl implements ExWingsService {


    private static Logger log = LoggerFactory.getLogger(ExWingsServiceImpl.class);

//    public static void main(String[] args) {
//        JSONObject json = new JSONObject();
//        json.put("orderid", ExWingsUtils.getOrderId());
//        json.put("mobile", "18616006011");
//        json.put("ts", ExWingsUtils.getTs());
//        json.put("type", 1);
//        json.put("amount", 1);
//        json.put("sign", ExWingsUtils.exwingsSign(json.get("orderid"), json.get("mobile"), json.get("type"), json.get("amount"), json.get("ts")));
//        JSONObject ret = HttpClientUtil.httpPost(ExWingsUtils.RECHARGE_URL, json);
//        System.out.println(ret);
//        if (ret != null) {
//            boolean verifySign = ExWingsUtils.exwingsVerify(ret.getString("sign"),ret.getString("result"), ret.getString("message"), ret.getString("orderid"), ret.getString("mobile"),
//                    ret.getString("type"),
//                    ret.getString("amount"),
//                    ret.getString("ts"));
//            System.out.println("verifySign=" + verifySign);
//        }
//    }


    public JSON balance() {
        JSONObject json = new JSONObject();
        json.put("ts", ExWingsUtils.getTs());
        json.put("sign", ExWingsUtils.exwingsSign(json.get("ts")));
        JSONObject ret = HttpClientUtil.httpPost(BALANCE_URL, json);
        System.out.println(ret);
        String result = ret.getString("result");
        String message = ret.getString("message");
        String consumed = ret.getString("consumed");
        String total = ret.getString("total");
        String balance = ret.getString("balance");
        String sign = ret.getString("sign");
        String ts = ret.getString("ts");
        System.out.println(json.toJSONString());
        String verifySign = ExWingsUtils.exwingsSign(result, message, total, consumed, balance, ts);
        System.out.println("verifySign="+verifySign);
        System.out.println("sign=      "+sign);
        return json;
    }

    public JSONObject recharge(OrderVo order) {
        OrderItemVo item = order.getItems().get(0);
        JSONObject json = new JSONObject();
        log.info("item.cardOid" + item.getCardOid() + "1");
        json.put("orderid", item.getCardOid());
        json.put("mobile", order.getConsigneeMobile());
        json.put("ts", ExWingsUtils.getTs());
        if (item.getSku().startsWith("1次")) {
            json.put("type", 0);
        } else if (item.getSku().startsWith("7天")) {
            json.put("type", 1);
        } else if (item.getSku().startsWith("30天")) {
            json.put("type", 2);
        } else {
            log.error("recharge() item.getSku()=" + item.getSku());
            return null;
        }
        json.put("amount", item.getNumber().intValue());
        json.put("sign", ExWingsUtils.exwingsSign(json.get("orderid"), json.get("mobile"), json.get("type"), json.get("amount"), json.get("ts")));
        log.info("json=" + json);
        JSONObject ret = HttpClientUtil.httpPost(ExWingsUtils.RECHARGE_URL, json);
        log.info("ret=" + ret);
        //校验签名
        if (ret != null) {
            boolean verifySign = ExWingsUtils.exwingsVerify(
                    ret.getString("sign"), ret.getString("result"), ret.getString("message"),
                    ret.getString("orderid"),
                    ret.getString("mobile"),
                    ret.getString("type"),
                    ret.getString("amount"),
                    ret.getString("ts"));
            log.info("verifySign=" + verifySign);
            log.info("sign=      " + ret.get("sign"));
            if (verifySign) {
                return ret;
            } else {
                return null;
            }
        }
        return null;
    }
}
