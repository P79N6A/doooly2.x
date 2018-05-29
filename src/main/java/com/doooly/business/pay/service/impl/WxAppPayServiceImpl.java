package com.doooly.business.pay.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.bean.WxPrePayParams;
import com.doooly.business.pay.bean.WxPrePayResult;
import com.doooly.business.pay.bean.WxQueryParams;
import com.doooly.business.pay.bean.WxQueryResult;
import com.doooly.business.pay.service.AbstractPaymentService;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.pay.utils.WxUtil;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dto.common.PayMsg;

/**
 * 微信APP支付
 * 
 * @author 2017-10-17 13:43:45 WANG
 *
 */
@Service
public class WxAppPayServiceImpl extends AbstractPaymentService {

	// 微信成功状态
	public static final String WX_RET_CODE_SUCCESS = "SUCCESS";

	
	@Override
	public String getPayType() {
		return PayFlowService.PAYTYPE_WEIXIN;
	}

	@Override
	protected PayMsg buildPayParams(List<OrderVo> orders, PayFlow flow, JSONObject json) {
		// 生成预支付交易单
		String goodsDesc = super.getGoodsDesc(orders);
		BigDecimal payAmount = super.getTotalAmount(orders);
		String orderNum = json.getString("orderNum");
		String clientIp = json.getString("clientIp");
		String channel = flow.getChannel();
		WxPrePayParams payParams = prePayParams(orderNum, payAmount, goodsDesc, clientIp, flow);
		WxPrePayResult retVo = unifiedOrder(payParams,channel);
		if (retVo == null) {
			return new PayMsg(PayMsg.failure_code, "unifiedOrder failed. retVo is null");
		}
		if (!WX_RET_CODE_SUCCESS.equals(retVo.getReturn_code()) || StringUtils.isEmpty(retVo.getPrepay_id())) {
			return new PayMsg(PayMsg.failure_code, "unifiedOrder failed. return_msg = " + retVo.getReturn_msg()
					+ ",prepayid = " + retVo.getPrepay_id());
		}
		// 成功 - 返回支付参数
		SortedMap<String, Object> signMap = getPayForm(retVo,channel);
		return new PayMsg(PayMsg.success_code, PayMsg.success_mess, signMap);
	}

	@Override
	protected Map<String, Object> resolveAndVerifyResult(String retStr, String payType, String channel) {
		try {
			WxQueryResult result = (WxQueryResult) WxUtil.fromXML(retStr, WxQueryResult.class);
			// 验证签名结果
			SortedMap<String, Object> sortMap = new TreeMap<String, Object>(BeanUtils.describe(result));
			boolean bool = WxUtil.verify(sortMap, result.getSign(), getPropByChannel(channel,"key"));
			if (!bool) {
				logger.error("签名错误.result= {}", result);
				return null;
			}
			// 验证微信返回结果
			String return_code = result.getResult_code();
			if (!WX_RET_CODE_SUCCESS.equals(return_code)) {
				logger.error("支付失败.msg = {},code = {}", result.getReturn_msg(), result.getErr_code_des());
				return null;
			}
			// 验证微信处理结果
			String result_code = result.getResult_code();
			if (!WX_RET_CODE_SUCCESS.equals(result_code)) {
				logger.error("支付平台处理失败.errCode={},errCodeDes={}", result.getErr_code(), result.getErr_code_des());
				return null;
			}
			String[] strs = result.getAttach().split("-");
			String orderNum = strs[0];
			String payFlowId = strs[1];
			List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
			int order_fee = orderService.getPayAmount(orders).multiply(new BigDecimal("100")).intValue();
			int total_fee = Integer.valueOf(result.getTotal_fee());
			// 验证支付金额与订单金额是否一致
			if (order_fee != total_fee) {
				logger.error("金额验证失败.order_fee={},total_fee={}", order_fee, total_fee);
				return null;
			}
			// 返回解析结果数据
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderNum", orderNum);
			map.put("payFlowId", payFlowId);
			map.put("orders", orders);
			map.put("transNo", result.getTransaction_id());
			return map;
		} catch (Exception e) {
			logger.error("resolveAndVerifyResult(). payType = {} , e = {}", payType, e);
		}
		return null;
	}

