package com.doooly.business.myorder.impl;

import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.myorder.constant.OrderType;
import com.doooly.business.myorder.constant.ProductType;
import com.doooly.business.myorder.dto.*;
import com.doooly.business.myorder.po.OrderDetailPoReq;
import com.doooly.business.myorder.po.OrderDetailReport;
import com.doooly.business.myorder.po.OrderPoReq;
import com.doooly.business.myorder.po.OrderPoResp;
import com.doooly.business.myorder.service.OrderService;
import com.doooly.business.pay.bean.AdOrderFlow;
import com.doooly.business.pay.utils.AESTool;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.dao.reachad.*;
import com.doooly.entity.reachad.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 我的订单
 * @author: xianpeng.hua
 * @date: 2018-11-09
 */
@Service("OrderServiceImpl")
public class OrderServiceImpl implements OrderService{
	
	private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	private final static String CP_AES_KEY = PropertiesConstants.dooolyBundle.getString("cp_aes_key");
	
	private final static int LATEST_ORDER_DAY =  45;
	
	 @Autowired
	 private AdOrderReportDao adOrderReportDao;
	 
	 @Autowired
    private AdOrderFlowDao adOrderFlowDao;
    @Autowired
    private AdOrderDetailDao adOrderDetailDao;
    @Autowired
    private AdUserDao adUserDao;
    
    @Autowired
    private AdGroupDao adGroupDao;
    
    @Autowired
    private ConfigDictServiceI configDictServiceI;
    
    @Autowired
	protected StringRedisTemplate redisTemplate;
    @Autowired
    private AdReturnFlowDao adReturnFlowDao;
	@Autowired
	private AdAvailablePointsDao adAvailablePointsDao;

    @Autowired
    private AdOrderIntegralRecordDao adOrderIntegralRecordDao;

	/**
	 * 订单列表
	 * @param orderReq
	 * @return
	 */
	@Override
	public List<OrderPoResp> getOrderList(OrderReq orderReq) {
		if(orderReq.getType() == null) {
			return null;
		}
		
		return handlerOrderList(orderReq);
	}

