package com.doooly.business.home.v2.servcie.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.business.home.v2.servcie.IndexServiceI;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.FloorTemplateConstants.DooolyRightConstants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.constants.VersionConstants;
import com.doooly.dao.reachad.AdBasicTypeDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessServicePJDao;
import com.doooly.dao.reachad.AdConsumeRechargeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.*;
import com.doooly.publish.rest.life.impl.IndexRestService;
import com.reach.redis.annotation.EnableCaching;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
@EnableCaching
public class IndexServiceImpl implements IndexServiceI {
	private static Logger logger = LoggerFactory.getLogger(IndexRestService.class);

	private String BASE_URL = PropertiesHolder.getProperty("BASE_URL") + "/businessinfo/";
	private static int DEAL_TYPE_ONLINE = 0;
	private static int DEAL_TYPE_OFFLINE = 1;

	@Autowired
	private AdBasicTypeDao adBasicTypeDao;
	@Autowired
	private AdConsumeRechargeDao adConsumeRechargeDao;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private AdArticleServiceI guideService;
	@Autowired
	private AdBusinessServicePJDao adBusinessServicePJDao;


    /**
     *  获得花积分页面楼层
     * @author wuzhangyi
     * @date 创建时间：2018-11-11
     * @version 1.0
     * @parameter
     * @since
     * @return
     */
    public String listSpendIntegralFloors(JSONObject params, HttpServletRequest request, String version) {
        long start = System.currentTimeMillis();
        String userToken = request.getHeader(Constants.TOKEN_NAME);
        logger.info("selectFloorsByVersion() userToken={}", userToken);
        if (StringUtils.isEmpty(userToken)) {
            return new MessageDataBean("1001", "userToken is null").toJsonString();
        }
        String userId = redisTemplate.opsForValue().get(userToken);
        String address = params.getString("address");
        // 取有返佣金额的商户
        try {
            logger.info("selectFloorsByVersion() userToken={},userId={},params={},version={}", userToken, userId,
                    params, version);
            List<AdBasicType> floors = adBasicTypeDao.getFloors(userId, AdBasicType.INDEX_TYPE, 0);

            if (CollectionUtils.isEmpty(floors)) {
                return new MessageDataBean("1000", "floors is null").toJsonString();
            }

            Map<String, Object> data = new HashMap<String, Object>();
            List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();

            for (AdBasicType floor : floors) {
                if (floor.getCode() != 20 && floor.getCode() != 21 && floor.getCode() != 23 && floor.getCode() != 24) {
                    Map<String, Object> item = new HashMap<String, Object>();

                    if (floor.getCode() == 25) {
                        if (VersionConstants.INTERFACE_VERSION_V2.equalsIgnoreCase(version)) {
                            List<AdBusinessServicePJ> beans = adBusinessServicePJDao.getDataByUserId(Long.valueOf(userId), "2");

                            if (!CollectionUtils.isEmpty(beans)) {
                                item.put("mainTitle", floor.getName());
                                item.put("isOnline", DEAL_TYPE_OFFLINE);
                                item.put("type", "9");

                                List<AdConsumeRecharge> list = new ArrayList<>(beans.size());

                                for (AdBusinessServicePJ a : beans) {
                                    AdConsumeRecharge adConsumeRecharge = new AdConsumeRecharge();
                                    adConsumeRecharge.setMainTitle(a.getServiceName());
                                    adConsumeRecharge.setIconUrl(a.getLogo());
                                    adConsumeRecharge.setSubUrl(a.getServiceUrl());

                                    if (0 != a.getServiceUrl().indexOf("/")) {
                                        adConsumeRecharge.setLinkUrl(PropertiesHolder.getProperty("BASE_URL") + "/" + a.getServiceUrl());
                                    } else {
                                        adConsumeRecharge.setLinkUrl(PropertiesHolder.getProperty("BASE_URL") + a.getServiceUrl());
                                    }

                                    list.add(adConsumeRecharge);
                                }

                                item.put("list", list);
                            }
                        }
                    } else {

                        // 消费卡券/充值缴费数据表
                        List<AdConsumeRecharge> beans = adConsumeRechargeDao.getConsumeRecharges(floor.getTemplateId(),
                                floor.getFloorId());

                        if (!CollectionUtils.isEmpty(beans)) {
                            for (AdConsumeRecharge bean : beans) {
                                bean.setIconUrl(bean.getGuideIconUrl());
                                String linkUrl = bean.getLinkUrl();
                                if (!StringUtils.isEmpty(bean.getLinkUrl()) && linkUrl.indexOf("#") > -1) {
                                    bean.setSubUrl(linkUrl.substring(linkUrl.indexOf("#") + 1, linkUrl.length()));
                                }
                            }
                            item.put("mainTitle", floor.getName());
                            item.put("isOnline", DEAL_TYPE_OFFLINE);

                            if (floor.getFloorId() == 24) {
                                item.put("type", "13");
                            } else {
                                item.put("type", "2");
                            }
                            
                            item.put("list", beans);
                        }
                    }

                    ls.add(item);
                }
            }
            data.put("floors", ls);
            logger.info("selectFloorsByVersion(), execution time = {}", System.currentTimeMillis() - start);
            return new MessageDataBean("1000", "success", data).toJsonString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("selectFloorsByVersion() obj={} exception={}", params, e.getMessage());
            return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
        }
    }


