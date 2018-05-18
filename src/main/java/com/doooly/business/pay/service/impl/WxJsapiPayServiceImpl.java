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

@Service
public class WxJsapiPayServiceImpl extends AbstractPaymentService {
	// 微信成功状态
	public static final String WX_RET_CODE_SUCCESS = "SUCCESS";
	// 微信JSAPI支付参数
	public final static String WXJS_APPID = PropertiesHolder.getProperty("wxjs.appid");
	public final static String WXJS_MCH_ID = PropertiesHolder.getProperty("wxjs.mch_id");
	public final static String WXJS_TRADE_TYPE = PropertiesHolder.getProperty("wxjs.trade_type");
	public final static String WXJS_KEY = PropertiesHolder.getProperty("wxjs.key");
	public final static String WXJS_UNIFIEDORDER_URL = PropertiesHolder.getProperty("wxjs.unifiedorder_url");
	public final static String WXJS_NOTIFY_URL = PropertiesHolder.getProperty("wxjs.notify_url");
	public final static String WXJS_ORDERQUERY_URL = PropertiesHolder.getProperty("wxjs.orderquery_url");

	@Override
	public String getPayType() {
		return PayFlowService.PAYTYPE_WEIXIN_JSAPI;
	}
	
	@Override
	protected PayMsg buildPayParams(List<OrderVo> orders, PayFlow flow, JSONObject json) {
		// 生成预支付交易单
		String goodsDesc = super.getGoodsDesc(orders);
		BigDecimal payAmount = super.getTotalAmount(orders);
		String orderNum = json.getString("orderNum");
		String clientIp = json.getString("clientIp");
		String openid = json.getString("openid");
		WxPrePayParams payParams = prePayParams(orderNum, payAmount, goodsDesc, clientIp, openid, flow);
		WxPrePayResult retVo = unifiedOrder(payParams);
		if (retVo == null) {
			return new PayMsg(PayMsg.failure_code, "unifiedOrder failed. retVo is null");
		}
		if (!WX_RET_CODE_SUCCESS.equals(retVo.getReturn_code()) || StringUtils.isEmpty(retVo.getPrepay_id())) {
			return new PayMsg(PayMsg.failure_code, "unifiedOrder failed. return_msg = " + retVo.getReturn_msg()+ ",prepayid = " + retVo.getPrepay_id());
		}
		// 成功 - 返回支付参数
		SortedMap<String, Object> signMap = getPayForm(retVo);
		return new PayMsg(PayMsg.success_code, PayMsg.success_mess, signMap);
	}

	/**
	 * 微信支付预付参数
	 * 
	 * @param order
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public WxPrePayParams prePayParams(String orderNum, BigDecimal totalAmount, String goodsDesc, String clientIp,
			String openid, PayFlow payFlow) {
		String fee = String.valueOf(totalAmount.multiply(new BigDecimal("100")).intValue());
		WxPrePayParams data = new WxPrePayParams();
		String trade_no = orderNum + "-" + payFlow.getId() + "-" + payFlow.getPayCount();
		// 应用ID
		data.setAppid(WXJS_APPID);
		// 商户号
		data.setMch_id(WXJS_MCH_ID);
		// 随机字符串
		data.setNonce_str(WxUtil.getNonceStr());
		// 商品描述
		data.setBody(goodsDesc);
		// 商品详情
		// data.setDetail(goodsDesc);
		// 携带自定义数据
		data.setAttach(trade_no);
		// 商户订单号(订单号-流水ID-交易次数)
		data.setOut_trade_no(trade_no);
		// 总金额
		data.setTotal_fee(fee);
		// 终端IP
		data.setSpbill_create_ip(clientIp);
		// 通知地址
		data.setNotify_url(WXJS_NOTIFY_URL);
		// 交易类型
		data.setTrade_type(WXJS_TRADE_TYPE);
		// openid (trade_type=JSAPI时（即公众号支付），此参数必传)
		data.setOpenid(openid);
		return data;
	}

	/**
	 * 微信统一下单
	 * 
	 * @param order
	 * @return
	 */
	private WxPrePayResult unifiedOrder(WxPrePayParams data) {
		try {
			logger.info("unifiedOrder() data = {}", data);
			SortedMap<String, Object> parameters = new TreeMap<String, Object>();
			parameters.put("appid", data.getAppid());
			parameters.put("mch_id", data.getMch_id());
			// parameters.put("device_info", data.getDevice_info());
			parameters.put("nonce_str", data.getNonce_str());
			parameters.put("body", data.getBody());
			parameters.put("attach", data.getAttach());
			parameters.put("out_trade_no", data.getOut_trade_no());
			parameters.put("total_fee", data.getTotal_fee());
			parameters.put("spbill_create_ip", data.getSpbill_create_ip());
			parameters.put("notify_url", data.getNotify_url());
			parameters.put("trade_type", data.getTrade_type());
			parameters.put("openid", data.getOpenid());
			String sign = WxUtil.createSign(parameters, WXJS_KEY);
			data.setSign(sign);
			// 生成微信预付单
			String prePayXml = WxUtil.toXml(data, WxPrePayParams.class);
			logger.info("unifiedOrder() xml = {}", prePayXml);
			String retXml = HttpClientUtil.httpsRequest(WXJS_UNIFIEDORDER_URL, prePayXml);
			logger.info("unifiedOrder() returnXml = {}", retXml);
			// 解析微信返回结果
			return (WxPrePayResult) WxUtil.fromXML(retXml, WxPrePayResult.class);
		} catch (Exception e) {
			logger.error("unifiedOrder() exception = {}", e);
		}
		return null;
	}

