package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdCouponActivityServiceI;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.publish.rest.life.WholeFamilyRestServiceI;

@Component
@Path("/WholeFamily")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WholeFamilyRestService implements WholeFamilyRestServiceI{
	private static Logger logger = Logger.getLogger(WholeFamilyRestService.class);
	@Autowired
	private AdCouponActivityServiceI adCouponActivityService;
	
	@POST
	@Path(value = "/getActivityInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public JSONObject getActivityInfo(JSONObject obj) {
		JSONObject res = new JSONObject();
		try {
			logger.info("=====================进入dooly接口查询卡券内容===================");
			// 获取活动Id
			String familyActivityId = obj.getString("familyActivityId");
			logger.info("=====================familyActivityId:"+familyActivityId+"===================");
			AdCouponActivity adCouponActivity = adCouponActivityService.getActivityInfo(familyActivityId);
			if(adCouponActivity!=null){
				//活动规则
				res.put("ruleImage", adCouponActivity.getRuleImage());
				//活动开始时间
				res.put("beginDate", adCouponActivity.getBeginDate());
				//活动结束时间
				res.put("endDate", adCouponActivity.getEndDate());
				
				res.put("code", "0");
			}
			logger.info("=====================res:"+res+"===================");
		} catch (Exception e) {
			logger.error("获取活动信息异常！", e);
			res.put("code", "1");
			res.put("msg", "获取活动信息异常");
			e.printStackTrace();
		}
		return res;
	}
	
	
}
