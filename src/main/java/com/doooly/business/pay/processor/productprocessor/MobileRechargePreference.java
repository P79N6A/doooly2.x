package com.doooly.business.pay.processor.productprocessor;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.common.service.impl.AdCouponActivityService;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.SendRedPack;
import com.doooly.business.pay.bean.SendRedPackResult;
import com.doooly.business.pay.bean.WxPrePayResult;
import com.doooly.business.pay.utils.WxUtil;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.common.util.WechatUtil;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdRechargeRecordDao;
import com.doooly.dao.report.SendRedPackRecordDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.*;
import com.doooly.entity.report.SendPackRecord;
import com.wechat.ThirdPartyWechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 兜礼话费特惠
 * 2018-04-18 11:48:52 WANG
 */
@Component
public class MobileRechargePreference implements ProductProcessor {

    public final static String WXJS_SENDREDPACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

    // 兜礼公众号好发送红包
    public final static String DOOOLY_WXJS_APPID = PropertiesHolder.getProperty("wxjs.appid");
    public final static String DOOOLY_WXJS_MCH_ID = PropertiesHolder.getProperty("wxjs.mch_id");
    public final static String DOOOLY_WXJS_KEY = PropertiesHolder.getProperty("wxjs.key");
    public final static String DOOOLY_CERT_SSL_PATH = PropertiesHolder.getProperty("wxjs.certpath");

    public final static String WUGANG_WXJS_APPID = PropertiesHolder.getProperty("wugang.certpath");
    public final static String WUGANG_WXJS_MCH_ID = PropertiesHolder.getProperty("wugang.mch_id");
    public final static String WUGANG_WXJS_KEY = PropertiesHolder.getProperty("wugang.key");
    public final static String WUGANG_CERT_SSL_PATH = PropertiesHolder.getProperty("wxjs.certpath");

    public final static String REDPACK_AMOUNT = PropertiesHolder.getProperty("redpack.amount");


    protected Logger logger = LoggerFactory.getLogger(MobileRechargePreference.class);

    @Autowired
    AdRechargeRecordDao adRechargeRecordDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AdCouponActivityConnDao adCouponActivityConnDao;
    @Autowired
    private FreeCouponBusinessServiceI freeCouponBusinessServiceI;
    @Autowired
    private AdCouponActivityService adCouponActivityService;
    @Autowired
    private AdUserServiceI adUserServiceI;
    @Autowired
    private SendRedPackRecordDao sendRedPackRecordDao;

    @Override
    public int getProcessCode() {
        return OrderService.ProductType.MOBILE_RECHARGE_PREFERENCE.getCode();
    }