	/**订单列表
	 * @param orderReq
	 * @return
	 */
	private List<OrderPoResp> handlerOrderList(OrderReq orderReq){
		OrderPoReq orderPoReq = new OrderPoReq();
		orderPoReq.setUserId(Long.parseLong(orderReq.getUserId()));
		orderPoReq.setStartIndex(orderReq.getCurrentPage()*orderReq.getPageSize()-orderReq.getPageSize());
		orderPoReq.setPageSize(orderReq.getPageSize());
		orderPoReq.setBusinessId(orderReq.getBusinessId());
		//所有订单列表
		if(OrderType.ALL == orderReq.getType()) {
			return adOrderReportDao.findALLOrderList(orderPoReq);
		}else if(OrderType.LATEST_ORDER == orderReq.getType()) {//最近订单（45天订单）
			String orderDay = configDictServiceI.getValueByTypeAndKey("ORDER", "LATEST_ORDER_DAY");
			orderPoReq.setBeginOrderDate(DateUtils.minusDays(new Date(), StringUtils.isNotEmpty(orderDay) ? Integer.parseInt(orderDay): LATEST_ORDER_DAY));
			orderPoReq.setEndOrderDate(new Date());
			return adOrderReportDao.findALLOrderList(orderPoReq);
		}else if(OrderType.LATEST_AMOUNT == orderReq.getType()) {//最近到账
			return adOrderReportDao.findLatestOrderAmountList(orderPoReq);
		}else if(OrderType.NOT_REBATE == orderReq.getType()) {//无返利
			return	adOrderReportDao.findNotRebateOrderList(orderPoReq);
		} else if (OrderType.WAIT_PAY == orderReq.getType()) {
			orderPoReq.setType(10);
			return adOrderReportDao.findALLOrderList(orderPoReq);
		} else if (OrderType.AREADY_PAY == orderReq.getType()) {
			orderPoReq.setType(1);
			return adOrderReportDao.findALLOrderList(orderPoReq);
		}
		return null;
	}
	/**
	 * 我的订单详细页面
	 */
	@Override
	public OrderDetailResp getOrderDetail(OrderDetailReq req) {
		OrderDetailResp resp = new OrderDetailResp();
		try {
			OrderDetailPoReq orderDetailPoReq = new OrderDetailPoReq();
			orderDetailPoReq.setOrderId(Long.parseLong(req.getOrderId()));
			orderDetailPoReq.setUserId(req.getUserId());
			//查询订单信息
			OrderDetailReport report =  adOrderReportDao.getOrderDetail(orderDetailPoReq);
            if(Constants.GIFT_ORDER_TYPE.equals(report.getRemarks())){
                resp.setOrderType("1");
            }
            String orderNumber = report.getOrderNumber();
            //查询手续费
            //查询订单手续费
            AdOrderIntegralRecord adOrderIntegralRecord = new AdOrderIntegralRecord();
            adOrderIntegralRecord.setOrderNumber(orderNumber);
            adOrderIntegralRecord.setPaymentType(1);
            BigDecimal totalService = adOrderIntegralRecordDao.sumIntegralRebateAmount(adOrderIntegralRecord);
            AdOrderReport adOrderReport = new AdOrderReport();
            adOrderReport.setId(Long.parseLong(req.getOrderId()));
            resp.setId(report.getId());
            resp.setBusinessRebateAmount(report.getBusinessRebateAmount());
            resp.setCompany(report.getCompany());
            resp.setConsigneeMobile(report.getConsigneeMobile());
            resp.setDelFlag(report.getDelFlag());
            resp.setIsBusinessRebate(report.getIsBusinessRebate());
            resp.setIsSource(report.getIsSource());
            resp.setIsUserRebate(report.getIsUserRebate());
            resp.setLogo(report.getLogo());
            resp.setOrderDate(DateUtils.formatDate(report.getOrderDate(), "yyyy.MM.dd HH:mm:ss"));
            resp.setOrderId(report.getOrderId());
            resp.setOrderNumber(orderNumber);
			resp.setProductType(report.getProductType());
			resp.setSavePrice(report.getSavePrice().subtract(totalService));
			resp.setServiceCharge(report.getServiceCharge()!=null?report.getServiceCharge().add(totalService):totalService);
			resp.setState(report.getState());
			resp.setStoreName(report.getStoreName());
			resp.setPayAmount(report.getTotalMount().add(totalService));
			resp.setAmountPayable(report.getTotalPrice());
			resp.setType(report.getType());
			resp.setUserId(report.getUserId());

			resp.setUserReturnAmount(report.getUserReturnAmount());
			resp.setVoucher(report.getVoucher());
			resp.setBusinessId(report.getBusinessId());
			resp.setIsSource(report.getIsSource());
			resp.setSystemDate(DateUtils.formatDate(new Date(), "yyyy.MM.dd HH:mm:ss"));
			resp.setCashDeskSource(report.getCashDeskSource());
            resp.setExpressCompanyName(report.getExpressCompanyName());
            resp.setCourierNumber(report.getCourierNumber());

			 //查询订单明细
	        AdOrderDetail adOrderDetailQuery = new AdOrderDetail();
	        adOrderDetailQuery.setAdOrderReport(adOrderReport);
			List<AdOrderDetail> adOrderDetailList = adOrderDetailDao.findListByAdOrderReport(adOrderDetailQuery);
			if(adOrderDetailList != null && adOrderDetailList.size() > 0) {
				AdOrderDetail orderDetail = adOrderDetailList.get(0);
				resp.setGoods(orderDetail.getGoods());
				
				resp.setCardCode(orderDetail.getCardCode());
				if(StringUtils.isNotEmpty(orderDetail.getCardPass())) {
					resp.setCardPass(AESTool.Decrypt(orderDetail.getCardPass(), CP_AES_KEY));
				}
				resp.setDuihuanUrl(orderDetail.getDuihuanUrl());
				resp.setProductImg(orderDetail.getProductImg());
				resp.setRetState(orderDetail.getRetState());
				
			    if(ProductType.TRAFFIC_TOP_UP.getValue().equals(report.getProductType()) && StringUtils.isNotEmpty(orderDetail.getSpecification()) && orderDetail.getSpecification().indexOf("/")!=-1){
			    	resp.setSpecification(orderDetail.getSpecification().split("/")[1]);
                }else{
                	resp.setSpecification(orderDetail.getSpecification());
                }
			}
			
			AdUser adUser = new AdUser();
			adUser.setId(report.getUserId());
			adOrderReport.setAdUser(adUser);
			putOrderReportToMapByOrderReportId(resp, report);
			resp.setBillingState(report.getBillingState());		
			String orderDay = configDictServiceI.getValueByTypeAndKey("ORDER", "LATEST_ORDER_DAY");
			Date intervalDayDate = DateUtils.addDays(report.getOrderDate(),  (StringUtils.isNotEmpty(orderDay) ? Integer.parseInt(orderDay): LATEST_ORDER_DAY));//
			resp.setIntegrateReturnDate(com.doooly.business.utils.DateUtils.formatDate(intervalDayDate, "yyyy.MM.dd"));		
		
			AdGroup adGroup =adGroupDao.getGroupLogoByUserId(report.getUserId().intValue());
			resp.setGroupShortName(adGroup != null ? adGroup.getGroupShortName() : "");
			//获取productId
			AdOrderDetail detail =  new AdOrderDetail();
			AdOrderReport adOrderReport1 = new AdOrderReport();
			adOrderReport1.setId(report.getId());
			detail.setAdOrderReport(adOrderReport1);
			List<AdOrderDetail>  orderDetailList = adOrderDetailDao.findListByAdOrderReport(detail);
			if(orderDetailList != null && orderDetailList.size() > 0){
				resp.setProductId(orderDetailList.get(0).getProductSkuId());
			}

			//减返利积分
			if(5 == report.getType()) {
				List<Long> orderList = Arrays.asList(new Long[]{resp.getId()});
				List<AdReturnFlow> orderFlow = this.getOrderList(orderList);
				if (orderFlow != null && orderFlow.size() > 0) {
					resp.setUserRebate(report.getUserRebate().subtract(orderFlow.get(0).getUserRebate()));
				}
			}else {
				resp.setUserRebate(report.getUserRebate());
			}

			if(resp.getUserRebate() == null || resp.getUserRebate().doubleValue() < 0){
				resp.setUserRebate(new BigDecimal(0));
			}
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
		}
		return resp;
	}
	
