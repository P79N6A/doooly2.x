package com.doooly.publish.rest.reachad.impl;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdAreaServiceI;
import com.doooly.business.common.service.AdSendGiftServiceI;
import com.doooly.common.constants.Constants;
import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdArea;
import com.doooly.entity.reachad.AdBusinessActivityOrder;
import com.doooly.publish.rest.reachad.AdBusinessActivityServiceI;
import com.doooly.publish.rest.reachad.BasicDataServiceI;

@Component
@Path("/basicData")
public class BasicDataService implements BasicDataServiceI {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AdAreaServiceI adAreaServiceI;

	@POST
	@Path(value = "/getAreaInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAreaInfo() {
		MessageDataBean m = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String,Object>();
		try {
			List<AdArea> list = adAreaServiceI.getServicedAreaList();
			if (!list.isEmpty()) {
				map.put("areaInfo", list);
				m.setData(map);
				m.setCode(MessageDataBean.success_code);
				m.setMess(MessageDataBean.success_mess);
			}else {
				map.put("areaInfo", null);
				m.setData(map);
				m.setCode(MessageDataBean.no_data_code);
				m.setMess(MessageDataBean.no_data_mess);
			}
			logger.info("getAreaInfo success=" + m.toJsonString());
		} catch (Exception e) {
			logger.error("getAreaInfo失败"+e);
			m.setCode(MessageDataBean.failure_code);
			m.setMess(MessageDataBean.failure_mess);
		}
		return m.toJsonString();
	}
}
