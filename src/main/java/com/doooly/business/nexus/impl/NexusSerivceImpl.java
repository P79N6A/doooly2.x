package com.doooly.business.nexus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.nexus.MaxxipointSecurity;
import com.doooly.business.nexus.NexusSerivce;
import com.doooly.business.nexus.NexusUtil;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.reachad.AdNexusBindDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdUser;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.doooly.business.nexus.MaxxipointSecurity.verify;
import static com.doooly.business.nexus.NexusUtil.jxcPublicKey;
import static com.doooly.business.nexus.NexusUtil.privateKey;
import static sun.rmi.rmic.newrmic.Constants.EXCEPTION;

@Service
public class NexusSerivceImpl implements NexusSerivce {

    @Autowired
    private AdNexusBindDao adNexusBindDao;
    @Autowired
    private AdUserServiceI adUserServiceI;

    private static Logger logger = LoggerFactory.getLogger(NexusSerivceImpl.class);

    //纳客宝积分交易成功码
    private static final String SUCCESS_STATUS = "100003";
    //纳客宝返回码
    private static final String RET_CODE = "00";
    //需要退款状态
    public static final String NEED_REFUND = "9999";

//    private static final String NEXUS_URL = "http://app-uat.maxxipoint.com/NexusService/api";
//    private static final String APPID = "844AB181D6878FF9";
//    private static final String APPSECRECT = "90586B08844AB181D6878FF9DDF118C9";
//
//    private static final String jxcPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfDvfIr5IRRCVIudeiKs53O+3cl3s2GCBqME2ywF31OhBIW8zC8PMmJkCuZk1Gt+y7IlYk2mPO3QCf7HZz1nd6P2WD869Bh/L6p8bN1zVplXleQ+Yqa8s4Bh4/jdLzs2+LBayowvUNju94QoPxo2ylljTez5g1D5wYVTVWZkClWQIDAQAB";
//
//    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAImde3QVAMisTip7zgXjtCh6wN5L\n" +
//            "0R17mXZrX+U3ADTAGVpJAnuVrLc4dAgkJd/H0lvDHAfiSCNHh4d1Y+w0AgjWf+qj8yGeyqdk6sKH\n" +
//            "RvGbRzQ/MBbIBgeSD8a0Y7pRpaCJaIyIyImYs/5gpisFW7jSowiKSiQe6vJ1lpjRqO6RAgMBAAEC\n" +
//            "gYBsIkQEphBUnxhYx6nO9Or2t+ZfhsHN4fZnl93ldf+Cc8Q4LpB13dm2qvR6BTWBjzmbg0e+Zi97\n" +
//            "EVTMuxCGZSOG+DAyiYDEm9TdiLCfGCRF9Fugi/y+PWmsRJME1kKkbZjiJruD+LbjQH892Tn5Y4A2\n" +
//            "mHvyLHI/amLqb4TUxLYyVQJBAMzxuKwXKQKPwb0gf3Iqa2NYZWGviXvdVOFWSC6GnkH3hESJxPiZ\n" +
//            "zKAaLFDaVOPAGXHX/6+X33L0CwcJ8evfw+sCQQCr5dzu1w3ZXPKfV+L3WOTEUhFYtgQDWMaVbS9Q\n" +
//            "43v0LFHPc/h5LT30udK8Rs9ePj+svWZ/xY1VAx/AwZfpbMRzAkBIUyWw1ZuLY+AjNkzDpWSwcomU\n" +
//            "p7YFGF7UBvcCNE+1R/xNk7EHan9kINhy0BoVJb3VBz0cYqRglO8vVLsjWpxxAkEAh93venhQYfWt\n" +
//            "b3Sv2IFSkDmtrEhxc5O/omvicjTbzGsbXrVzN5Qi3EPj5Ryy2vKosYgic+tZglAt0NUzlTR7MQJB\n" +
//            "AKbRL+yUfVjB16ZjRrEf65MsW1IAqqXocSWOttjTXJuSfLdmI1BYj4XJNvuUg5FsO1W7lqalO0km\n" +
//            "BkV2ycWi4S0=";

