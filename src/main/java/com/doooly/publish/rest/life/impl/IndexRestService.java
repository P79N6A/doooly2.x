package com.doooly.publish.rest.life.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.home.v2.servcie.IndexServiceI;
import com.doooly.common.constants.VersionConstants;

/**
 * 兜礼首页接口（微信端/app）
 * 接口版本管理
 */
@Component
@Path("/wechat/indexService")
public class IndexRestService {
	@Autowired
	private IndexServiceI indexService;

	/**
	 * 首页楼层查询
	 * 
	 * 接口v2.2:首页接口新增楼层
	 * 
	* @author  hutao 
	* @date 创建时间：2018年10月23日 下午4:13:36 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/index/v2_2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String indexV2_2(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByV2_2(params, request, VersionConstants.INTERFACE_VERSION_V2_2);
	}
	
	/**
	 * 首页楼层查询
	 * 
	 * 接口v2：新增每日特惠
	 * 
	 * @author  hutao 
	 * @date 创建时间：2018年10月23日 下午4:13:36 
	 * @version 1.0 
	 * @parameter  
	 * @since  
	 * @return
	 */
	@POST
	@Path(value = "/index/v2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String indexV2(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByVersion(params, request, VersionConstants.INTERFACE_VERSION_V2);
	}

	/**
	 * 首页楼层查询
	 * 
	 * 接口v1
	 * 
	* @author  hutao 
	* @date 创建时间：2018年10月23日 下午4:15:10 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/index")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String index(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByVersion(params, request, VersionConstants.INTERFACE_VERSION_V1);
	}
}
