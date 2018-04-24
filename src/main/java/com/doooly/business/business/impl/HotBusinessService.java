package com.doooly.business.business.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.business.HotBusinessServiceI;
import com.doooly.business.utils.Pagelab;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.common.constants.ResponseCode;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessPrivilegeActivityDao;
import com.doooly.dao.reachad.AdBusinessServicePJDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdadDao;
import com.doooly.dao.reachad.SysDictDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAd;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessServicePJ;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.SysDict;

/**
 * @Description: 首页活动
 * @author: qing.zhang
 * @date: 2017-05-31
 */
@Service
public class HotBusinessService implements HotBusinessServiceI{
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
	private AdBusinessPrivilegeActivityDao adBusinessPrivilegeActivityDao;
	private int HOTBUSINESS = 1;
	private int INDEXADS = 2;
	private int HOTMERCHATADS = 3;
	@Override
	public MessageDataBean getIndexData(Integer userId,Integer type) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
//		if (!StringUtils.isBlank(address)) {
//			Pagelab pagelab = new Pagelab(1, 10);
//			// 查询总数
//			int totalNum = adBusinessDao.getHotTotalNum(userId,address);
//			pagelab.setTotalNum(totalNum);
//			List<AdBusiness> merchants = adBusinessDao.findHotMerchantByPage(userId,address,"",pagelab.getStartIndex(), pagelab.getPageSize());
//			if (!merchants.isEmpty()) {
//				map.put("hotMerchantList", merchants);
//				map.put("countPage", pagelab.getCountPage());
//				messageDataBean.setCode(MessageDataBean.success_code);
//			} else {
//				map.put("hotMerchantList", null);
//				map.put("countPage", 0);
//				messageDataBean.setCode(MessageDataBean.no_data_code);
//			}
//		}else {
//			map.put("countPage", 0);
//			map.put("hotMerchantList", null);
//			messageDataBean.setCode(MessageDataBean.success_code);
//		}
		List<AdAd> ads = adAdDao.findAllByType(INDEXADS,type,userId);
		if (!ads.isEmpty()) {
			map.put("ads", ads);
			messageDataBean.setCode(MessageDataBean.success_code);
		}else {
			map.put("ads", null);
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		AdGroup logo = adGroupDao.getGroupLogoByUserId(userId);
		if (logo != null) {
			map.put("adGroup", logo);
			map.put("groupShortName", logo.getGroupShortName());
		}else {
			map.put("logo", null);
			map.put("groupShortName", null);
		}
		AdUser user = adUserDao.getById(userId+"");
		if (user != null) {
			map.put("type", user.getType());
		}else {
			map.put("type", null);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}
	@Override
	public MessageDataBean getHotMerchatData(Integer userId, String address,Integer type) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isBlank(address)) {
			Pagelab pagelab = new Pagelab(1, 10);
			// 查询总数
			int totalNum = adBusinessDao.getHotTotalNum(userId,address);
			pagelab.setTotalNum(totalNum);
			List<AdBusiness> merchants = adBusinessDao.findHotMerchantByPage(userId,address,"",pagelab.getStartIndex(), pagelab.getPageSize());
			if (!merchants.isEmpty()) {
				map.put("hotMerchantList", merchants);
				map.put("countPage", pagelab.getCountPage());
				messageDataBean.setCode(MessageDataBean.success_code);
			} else {
				map.put("hotMerchantList", null);
				map.put("countPage", 0);
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		}else {
			map.put("countPage", 0);
			map.put("hotMerchantList", null);
			messageDataBean.setCode(MessageDataBean.success_code);
		}
//		List<AdAd> ads = adAdDao.findAllByType(HOTMERCHATADS,type,userId);
//		if (!ads.isEmpty()) {
//			map.put("ads", ads);
//		}else {
//			map.put("ads", null);
//		}
		
		
//		AdGroup logo = adGroupDao.getGroupLogoByUserId(userId);
//		if (logo != null) {
//			map.put("adGroup", logo);
//			map.put("groupShortName", logo.getGroupShortName());
//		}else {
//			map.put("logo", null);
//			map.put("groupShortName", null);
//		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getHotDatas(Integer userId, String address, Integer currentPage, Integer pageSize,String type) {
		// TODO Auto-generated method stub
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Pagelab pagelab = new Pagelab(currentPage,pageSize);
		// 查询总数
		int totalNum = adBusinessDao.getHotTotalNum(userId,address);
		pagelab.setTotalNum(totalNum);
		List<AdBusiness> merchants = adBusinessDao.findHotMerchantByPage(userId,address,type,pagelab.getStartIndex(), pageSize);
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
		List<SysDict> dicts =sysDictDao.findAllByUserIdAndAddress(userId,address);
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
	public MessageDataBean getBusinessInfo(Long adBusinessId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdBusiness adBusiness = adBusinessDao.get(adBusinessId + "");
//		AdGroup logo = adGroupDao.getGroupLogoByUserId(userId.intValue());
//		if (logo != null) {
//			map.put("logo", logo.getLogoUrl());
//			map.put("groupShortName", logo.getGroupShortName());
//		}else {
//			map.put("logo", null);
//			map.put("groupShortName", null);
//		}
		if (adBusiness != null) {
			//获取商家已上架商品id
			Integer productId = adBusinessDao.getMarketableProductId(adBusiness.getId());
			map.put("adBusiness", adBusiness);
			if (productId==null) {
				map.put("productId", 0);
			}else {
				map.put("productId", productId);
			}
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;
	}
	@Override
	public MessageDataBean getBusinessServiceData(Long userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdBusinessServicePJ> adBusinessServicePJs = adBusinessServicePJDao.getDataByUserId(userId );
		if (!adBusinessServicePJs.isEmpty()) {
			map.put("adBusinessServices", adBusinessServicePJs);
			messageDataBean.setData(map);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
			messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
		} else {
			map.put("adBusinessServices", null);
			messageDataBean.setData(map);
			messageDataBean.setCode(SystemCode.SUCCESS_NULL.getCode()+"");
			messageDataBean.setMess(SystemCode.SUCCESS_NULL.getMsg());
		}
		return messageDataBean;
	}

}