    private static final String NEXUS_URL = PropertiesHolder.getProperty("nexus_recharge_url");
    private static final String APPID = PropertiesHolder.getProperty("nexus_appid");
    private static final String APPSECRECT = PropertiesHolder.getProperty("nexus_appsecrect");

    /**
     * 回滚
     * @param tranSeq
     * @param srcTranSeq
     * @param srcTranOutSeq
     * @return
     */
    public static String rollback(String tranSeq,String srcTranSeq,String srcTranOutSeq) {
        try {
            String timestamp = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
            SortedMap<String, Object> bussinessData = new TreeMap<String, Object>();
            bussinessData.put("tranSeq", tranSeq);
            bussinessData.put("tranTime", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            bussinessData.put("srcTranSeq", srcTranSeq);
            bussinessData.put("srcTranOutSeq", srcTranOutSeq);
            bussinessData.put("extend1", null);
            bussinessData.put("extend2", null);
            bussinessData.put("extend3", null);
            String jsonData = JSON.toJSONString(bussinessData);
            logger.info("bussinessData={}", bussinessData);
            //业务数据加密
            byte[] encodeJsonData1 = MaxxipointSecurity.encryptByPublicKey(jxcPublicKey, jsonData.getBytes("UTF-8"));
            String encodeJsonData = Base64.encodeBase64String(encodeJsonData1);
            //生成签名
            byte[] reqSignByte = MaxxipointSecurity.sign(privateKey, encodeJsonData1);
            String sign = Base64.encodeBase64String(reqSignByte);
            //nonce
            String nonce = "appId=" + APPID + "&appSecret=" + APPSECRECT + "&timestamp=" + timestamp;
            String md5Str1 = Base64.encodeBase64String(MaxxipointSecurity.encryptMD5(nonce.getBytes("UTF-8")));
            //请求参数
            JSONObject reqJson = new JSONObject();
            reqJson.put("appId", APPID);
            reqJson.put("timestamp", timestamp);
            reqJson.put("method", "points.exchange.tran.rollback");
            reqJson.put("nonce", md5Str1);
            reqJson.put("data", encodeJsonData);
            reqJson.put("sign", sign);
            String ret = HttpClientUtil.sendPost(reqJson, NEXUS_URL);
            logger.info("sendPost：ret=" + ret);
            //接口未响应
            if(MessageDataBean.failure_code.equals(ret)){
                return MessageDataBean.failure_code;
            }
            //如果接口失败或者没有响应调用查询接口, 如果查询结果没有响应或者失败调用退款接口
            logger.info("======================================================");
            JSONObject object = JSONObject.parseObject(ret);
            String retCode = object.getString("respCode");
            String retData = object.getString("respData");
            String retSign = object.getString("sign");
            logger.info("返回码：" + retCode);
            byte[] retByte = Base64.decodeBase64(retData);
            byte[] signByte = Base64.decodeBase64(retSign);
            logger.info("安全校验结果：" + verify(jxcPublicKey, retByte, signByte));
            logger.info("解密后数据：" + new String(MaxxipointSecurity.decryptByPrivateKey(privateKey, retByte), "UTF-8"));
            if(verify(jxcPublicKey, retByte, signByte)){
                return new String(MaxxipointSecurity.decryptByPrivateKey(privateKey, retByte), "UTF-8");
            }
        } catch (Exception e) {
            logger.error("query() e={}", e);
            e.printStackTrace();
        }
        return EXCEPTION;
    }

    /***
     * 查询接口
     * 返回 1001查询接口未响应<br>
     *
     * @param tranOutSeq
     * @return
     */
    public static String query(String tranOutSeq) {
        try {
            String timestamp = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
            SortedMap<String, Object> bussinessData = new TreeMap<String, Object>();
            bussinessData.put("tranOutSeq", tranOutSeq);
            bussinessData.put("extend1", null);
            bussinessData.put("extend2", null);
            bussinessData.put("extend3", null);
            String jsonData = JSON.toJSONString(bussinessData);
            logger.info("bussinessData={}", bussinessData);
            //业务数据加密
            byte[] encodeJsonData1 = MaxxipointSecurity.encryptByPublicKey(jxcPublicKey, jsonData.getBytes("UTF-8"));
            String encodeJsonData = Base64.encodeBase64String(encodeJsonData1);
            //生成签名
            byte[] reqSignByte = MaxxipointSecurity.sign(privateKey, encodeJsonData1);
            String sign = Base64.encodeBase64String(reqSignByte);
            //nonce
            String nonce = "appId=" + APPID + "&appSecret=" + APPSECRECT + "&timestamp=" + timestamp;
            String md5Str1 = Base64.encodeBase64String(MaxxipointSecurity.encryptMD5(nonce.getBytes("UTF-8")));
            //请求参数
            JSONObject reqJson = new JSONObject();
            reqJson.put("appId", APPID);
            reqJson.put("timestamp", timestamp);
            reqJson.put("method", "points.exchange.tran.query");
            reqJson.put("nonce", md5Str1);
            reqJson.put("data", encodeJsonData);
            reqJson.put("sign", sign);
            String ret = HttpClientUtil.sendPost(reqJson, NEXUS_URL);
            logger.info("sendPost：ret=" + ret);
            //接口未响应
            if(MessageDataBean.failure_code.equals(ret)){
                return MessageDataBean.failure_code;
            }
            //如果接口失败或者没有响应调用查询接口, 如果查询结果没有响应或者失败调用退款接口
            logger.info("======================================================");
            JSONObject object = JSONObject.parseObject(ret);
            String retCode = object.getString("respCode");
            String retData = object.getString("respData");
            String retSign = object.getString("sign");
            logger.info("返回码：" + retCode);
            byte[] retByte = Base64.decodeBase64(retData);
            byte[] signByte = Base64.decodeBase64(retSign);
            logger.info("安全校验结果：" + verify(jxcPublicKey, retByte, signByte));
            logger.info("解密后数据：" + new String(MaxxipointSecurity.decryptByPrivateKey(privateKey, retByte), "UTF-8"));
            if(verify(jxcPublicKey, retByte, signByte)){
                return new String(MaxxipointSecurity.decryptByPrivateKey(privateKey, retByte), "UTF-8");
            }
        } catch (Exception e) {
            logger.error("query() e={}", e);
            e.printStackTrace();
        }
        return null;
    }

        /***
         * 四种情况视为退款情况
         * 1.没有bindid
         * 2.调用接口没有响应也而且查不到结果
         * 3.纳客宝返回码错误
         * 4.纳客宝处理状态错误
         *
         * @param order
         * @return
         */
    @Override
    public MessageDataBean consume(OrderVo order,AdUser adUser) {
        try {
            String bindId = adNexusBindDao.getBindId(order.getUserId());
            if (StringUtil.isNullOrEmpty(bindId)) {
                return new MessageDataBean(NEED_REFUND, "bindId is null");
            }
            int integral = order.getTotalMount().multiply(new BigDecimal("100")).intValue();
            order.getItems().get(0);
            OrderItemVo item = order.getItems().get(0);
            logger.info("item.cardOid=" + item.getCardOid());
            String tranSeq = item.getCardOid();
            String timestamp = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
            SortedMap<String, Object> bussinessData = new TreeMap<String, Object>();
            bussinessData.put("userAccount", bindId); //用户授权时，纳客宝返回的用户账号代码
            bussinessData.put("amount", String.valueOf(integral));
            bussinessData.put("outAccount", adUser.getTelephone());
            bussinessData.put("consumeCode", "1000");//1000增加,2000扣减
            bussinessData.put("tranSeq", tranSeq); //商户请求交易流水号（每次请求必须唯一，多次请求值相同视为重复请求）
            bussinessData.put("tranTime", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
            bussinessData.put("remarks", "a"); //remarks
            bussinessData.put("extend1", null);
            bussinessData.put("extend2", null);
            bussinessData.put("extend3", null);
            String jsonData = JSON.toJSONString(bussinessData);
            logger.info("jsonData={}", jsonData);
            //业务数据加密
            byte[] encodeJsonData1 = MaxxipointSecurity.encryptByPublicKey(jxcPublicKey, jsonData.getBytes("UTF-8"));
            String encodeJsonData = Base64.encodeBase64String(encodeJsonData1);
            //生成签名
            byte[] reqSignByte = MaxxipointSecurity.sign(privateKey, encodeJsonData1);
            String sign = Base64.encodeBase64String(reqSignByte);
            //nonce
            String nonce = "appId=" + APPID + "&appSecret=" + APPSECRECT + "&timestamp=" + timestamp;
            String md5Str1 = Base64.encodeBase64String(MaxxipointSecurity.encryptMD5(nonce.getBytes("UTF-8")));
            //请求参数
            JSONObject reqJson = new JSONObject();
            reqJson.put("appId", APPID);
            reqJson.put("timestamp", timestamp);
            reqJson.put("method", "points.exchange.consume");
            reqJson.put("nonce", md5Str1);
            reqJson.put("data", encodeJsonData);
            reqJson.put("sign", sign);
            String ret = HttpClientUtil.sendPost(reqJson, NEXUS_URL);
            logger.info("ret={}", ret);
            //调用接口失败或者超时,调用查询接口
            if(MessageDataBean.failure_code.equals(ret)){
                String result = query(tranSeq);
                //查询接口未响应,调用回滚
                if(StringUtils.isEmpty(result)) {
                    if (MessageDataBean.failure_code.equals(result)) {
                        //调用回滚
                        //String srcTranOutSeq = null;
                        //String rollBackTranSeq = ExWingsUtils.getOrderId();
                        //String rollback = rollback(rollBackTranSeq, tranSeq, srcTranOutSeq);
                        //JSONObject object = JSONObject.parseObject(rollback);
                        //String sataus = object.getString("status");
                        ////回滚成功需要退款
                        //if("100004".equals(sataus)){
                        //    return new MessageDataBean(NEED_REFUND, "the order has been rolled back, status="+sataus);
                        //}
                        return new MessageDataBean(MessageDataBean.failure_code, "纳客宝系统请求失败,联系纳客宝排查!");
                    }
                }
            }
            JSONObject object = JSONObject.parseObject(ret);
            String retCode = object.getString("respCode");
            String retData = object.getString("respData");
            String statusMsg = object.getString("statusMsg");
            String retSign = object.getString("sign");
            byte[] retByte = Base64.decodeBase64(retData);
            byte[] signByte = Base64.decodeBase64(retSign);
            boolean verify = MaxxipointSecurity.verify(jxcPublicKey, retByte, signByte);
            //签名验证
            logger.info("verify={}", verify);
            if(!verify){
                //验证失败情况下不能退款
                return new MessageDataBean(MessageDataBean.failure_code, "验证签名错误!");
            }
            //返回码验证
            logger.info("retCode={},statusMsg={}", retCode,statusMsg);
            if(!RET_CODE.equals(retCode)){
                return new MessageDataBean(NEED_REFUND, NexusUtil.getRetMsg(retCode));
            }
            //解析返回结果
            String retJson = new String(MaxxipointSecurity.decryptByPrivateKey(privateKey, retByte), "UTF-8");
            logger.info("retJson={}", retJson);
            JSONObject jso = JSON.parseObject(retJson);
            String status = jso.getString("status");
            String tranOutSeq = jso.getString("tranOutSeq");
            String statusMsg2 = jso.getString("statusMsg");
            if (SUCCESS_STATUS.equals(status)) {
                Map data = new HashMap();
                data.put("tranOutSeq",tranOutSeq);
                return new MessageDataBean(MessageDataBean.success_code, "success", data);
            } else {
                return new MessageDataBean(NEED_REFUND, statusMsg2 + ";status=" + status);
            }
        } catch (Exception e) {
            logger.info("e = {}", e);
            return new MessageDataBean(MessageDataBean.failure_code, "failed");
        }
    }


    public static void main(String[] args) {
        try {

            rollback("1821800019488","12320180806060635","182180001946");
            //query("123456781");
//            String timestamp = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
//            String tranSeq = "123" + timestamp;
//            SortedMap<String, Object> bussinessData = new TreeMap<String, Object>();
//            bussinessData.put("userAccount", "6866f3d3695866fd831c5d0fe89ccf3e"); //用户授权时，纳客宝返回的用户账号代码
//            bussinessData.put("amount", "1000");    //
//            bussinessData.put("outAccount", "18616006011");    //
//            bussinessData.put("consumeCode", "1000");//1000增加,2000扣减
//            bussinessData.put("tranSeq", tranSeq); //商户请求交易流水号（每次请求必须唯一，多次请求值相同视为重复请求）
//            bussinessData.put("tranTime", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
//            bussinessData.put("remarks", "a"); //remarks
//            bussinessData.put("extend1", null);
//            bussinessData.put("extend2", null);
//            bussinessData.put("extend3", null);
//            String jsonData = JSON.toJSONString(bussinessData);
//            System.out.println(jsonData);
//            //业务数据加密
//            byte[] encodeJsonData1 = MaxxipointSecurity.encryptByPublicKey(jxcPublicKey, jsonData.getBytes("UTF-8"));
//            String encodeJsonData = Base64.encodeBase64String(encodeJsonData1);
//            //生成签名
//            byte[] reqSignByte = MaxxipointSecurity.sign(privateKey, encodeJsonData1);
//            String sign = Base64.encodeBase64String(reqSignByte);
//            //nonce
//            String nonce = "appId=" + APPID + "&appSecret=" + APPSECRECT + "&timestamp=" + timestamp;
//            String md5Str1 = Base64.encodeBase64String(MaxxipointSecurity.encryptMD5(nonce.getBytes("UTF-8")));
//            //请求参数
//            JSONObject reqJson = new JSONObject();
//            reqJson.put("appId", APPID);
//            reqJson.put("timestamp", timestamp);
//            reqJson.put("method", "points.exchange.consume");
//            reqJson.put("nonce", md5Str1);
//            reqJson.put("data", encodeJsonData);
//            reqJson.put("sign", sign);
//            String ret = HttpClientUtil.sendPost(reqJson, NEXUS_URL);
//            //调用接口失败或者超时,调用查询接口
//            if(MessageDataBean.failure_code.equals(ret)){
//                String result = query(tranSeq);
//                if(result != null) {
//                    //查询接口未响应,调用回滚
//                    if (MessageDataBean.failure_code.equals(result)) {
//                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//                    }
//                }
//            }
//            //如果接口失败或者没有响应调用查询接口, 如果查询结果没有响应或者失败调用退款接口
//            logger.info(ret);
//            System.out.println("======================================================");
//            JSONObject object = JSONObject.parseObject(ret);
//            String retCode = object.getString("respCode");
//            String retData = object.getString("respData");
//            String retSign = object.getString("sign");
//            System.out.println("返回码：" + retCode);
//            byte[] retByte = Base64.decodeBase64(retData);
//            byte[] signByte = Base64.decodeBase64(retSign);
//            System.out.println("安全校验结果：" + verify(jxcPublicKey, retByte, signByte));
//            System.out.println("解密后数据：" + new String(MaxxipointSecurity.decryptByPrivateKey(privateKey, retByte), "UTF-8"));
//            System.out.println("结束");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
