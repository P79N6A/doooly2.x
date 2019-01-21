package com.doooly.business.ele.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.business.AdBusinessServiceI;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.order.service.AdOrderReportServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.common.elm.ELMConstants;
import com.doooly.common.elm.OrderTypeEnum;
import com.doooly.common.elm.SignUtils;
import com.doooly.dao.reachad.AdOrderDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.doooly.common.elm.ELMConstants.ELM_ORDER_PREFIX;
import static com.doooly.common.token.TokenUtil.TOKEN_EXPIRE;

/**
 * @Description: 饿了么实现
 * @author: qing.zhang
 * @date: 2019-01-03
 */
@Service
public class ELMServiceImpl implements ELMServiceI{

    private Logger logger = LoggerFactory.getLogger(ELMServiceImpl.class);

    @Autowired
    private ConfigDictServiceI configDictServiceI;
    @Autowired
    private AdBusinessServiceI adBusinessServiceI;
    @Autowired
    private NewPaymentServiceI paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AdOrderReportServiceI adOrderReportServiceI;
    @Autowired
    private AdOrderDetailDao detailDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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
        String orderNo = obj.getString("orderNo");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE, ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        Boolean flag = SignUtils.validParam(consumerNo,timeStamp,sign,obj,eleConfig);
        if(!flag){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }else {
            //验证成功将订单信息放入缓存
            stringRedisTemplate.opsForValue().set(String.format(ELMConstants.ELM_ORDER_PREFIX,orderNo), obj.toJSONString());
            return ResultModel.success_ok("获取订单信息成功");
        }
    }

    /**
     * 饿了么 状态推送
     * @param obj
     * @param httpServletRequest
     * @return
     */
    @Override
    public ResultModel orderStatusPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        String consumerNo = httpServletRequest.getHeader("consumerNo");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE, ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        String elmConsumerSecret = eleConfig.getString("consumerSecret");
        Boolean flag = SignUtils.validParam(consumerNo,timeStamp,sign,obj,eleConfig);
        if(!flag){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }
        String orderNo = obj.getString("orderNo");
        Integer status = obj.getInteger("status");
        String remark = obj.getString("remark");
        //查询doooly订单
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNo);
        OrderVo o = adOrderReportServiceI.getOrderLimt(order);
        if(o==null){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }
        //修改doooly订单状态
/*        if(OrderTypeEnum.OrderTypeEnum10.getCode()==status){
            //订单取消
            orderService.cancleOrder(o.getUserId(), orderNo);
        }else {
            //更新
        }*/
        OrderItemVo newItem = new OrderItemVo();
        newItem.setOrderReportId(order.getId());
        newItem.setRetCode(String.valueOf(status));
        newItem.setRetMsg(remark);
        newItem.setRetState(OrderTypeEnum.getOrderTypeByCode(status));
        logger.info("update elm order status, item = {}",newItem);
        orderService.updateOrderItem(newItem);
        return ResultModel.success_ok("收到状态");
    }
}
