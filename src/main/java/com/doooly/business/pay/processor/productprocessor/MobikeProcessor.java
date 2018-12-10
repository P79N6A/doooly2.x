package com.doooly.business.pay.processor.productprocessor;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.exwings.ExWingsService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.ProductType;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.service.RefundService;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/***
 * 摩拜充值
 *
 * @author 2017-10-11 17:34:21 WANG
 *
 */
@Component
public class MobikeProcessor implements ProductProcessor{

    protected Logger logger = LoggerFactory.getLogger(MobikeProcessor.class);

    @Autowired
    private ExWingsService exWingsService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private AdUserServiceI adUserServiceI;
    @Autowired
    private OrderService orderService;

    @Override
    public int getProcessCode() {
        return ProductType.MOBIKE_RECHARGE.getCode();
    }

    @Override
    public PayMsg process(OrderVo order) {
        logger.error("MobikeProcessor start.");
        if (order != null && order.getItems() != null) {
            //订单充值
            OrderItemVo oldItem = order.getItems().get(0);
            //退款
            PayMsg payMsg = refundService.autoRefund(order.getUserId(), order.getOrderNumber());
            // 如果退款失败发送以下短信
            //【兜礼】尊敬的用户，您本次的话费充值/流量充值/都市旅游卡充值失败，积分会在两个工作日内退回，微信支付的退款事宜请联系兜礼客服热线4001582212咨询！
            if (!PayMsg.success_code.equals(payMsg.getCode())) {
                AdUser user = adUserServiceI.getById(order.getUserId().intValue());
                String mobiles = user.getTelephone();
                String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.recharge_fail_template_code;
                JSONObject paramSMSJSON = new JSONObject();
                paramSMSJSON.put("productType", OrderService.ProductType.getProductTypeName(order.getProductType()));
                paramSMSJSON.put("phone", "4001582212");
                int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
                logger.info("发送退款审批短信. i = {}", i);
            }
            /*if (!ExWingsService.MOBIKE_SUCCESS_CODE.equals(oldItem.getRetState())) {
                JSONObject json = exWingsService.recharge(order);
                //保存充值结果
                updateOrderItemSuccesss(oldItem, json);
                //充值失败调用退款接口
                if (json != null && !ExWingsService.MOBIKE_SUCCESS_CODE.equals(json.getString("result"))) {
                    //0	    请求成功
                    //100	请求失败，账户余额不足
                    //200	非法请求，签名错误
                    //250	未登录系统
                    //300	非法请求，ip地址错误

                    //313	用户已领过骑行券
                    //400	非法的请求参数，具体错误信息参见返回的message内容
                    //500	系统维护，请求拒绝
                    //退款
                    PayMsg payMsg = refundService.autoRefund(order.getUserId(), order.getOrderNumber());
                    // 如果退款失败发送以下短信
                    //【兜礼】尊敬的用户，您本次的话费充值/流量充值/都市旅游卡充值失败，积分会在两个工作日内退回，微信支付的退款事宜请联系兜礼客服热线4001582212咨询！
                    if (!PayMsg.success_code.equals(payMsg.getCode())) {
                        AdUser user = adUserServiceI.getById(order.getUserId().intValue());
                        String mobiles = user.getTelephone();
                        String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.recharge_fail_template_code;
                        JSONObject paramSMSJSON = new JSONObject();
                        paramSMSJSON.put("productType", OrderService.ProductType.getProductTypeName(order.getProductType()));
                        paramSMSJSON.put("phone", "4001582212");
                        int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
                        logger.info("发送退款审批短信. i = {}", i);
                    }
                }
            }*/
        } else {
            logger.error("MobikeProcessor failed. order = {}", order);
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
            newItem.setUpdateBy("MobikeProcessor");
            newItem.setUpdateDate(new Date());
            return orderService.updateOrderItem(newItem);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("MobikeProcessor e = {}", e);
        }
        return 0;
    }

}
