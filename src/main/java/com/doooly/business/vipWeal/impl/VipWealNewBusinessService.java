package com.doooly.business.vipWeal.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.doooly.business.lightenBusiness.AdLightenBusinessServiceI;
import com.doooly.business.mall.service.MallBusinessServiceI;
import com.doooly.business.vipWeal.VipWealNewBusinessServiceI;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdProductDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdProduct;
import com.doooly.entity.reachad.AdUser;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 答题活动业务Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
@Service
public class VipWealNewBusinessService implements VipWealNewBusinessServiceI {
	private static Logger logger = Logger.getLogger(VipWealNewBusinessService.class);
	@Autowired
	private AdProductDao adProductDao;
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;
	@Autowired
	private MallBusinessServiceI mallBusinessServiceI;
	@Autowired
	private AdLightenBusinessServiceI adLightenBusinessServiceI;
	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private AdGroupDao adGroupDao;
	@Autowired
	private AdUserDao adUserDao;
	@Override
	public MessageDataBean getBenefitsData(Integer userId, String address) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取专享福利的活动和商品
		// 活动由对应开放企业来确认
		try {
			List<AdCouponActivity> activities = adCouponActivityDao.getBenefitsActivityList(userId);
			logger.info("专享福利获取的活动列表为+"+JSON.toJSONString(activities));
			if (!activities.isEmpty()) {
				for (AdCouponActivity adCouponActivity : activities) {
					adCouponActivity.setUserCount(adCouponActivityDao.getActivityUserCount(adCouponActivity.getId()));
				}
				map.put("activityList", activities);
			}else {
				map.put("activityList", null);
			}
			// 商品则由商家服务地区和是否热门推荐来取值
			List<AdProduct> products = adProductDao.getBenefitsProductList(address);
			logger.info("专享福利获取的商品列表为+"+JSON.toJSONString(products));
			if (!products.isEmpty()) {
				for (AdProduct adProduct : products) {
					adProduct.setPrice(adProduct.getMarketPrice().multiply(new BigDecimal(adProduct.getDiscount())).divide(new BigDecimal(10)));
					Double productRebate = mallBusinessServiceI.getProductRebate(adProduct);
					adProduct.setRebate(productRebate+"");
					Integer lightenBusinessType = adLightenBusinessServiceI.lightenBusinessType(userId.toString(), adProduct.getBusinessId());
					adProduct.setLightenType(lightenBusinessType+"");
				}
				map.put("productList", products);
			}else {
				map.put("productList", null);
			}
			AdGroup miniLogo = adGroupDao.getGroupLogoByUserId(userId);
			if (miniLogo !=null) {
				map.put("logo", miniLogo.getMiniLogoUrl());
				map.put("groupShortName", miniLogo.getGroupShortName());
			}else {
				map.put("logo", null);
				map.put("groupShortName", null);
			}
			if (!map.isEmpty()) {
				messageDataBean.setCode(MessageDataBean.success_code);
				messageDataBean.setData(map);
			}else {
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		} catch (Exception e) {
			logger.error("专享福利获取活动和商品列表时出错+"+e);
			messageDataBean.setCode(MessageDataBean.failure_code);
			e.printStackTrace();
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean;
	}
	@Override
	public MessageDataBean getIntegralAndBusinessList(Long userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取用户积分
		try {
			AdUser user = adUserDao.selectByPrimaryKey(userId);
			if (user !=null) {
				map.put("integral", user.getIntegral());
			}else {
				map.put("integral", null);
			}
			// 获取用户的点亮商家信息
			List<AdBusiness> list =adBusinessDao.findScanList();
			if (list !=null) {
				for (AdBusiness adBusiness : list) {
					Integer lightenBusinessType = adLightenBusinessServiceI.lightenBusinessType(userId.toString(), adBusiness.getId()+"");
					adBusiness.setLightenType(lightenBusinessType.toString());
				}
				map.put("businessList", list);
			}else {
				map.put("businessList", null);
			}
			if (!map.isEmpty()) {
				logger.error("扫码优惠获取积分和商家列表为:+"+JSON.toJSONString(map));
				messageDataBean.setCode(MessageDataBean.success_code);
				messageDataBean.setData(map);
			}else {
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		} catch (Exception e) {
			messageDataBean.setCode(MessageDataBean.failure_code);
			e.printStackTrace();
		}
		return messageDataBean;
	}



}
