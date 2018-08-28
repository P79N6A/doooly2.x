package com.doooly.business.scanDiscount.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.scanDiscount.ScanDiscountServiceI;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
public class ScanDiscountService implements ScanDiscountServiceI{
	private static Logger logger = Logger.getLogger(ScanDiscountService.class);
	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private AdGroupDao adGroupDao;
	@Autowired
	private AdUserDao adUserDao;
	@Override
	public HashMap<String, Object> getBusinessList(JSONObject data) {
		
		HashMap<String, Object> res = new HashMap<>();
		String userId = data.getString("userId");
//		Integer i = adUserDao.isDongHang(Integer.valueOf(userId));
//		if (i ==null) {
			res.put("isDongHang", 0);
//		}else {
//			res.put("isDongHang", 1);
//		}
		List<AdBusiness> list = adBusinessDao.findBusinessList(userId);
		AdGroup adGroup = adGroupDao.getGroupLogoByUserId(Integer.valueOf(userId));
		if(list != null && list.size() > 0){
			res.put("businessList", list);
			res.put("code", "1000");
			res.put("groupShortName", adGroup.getGroupShortName());
		}else{
			res.put("businessList", null);
			res.put("code", "1001");
			res.put("groupShortName", adGroup.getGroupShortName());
		}
		return res;
		
	}
	@Override
	public MessageDataBean getScanDiscount(JSONObject data) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String userId = data.getString("userId");
		String company = data.getString("company");
		String groupShortName = data.getString("groupShortName");
		String businessId = data.getString("businessId");
		String miniLogo = data.getString("miniLogo");
		if (StringUtils.isNotBlank(userId)
				&&StringUtils.isNotBlank(company)
				&&StringUtils.isNotBlank(groupShortName)
				&&StringUtils.isNotBlank(businessId)
				&&StringUtils.isNotBlank(miniLogo)) {
			
			AdUser adUser = adUserDao.getById(Integer.valueOf(userId));
			BigDecimal availablePoint = adUserDao.getAvailablePoint(userId);
			if (company.equals("大润发")) {
				map.put("dimensionCode", adUser.getCardNumber());
				map.put("dimensionCodeForShow",adUser.getCardNumber() );
				map.put("company", company);
				map.put("groupShortName", groupShortName);
				map.put("businessId", businessId);
				map.put("miniLogo", miniLogo);
				map.put("userId", userId);
				map.put("availablePoint",availablePoint!=null ? availablePoint : new BigDecimal("0"));
			}else {
				String url = WebService.WEBURL+"services/rest/makeVerificationCode";
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("businessId", PropertiesConstants.dooolyBundle.getString("businessId"));
				jsonObject.put("storesId", "WX_Reachlife");
				jsonObject.put("cardNumber", adUser.getCardNumber());
				jsonObject.put("isCheck", 0);
                logger.info("调用获取短信验证码路径: " + url);
				String result = HTTPSClientUtils.sendPostNew(jsonObject.toJSONString(), url);
				JSONObject resultJSON = JSONObject.parseObject(result);
				logger.info("调用获取短信验证码返回结果: " + resultJSON);
//				map.put("code", resultJSON.getString("code"));
//				map.put("info", resultJSON.getString("info"));
				
				if (resultJSON.containsKey("VerificationCode")) {
					map.put("msgCode", resultJSON.getString("VerificationCode"));
				}
				if (resultJSON.containsKey("VerificationCode18")) {
					map.put("dimensionCode", resultJSON.getString("VerificationCode18"));
					StringBuffer buffer = new StringBuffer((String) resultJSON.getString("VerificationCode18"));
					buffer.insert(4, " ");
					buffer.insert(9, " ");
					buffer.insert(14, " ");
					map.put("dimensionCodeForShow",buffer );
				}
				map.put("company", company); 
				map.put("groupShortName", groupShortName);
				map.put("businessId", businessId);
				map.put("miniLogo", miniLogo);
				map.put("userId", userId);
				map.put("availablePoint", availablePoint!=null ? availablePoint : new BigDecimal("0"));
			}
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		}else {
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

}
