package com.doooly.publish.rest.life.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.dao.reachad.AdBasicTypeDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdConsumeRechargeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBasicType;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdConsumeRecharge;

/**
 * app2.1首页接口
 *
 */
@Component
@Path("/wechat/indexService")
public class IndexRestService {

	private static Logger logger = LoggerFactory.getLogger(IndexRestService.class);

	private String BASE_URL = PropertiesHolder.getProperty("BASE_URL") + "/businessinfo/";
	private static int DEAL_TYPE_ONLINE = 0;
	private static int DEAL_TYPE_OFFLINE = 1;

	// @Autowired
	// private HotBusinessService hotBusinessService;
	@Autowired
	private AdBasicTypeDao adBasicTypeDao;
	@Autowired
	private AdConsumeRechargeDao adConsumeRechargeDao;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private AdBusinessDao adBusinessDao;

	@POST
	@Path(value = "/index/v2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String indexV2(JSONObject params, @Context HttpServletRequest request, @Context HttpServletResponse response) {
		long start = System.currentTimeMillis();
		String userToken = request.getHeader(Constants.TOKEN_NAME);
		logger.info("index() userToken={}", userToken);
		if (StringUtils.isEmpty(userToken)) {
			return new MessageDataBean("1001", "userToken is null").toJsonString();
		}
		String userId = redisTemplate.opsForValue().get(userToken);
		String address = params.getString("address");
		// 取有返佣金额的商户
		try {
			logger.info("index() userToken={},userId={},params={}", userToken, userId, params);
			List<AdBasicType> floors = adBasicTypeDao.getFloors(userId);
			if (CollectionUtils.isEmpty(floors)) {
				return new MessageDataBean("1000", "floors is null").toJsonString();
			}
			Map<String, Object> data = new HashMap<String, Object>();
			List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
			for (AdBasicType floor : floors) {
				Map<String, Object> item = new HashMap<String, Object>();
				if (floor.getCode() == 20) {
					// 线上商户
					List<AdConsumeRecharge> getBussiness = this.getBussiness(userId, address, DEAL_TYPE_ONLINE);
					if (!CollectionUtils.isEmpty(getBussiness)) {
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_ONLINE);
						item.put("type", "1");
						item.put("list", getBussiness);
					}
				} else if (floor.getCode() == 21) {
					// 线下商户
					List<AdConsumeRecharge> getBussiness = this.getBussiness(userId, address, DEAL_TYPE_OFFLINE);
					if (!CollectionUtils.isEmpty(getBussiness)) {
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_OFFLINE);
						item.put("type", "1");
						item.put("list", getBussiness);
					}
				} else if (floor.getCode() == 23) {
					// 每日特惠数据
					List<AdConsumeRecharge> beans = adConsumeRechargeDao.getConsumeRecharges(floor.getTemplateId(),
							floor.getFloorId());
					if (!CollectionUtils.isEmpty(beans)) {
						for (AdConsumeRecharge bean : beans) {
							String linkUrl = bean.getLinkUrl();
							if (!StringUtils.isEmpty(bean.getLinkUrl()) && linkUrl.indexOf("#") > -1) {
								bean.setSubUrl(linkUrl.substring(linkUrl.indexOf("#") + 1, linkUrl.length()));
							}
						}
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_OFFLINE);
						item.put("type", "3");
						item.put("list", beans);
					}
				} else {
					// 消费卡券/充值缴费数据表
					List<AdConsumeRecharge> beans = adConsumeRechargeDao.getConsumeRecharges(floor.getTemplateId(),
							floor.getFloorId());
					if (!CollectionUtils.isEmpty(beans)) {
						for (AdConsumeRecharge bean : beans) {
							String linkUrl = bean.getLinkUrl();
							if (!StringUtils.isEmpty(bean.getLinkUrl()) && linkUrl.indexOf("#") > -1) {
								bean.setSubUrl(linkUrl.substring(linkUrl.indexOf("#") + 1, linkUrl.length()));
							}
						}
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_OFFLINE);
						item.put("type", "2");
						item.put("list", beans);
					}
				}
				ls.add(item);
			}
			data.put("floors", ls);
			logger.info("index(), execution time = {}", System.currentTimeMillis() - start);
			return new MessageDataBean("1000", "success", data).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("index() obj={} exception={}", params, e.getMessage());
			return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
		}
	}

	@POST
	@Path(value = "/index")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String index(JSONObject params, @Context HttpServletRequest request, @Context HttpServletResponse response) {
		long start = System.currentTimeMillis();
		String userToken = request.getHeader(Constants.TOKEN_NAME);
		logger.info("index() userToken={}", userToken);
		if (StringUtils.isEmpty(userToken)) {
			return new MessageDataBean("1001", "userToken is null").toJsonString();
		}
		String userId = redisTemplate.opsForValue().get(userToken);
		String address = params.getString("address");
		// 取有返佣金额的商户
		try {
			logger.info("index() userToken={},userId={},params={}", userToken, userId, params);
			List<AdBasicType> floors = adBasicTypeDao.getFloors(userId);
			if (CollectionUtils.isEmpty(floors)) {
				return new MessageDataBean("1000", "floors is null").toJsonString();
			}
			Map<String, Object> data = new HashMap<String, Object>();
			List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
			for (AdBasicType floor : floors) {
				Map<String, Object> item = new HashMap<String, Object>();
				if (floor.getCode() == 20) {
					// 线上商户
					List<AdConsumeRecharge> getBussiness = this.getBussiness(userId, address, DEAL_TYPE_ONLINE);
					if (!CollectionUtils.isEmpty(getBussiness)) {
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_ONLINE);
						item.put("type", "1");
						item.put("list", getBussiness);
					}
				} else if (floor.getCode() == 21) {
					// 线下商户
					List<AdConsumeRecharge> getBussiness = this.getBussiness(userId, address, DEAL_TYPE_OFFLINE);
					if (!CollectionUtils.isEmpty(getBussiness)) {
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_OFFLINE);
						item.put("type", "1");
						item.put("list", getBussiness);
					}
				} else if (floor.getCode() == 23) {
					// 每日特惠数据
					/*List<AdConsumeRecharge> beans = adConsumeRechargeDao.getConsumeRecharges(floor.getTemplateId(),
							floor.getFloorId());
					if (!CollectionUtils.isEmpty(beans)) {
						for (AdConsumeRecharge bean : beans) {
							String linkUrl = bean.getLinkUrl();
							if (!StringUtils.isEmpty(bean.getLinkUrl()) && linkUrl.indexOf("#") > -1) {
								bean.setSubUrl(linkUrl.substring(linkUrl.indexOf("#") + 1, linkUrl.length()));
							}
						}
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_OFFLINE);
						item.put("type", "3");
						item.put("list", beans);
					}*/
				} else {
					// 消费卡券/充值缴费数据表
					List<AdConsumeRecharge> beans = adConsumeRechargeDao.getConsumeRecharges(floor.getTemplateId(),
							floor.getFloorId());
					if (!CollectionUtils.isEmpty(beans)) {
						for (AdConsumeRecharge bean : beans) {
							String linkUrl = bean.getLinkUrl();
							if (!StringUtils.isEmpty(bean.getLinkUrl()) && linkUrl.indexOf("#") > -1) {
								bean.setSubUrl(linkUrl.substring(linkUrl.indexOf("#") + 1, linkUrl.length()));
							}
						}
						item.put("title", floor.getName());
						item.put("isOnline", DEAL_TYPE_OFFLINE);
						item.put("type", "2");
						item.put("list", beans);
					}
				}
				ls.add(item);
			}
			data.put("floors", ls);
			logger.info("index(), execution time = {}", System.currentTimeMillis() - start);
			return new MessageDataBean("1000", "success", data).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("index() obj={} exception={}", params, e.getMessage());
			return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
		}
	}

	private List<AdConsumeRecharge> getBussiness(String userId, String address, int dealType) {
		List<AdBusiness> merchants = adBusinessDao.findHotMerchantsByDealType(Integer.valueOf(userId), null, address,
				dealType);
		List<AdConsumeRecharge> beans = null;
		if (!CollectionUtils.isEmpty(merchants)) {
			beans = new ArrayList<AdConsumeRecharge>();
			for (AdBusiness merchant : merchants) {
				AdConsumeRecharge bean = new AdConsumeRecharge();
				bean.setMainTitle(merchant.getCompany());
				StringBuilder sb = new StringBuilder();
				if (merchant.getDiscount() != null && merchant.getDiscount() > 0) {
					sb.append(merchant.getDiscount() + "折 ");
				}
				if (!StringUtils.isEmpty(merchant.getMaxUserRebate())
						&& new BigDecimal(merchant.getMaxUserRebate()).compareTo(BigDecimal.ZERO) == 1) {
					sb.append("返" + merchant.getMaxUserRebate() + "%");
				}
				bean.setSubTitle(sb.toString());
				bean.setIconUrl(merchant.getLogo());
				bean.setLinkUrl(BASE_URL + dealType + "/" + merchant.getId());
				bean.setSubUrl(BASE_URL.substring(BASE_URL.indexOf("#") + 1, BASE_URL.length()) + dealType + "/"
						+ merchant.getId());
				bean.setIsSupportIntegral(merchant.getIsSupportIntegral());
				beans.add(bean);
			}
		}
		return beans;
	}

}
