package com.doooly.publish.rest.life.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.business.product.entity.AdGroupSelfProductPrice;
import com.doooly.business.product.service.ProductService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAd;
import com.doooly.publish.rest.life.SelfProductRestServiceI;

/**
 * 自营商品 REST Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年9月25日
 */
@Component
@Path("/selfProduct")
public class SelfProductRestService implements SelfProductRestServiceI {

	private static Logger logger = Logger.getLogger(SelfProductRestService.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MallBusinessService mallBusinessService;

	@POST
	@Path(value = "/list")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getSelfProductList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			int currentPage = obj.getInteger("currentPage");// 当前页
			int pageSize = obj.getInteger("pageSize");// 每页显示条数
			String userId = obj.getString("userId");// 会员id
			HashMap<String, Object> map = productService.getSelfProductList(currentPage, pageSize, userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取首页 “卡券购买”列表异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/detail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getSelfProductDetail(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String productId = obj.getString("productId");
			String userId = obj.getString("userId");
			HashMap<String, Object> map = productService.getSelfProductDetail(productId, userId);
			messageDataBean.setCode((String) map.get("code"));
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券商品详情页信息异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/getSelfProductByName")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getSelfProductByName(JSONObject obj) {
		logger.info("getSelfProductByName = {}"+obj);
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String activityName = obj.getString("activityName");
			String groupId = obj.getString("groupId");
			long userId = obj.getLong("userId");
			AdGroupSelfProductPrice adGroupSelfProductPrice = productService.getSelfProductSkuListByName(activityName);
			int skuId = adGroupSelfProductPrice.getSkuId();
			ActivityInfo actInfo = orderService.getActivityInfo(groupId, skuId);
			int num = 0;
			if(actInfo != null) {
				String actType = actInfo.getActivityName();
				num = orderService.getBuyNum(userId, skuId, actType);
			}
			String weekList = adGroupSelfProductPrice.getWeekList();
			weekList = weekList.substring(1, weekList.length()-1).replaceAll(" ", "");
			List<String> weekLists = Arrays.asList(weekList.split(","));
			String activityOfTime = adGroupSelfProductPrice.getActivityOfTime();
			List<String> activityOfTimeList = Arrays.asList(activityOfTime.split("-"));
			int startHour = Integer.parseInt(activityOfTimeList.get(0));
			int endHour = Integer.parseInt(activityOfTimeList.get(1));
			
			if(num - adGroupSelfProductPrice.getBuyNumberLimit() >= 0) {
				adGroupSelfProductPrice.setIsStart("4");
			}else {
				Calendar calendar = Calendar.getInstance();
				String week = calendar.get(Calendar.DAY_OF_WEEK)+"";
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				if(weekLists.contains(week) && hour >= startHour && hour <= endHour) {
					adGroupSelfProductPrice.setIsStart("2");
				}else {
					adGroupSelfProductPrice.setIsStart("1");
				}
//				Date now = calendar.getTime();
//				if(now.compareTo(adGroupSelfProductPrice.getSpecialStartDate()) < 0) {
//					adGroupSelfProductPrice.setIsStart("1");
//				} else if(now.compareTo(adGroupSelfProductPrice.getSpecialStartDate()) >= 0 && 
//						now.compareTo(adGroupSelfProductPrice.getSpecialEndDate()) <= 0) {
//					adGroupSelfProductPrice.setIsStart("2");
//				} else if(now.compareTo(adGroupSelfProductPrice.getSpecialEndDate()) > 0){
//					adGroupSelfProductPrice.setIsStart("3");
//				}
			}
			
			AdAd ad = mallBusinessService.findByTypeAndGroup(Integer.parseInt(groupId), activityName);
			adGroupSelfProductPrice.setUrl(ad.getImageLinkUrl());
			adGroupSelfProductPrice.setImage(ad.getImagePath());
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("adGroupSelfProductPrice", adGroupSelfProductPrice);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券商品详情页信息异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
