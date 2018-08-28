package com.doooly.business.lightenBusiness.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.lightenBusiness.AdLightenBusinessServiceI;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdLightenBusinessDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessLighten;
import com.doooly.entity.reachad.AdUser;

@Service 
@Transactional
public class AdLightenBusinessService implements AdLightenBusinessServiceI{
	//app上线时间
	private final String APP_ONLINE_TIME = PropertiesConstants.dooolyBundle.getString("app_online_time");  
	
	//可点亮次数	
	private static final Integer NUMBER_LIGHT = 3;
	@Autowired
	private AdLightenBusinessDao adLightenBusinessDao;
	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private AdUserDao adUserDao;

	/**
	 * 获取所有商户
	 */
	@Override
	public JSONObject getAllBusiness(JSONObject obj) {
		JSONObject res = new JSONObject();
		try {
			String userId = obj.getString("userId");
			String address = obj.getString("address");
			int flag = obj.getInteger("flag");
			if(flag == 1 ){
				List<AdBusiness> list = adBusinessDao.findAllBusinessByUserId(userId);
				if(list.size()>0){
					res.put("code", "1000");
					res.put("adBusniessList", list);
					return res;
				}else{
					res.put("code", "1001");
					res.put("dsec", "没有查询到商户");
					return res;
				}
			}
			//查询用户是否已经点亮过商户，如果点亮过商户直接跳过开启点亮过程
			List<AdBusinessLighten> adBusinessLightenList =  adLightenBusinessDao.getByUserIdIsLingten(userId);
			if(adBusinessLightenList.size()>0){
				res.put("code", "1005");
				return res;
			}
			List<AdBusiness> list = adBusinessDao.findAllBusinessLogo(address);
			if(list.size()>0){
				res.put("code", "1000");
				res.put("adBusniessList", list);
			}else{
				res.put("code", "1001");
			}
		} catch (Exception e) {
			res.put("code", "4000");
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 开启折扣之旅
	 */
	@Override
	public JSONObject lightenBusiness(JSONObject obj) {
		System.out.println(APP_ONLINE_TIME);
		JSONObject res = new JSONObject();
		try {
			String userId = obj.getString("userId");
			String businessIds = obj.getString("businessIdList");
			List<String> businessIdList = JSONObject.parseArray(businessIds, String.class);
			//如果为老用户则把所有商户均点亮
			//查询该用户是否在app上线之前注册的
			AdUser adUser = adUserDao.getById(Integer.valueOf(userId));
			Date appDate = new SimpleDateFormat("yyyy-MM-dd").parse(APP_ONLINE_TIME);
			if(adUser.getCreateDate().before(appDate)){
				List<AdBusiness> businessList = adBusinessDao.findAllBusinessAllLogo();
				List<String> businessIdAllList = new ArrayList<String>();
				for (AdBusiness adBusiness : businessList) {
					businessIdAllList.add(adBusiness.getId().toString());
				}
				adLightenBusinessDao.lightenBusiness(userId, businessIdAllList);
				res.put("code", "1000");
			}else{
				//选择的商户超过3个
				if(businessIdList.size()>3){
					res.put("code", "1002");
				}else{
				//新用户直接把选择的商户点亮
					adLightenBusinessDao.lightenBusiness(userId,businessIdList);
					res.put("code", "1000");
				}
			}
		} catch (Exception e) {
			res.put("code", "4000");
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 预约点亮商户或者直接点亮商户
	 */
	@Override
	public JSONObject reservationOrLightenBusiness(JSONObject obj) {
		JSONObject res = new JSONObject();
		
		try {
			String userId = obj.getString("userId");
			String businessId = obj.getString("businessId");
			String type = obj.getString("type");
			AdBusinessLighten adBusinessLighten = new AdBusinessLighten();
			adBusinessLighten.setBusinessId(businessId);
			adBusinessLighten.setCreateTime(new Date());
			adBusinessLighten.setUserId(userId);
			adBusinessLighten.setDelFlag("0");
			adBusinessLighten.setLightenTime(new Date());
			adBusinessLighten.setUpdateTime(new Date());
			adBusinessLighten.setType(Integer.valueOf(type));
			adLightenBusinessDao.insert(adBusinessLighten);
			res.put("code", "1000");
		} catch (Exception e) {
			res.put("code", "4000");
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 根据userId和businessId返回状态，是否可以点亮商户或者可以预约点亮商户
	 * @param userId
	 * @param businessId
	 * @return
	 */
	public Integer  lightenBusinessType(String userId,String businessId){
		AdBusinessLighten adBusinessLighten =null;
		if(userId!=null && StringUtils.isNotBlank(userId) && businessId != null && StringUtils.isNotBlank(businessId)){
			//通过用户ID查询已经点亮商户记录表中有几条记录
			List<AdBusinessLighten> adBusinessLightenList =  adLightenBusinessDao.getByUserId(userId);
			//查询出的记录
			if(adBusinessLightenList.size() < NUMBER_LIGHT){
				//小于可点亮商户数量，则表示还可以进行点亮
				//查询该商户是否已经被点亮
				adBusinessLighten = adLightenBusinessDao.lightenBusinessType(userId,businessId);
				//没有记录表示该商户可以被点亮
				if(adBusinessLighten == null){ 
					return 1;
				}else{
					return 2;
				}
			}else{
				//查询的记录大于可点亮商户次数则表示用户没有点亮机会，只有预约点亮权限
				adBusinessLighten = adLightenBusinessDao.lightenBusinessType(userId,businessId);
				//type=1
				//没有记录
				if(adBusinessLighten == null ){
					return 0;
				}else {
					if (adBusinessLighten.getType()==0) {
						return 3;
					}else {
						return 2;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 获取商户详情
	 */
	@Override
	public JSONObject getBusinessDeatil(JSONObject obj) {
		JSONObject res = new JSONObject();
		try {
			String id = obj.getString("businessId");
			AdBusiness adBusiness = adBusinessDao.get(id);
			if(adBusiness!=null){
				res.put("code","1000");
				res.put("adBusiness", adBusiness);
			}else{
				res.put("code","1001");
			}
		} catch (Exception e) {
			res.put("code","4000");
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public MessageDataBean lightSearch(String businessId, String userId) {
		// TODO Auto-generated method stub
		MessageDataBean m= new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			Integer lightenBusinessType = this.lightenBusinessType(userId, businessId);
			m.setCode(MessageDataBean.success_code);
			map.put("lightenType", lightenBusinessType);
			m.setData(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			m.setCode(MessageDataBean.failure_code);
			e.printStackTrace();
		}
		return m;
	}

}
