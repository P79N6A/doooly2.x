package com.doooly.business.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.business.HotBusinessServiceI;
import com.doooly.business.utils.Pagelab;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.dao.reachad.*;
import com.doooly.dao.report.EnterpriseAccountResultDao;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.*;
import com.doooly.entity.report.EnterpriseAccountResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 首页活动
 * @author: qing.zhang
 * @date: 2017-05-31
 */
@Service
public class HotBusinessService implements HotBusinessServiceI {
	private static final Logger logger = LoggerFactory.getLogger(HotBusinessService.class);
	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private AdGroupDao adGroupDao;
	@Autowired
	private SysDictDao sysDictDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdadDao adAdDao;
	@Autowired
	private AdBusinessServicePJDao adBusinessServicePJDao;
	@Autowired
	private EnterpriseAccountResultDao enterpriseAccountResultDao;
	@Autowired
	protected StringRedisTemplate redisTemplate;
	// @Autowired
	// private AdBusinessPrivilegeActivityDao adBusinessPrivilegeActivityDao;
	// private int HOTBUSINESS = 1;
	private int INDEXADS = 2;
	// private int HOTMERCHATADS = 3;

	@Override
	public MessageDataBean getIndexData(Integer userId, Integer type, Integer adType) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// if (!StringUtils.isBlank(address)) {
		// Pagelab pagelab = new Pagelab(1, 10);
		// // 查询总数
		// int totalNum = adBusinessDao.getHotTotalNum(userId,address);
		// pagelab.setTotalNum(totalNum);
		// List<AdBusiness> merchants =
		// adBusinessDao.findHotMerchantByPage(userId,address,"",pagelab.getStartIndex(),
		// pagelab.getPageSize());
		// if (!merchants.isEmpty()) {
		// map.put("hotMerchantList", merchants);
		// map.put("countPage", pagelab.getCountPage());
		// messageDataBean.setCode(MessageDataBean.success_code);
		// } else {
		// map.put("hotMerchantList", null);
		// map.put("countPage", 0);
		// messageDataBean.setCode(MessageDataBean.no_data_code);
		// }
		// }else {
		// map.put("countPage", 0);
		// map.put("hotMerchantList", null);
		// messageDataBean.setCode(MessageDataBean.success_code);
		// }
		int pageType = INDEXADS;
		if (null != adType && adType == 1) {
			pageType = 8;
		}
		List<AdAd> ads = adAdDao.findAllByType(pageType, type, userId);
		if (!ads.isEmpty()) {
			map.put("ads", ads);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			map.put("ads", null);
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		AdGroup logo = adGroupDao.getGroupLogoByUserId(userId);
		if (logo != null) {
			map.put("adGroup", logo);
			map.put("groupShortName", logo.getGroupShortName());
		} else {
			map.put("logo", null);
			map.put("groupShortName", null);
		}
		AdUser user = adUserDao.getById(userId + "");
		if (user != null) {
			map.put("type", user.getType());
		} else {
			map.put("type", null);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getHotMerchatData(Integer userId, String address, Integer type, Integer shopType) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isBlank(address)) {
			Pagelab pagelab = new Pagelab(1, 10);
			// 查询总数
			int totalNum = adBusinessDao.getHotTotalNum(userId, address, String.valueOf(type), shopType);
			pagelab.setTotalNum(totalNum);
			List<AdBusiness> merchants = adBusinessDao.findHotMerchantByPage(userId, address, "",
					pagelab.getStartIndex(), pagelab.getPageSize(), shopType);
			if (!merchants.isEmpty()) {
				map.put("hotMerchantList", merchants);
				map.put("countPage", pagelab.getCountPage());
				messageDataBean.setCode(MessageDataBean.success_code);
			} else {
				map.put("hotMerchantList", null);
				map.put("countPage", 0);
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		} else {
			map.put("countPage", 0);
			map.put("hotMerchantList", null);
			messageDataBean.setCode(MessageDataBean.success_code);
		}
		// List<AdAd> ads = adAdDao.findAllByType(HOTMERCHATADS,type,userId);
		// if (!ads.isEmpty()) {
		// map.put("ads", ads);
		// }else {
		// map.put("ads", null);
		// }

		// AdGroup logo = adGroupDao.getGroupLogoByUserId(userId);
		// if (logo != null) {
		// map.put("adGroup", logo);
		// map.put("groupShortName", logo.getGroupShortName());
		// }else {
		// map.put("logo", null);
		// map.put("groupShortName", null);
		// }
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getHotDatas(Integer userId, String address, Integer currentPage, Integer pageSize,
			String type, Integer shopType) {
		// TODO Auto-generated method stub
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Pagelab pagelab = new Pagelab(currentPage, pageSize);
		// 查询总数
		int totalNum = adBusinessDao.getHotTotalNum(userId, address, type, shopType);
		pagelab.setTotalNum(totalNum);
		List<AdBusiness> merchants = adBusinessDao.findHotMerchantByPage(userId, address, type, pagelab.getStartIndex(),
				pageSize, shopType);
		if (!merchants.isEmpty()) {
			map.put("hotMerchantList", merchants);
			map.put("countPage", pagelab.getCountPage());
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			map.put("hotMerchantList", null);
			map.put("countPage", 0);
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getDictDatas(Integer userId, String address) {
		// TODO Auto-generated method stub
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<SysDict> dicts = sysDictDao.findAllByUserIdAndAddress(userId, address);
		if (!dicts.isEmpty()) {
			map.put("dicts", dicts);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			map.put("dicts", null);
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getBusinessInfo(Long adBusinessId, String token) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdBusiness adBusiness = adBusinessDao.get(adBusinessId + "");
		// AdGroup logo = adGroupDao.getGroupLogoByUserId(userId.intValue());
		// if (logo != null) {
		// map.put("logo", logo.getLogoUrl());
		// map.put("groupShortName", logo.getGroupShortName());
		// }else {
		// map.put("logo", null);
		// map.put("groupShortName", null);
		// }
		if (adBusiness != null) {
			// 获取商家已上架商品id
			Integer productId = adBusinessDao.getMarketableProductId(adBusiness.getId());
			map.put("adBusiness", adBusiness);
			if (productId == null) {
				map.put("productId", 0);
			} else {
				map.put("productId", productId);
			}
			// 如果是唯品会,校验会员同步状态
			if (adBusiness.getCompany().contains("唯品会")) {
				// 获取同步结果
				EnterpriseAccountResult resultParam = new EnterpriseAccountResult();
				// 查询用户手机号,激活时间
				AdUser adUser = new AdUser();
				adUser.setId(Long.valueOf(redisTemplate.opsForValue().get(token).toString()));
				adUser = adUserDao.getUserActiveInfo(adUser);
				resultParam.setPhoneNo(adUser.getTelephone());
				EnterpriseAccountResult result = enterpriseAccountResultDao.getResult(resultParam);
				if (result != null) {
					logger.info("====>>企业会员升级结果-result：" + result.getResultCode() + ",==msg：" + result.getResultDesc());
					if ("001".equals(result.getResultCode())) {
						adBusiness.setUpGradeState(Constants.VIP_VOP_EMPLOYEE_CODE_SUCCESS);
					} else {
						//返回码002并不都是升级失败的，只有以下错误信息可以认定为升级失败
						if(Constants.VIP_VOP_EMPLOYEE_ERROR_LIST.contains(result.getResultDesc())){
							adBusiness.setUpGradeState(Constants.VIP_VOP_EMPLOYEE_CODE_FAILURE);
						}else{
							//指定兜礼会员在唯品会的企业ID为16，如果有此异常，认定为升级成功；否则是在唯品会的其它企业，认定升级失败
							if(StringUtils.isNotEmpty(result.getResultDesc()) && result.getResultDesc().contains(Constants.VIP_VOP_ENTERPRISE_EXIST_DESC) && !Constants.VIP_VOP_EMPLOYEE_SUCCESS_DESC.equals(result.getResultDesc())){
								adBusiness.setUpGradeState(Constants.VIP_VOP_EMPLOYEE_CODE_FAILURE);
							}else{
								adBusiness.setUpGradeState(Constants.VIP_VOP_EMPLOYEE_CODE_SUCCESS);
							}
						}
					}
				} else {
					// 无同步记录,验证激活时间是否在
					logger.info("====>>用户激活结果-无同步记录,激活时间是否超出三天：" + adUser.getSyncFlag() + ",==同步开始时间："
							+ adUser.getSyncBeginDate() + ",==激活时间：" + adUser.getActiveDateStr());
					if ("true".equals(adUser.getSyncFlag())) {
						//由于2017-3之前导入唯品会的数据无法通过唯品会大企业员工升级接口查询，只能默认升级成功
						adBusiness.setUpGradeState(Constants.VIP_VOP_EMPLOYEE_CODE_SUCCESS);
					} else {
						//升级中
						adBusiness.setUpGradeState("2");
					}
				}
			}
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;
	}

	/**
	 * 2.0的接口
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public MessageDataBean getBusinessServiceData(Long userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdBusinessServicePJ> adBusinessServicePJs = adBusinessServicePJDao.getDataByUserId(userId, null);
		if (!adBusinessServicePJs.isEmpty()) {
			map.put("adBusinessServices", adBusinessServicePJs);
			messageDataBean.setData(map);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode() + "");
			messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
		} else {
			map.put("adBusinessServices", null);
			messageDataBean.setData(map);
			messageDataBean.setCode(SystemCode.SUCCESS_NULL.getCode() + "");
			messageDataBean.setMess(SystemCode.SUCCESS_NULL.getMsg());
		}
		return messageDataBean;
	}

	/***
	 * 2.1的接口
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public MessageDataBean getBusinessServiceDataV21(Long userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdBusinessServicePJ> list1 = adBusinessServicePJDao.getDataByUserId(userId, "1");
		List<AdBusinessServicePJ> list2 = adBusinessServicePJDao.getDataByUserId(userId, "2");
		if (!CollectionUtils.isEmpty(list1) || !CollectionUtils.isEmpty(list2)) {
			map.put("list1", list1);
			map.put("list2", list2);
			messageDataBean.setData(map);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode() + "");
			messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
		} else {
			map.put("list1", null);
			map.put("list2", null);
			messageDataBean.setData(map);
			messageDataBean.setCode(SystemCode.SUCCESS_NULL.getCode() + "");
			messageDataBean.setMess(SystemCode.SUCCESS_NULL.getMsg());
		}

		return messageDataBean;
	}

	/**
	 * 2.0可用积分服务-商户详情
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public MessageDataBean getBusinessInfoService(JSONObject params) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			JSONObject jsonData = new JSONObject();
			String channel = params.getString(ConstantsLogin.CHANNEL);
			String businessId = params.getString("id");
			AdBusiness adBusiness = adBusinessDao.get(businessId);
			if (adBusiness != null) {
				// 获取商家已上架商品id
				Integer productId = adBusinessDao.getMarketableProductId(adBusiness.getId());
				jsonData.put("adBusiness", adBusiness);
				if (productId == null) {
					jsonData.put("productId", 0);
				} else {
					jsonData.put("productId", productId);
				}
				// 请求终端区分
				if (channel.contains("app")) {
					jsonData.put(ConstantsLogin.CHANNEL, 1);
				} else {
					jsonData.put(ConstantsLogin.CHANNEL, 0);
				}
				messageDataBean.setJsonData(jsonData);
				messageDataBean.setCode(MessageDataBean.success_code);
				messageDataBean.setMess(MessageDataBean.success_mess);
			} else {
				messageDataBean.setCode(MessageDataBean.no_data_code);
				messageDataBean.setMess("商户数据为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageDataBean;
	}
}