	@Override
	protected PayMsg queryPayResult(PayFlow flow) {
		try {
			String channel = flow.getChannel();
			WxQueryParams params = buildQueryParams(flow);
			String retXml = orderQuery(params,flow.getChannel());
			WxQueryResult result = (WxQueryResult) WxUtil.fromXML(retXml, WxQueryResult.class);
			//校验查询结果
			SortedMap<String, Object> sortMap = new TreeMap<String, Object>(BeanUtils.describe(result));
			boolean bool = WxUtil.verify(sortMap, result.getSign(), getPropByChannel(channel,"key"));
			if (!bool) {
				return new PayMsg(PayMsg.failure_code, "签名错误.");
			}
			if (!WX_RET_CODE_SUCCESS.equals(result.getReturn_code())) {
				return new PayMsg(PayMsg.failure_code, "支付平台处理返回结果失败.return_code=" + result.getReturn_code() + ",return_code=" + result.getReturn_msg());
			}
			if (!WX_RET_CODE_SUCCESS.equals(result.getResult_code())) {
				return new PayMsg(PayMsg.failure_code, "支付平台处理结果失败.result_code="+ result.getResult_code() + ",err_code_des=" + result.getErr_code_des());
			}
			if (!WX_RET_CODE_SUCCESS.equals(result.getTrade_state())) {
				return new PayMsg(PayMsg.failure_code, "支付平台处理订单状态失败.");
			}
			//处理查询结果
			return super.handlePayResult(retXml,channel);
		} catch (Exception e) {
			logger.error("queryPayResult() e = {}",  e);
		}
		return new PayMsg(PayMsg.failure_code, "发生异常");
	
	}
	/**
	 * 查询微信支付结果
	 * 
	 * @param order
	 * @return
	 */
	private String orderQuery(WxQueryParams data,String channel) {
		try {
			// logger.info("unifiedOrder() data = {}", data);
			SortedMap<String, Object> params = new TreeMap<String, Object>();
			params.put("appid", data.getAppid());
			params.put("mch_id", data.getMch_id());
			if(!StringUtils.isEmpty(data.getTransaction_id())){
				params.put("transaction_id", data.getTransaction_id());
			}else{
				params.put("out_trade_no", data.getOut_trade_no());
			}
			params.put("nonce_str", data.getNonce_str());
			String sign = WxUtil.createSign(params, getPropByChannel(channel,"key"));
			data.setSign(sign);
			String xmlParas = WxUtil.toXml(data, WxQueryParams.class);
			logger.info("orderQuery() xmlParas = {}", xmlParas);
			String retXml = HttpClientUtil.httpsRequest(getPropByChannel(channel,"orderquery_url"), xmlParas);
			logger.info("orderQuery() retXml = {}", retXml);
			return retXml;
		} catch (Exception e) {
			logger.error("orderQuery() exception = {}", e);
		}
		return null;
	}
	
	private WxQueryParams buildQueryParams(PayFlow flow) {
		WxQueryParams params = new WxQueryParams();
		params.setAppid(getPropByChannel(flow.getChannel(),"appid"));
		params.setMch_id(getPropByChannel(flow.getChannel(),"mch_id"));
		if(!StringUtils.isEmpty(flow.getTransNo())) {
			params.setTransaction_id(flow.getTransNo());
		}else{
			params.setOut_trade_no(flow.getOrderNumber() + "-" + flow.getId() + "-" + flow.getPayCount());
		}
		params.setNonce_str(WxUtil.getNonceStr());
		return params;
	}
	