	/**
	 * 微信支付参数
	 * 
	 * @param retVo
	 * @return
	 */
	private SortedMap<String, Object> getPayForm(WxPrePayResult retVo) {
		SortedMap<String, Object> signMap = new TreeMap<String, Object>();
		signMap.put("appId", retVo.getAppid());
		signMap.put("timeStamp", WxUtil.getTimeStamp());
		signMap.put("nonceStr", WxUtil.getNonceStr());
		signMap.put("package", "prepay_id=" + retVo.getPrepay_id());
		signMap.put("signType", "MD5");
		String paySign = WxUtil.createSign(signMap, WXJS_KEY);
		signMap.put("paySign", paySign);
		logger.info("signMap = {}", signMap);
		return signMap;
	}

	/**
	 * 解析返回结果并验证
	 * 
	 * @param retStr
	 * @param payType
	 * @return
	 */
	@Override
	protected Map<String, Object> resolveAndVerifyResult(String retStr, String payType,String channel) {
		try {
			WxQueryResult result = (WxQueryResult) WxUtil.fromXML(retStr, WxQueryResult.class);
			// 验证签名结果
			SortedMap<String, Object> sortMap = new TreeMap<String, Object>(BeanUtils.describe(result));
			boolean bool = WxUtil.verify(sortMap, result.getSign(), WXJS_KEY);
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

	/**
	 * 调用支付平台API查询支付结果
	 */
	@Override
	protected PayMsg queryPayResult(PayFlow flow) {
		try {
			WxQueryParams params = buildQueryParams(flow);
			String retXml = orderQuery(params);
			WxQueryResult result = (WxQueryResult) WxUtil.fromXML(retXml, WxQueryResult.class);
			//校验查询结果
			SortedMap<String, Object> sortMap = new TreeMap<String, Object>(BeanUtils.describe(result));
			boolean bool = WxUtil.verify(sortMap, result.getSign(), WXJS_KEY);
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
			return super.handlePayResult(retXml,flow.getChannel());
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
	private String orderQuery(WxQueryParams data) {
		try {
			// logger.info("unifiedOrder() data = {}", data);
			SortedMap<String, Object> params = new TreeMap<String, Object>();
			params.put("appid", data.getAppid());
			params.put("mch_id", data.getMch_id());
			if (!StringUtils.isEmpty(data.getTransaction_id())) {
				params.put("transaction_id", data.getTransaction_id());
			} else {
				params.put("out_trade_no", data.getOut_trade_no());
			}
			params.put("nonce_str", data.getNonce_str());
			String sign = WxUtil.createSign(params, WXJS_KEY);
			data.setSign(sign);
			String xmlParas = WxUtil.toXml(data, WxQueryParams.class);
			logger.info("orderQuery() xmlParas = {}", xmlParas);
			String retXml = HttpClientUtil.httpsRequest(WXJS_ORDERQUERY_URL, xmlParas);
			logger.info("orderQuery() retXml = {}", retXml);
			return retXml;
		} catch (Exception e) {
			logger.error("orderQuery() exception = {}", e);
		}
		return null;
	}
	
	private WxQueryParams buildQueryParams(PayFlow flow) {
		WxQueryParams params = new WxQueryParams();
		params.setAppid(WXJS_APPID);
		params.setMch_id(WXJS_MCH_ID);
		if(!StringUtils.isEmpty(flow.getTransNo())) {
			params.setTransaction_id(flow.getTransNo());
		}else{
			params.setOut_trade_no(flow.getOrderNumber() + "-" + flow.getId() + "-" + flow.getPayCount());
		}
		params.setNonce_str(WxUtil.getNonceStr());
		return params;
	}
}
