package com.doooly.business.pay.processor.productprocessor;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.ofpay.service.OfPayService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.utils.AESTool;
import com.doooly.business.product.service.ProductCouponService;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdSelfProductCoupon;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 自营卡券
 * Created by john on 18/4/16.
 */
@Component
public class SelfCardPswProcessor implements ProductProcessor {

    protected Logger logger = LoggerFactory.getLogger(SelfCardPswProcessor.class);
    private final String CP_AES_KEY = PropertiesConstants.dooolyBundle.getString("cp_aes_key");

    @Autowired
    ProductCouponService productCouponService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AdUserServiceI adUserServiceI;

    @Override
    public int getProcessCode() {
        return OrderService.ProductType.SELF_CARDPSW_RECHARGE.getCode();
    }

    @Override
    public PayMsg process(OrderVo order) {
        logger.info("SelfCardPswProcessor  orderNum = {}", order.getOrderNumber());
        boolean bool = true;
        OrderItemVo item  = order.getItems().get(0);
        String str[] = item.getProductSkuId().split("-");
        AdSelfProductCoupon coupon = productCouponService.getProductCoupon(String.valueOf(order.getId()), order.getUserId(), str[1]);
        if(coupon != null){
            //logger.info("SelfCardPswProcessor  coupon = {}", coupon);
            Map<String, String> retMap = new HashMap<String,String>();
            String cardno = coupon.getCouponCardNumber();
            String cardpws = coupon.getCouponCardPassword();
            String retcode = "1";
            retMap.put("retcode","1");
            retMap.put("cardid",order.getOrderNumber());
            retMap.put("cardpws",cardpws);
            retMap.put("cardno",cardno);
            // 保存充值结果
            int rows = updateOrderItemSuccesss(item, retMap);
            logger.info("updateOrderItem  rows = {}",  rows);
            // ================发送卡号和密码短信息==================
            String orderDetail = item.getGoods() + item.getSku();
            if (!StringUtils.isEmpty(cardno) && !StringUtils.isEmpty(cardpws)) {
                String mobiles = order.getConsigneeMobile();
                String alidayuSmsCode = "SMS_124330065";
                JSONObject paramSMSJSON = new JSONObject();
                paramSMSJSON.put("product", orderDetail);
                //paramSMSJSON.put("cardNo", cardno);
                //paramSMSJSON.put("cardPsw", cardpws);
                int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
                logger.info("发送卡密短信. i = {}", i);
            } else {
                logger.info("发送短信失败.未获得卡密.");
            }
            // =================微信消息推送=======================
            JSONObject data = new JSONObject();
            data.put("userId", order.getUserId());
            data.put("orderId", order.getId());
            data.put("goods", item.getGoods());
            data.put("sku", item.getSku());
            data.put("cardNo", cardno);
            data.put("cardPsw", cardpws);
            redisTemplate.convertAndSend("CARDPSW_CHANNEL", data.toString());

        }else{
            logger.error("活动自营卡券失败. coupon = {}",coupon);
            Map<String, String> retMap = new HashMap<String,String>();
            retMap.put("retcode","9");
            retMap.put("err_msg","自营卡券失败,未取到卡券");
            int rows = updateOrderItemSuccesss(item, retMap);
            logger.info("updateOrderItem  rows = {}",  rows);
        }
        // 如果卡密获取失败发送以下短信
        //【兜礼】尊敬的用户，您本次的话费充值/流量充值/都市旅游卡充值失败，积分会在两个工作日内退回，微信支付的退款事宜请联系兜礼客服热线4001582212咨询！
        if(!bool){
            AdUser user = adUserServiceI.getById(order.getUserId().intValue());
            String mobiles = user.getTelephone();
            String alidayuSmsCode = "SMS_125955124";
            JSONObject paramSMSJSON = new JSONObject();
            paramSMSJSON.put("productType", OrderService.ProductType.getProductTypeName(order.getProductType()));
            paramSMSJSON.put("phone", "4001582212");
            int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
            logger.info("发送退款审批短信. i = {}", i);
        }
        return null;
    }

    /***
     *  保存充值结果
     * @param order
     * @param retMap
     * @return
     */
    private int updateOrderItemSuccesss(OrderItemVo oldItem, Map<String, String> retMap) {
        try {
            String retcode = retMap.get("retcode");
            String game_state = retMap.get("game_state");
            String msg = "";
            if(OF_SUCCESS_CODE.equals(retcode)){
                msg = "卡密获取成功.";
                game_state = OF_SUCCESS_CODE;
            }else{
                game_state = "-1";
                msg = retMap.get("err_msg");
            }
            OrderItemVo newItem = new OrderItemVo();
            newItem.setId(oldItem.getId());
            newItem.setRetCode(retMap.get("retcode"));
            newItem.setRetMsg(msg);
            newItem.setRetState(game_state);
            newItem.setCardOid(retMap.get("cardid"));
            String cardpws = retMap.get("cardpws");
            if(null != cardpws){
                //卡密需要加密
                newItem.setCardPass(AESTool.Encrypt(cardpws, CP_AES_KEY));
            }
            newItem.setCardCode(retMap.get("cardno"));
            logger.info("item = {}",newItem);
            return orderService.updateOrderItem(newItem);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("updateOrderItemSuccesss e = {}", e);
        }
        return 0;
    }
}