	 private void putOrderReportToMapByOrderReportId(OrderDetailResp resp, OrderDetailReport orderDetailReport) {
	        AdUserBusinessExpansion adUserBusinessExpansion = null;
	        AdOrderReport adOrderReport = new AdOrderReport();
			adOrderReport.setId(orderDetailReport.getId());
	        //都市旅游卡订单信息
	        if (orderDetailReport.getProductType() != null && orderDetailReport.getProductType() == 5) {
	            adUserBusinessExpansion = adOrderReportDao.findSctcdAccount(adOrderReport);
	        }
	        AdOrderFlow adOrderFlowQuery = new AdOrderFlow();
	        adOrderFlowQuery.setAdOrderReport(adOrderReport);
	        List<AdOrderFlow> adOrderFlowList = adOrderFlowDao.findListByAdOrderReport(adOrderFlowQuery);
	        //根据流水获取支付方式
	        if (CollectionUtils.isNotEmpty(adOrderFlowList)) {
	            adOrderReport.setAdOrderFlowList(adOrderFlowList);
	            String payTypeStr = "";
	            Map<String,String> payTypeMap = new HashMap<>();
	            for (AdOrderFlow adOrderFlow : adOrderFlowList) {
	                if (adOrderFlow.getPayType() == AdOrderFlow.PAY_TYPE_PLATFORM_POINT) {
	                    payTypeMap.put("0","兜礼积分");
	                }else if(adOrderFlow.getPayType() == AdOrderFlow.PAY_TYPE_WECHAT){
	                    payTypeMap.put("3","微信支付");
	                }else if(adOrderFlow.getPayType() == AdOrderFlow.PAY_TYPE_ALIPAY) {
						payTypeMap.put("6", "支付宝支付");
					}else {
	                    payTypeMap.put("1","现金支付");
	                }
	            }
	            StringBuilder stringBuilder = new StringBuilder();
	            for (String s : payTypeMap.values()) {
	                stringBuilder.append(s).append("/");
	            }
	            if (StringUtils.isNotBlank(stringBuilder)) {
	                payTypeStr = stringBuilder.toString().substring(0, stringBuilder.length()-1);
	            }
	            resp.setPayTypeStr(payTypeStr);
	        }
	  
	     /*   // 认证会员并且开通了返利开关
	        int resultNum = adUserDao.findOpenRebateSwitchNum(adOrderReport.getAdUser().getId());
	        adOrderReport.setOpenRebateSwitch(resultNum > 0);*/
	       
	        if (orderDetailReport.getProductType() != null && orderDetailReport.getProductType() == ProductType.SWISS_CARD.getValue()) {//旅游卡
	        	resp.setSctcdCardno(orderDetailReport.getRemarks());
	        }
	        
	        if (adUserBusinessExpansion != null) {
	        	resp.setSctcdCardno(adUserBusinessExpansion.getF1());
	        	resp.setSctcdAccountMobile(adUserBusinessExpansion.getF2());
	        	resp.setSctcdAccountIdCard(adUserBusinessExpansion.getF5());
	        }
	    }
	 

