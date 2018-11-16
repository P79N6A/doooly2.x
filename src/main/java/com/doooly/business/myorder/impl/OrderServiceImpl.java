package com.doooly.business.myorder.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.myorder.constant.OrderType;
import com.doooly.business.myorder.constant.ProductType;
import com.doooly.business.myorder.dto.OrderDetailReq;
import com.doooly.business.myorder.dto.OrderDetailResp;
import com.doooly.business.myorder.dto.OrderReq;
import com.doooly.business.myorder.po.OrderDetailReport;
import com.doooly.business.myorder.po.OrderPoReq;
import com.doooly.business.myorder.po.OrderPoResp;
import com.doooly.business.myorder.service.OrderService;
import com.doooly.business.pay.bean.AdOrderFlow;
import com.doooly.business.pay.utils.AESTool;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdOrderDetailDao;
import com.doooly.dao.reachad.AdOrderFlowDao;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdOrderDetail;
import com.doooly.entity.reachad.AdOrderReport;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserBusinessExpansion;

/**
 * @Description: 我的订单
 * @author: xianpeng.hua
 * @date: 2018-11-09
 */
@Service("OrderServiceImpl")
public class OrderServiceImpl implements OrderService{
	
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
	 
	@Override
	public List<OrderPoResp> getOrderList(OrderReq orderReq) {
		if(orderReq.getType() == null) {
			return null;
		}
		
		return handlerOrderList(orderReq);
	}

	/**
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
			//查询订单信息
			OrderDetailReport report =  adOrderReportDao.getOrderDetailById(req.getOrderId());
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
			resp.setOrderDate(com.doooly.business.utils.DateUtils.formatDate(report.getOrderDate(), "yyyy.MM.dd HH:mm:ss"));
			resp.setOrderId(report.getOrderId());
			resp.setOrderNumber(report.getOrderNumber());
			resp.setProductType(report.getProductType());
			resp.setSavePrice(report.getSavePrice());
			resp.setServiceCharge(report.getServiceCharge());
			resp.setState(report.getState());
			resp.setStoreName(report.getStoreName());
			resp.setPayAmount(report.getTotalMount());
			resp.setAmountPayable(report.getTotalPrice());
			resp.setType(report.getType());
			resp.setUserId(report.getUserId());
			resp.setUserRebate(report.getUserRebate());
			resp.setUserReturnAmount(report.getUserReturnAmount());
			resp.setVoucher(report.getVoucher());
			resp.setBusinessId(report.getBusinessId());
			resp.setIsSource(report.getIsSource());
			
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
			putOrderReportToMapByOrderReportId(resp, adOrderReport);
			resp.setBillingState(report.getBillingState());		
			String orderDay = configDictServiceI.getValueByTypeAndKey("ORDER", "LATEST_ORDER_DAY");
			Date intervalDayDate = DateUtils.addDays(report.getOrderDate(),  (StringUtils.isNotEmpty(orderDay) ? Integer.parseInt(orderDay): LATEST_ORDER_DAY));//
			resp.setIntegrateReturnDate(com.doooly.business.utils.DateUtils.formatDate(intervalDayDate, "yyyy.MM.dd"));		
		
			AdGroup adGroup =adGroupDao.getGroupLogoByUserId(report.getUserId().intValue());
			resp.setGroupShortName(adGroup != null ? adGroup.getGroupShortName() : "");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	 private void putOrderReportToMapByOrderReportId(OrderDetailResp resp, AdOrderReport adOrderReport) {
	        AdUserBusinessExpansion adUserBusinessExpansion = null;
	        //都市旅游卡订单信息
	        if (adOrderReport.getProductType() == 5) {
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
	       
	        if (adOrderReport.getProductType() == ProductType.SWISS_CARD.getValue()) {//旅游卡
	        	resp.setSctcdCardno(adOrderReport.getRemarks());
	        }
	        
	        if (adUserBusinessExpansion != null) {
	        	resp.setSctcdCardno(adUserBusinessExpansion.getF1());
	        	resp.setSctcdAccountMobile(adUserBusinessExpansion.getF2());
	        	resp.setSctcdAccountIdCard(adUserBusinessExpansion.getF5());
	        }
	    }
	 

		@Override
		public List<Map<String,String>> getOrderdDetailSum(OrderPoReq req) {
			
			List<Map<String,String>> list = adOrderReportDao.findOrderdDetailSum(req);
			putList(list, req);
			return list;
		}
		
		private void putList(List<Map<String,String>> list,OrderPoReq req) {
			AdGroup adGroup =adGroupDao.getGroupLogoByUserId(req.getUserId().intValue());
			for(Map<String,String> map : list) {
				map.put("groupShortName",adGroup != null ? adGroup.getGroupShortName() : "");
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
			}
			return 0L;
		}
	
}
