package com.doooly.publish.rest.life.impl;

import java.util.Date;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.selfApplication.SelfApplicationServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.SelfApplicationRestServiceI;

/**
 * 答题活动 rest service实现
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
@Component
@Path("/selfApplication")
public class SelfApplicationRestService implements SelfApplicationRestServiceI {

	private static Logger logger = Logger.getLogger(SelfApplicationRestService.class);

	@Autowired
	private SelfApplicationServiceI selfApplicationServiceI;

	@POST
	@Path(value = "/index")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String index(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			messageDataBean = selfApplicationServiceI.getAllCompany();
		} catch (Exception e) {
			logger.error("员工自主申请获取所有公司报错！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/departmentDatas")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String departmentDatas(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取所选择的公司id
			String companyId = obj.getString("companyId");
			logger.info("员工自主申请时根据所选企业获取部门时的groupId为"+companyId+"=======");
			messageDataBean = selfApplicationServiceI.getDepartmentDatasByGroupId(companyId);
		} catch (Exception e) {
			logger.error("员工自主申请时根据所选企业获取部门时报错！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	@POST
	@Path(value = "/submit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String submit(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
//			data.put("companyId", companyId);
//			data.put("departmentId", departmentId);
//			data.put("name", name);
//			data.put("telephone",telephone );
//			data.put("workNumber",workNumber );
//			data.put("sex", sex);
//			data.put("hiredate", hiredate);
			String companyId = obj.getString("companyId");
			String departmentId = obj.getString("departmentId");
			String name = obj.getString("name");
			String telephone = obj.getString("telephone");
			String workNumber = obj.getString("workNumber");
			String sex = obj.getString("sex");
			Date hiredate = obj.getDate("hiredate");
			// 返回验证结果
			messageDataBean = selfApplicationServiceI.submitData(companyId,departmentId,name,telephone,workNumber
					,sex,hiredate);
		} catch (Exception e) {
			logger.error("获取验证答题结果异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
