package com.doooly.business.pay.processor.productprocessor;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.nexus.impl.NexusSerivceImpl;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.ProductType;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.service.RefundService;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/***
 * 集享积分交易
 *
 * @author 2017-10-11 17:34:21 WANG
 *
 */
@Component
public class NexusProcessor implements ProductProcessor{

    protected Logger logger = LoggerFactory.getLogger(NexusProcessor.class);

    @Autowired
    private RefundService refundService;
    @Autowired
    private AdUserServiceI adUserServiceI;
    @Autowired
    private OrderService orderService;
    @Autowired
    private NexusSerivceImpl nexusSerivceImpl;

    @Override
    public int getProcessCode() {
        return ProductType.NEXUS_RECHARGE.getCode();
    }

    @Override
    public PayMsg process(OrderVo order) {
        logger.error("NexusProcessor start.");
        if (order != null && order.getItems() != null) {
            //订单充值
            OrderItemVo oldItem = order.getItems().get(0);
            AdUser user = adUserServiceI.getById(String.valueOf(order.getUserId()));
            MessageDataBean msg = nexusSerivceImpl.consume(order, user);
            //保存充值结果
            logger.info("NexusProcessor msg={}",msg.toJsonString());
            JSONObject retMap = new JSONObject();
            if(msg != null && MessageDataBean.success_code.equals(msg.getCode())){
                retMap.put("result","1");
                retMap.put("message", msg.getMess() + ";" + msg.data.get("tranOutSeq"));
                retMap.put("result","1");
                retMap.put("orderid",oldItem.getCardOid());
            }else{
                retMap.put("result","0");
                retMap.put("message",msg.getMess());
                retMap.put("result","0");
                retMap.put("orderid", oldItem.getCardOid());
            }
            updateOrderItemSuccesss(oldItem, retMap);
            //是否需要退款
            logger.info("consume() msg = {}", msg);
            if (msg != null && NexusSerivceImpl.NEED_REFUND.equals(msg.getCode())) {
                PayMsg refMsg = refundService.autoRefund(order.getUserId(), order.getOrderNumber());
                // 如果退款失败发送以下短信
                //【兜礼】尊敬的用户，您本次的话费充值/流量充值/都市旅游卡充值失败，积分会在两个工作日内退回，微信支付的退款事宜请联系兜礼客服热线4001582212咨询！
                if (!PayMsg.success_code.equals(refMsg.getCode())) {
                    String mobiles = user.getTelephone();
                    String alidayuSmsCode = "SMS_125955124";
                    JSONObject paramSMSJSON = new JSONObject();
                    paramSMSJSON.put("productType", ProductType.getProductTypeName(order.getProductType()));
                    paramSMSJSON.put("phone", "4001582212");
                    int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
                    logger.info("发送退款审批短信. i = {}", i);
                }
            }
        } else {
            logger.error("NexusProcessor failed. order = {}", order);
        }
        return null;
    }

    /***
     *  保存充值结果
     * @param order
     * @param retMap
     * @return
     */
    private int updateOrderItemSuccesss(OrderItemVo oldItem, JSONObject retMap) {
        try {
            String retcode = "-1";
            String game_state = "-1";
            String msg = "失败.";
            String cardid = "";
            if (retMap != null) {
                retcode = retMap.getString("result");
                msg = retMap.getString("message");
                cardid = retMap.getString("orderid");
                game_state = retMap.getString("result");
            }
            OrderItemVo newItem = new OrderItemVo();
            newItem.setId(oldItem.getId());
            newItem.setRetCode(retcode);
            newItem.setRetMsg(msg);
            newItem.setRetState(game_state);
            newItem.setCardOid(cardid);
            newItem.setUpdateBy("NexusProcessor");
            newItem.setUpdateDate(new Date());
            return orderService.updateOrderItem(newItem);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("NexusProcessor e = {}", e);
        }
        return 0;
    }

}