		@Override
		public List<Map<String,Object>> getOrderdDetailSum(OrderPoReq req) {
			
			List<Map<String,Object>> list = adOrderReportDao.findOrderdDetailSum(req);
			putList(list, req);
			return list;
		}
		
		private void putList(List<Map<String,Object>> list,OrderPoReq req) {
	        String key1 = "orderDate";
            String key2 = "totalMonthMount";
            String key3 = "totalMonthsaveMount";
	        String key4 = "totalServiceCharge";
			AdGroup adGroup =adGroupDao.getGroupLogoByUserId(req.getUserId().intValue());
            List<Map<String,Object>> chargeList = adOrderIntegralRecordDao.findOrderServiceChargeSum(req);
            for(Map<String,Object> map : list) {
                map.put("groupShortName",adGroup != null ? adGroup.getGroupShortName() : "");
                String value1 = (String) map.get(key1);
                BigDecimal value2 = (BigDecimal) map.get(key2);
                BigDecimal value3 = (BigDecimal) map.get(key3);
                for (Map<String, Object> stringStringMap : chargeList) {
                    String value11 = (String) stringStringMap.get(key1);
                    BigDecimal value4 = (BigDecimal) stringStringMap.get(key4);
                    if(value1.equals(value11)){
                        BigDecimal totalMonthMount = value2.add(value4);
                        BigDecimal totalMonthsaveMount = value3.subtract(value4);
                        map.put(key2,totalMonthMount);
                        map.put(key3,totalMonthsaveMount);
                        break;
                    }
                }
            }



		}
		

		@Override
		public Long countOrderNum(OrderReq orderReq) {
			OrderPoReq orderPoReq = new OrderPoReq();
			orderPoReq.setUserId(Long.parseLong(orderReq.getUserId()));
			orderPoReq.setStartIndex(orderReq.getCurrentPage()*orderReq.getPageSize()-orderReq.getPageSize());
			orderPoReq.setPageSize(orderReq.getPageSize());
			orderPoReq.setBusinessId(orderReq.getBusinessId());
			//所有订单列表
			if(OrderType.ALL == orderReq.getType()) {
				return adOrderReportDao.countALLOrder(orderPoReq);
			}else if(OrderType.LATEST_ORDER == orderReq.getType()) {//最近订单（45天订单）
				String orderDay = configDictServiceI.getValueByTypeAndKey("ORDER", "LATEST_ORDER_DAY");
				orderPoReq.setBeginOrderDate(DateUtils.minusDays(new Date(), StringUtils.isNotEmpty(orderDay) ? Integer.parseInt(orderDay): LATEST_ORDER_DAY));
				orderPoReq.setEndOrderDate(new Date());
				return adOrderReportDao.countALLOrder(orderPoReq);
			}else if(OrderType.LATEST_AMOUNT == orderReq.getType()) {//最近到账
				return adOrderReportDao.countLatestOrderAmount(orderPoReq);
			}else if(OrderType.NOT_REBATE == orderReq.getType()) {//无返利
				return	adOrderReportDao.countNotRebateOrder(orderPoReq);
			} else if (OrderType.WAIT_PAY == orderReq.getType()) {
				orderPoReq.setType(10);
				return adOrderReportDao.findALLOrderSum(orderPoReq);
			} else if (OrderType.AREADY_PAY == orderReq.getType()) {
				orderPoReq.setType(1);
				return adOrderReportDao.findALLOrderSum(orderPoReq);
			}
			return 0L;
		}

