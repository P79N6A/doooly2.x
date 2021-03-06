package com.doooly.business.home.v2.servcie.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.business.home.v2.servcie.IndexServiceI;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.FloorTemplateConstants.DooolyRightConstants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.constants.RedisConstants;
import com.doooly.common.constants.VersionConstants;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.reachad.AdProductExtend;
import com.doooly.entity.reachad.*;
import com.reach.redis.annotation.Cacheable;
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
	private static Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

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
	@Autowired
	private AdGroupDao adGroupDao;


    /**
     *  获得花积分页面楼层
     * @author wuzhangyi
     * @date 创建时间：2018-11-11
     * @version 1.0
     * @parameter
     * @since
     * @return
     */
    @Cacheable(module = "TEMPLATE", event = "listSpendIntegralFloors", key = "groupId, address",
            expiresKey = "expires", required = true)
    public String listSpendIntegralFloors(Map<String, String> map) {
        long start = System.currentTimeMillis();

        map.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE + "");

        long bussinessExpores = RedisConstants.REDIS_CACHE_EXPIRATION_DATE;

        try {
            List<AdBasicType> floors = adBasicTypeDao.getFloors(map.get("userId"), AdBasicType.INDEX_TYPE, 0);

            if (CollectionUtils.isEmpty(floors)) {
                return new MessageDataBean("1005", "floors is null").toJsonString();
            }

            Map<String, Object> data = new HashMap<String, Object>();
            List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();

            for (AdBasicType floor : floors) {
                if (floor.getCode() != 20 && floor.getCode() != 21 && floor.getCode() != 23 && floor.getCode() != 24) {
                    Map<String, Object> item = new HashMap<String, Object>();

                    if (floor.getCode() == 25) {
                        List<AdBusinessServicePJ> beans = adBusinessServicePJDao.getDataByUserId(Long.valueOf(map.get("userId")), "2", map.get("address"));

                        if (!CollectionUtils.isEmpty(beans)) {
                            item.put("mainTitle", floor.getName());
                            item.put("isOnline", DEAL_TYPE_OFFLINE);
                            item.put("type", "9");
                            List<AdConsumeRecharge> list = new ArrayList<>(beans.size());

                            // 失效时间
//                            Long date = (System.currentTimeMillis() / 1000) + RedisConstants.REDIS_CACHE_EXPIRATION_DATE;

                            for (AdBusinessServicePJ a : beans) {
                                AdConsumeRecharge adConsumeRecharge = new AdConsumeRecharge();
                                adConsumeRecharge.setMainTitle(a.getServiceName());
                                adConsumeRecharge.setIconUrl(a.getLogo());
                                adConsumeRecharge.setServerEndTime(a.getServerEndTime());

                                if (0 != a.getServiceUrl().indexOf("/")) {
                                    adConsumeRecharge.setLinkUrl(PropertiesHolder.getProperty("BASE_URL") + "/" + a.getServiceUrl());
                                    adConsumeRecharge.setSubUrl("/" + a.getServiceUrl());
                                } else {
                                    adConsumeRecharge.setLinkUrl(PropertiesHolder.getProperty("BASE_URL") + a.getServiceUrl());
                                    adConsumeRecharge.setSubUrl(a.getServiceUrl());
                                }

                                // 如果失效时间大于商户服务结束时间，则修改失效时间为商户服务结束时间
//                                if (date > (adConsumeRecharge.getServerEndTime().getTime()) / 1000) {
//                                    date = (adConsumeRecharge.getServerEndTime().getTime()) / 1000;
//                                }

                                list.add(adConsumeRecharge);
                            }
                            item.put("list", list);
//                            bussinessExpores = date - (System.currentTimeMillis() / 1000);
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

//            map.put("expires", bussinessExpores + "");
//            logger.info("listSpendIntegralFloors>>失效时间====" + bussinessExpores);
            return new MessageDataBean("1000", "success", data).toJsonString();
        } catch (Exception e) {
            e.printStackTrace();
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
        String userId = String.valueOf(redisTemplate.opsForValue().get(userToken));
        String address = params.getString("address");
        // 取有返佣金额的商户
        try {
            logger.info("index() userToken={},userId={},params={},version={}", userToken, userId, params, version);
            List<AdBasicType> floors = adBasicTypeDao.getFloors(userId, AdBasicType.INDEX_TYPE, 2);
            if (CollectionUtils.isEmpty(floors)) {
                return new MessageDataBean("1005", "floors is null").toJsonString();
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

    /**
     * 兜礼权益
     * @return
     */
    @Cacheable(module = "TEMPLATE", event = "selectFloorsByV2_2", key = "groupId, address",
            expiresKey = "expires", required = true)
	@Override
	public String selectFloorsByV2_2(Map<String, String> map) {
		long start = System.currentTimeMillis();
        String userId = map.get("userId");
        String address = map.get("address");
        String groupId = map.get("groupId");

        map.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE + "");

        long guideExpores = RedisConstants.REDIS_CACHE_EXPIRATION_DATE;
//        long bussinessExpores = RedisConstants.REDIS_CACHE_EXPIRATION_DATE;

		try {
			List<AdBasicType> floors = adBasicTypeDao.getFloors(userId, AdBasicType.DOOOLY_RIGHTS_TYPE, 1);

			if (CollectionUtils.isEmpty(floors)) {
				return new MessageDataBean("1005", "floors is null").toJsonString();
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
                    List<AdConsumeRecharge> bussinessList = getBussiness(userId, address,
                            Arrays.asList(DEAL_TYPE_OFFLINE, DEAL_TYPE_ONLINE), VersionConstants.INTERFACE_VERSION_V2_2);


                    floorJson.put("list", bussinessList);

//                    // 失效时间
//                    Long date = (System.currentTimeMillis() / 1000) + RedisConstants.REDIS_CACHE_EXPIRATION_DATE;
//
//                    for (AdConsumeRecharge adConsumeRecharge : bussinessList) {
//                        // 如果失效时间大于商户服务结束时间，则修改失效时间为商户服务结束时间
//                        if (date > (adConsumeRecharge.getServerEndTime().getTime()) / 1000) {
//                            date = (adConsumeRecharge.getServerEndTime().getTime()) / 1000;
//                        }
//                    }
//                    bussinessExpores = date - (System.currentTimeMillis() / 1000);

				} else if (floorType == DooolyRightConstants.FLOOR_TYPE_NEIBUJIA) {
					// 员工内部专享价
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("userId", userId);
                    paramMap.put("guideCategoryId", null);
                    paramMap.put("recommendHomepage", "1");
                    paramMap.put("currentPage", "1");
                    paramMap.put("pageSize", "20");
                    paramMap.put("groupId", groupId);
					MessageDataBean guideData = guideService.getGuideProductListv2(paramMap);

					if (MessageDataBean.success_code.equals(guideData.getCode())) {
						List<AdProduct> datas = (List<AdProduct>) guideData.getData().get("adProducts");
						JSONArray listJson = new JSONArray();
                        guideExpores = Long.valueOf(guideData.getData().get("expires").toString());

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

            if (guideExpores > RedisConstants.REDIS_CACHE_EXPIRATION_DATE) {
                map.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE + "");
            } else {
                map.put("expires", guideExpores + "");
            }
            logger.info("selectFloorsByV2_2>>有效时间====" + map.get("expires"));

            //查询企业信息
            /*AdGroup adGroup = adGroupDao.findGroupByUserId(userId);
            result.put("adGroup",adGroup);*/

			return new MessageDataBean(MessageDataBean.success_code, MessageDataBean.success_mess, result)
					.toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
		}
	}


    /**
     * 兜礼权益
     * @return
     */
    @Cacheable(module = "TEMPLATE", event = "selectFloorsByV3_3", key = "groupId, address",
            expiresKey = "expires", required = true)
    @Override
    public String selectFloorsByV3_3(Map<String, String> map) {
        long start = System.currentTimeMillis();
        String userId = map.get("userId");
        String address = map.get("address");
        String groupId = map.get("groupId");

        map.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE + "");

        long guideExpores = RedisConstants.REDIS_CACHE_EXPIRATION_DATE;
//        long bussinessExpores = RedisConstants.REDIS_CACHE_EXPIRATION_DATE;

        try {
            List<AdBasicType> floors = adBasicTypeDao.getFloors(userId, AdBasicType.DOOOLY_RIGHTS_TYPE, 1);

            if (CollectionUtils.isEmpty(floors)) {
                return new MessageDataBean("1005", "floors is null").toJsonString();
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
                    List<AdConsumeRecharge> bussinessList = getBussiness(userId, address,
                            Arrays.asList(DEAL_TYPE_OFFLINE, DEAL_TYPE_ONLINE), VersionConstants.INTERFACE_VERSION_V2_2);


                    floorJson.put("list", bussinessList);

//                    // 失效时间
//                    Long date = (System.currentTimeMillis() / 1000) + RedisConstants.REDIS_CACHE_EXPIRATION_DATE;
//
//                    for (AdConsumeRecharge adConsumeRecharge : bussinessList) {
//                        // 如果失效时间大于商户服务结束时间，则修改失效时间为商户服务结束时间
//                        if (date > (adConsumeRecharge.getServerEndTime().getTime()) / 1000) {
//                            date = (adConsumeRecharge.getServerEndTime().getTime()) / 1000;
//                        }
//                    }
//                    bussinessExpores = date - (System.currentTimeMillis() / 1000);

                } else if (floorType == DooolyRightConstants.FLOOR_TYPE_NEIBUJIA) {
                    // 员工内部专享价
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("userId", userId);
                    paramMap.put("guideCategoryId", null);
                    paramMap.put("recommendHomepage", "1");
                    paramMap.put("currentPage", "1");
                    paramMap.put("pageSize", "20");
                    paramMap.put("groupId", groupId);
                    MessageDataBean guideData = guideService.getGuideProductListv3(paramMap);

                    if (MessageDataBean.success_code.equals(guideData.getCode())) {
                        List<AdProductExtend> datas = (List<AdProductExtend>) guideData.getData().get("adProducts");
                        JSONArray listJson = new JSONArray();
                        guideExpores = Long.valueOf(guideData.getData().get("expires").toString());

                        for (AdProductExtend product : datas) {
                            itemJson = new JSONObject();
                            itemJson.put("iconUrl", product.getImageWechat());
                            itemJson.put("linkUrl", product.getLinkUrlWechat());
                            itemJson.put("title", product.getName());
                            itemJson.put("marketPrice", product.getMarketPrice());
                            itemJson.put("price", product.getPrice());  //不是品牌馆取折后价
                            itemJson.put("dooolyPrice", product.getDooolyPrice());  //品牌馆取兜礼价格
                            itemJson.put("rebate", product.getRebate());
                            itemJson.put("expressage", product.getBusinessName());
                            itemJson.put("isHot", product.getIsHot());
                            itemJson.put("shippingMethod", product.getShippingMethod());
                            itemJson.put("isStar", product.getIsStar());
                            itemJson.put("userRebate", product.getUserRebate().toString());
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

            if (guideExpores > RedisConstants.REDIS_CACHE_EXPIRATION_DATE) {
                map.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE + "");
            } else {
                map.put("expires", guideExpores + "");
            }
            logger.info("selectFloorsByV2_2>>有效时间====" + map.get("expires"));

            //查询企业信息
            /*AdGroup adGroup = adGroupDao.findGroupByUserId(userId);
            result.put("adGroup",adGroup);*/

            return new MessageDataBean(MessageDataBean.success_code, MessageDataBean.success_mess, result)
                    .toJsonString();
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
        }
    }


	@Override
	public String getUserGroupInfo(String userId) {
        //查询企业信息
        MessageDataBean messageDataBean = new MessageDataBean(MessageDataBean.success_code, MessageDataBean.success_mess);
        Map<String,Object> map = new HashMap<>();
        AdGroup adGroup = adGroupDao.findGroupByUserId(userId);
        if (adGroup != null) {
            map.put("adGroup",adGroup);
        } else {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("单位信息不存在");
        }
        messageDataBean.setData(map);
        return messageDataBean.toJsonString();
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
				bean.setServerEndTime(merchant.getServerEndTime());
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