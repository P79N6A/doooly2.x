package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.impl.AdUserService;
import com.doooly.business.familyInviteApp.AdInvitationService;
import com.doooly.publish.rest.life.FamilyInviteAppRestServiceI;

@Component
@Path("/familyInviteApp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FamilyInviteAppRestService implements FamilyInviteAppRestServiceI {
	@Autowired
	private AdInvitationService adInvitationService;
	@Autowired
	private AdUserService adUserService;
	
	private static Logger logger = Logger.getLogger(FamilyInviteAppRestService.class);
	
	/**
	 * 获取邀请人相关信息（邀请码/已邀请人列表等）
	 */
	@POST
	@Path(value = "/myInvitation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public JSONObject myInvitation(JSONObject jsonReq) {
		JSONObject resjson = new JSONObject();
		try {
			// 1.参数有消息验证

			// 2.获取邀请人相关信息
			resjson = adInvitationService.getInvitationDetail(jsonReq);
			resjson.put("code", 0);
			resjson.put("desc", "OK");
		} catch (Exception e) {
			logger.error("获取会员邀请信息出错", e);
			e.printStackTrace();
			resjson.put("code", -1);
			resjson.put("desc", "获取会员邀请信息出错，" + e.getMessage());
		}
		return resjson;
	}
	
	@POST
	@Path(value = "/checkTelephone")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public JSONObject checkTelephone(@RequestBody JSONObject data) {
		JSONObject result = new JSONObject();
		try {
			result = adUserService.checkTelephone(data);
		} catch (Exception e) {
			result.put("code", "1");
			result.put("info", "服务器异常");
			e.printStackTrace();
		}
		return result;
	}
	
	
	@POST
	@Path(value = "/getGroupInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public JSONObject getGroupInfo(JSONObject jsonObj) {
		// 1.获取用户企业
		JSONObject resultJson = new JSONObject();
		try {
			String userId = jsonObj.getString("userId");
			
			resultJson = adInvitationService.getGroupInfo(userId);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", -1);
			resultJson.put("desc", "获取用户企业失败!");
		}
		return resultJson;
	}
	
	/**
	 * 
	 * @param reqJson
	 * @return
	 */
	@POST
	@Path(value = "/saveUserNotActive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public JSONObject saveUserNotActive(JSONObject jsonObj) {
		// 1.完成家属会员注册及激活
		JSONObject resultJson = new JSONObject();
		try {
			String invitationCode = jsonObj.getString("invitationCode");
			Integer invitationType = jsonObj.getInteger("invitationType");
			String famMobile = jsonObj.getString("famMobile");
			String famPassword = jsonObj.getString("password");
			String name = jsonObj.getString("name");
			resultJson = adInvitationService.saveUserNotActive(famMobile,
					invitationCode, invitationType, famPassword, name);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", -1);
			resultJson.put("desc", "家属创建失败!");
		}
		return resultJson;
	}
	
	
	
	@POST
	@Path(value = "/getMyFamilyInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public JSONObject getMyFamilyInfo(@RequestBody JSONObject jsonObj){
		// 1.获取我的家属信息
				JSONObject resultJson = new JSONObject();
				try {
					String userId = jsonObj.getString("userId");
					
					resultJson = adInvitationService.getMyFamilyInfo(userId);
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", -1);
					resultJson.put("desc", "获取我的家属信息失败!");
				}
				return resultJson;
	}
	
}