    /**
     *
     * @author hutao
     * @date 创建时间：2018年10月23日 下午3:40:18
     * @version 1.0
     * @parameter
     * @since
     * @return
     */
    public String selectFloorsByVersion(JSONObject params, HttpServletRequest request, String version) {
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
            logger.info("index() userToken={},userId={},params={},version={}", userToken, userId, params, version);
            List<AdBasicType> floors = adBasicTypeDao.getFloors(userId, AdBasicType.INDEX_TYPE, 2);
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
                } else if (floor.getCode() == 23 || floor.getCode() == 24) {
                    if (VersionConstants.INTERFACE_VERSION_V2.equalsIgnoreCase(version)) {
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
                            if(floor.getCode() == 24 ){
                                item.put("type", "4");
                            }else {
                                item.put("type", "3");
                            }
                            item.put("list", beans);
                        }
                    }
                } else {
                    if (floor.getCode() != 25) {
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

	@Override
	public String selectFloorsByV2_2(JSONObject params, HttpServletRequest request, String version) {
		long start = System.currentTimeMillis();
		String userToken = request.getHeader(Constants.TOKEN_NAME);
		logger.info("selectFloorsByV2_2() userToken={}", userToken);
		if (StringUtils.isEmpty(userToken)) {
			return new MessageDataBean("1001", "userToken is null").toJsonString();
		}
		String userId = redisTemplate.opsForValue().get(userToken);
		String address = params.getString("address");
		// 取有返佣金额的商户
		try {
			logger.info("selectFloorsByV2_2() userToken={},userId={},params={},version={}", userToken, userId, params,
					version);
			List<AdBasicType> floors = adBasicTypeDao.getFloors(userId, AdBasicType.DOOOLY_RIGHTS_TYPE, 1);
			if (CollectionUtils.isEmpty(floors)) {
				return new MessageDataBean("1000", "floors is null").toJsonString();
			}

			Map<String, Object> result = new HashMap<>();
			JSONArray floorArrays = new JSONArray();
			JSONObject itemJson = null;
			Integer floorType = null;
			for (AdBasicType floor : floors) {
				floorType = floor.getFloorType();
				JSONObject floorJson = new JSONObject();
				floorJson.put("mainTitle", floor.getName());
				floorJson.put("subTitle", floor.getSubTitle());
				floorJson.put("type", floorType);
				if (floorType == DooolyRightConstants.FLOOR_TYPE_GOUWUTEQUAN) {
					// 兜礼权益商户（线上/线下）
					floorJson.put("list",
							getBussiness(userId, address, Arrays.asList(DEAL_TYPE_OFFLINE, DEAL_TYPE_ONLINE), version));
				} else if (floorType == DooolyRightConstants.FLOOR_TYPE_NEIBUJIA) {
					// 员工内部专享价
					MessageDataBean guideData = guideService.getGuideProductListv2(null, 1, 20, userId, "1");
					if (MessageDataBean.success_code == guideData.getCode()) {
						List<AdProduct> datas = (List<AdProduct>) guideData.getData().get("adProducts");
						JSONArray listJson = new JSONArray();
						for (AdProduct product : datas) {
							itemJson = new JSONObject();
							itemJson.put("iconUrl", product.getImageWechat());
							itemJson.put("linkUrl", product.getLinkUrlWechat());
							itemJson.put("title", product.getName());
							itemJson.put("marketPrice", product.getMarketPrice());
							itemJson.put("dooolyPrice", product.getPrice());
							itemJson.put("rebate", product.getRebate());
							itemJson.put("expressage", product.getBusinessName());
                            itemJson.put("isHot", product.getIsHot());
                            itemJson.put("shippingMethod", product.getShippingMethod());
							listJson.add(itemJson);
						}
						floorJson.put("list", listJson);
					}

				} else {
					List<AdConsumeRecharge> adConsumeRechargeList = adConsumeRechargeDao
							.getConsumeRecharges(floor.getTemplateId(), floor.getFloorId());
					JSONArray listJson = new JSONArray();
					for (AdConsumeRecharge recharge : adConsumeRechargeList) {
						itemJson = new JSONObject();
						itemJson.put("iconUrl", recharge.getIconUrl());
						String linkUrl = recharge.getLinkUrl();
						itemJson.put("linkUrl", linkUrl);
						itemJson.put("mainTitle", recharge.getMainTitle());
						itemJson.put("subTitle", recharge.getSubTitle());
						if (StringUtils.isNotBlank(recharge.getGuideIconUrl())) {
							// 热销品牌导购图
							itemJson.put("guideIconUrl", recharge.getGuideIconUrl());
						}
						if (StringUtils.isNotBlank(linkUrl) && linkUrl.indexOf("#") > -1) {
							itemJson.put("subUrl", linkUrl.substring(linkUrl.indexOf("#") + 1, linkUrl.length()));
						}
						if (floorType == DooolyRightConstants.FLOOR_TYPE_DAOHANG) {
							itemJson.put("cornerMark", recharge.getCornerMark());
						}
						listJson.add(itemJson);
					}
					floorJson.put("list", listJson);
				}
				floorArrays.add(floorJson);
			}
			result.put("floors", floorArrays);
			logger.info("selectFloorsByV2_2(), execution time = {}", System.currentTimeMillis() - start);
			return new MessageDataBean(MessageDataBean.success_code, MessageDataBean.success_mess, result)
					.toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("selectFloorsByV2_2() obj={} exception={}", params, e.getMessage());
			return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
		}
	}

	/**
	 * 查询热门商户
	 * 
	 * @author hutao
	 * @date 创建时间：2018年11月2日 上午11:23:30
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	private List<AdConsumeRecharge> getBussiness(String userId, String address, List<Integer> dealTypeList,
			String version) {
		List<AdBusiness> merchants = adBusinessDao.findHotMerchantsByDealType(Integer.valueOf(userId), null, address,
				dealTypeList);
		List<AdConsumeRecharge> beans = null;
		if (!CollectionUtils.isEmpty(merchants)) {
			beans = new ArrayList<AdConsumeRecharge>();
			for (AdBusiness merchant : merchants) {
				AdConsumeRecharge bean = new AdConsumeRecharge();
				bean.setMainTitle(merchant.getCompany());
				// 前折信息
				String promotionInfo = "";
				// 返利信息
				String rebateInfo = "";
				if (merchant.getDiscount() != null && merchant.getDiscount() > 0) {
					promotionInfo = merchant.getDiscount() + "折 ";
				}
				if (!StringUtils.isEmpty(merchant.getMaxUserRebate())
						&& new BigDecimal(merchant.getMaxUserRebate()).compareTo(BigDecimal.ZERO) == 1) {
					rebateInfo = "返" + merchant.getMaxUserRebate() + "%";
				}
				if (VersionConstants.INTERFACE_VERSION_V2_2.equals(version)) {
					bean.setCornerMark(rebateInfo);
					bean.setMainTitle(merchant.getSubTitle());
					if(StringUtils.isNotBlank(promotionInfo)){
						bean.setSubTitle(promotionInfo.trim()+"起");
					}
				} else {
					bean.setSubTitle(promotionInfo+" "+rebateInfo);
				}
				bean.setIconUrl(merchant.getLogo());

				bean.setLinkUrl(BASE_URL + merchant.getDealType() + "/" + merchant.getId());
				bean.setSubUrl(BASE_URL.substring(BASE_URL.indexOf("#") + 1, BASE_URL.length()) + merchant.getDealType()
						+ "/" + merchant.getId());
				bean.setIsSupportIntegral(merchant.getIsSupportIntegral());
				beans.add(bean);
			}
		}
		return beans;
	}

    private List<AdConsumeRecharge> getBussiness(String userId, String address, int dealType) {
        List<AdBusiness> merchants = adBusinessDao.findHotMerchantsByDealType(Integer.valueOf(userId), null, address,
                Arrays.asList(dealType));
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