    @Override
    public PayMsg process(OrderVo order) {
        logger.error("MobileRechargePreference orderNumber = {}", order.getOrderNumber());
        try {
            //1.====================修改参与记录,标记下单结束====================
            int rows = adRechargeRecordDao.updateStateOrDelFlag(order.getOrderNumber(), 0);
            logger.error("updateStateOrDelFlag.rows = {}", rows);
            //2.====================发送红包给sourceId. 同时推送消息================
            AdRechargeRecord record = adRechargeRecordDao.getLastRecord(String.valueOf(order.getUserId()));
            logger.info("record = {}", record);
            Long sourceUserId = record.getSourceUserId() ;
            String channel = record.getChannel();
            String sourceOpenId = record.getSourceOpenId();
            if (!sourceOpenId.equals(record.getOpenId())) {
                //发红包
                logger.info("channel={},sourceUserId={},sourceOpenId={}", channel, sourceUserId, sourceOpenId);
                SendRedPackResult result = null;
                if ("doooly".equals(record.getChannel())) {
                    result =  sendRedPackToDoooly(order.getOrderNumber(), sourceOpenId);
                } else if ("wugang".equals(record.getChannel())) {
                    result = sendRedPackToWugang(order.getOrderNumber(), sourceOpenId);
                }
                String sourceNickName = WechatUtil.getWechatUserByOpenId(record.getSourceOpenId(), record.getChannel()).get("nickname");
                logger.info("channel={},sourceUserId={},sourceOpenId={},result={}", channel, sourceUserId,sourceOpenId, result);
                if(result != null){
                    //给分享人推送信息1
                    if("SUCCESS".equals(result.getResult_code())) {
                        JSONObject data = new JSONObject();
                        data.put("openId", sourceOpenId);
                        data.put("channel", channel);
                        data.put("userId", sourceUserId);
                        data.put("telphone", sourceNickName);//改为昵称
                        redisTemplate.convertAndSend("SEND_REDPACK_CHANNEL", data.toString());
                        logger.info("SEND_REDPACK_CHANNEL end.");
                    }
                    //保存发送记录
                    SendPackRecord packRecord = new SendPackRecord();
                    packRecord.setChannel(channel);
                    packRecord.setUser_id(record.getUserId());
                    packRecord.setOpen_id(record.getOpenId());
                    packRecord.setSource_user_id(record.getSourceUserId());
                    packRecord.setSource_open_id(record.getSourceOpenId());
                    packRecord.setSource_nike_name(sourceNickName.getBytes());
                    packRecord.setResult(result.getResult_code());
                    sendRedPackRecordDao.insert(packRecord);
                }
                //给分享人推送信息2
                AdUser user = adUserServiceI.getById(String.valueOf(record.getUserId()));
                JSONObject data2 = new JSONObject();
                data2.put("openId", sourceOpenId);
                data2.put("channel", channel);
                data2.put("userId", sourceUserId);
                data2.put("telphone",sourceNickName);//改为昵称
                data2.put("userName", user.getName());
                logger.info("data2 = {}", data2);
                redisTemplate.convertAndSend("SEND_REDPACK_CHANNEL2", data2.toString());
                logger.info("SEND_REDPACK_CHANNEL2 end.");
            }
            //3.====================发50元券给下单用户==============================
            AdCouponActivity activity = adCouponActivityService.getActivityIdByIdFlag("recharge_activity");
            logger.info("activity = {}", activity);
            AdCoupon adCoupon = adCouponActivityConnDao.getActivityConnByActivityId(String.valueOf(activity.getId())).get(0).getCoupon();
            AdCouponCode code = freeCouponBusinessServiceI.sendCoupon(adCoupon.getBusinessId(), activity.getId(), (int) order.getUserId(), adCoupon.getId().intValue());
            logger.info("activityId = {},code = {}", activity.getId(), code.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("MobileRechargePreference e = {}", e);
        }
        return null;
    }

    public  SendRedPackResult sendRedPackToDoooly(String orderNumber,String sourceOpenId) {
        SendRedPack sendRedPack = new SendRedPack();
        sendRedPack.setNonce_str(WxUtil.getNonceStr());
        sendRedPack.setMch_billno(orderNumber);
        sendRedPack.setMch_id(DOOOLY_WXJS_MCH_ID);
        sendRedPack.setWxappid(DOOOLY_WXJS_APPID);
        sendRedPack.setSend_name("兜礼");
        sendRedPack.setRe_openid(sourceOpenId);
        sendRedPack.setTotal_amount(REDPACK_AMOUNT);
        sendRedPack.setTotal_num("1");
        sendRedPack.setWishing("恭喜您成功召唤亲友享话费福利！快来拆红包~");
        sendRedPack.setClient_ip("127.0.0.1");
        sendRedPack.setAct_name("兜礼话费特惠");
        sendRedPack.setRemark("召唤越多，这样的惊喜就越多哟！");

        //把请求参数打包成数组
        SortedMap<String, Object> data = new TreeMap<String, Object>();
        data.put("nonce_str", sendRedPack.getNonce_str());
        data.put("mch_billno", sendRedPack.getMch_billno());
        data.put("mch_id", sendRedPack.getMch_id());
        data.put("wxappid", sendRedPack.getWxappid());
        data.put("send_name", sendRedPack.getSend_name());
        data.put("re_openid", sendRedPack.getRe_openid());
        data.put("total_amount", sendRedPack.getTotal_amount());
        data.put("total_num", sendRedPack.getTotal_num());
        data.put("wishing", sendRedPack.getWishing());
        data.put("client_ip", sendRedPack.getClient_ip());
        data.put("act_name", sendRedPack.getAct_name());
        data.put("remark", sendRedPack.getRemark());
        String sign = WxUtil.createSign(data, DOOOLY_WXJS_KEY);
        sendRedPack.setSign(sign);

        String respXml = WxUtil.toXml(sendRedPack, SendRedPack.class);
        logger.info("sendRedPackToDoooly() orderNumber={} respXml = {}",orderNumber, respXml);
        String retXml = HttpClientUtil.httsRequestCert(WXJS_SENDREDPACK_URL, DOOOLY_WXJS_MCH_ID, DOOOLY_CERT_SSL_PATH, respXml);
        logger.info("sendRedPackToDoooly() orderNumber={} retXml = {}",orderNumber, retXml);
        return (SendRedPackResult)WxUtil.fromXML(retXml, SendRedPackResult.class);
    }


    public  SendRedPackResult sendRedPackToWugang(String orderNumber,String sourceOpenId) {
        SendRedPack sendRedPack = new SendRedPack();
        sendRedPack.setNonce_str(WxUtil.getNonceStr());
        sendRedPack.setMch_billno(orderNumber);
        sendRedPack.setMch_id(WUGANG_WXJS_MCH_ID);
        sendRedPack.setWxappid(WUGANG_WXJS_APPID);
        sendRedPack.setSend_name("兜礼");
        sendRedPack.setRe_openid(sourceOpenId);
        sendRedPack.setTotal_amount(REDPACK_AMOUNT);
        sendRedPack.setTotal_num("1");
        sendRedPack.setWishing("恭喜您成功召唤亲友享话费福利！快来拆红包~");
        sendRedPack.setClient_ip("127.0.0.1");
        sendRedPack.setAct_name("兜礼话费特惠");
        sendRedPack.setRemark("召唤越多，这样的惊喜就越多哟！");

        //把请求参数打包成数组
        SortedMap<String, Object> data = new TreeMap<String, Object>();
        data.put("nonce_str", sendRedPack.getNonce_str());
        data.put("mch_billno", sendRedPack.getMch_billno());
        data.put("mch_id", sendRedPack.getMch_id());
        data.put("wxappid", sendRedPack.getWxappid());
        data.put("send_name", sendRedPack.getSend_name());
        data.put("re_openid", sendRedPack.getRe_openid());
        data.put("total_amount", sendRedPack.getTotal_amount());
        data.put("total_num", sendRedPack.getTotal_num());
        data.put("wishing", sendRedPack.getWishing());
        data.put("client_ip", sendRedPack.getClient_ip());
        data.put("act_name", sendRedPack.getAct_name());
        data.put("remark", sendRedPack.getRemark());
        String sign = WxUtil.createSign(data, WUGANG_WXJS_KEY);
        sendRedPack.setSign(sign);

        String respXml = WxUtil.toXml(sendRedPack, SendRedPack.class);
        logger.info("sendRedPackToWugang() orderNumber={} respXml = {}",orderNumber, respXml);
        String retXml = HttpClientUtil.httsRequestCert(WXJS_SENDREDPACK_URL, WUGANG_WXJS_MCH_ID, WUGANG_CERT_SSL_PATH, respXml);
        logger.info("sendRedPackToWugang() orderNumber={} retXml = {}",orderNumber, retXml);
        return (SendRedPackResult)WxUtil.fromXML(retXml, SendRedPackResult.class);
    }


}
