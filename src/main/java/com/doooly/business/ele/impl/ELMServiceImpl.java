package com.doooly.business.ele.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.business.AdBusinessServiceI;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.order.service.AdOrderReportServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.utils.AESTool;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.impl.NewPaymentService;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.MD5Util;
import com.doooly.common.elm.ELMConstants;
import com.doooly.common.elm.OrderTypeEnum;
import com.doooly.common.elm.SignUtils;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.reachad.AdOrderDetailDao;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        Boolean flag = SignUtils.validParam(consumerNo,timeStamp,sign,obj,eleConfig);
        if(!flag){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }else {
            return ResultModel.success_ok("获取订单金额成功");
        }
       /* //创建doooly订单
        String orderNo = obj.getString("orderNo");
        JSONObject orderGetParam = new JSONObject();
        orderGetParam.put("orderNo",orderNo);
        String orderParam = Hex.encodeHexString(orderGetParam.toJSONString().getBytes());
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpClientUtil.HEADER_CONTENT_TYPE, HttpClientUtil.HEADER_CONTENT_TYPE_JSON);
        headerMap.put("consumerNo",consumerNo);
        String timeNow = String.valueOf(DateUtils.getNowTime());
        headerMap.put("timeStamp", timeNow);
        headerMap.put("sign", MD5Util.MD5Encode(orderParam + elmConsumerSecret + timeNow, ELMConstants.CHARSET));
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("org",orderParam);
        String result = HttpClientUtil.doPost(ELMConstants.ELM_URL + ELMConstants.QUERY_ORDER, headerMap, paramMap);
        JSONObject jsonResult = JSONObject.parseObject(result);
        Integer code = jsonResult.getInteger("code");
        if(code == GlobalResultStatusEnum.SUCCESS_OK.getCode()){
            JSONObject data = jsonResult.getJSONObject("data");
            //查询订单成功下支付单
            AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
            paramAdBusinessExpandInfo.setShopId(consumerNo);
            AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
            data.put("businessId",adBusinessExpandInfo.getId());
            return paymentService.unifiedElmorder(data);
        }else {
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }*/
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