	/**
	 * 取消订单提醒
	 * @param req
	 */
	@Override
		public void cannelHint(OrderHintReq req) {
			try {
				ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
				String[] list = req.getHintState().split(",");
				for (int i = 0; i < list.length; i++) {
					Integer state = Integer.parseInt((list[i]));
					//0,1,2
					String orderTotal = opsForValue.get("ordertotal:"+req.getUserId()+":"+state);
					Integer value = StringUtils.isEmpty(orderTotal) ? 0 : Integer.parseInt(orderTotal);
					Integer total = getTotal(req,state);
					logger.info("total:{}",total);
					if(total != value) {
						opsForValue.set("ordertotal:"+req.getUserId()+":"+state,String.valueOf(total));
					}
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
			}

		}

	/**
	 * 取消用户提醒
	 */
	@Override
	public void cannelUserFlag(String userId, String flags) {
		try {
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			String[] list = flags.split(",");
			for (String state : list) {
				Long now = System.currentTimeMillis();
				opsForValue.set("userFlag:" + userId + ":" + state, now.toString());
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 获取订单总数
	 * @param req
	 * @param state
	 * @return
	 */
		private int getTotal(OrderHintReq req,Integer state) {
			OrderPoReq orderPoReq = new OrderPoReq();
			orderPoReq.setUserId(req.getUserId());
			if(0 == state) {
				String orderDay = configDictServiceI.getValueByTypeAndKey("ORDER", "LATEST_ORDER_DAY");
				orderPoReq.setBeginOrderDate(DateUtils.minusDays(new Date(), StringUtils.isNotEmpty(orderDay) ? Integer.parseInt(orderDay): LATEST_ORDER_DAY));
				orderPoReq.setEndOrderDate(new Date());
				return  adOrderReportDao.getLatestOrderTotal(orderPoReq);
			}else if(1 == state) {
				return adOrderReportDao.getLatestAmountTotal(orderPoReq);
			}else if(2 ==  state) {
				return adOrderReportDao.getNotRebateOrderTotal(orderPoReq);
			}
			return 0;
		}

	/**
	 * 获取订单提醒
	 * @param req
	 * @return
	 */
	@Override
	public HintResp getHint(HintReq req) {
		HintResp hintResp = new HintResp();
		try {
			OrderPoReq orderPoReq = new OrderPoReq();
			orderPoReq.setUserId(Long.parseLong(req.getUserId()));
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			String orderTotal = opsForValue.get("ordertotal:"+req.getUserId()+":0");//已下单
			String finishTotal = opsForValue.get("ordertotal:"+req.getUserId()+":1");//已完成
			String cancelTotal = opsForValue.get("ordertotal:"+req.getUserId()+":2");//已取消
			logger.info(String.format("用户 %s redis中已下单数:%s,已完成数:%s,已取消数:%s", req.getUserId(),orderTotal,finishTotal,cancelTotal));
			String orderDay = configDictServiceI.getValueByTypeAndKey("ORDER", "LATEST_ORDER_DAY");
			orderPoReq.setBeginOrderDate(DateUtils.minusDays(new Date(), StringUtils.isNotEmpty(orderDay) ? Integer.parseInt(orderDay): LATEST_ORDER_DAY));
			orderPoReq.setEndOrderDate(new Date());
			int orderTotalMap = adOrderReportDao.getLatestOrderTotal(orderPoReq);
			int finishTotalMap = adOrderReportDao.getLatestAmountTotal(orderPoReq);
			int cancelTotalMap = adOrderReportDao.getNotRebateOrderTotal(orderPoReq);
			logger.info(String.format("用户 %s DB中已下单数:%s,已完成数:%s,已取消数:%s", req.getUserId(),orderTotalMap,finishTotalMap,cancelTotalMap));

			// 3.有新订单 设置flag
			if(orderTotalMap>(StringUtils.isEmpty(orderTotal) ? 0 : Integer.parseInt(orderTotal))){
				hintResp.setNewOrderFlag(true);
			}else{
				resetRedis(orderTotalMap,orderTotal,opsForValue,req.getUserId(),0);
				hintResp.setNewOrderFlag(false);
			}
			if(finishTotalMap>(StringUtils.isEmpty(finishTotal) ? 0 : Integer.parseInt(finishTotal))){
				hintResp.setNewFinishFlag(true);
			}else{
				resetRedis(finishTotalMap,finishTotal,opsForValue,req.getUserId(),1);
				hintResp.setNewFinishFlag(false);
			}
			if(cancelTotalMap>(StringUtils.isEmpty(cancelTotal) ? 0 : Integer.parseInt(cancelTotal))){
				hintResp.setNewCancelFlag(true);
			}else{
				resetRedis(cancelTotalMap,cancelTotal,opsForValue,req.getUserId(),2);
				hintResp.setNewCancelFlag(false);
			}



		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return hintResp;
	}

	/**
	 * 获取个人中心提醒
	 * @param req
	 * @return
	 */
	@Override
	public HintResp getUserFlag(HintReq req) {
		HintResp hintResp = new HintResp();
		try {
			OrderPoReq orderPoReq = new OrderPoReq();
			orderPoReq.setUserId(Long.parseLong(req.getUserId()));
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			// 查询redis记录的上次点击时间
			// 最近下单
			Long recentlyPlacedOrderFlag = Long.valueOf(opsForValue.get("userFlag:"+req.getUserId() + ":0") != null ?
					opsForValue.get("userFlag:"+req.getUserId() + ":0") : "0");
			//待付款
			Long pendingPaymentFlag = Long.valueOf(opsForValue.get("userFlag:" + req.getUserId() + ":1") != null ?
					opsForValue.get("userFlag:" + req.getUserId() + ":1") : "0");
			//最近到账积分
			Long recentArrivalFlag = Long.valueOf(opsForValue.get("userFlag:"+req.getUserId() + ":2") != null ?
					opsForValue.get("userFlag:"+req.getUserId() + ":2") : "0");
			//即将到账
			Long imminentArrivalFlag = Long.valueOf(opsForValue.get("userFlag:" + req.getUserId() + ":3") != null ?
					opsForValue.get("userFlag:" + req.getUserId() + ":3") : "0");
			logger.info(String.format("用户 %s redis中最近下单点击时间：%s,待付款点击时间：%s，最近到账积分点击时间：%s，即将到账点击时间：%s",
					req.getUserId(),recentlyPlacedOrderFlag,pendingPaymentFlag,recentArrivalFlag, imminentArrivalFlag));

			Date recentlyPlacedOrderDate = adOrderReportDao.getMaxOrderDateByUserAndType(req.getUserId(), null);
			Date pendingPaymentDate = adOrderReportDao.getMaxOrderDateByUserAndType(req.getUserId(), "10");
//			Date recentArrivalDate = adUserDao.getReturnPointsMaxUpdateDateByUser(req.getUserId(), "3");
			Date recentArrivalDate = adAvailablePointsDao.maxArrivalAccountUpdateDate(req.getUserId());
			Date imminentArrivalDate = adUserDao.getReturnPointsMaxCreateDateByUser(req.getUserId(), "2");

			if (recentlyPlacedOrderDate != null && recentlyPlacedOrderDate.getTime() > recentlyPlacedOrderFlag) {
				hintResp.setRecentlyPlacedOrderFlag(true);
			} else {
				hintResp.setRecentlyPlacedOrderFlag(false);
			}

			if (pendingPaymentDate != null && pendingPaymentDate.getTime() > pendingPaymentFlag) {
				hintResp.setPendingPaymentFlag(true);
			} else {
				hintResp.setPendingPaymentFlag(false);
			}

			if (recentArrivalDate != null && recentArrivalDate.getTime() > recentArrivalFlag) {
				hintResp.setRecentArrivalFlag(true);
			} else {
				hintResp.setRecentArrivalFlag(false);
			}

			if (imminentArrivalDate != null && imminentArrivalDate.getTime() > imminentArrivalFlag) {
				hintResp.setImminentArrivalFlag(true);
			} else {
				hintResp.setImminentArrivalFlag(false);
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return hintResp;
	}

	private void resetRedis(int value1,String value2,ValueOperations<String, String> opsForValue,String  userId,int state){
		if(value1<(StringUtils.isEmpty(value2) ? 0 : Integer.parseInt(value2))){
			opsForValue.set("ordertotal:"+userId+":"+state,String.valueOf(value1));
		}
	}

	/**
	 * 删除订单
	 * @param req
	 * @return
	 */
	@Override
	public boolean deleteOrder(OrderDeleteReq req) {
		OrderDetailPoReq orderDetailPoReq = new OrderDetailPoReq();
		orderDetailPoReq.setOrderId(req.getOrderId());
		orderDetailPoReq.setUserId(req.getUserId());
		Integer result = adOrderReportDao.deleteOrder(orderDetailPoReq);
		if(result != null && result >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<AdReturnFlow> getOrderList(List<Long> orderList) {
		if(orderList == null || orderList.size() <=0){
			return null;
		}
		return adReturnFlowDao.getOrderList(orderList);
	}

	@Override
	public List<AdOrderDetail> finDetailByOrder(List<Long> list) {
		if(list == null || list.size() <=0){
			return null;
		}
		return this.adOrderDetailDao.finDetailByOrder(list);
	}
}