	/**
	 * 微信支付参数
	 * 
	 * @param retVo
	 * @return
	 */
	private SortedMap<String, Object> getPayForm(WxPrePayResult retVo,String channel) {
		SortedMap<String,Object> signMap = new TreeMap<String,Object>();
		signMap.put("appid", retVo.getAppid());
		signMap.put("partnerid", retVo.getMch_id());
		signMap.put("prepayid", retVo.getPrepay_id());
		signMap.put("package", "Sign=WXPay");
		signMap.put("noncestr", WxUtil.getNonceStr());
		signMap.put("timestamp", WxUtil.getTimeStamp());
		String paySign = WxUtil.createSign(signMap, getPropByChannel(channel,"key"));
		signMap.put("sign", paySign);
		logger.info("signMap = {}",signMap);
		return signMap;
	}
	
	/**
	 * 微信支付预付参数
	 * 
	 * @param order
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private WxPrePayParams prePayParams(String orderNum,BigDecimal totalAmount,String goodsDesc,String clientIp,PayFlow payFlow)  {
		String fee = String.valueOf(totalAmount.multiply(new BigDecimal("100")).intValue());
		WxPrePayParams data = new WxPrePayParams();
		String trade_no = orderNum + "-" + payFlow.getId() + "-" + payFlow.getPayCount();
		//应用ID
		data.setAppid(getPropByChannel(payFlow.getChannel(),"appid"));
		//商户号
		data.setMch_id(getPropByChannel(payFlow.getChannel(),"mch_id"));
		//随机字符串	
		data.setNonce_str(WxUtil.getNonceStr());
		//商品描述
		data.setBody(goodsDesc);
		//商品详情
		//data.setDetail(goodsDesc);
		//携带自定义数据
		data.setAttach(trade_no);
		//商户订单号(订单号-流水ID-交易次数)
		data.setOut_trade_no(trade_no);
		//总金额
		data.setTotal_fee(fee);
		//终端IP
		data.setSpbill_create_ip(clientIp);
		//通知地址
		data.setNotify_url(getPropByChannel(payFlow.getChannel(),"notify_url"));
		//交易类型
		data.setTrade_type(getPropByChannel(payFlow.getChannel(),"trade_type"));
		return data;
	}	
	
	/**
	 * 微信统一下单
	 * @param order
	 * @return
	 */
	private WxPrePayResult unifiedOrder(WxPrePayParams data,String channel) {
		try {
			logger.info("unifiedOrder() data = {}", data);
			SortedMap<String, Object> parameters = new TreeMap<String, Object>();
			parameters.put("appid", data.getAppid());
			parameters.put("mch_id", data.getMch_id());
			//parameters.put("device_info", data.getDevice_info());
			parameters.put("nonce_str", data.getNonce_str());
			parameters.put("body", data.getBody());
			parameters.put("attach", data.getAttach());
			parameters.put("out_trade_no", data.getOut_trade_no());
			parameters.put("total_fee", data.getTotal_fee());
			parameters.put("spbill_create_ip", data.getSpbill_create_ip());
			parameters.put("notify_url", data.getNotify_url());
			parameters.put("trade_type", data.getTrade_type());
			//parameters.put("detail", data.getDetail());
			String sign = WxUtil.createSign(parameters, getPropByChannel(channel,"key"));
			data.setSign(sign);
			//生成微信预付单
			String prePayXml = WxUtil.toXml(data,WxPrePayParams.class);
			logger.info("unifiedOrder() xml = {}", prePayXml);
			String retXml = HttpClientUtil.httpsRequest(getPropByChannel(channel,"unifiedorder_url"), prePayXml);
			logger.info("unifiedOrder() returnXml = {}", retXml);
			//解析微信返回结果
			return (WxPrePayResult) WxUtil.fromXML(retXml, WxPrePayResult.class);
		} catch (Exception e) {
			logger.error("unifiedOrder() exception = {}", e);
		}
		return null;
	}

	public String getPropByChannel(String channel,String key){
		String prefix = "wx";
		if ("wiscoapp".equals(channel)) {
			//武钢app配置
			prefix = "wiscoapp";
		} else {
			//微信app配置
			prefix = "wx";
		}
		return PropertiesHolder.getProperty(prefix + "." + key);
	}

	
}
