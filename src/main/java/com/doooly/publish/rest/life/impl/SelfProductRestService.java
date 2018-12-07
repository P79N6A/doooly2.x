package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.business.product.entity.AdGroupSelfProductPrice;
import com.doooly.business.product.service.ProductService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAd;
import com.doooly.publish.rest.life.SelfProductRestServiceI;
import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

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
			String activityName = obj.getString("activityName");
			HashMap<String, Object> map = productService.getSelfProductDetail(productId, userId);
			List<AdGroupSelfProductPrice> adGroupSelfProductPriceList = productService.getSelfProductAirport(activityName,productId);
			if (adGroupSelfProductPriceList != null && adGroupSelfProductPriceList.size() > 0) {
				map.put("adGroupSelfProductPrice",adGroupSelfProductPriceList.get(0));
			}
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

			AdGroupSelfProductPrice adGroupSelfProductPrice = productService.getSelfProductSkuListByName(activityName, Integer.parseInt(groupId));

			// 是否有话费活动
			if (adGroupSelfProductPrice != null) {
				AdAd ad = mallBusinessService.findByTypeAndGroup(Integer.parseInt(groupId), activityName);

				// 此活动是否配置广告位
				if (ad != null) {
					Calendar calendar = Calendar.getInstance();
					Date now = calendar.getTime();

					// 当前日期小于活动结束日期
					if (now.compareTo(adGroupSelfProductPrice.getSpecialEndDate()) < 0) {
						int skuId = adGroupSelfProductPrice.getSkuId();
						ActivityInfo actInfo = orderService.getActivityInfo(groupId, skuId);
						int num = 0;

						if (actInfo != null) {
							String actType = actInfo.getActivityName();
							num = orderService.getBuyNum(userId, skuId, actType);
						}

						String weekList = adGroupSelfProductPrice.getWeekList();
						weekList = weekList.substring(1, weekList.length() - 1).replaceAll(" ", "");
						List<String> weekLists = Arrays.asList(weekList.split(","));
						String activityOfTime = adGroupSelfProductPrice.getActivityOfTime();
						List<String> activityOfTimeList = Arrays.asList(activityOfTime.split("-"));
						int startHour = Integer.parseInt(activityOfTimeList.get(0));
						int endHour = Integer.parseInt(activityOfTimeList.get(1));

						String week = calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? (7 + "") : calendar.get(Calendar.DAY_OF_WEEK) - 1 + "";
						int hour = calendar.get(Calendar.HOUR_OF_DAY);

						if (weekLists.contains(week) && hour >= startHour && hour < endHour + 1) {
							if (adGroupSelfProductPrice.getBuyNumberLimit() - num <= 0) {
								adGroupSelfProductPrice.setIsStart("4");
							} else {
								adGroupSelfProductPrice.setIsStart("2");
							}
						} else {
							adGroupSelfProductPrice.setIsStart("1");
						}
					} else {
						adGroupSelfProductPrice.setIsStart("3");
					}

					adGroupSelfProductPrice.setUrl(ad.getImageLinkUrl());
					adGroupSelfProductPrice.setImage(ad.getImagePath());
					HashMap<String,Object> map = new HashMap<String,Object>();
					map.put("adGroupSelfProductPrice", adGroupSelfProductPrice);
					messageDataBean.setCode(MessageDataBean.success_code);
					messageDataBean.setData(map);
				} else {
					messageDataBean.setCode(MessageDataBean.null_code);
					logger.info("当前企业ID：" + groupId + "--未配置<" + activityName + ">广告位");
				}

			} else {
				messageDataBean.setCode(MessageDataBean.null_code);
				logger.info("无话费充值活动：" + activityName);
			}
		} catch (Exception e) {
			logger.error("获取卡券商品详情页信息异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

    @POST
    @Path(value = "/getActivityByType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String getActivityByType(JSONObject obj) {
        logger.info("getActivityByType = {}"+obj);
        MessageDataBean messageDataBean = new MessageDataBean();
        String type = obj.getString("type");
        String groupId = obj.getString("groupId");

        String result = mallBusinessService.getActivityByTypeAndGroup(Integer.parseInt(type), Integer.parseInt(groupId));

        return result;
    }




	@POST
	@Path(value = "/getSelfProductAirport")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getSelfProductAirport(JSONObject obj) {
		logger.info("getSelfProductAirport = {}"+obj);
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String,Object> map = new HashMap<>();
		try {
			String activityName = obj.getString("activityName");
            int pageNum = getPageNum(obj);
            int pageSize = getPageSize(obj);
			PageHelper.startPage(pageNum,pageSize);
            List<AdGroupSelfProductPrice> adGroupSelfProductPriceList = productService.getSelfProductAirport(activityName,null);
			map.put("adGroupSelfProductPriceList", adGroupSelfProductPriceList);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券商品详情页信息异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}


	private int getPageSize(JSONObject jsonObject) {
	    int pageSize = 2;
	    try {
            pageSize = Integer.parseInt(jsonObject.getString("pageSize"));
        } catch (Exception e) {
	        e.printStackTrace();
        }
        return pageSize;
    }


    private int getPageNum(JSONObject jsonObject) {
        int pageNum = 1;
        try {
            pageNum = Integer.parseInt(jsonObject.getString("pageNum"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageNum;
    }

}
