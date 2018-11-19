package com.doooly.publish.rest.app.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.appversion.AppVersionServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.app.AppErrorLogService;

/**
 * 
 * @ClassName: AppErrorLogServiceRest
 * @Description: 记录app(ios/android/h5)错误日志信息
 * @author hutao
 * @date 2018年11月16日
 *
 */
@Component
@Path("/app/error-log")
@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AppErrorLogServiceRest implements AppErrorLogService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AppVersionServiceI appVersionService;

	/**
	 * 保存错误日志信息
	 */
	@POST
	@Path(value = "/save/v1")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public MessageDataBean insert(JSONObject reqJson, @Context HttpServletRequest request) {
		log.info("保存app异常信息请求参数={}", reqJson);
		MessageDataBean result = appVersionService.saveErrorLog(reqJson, request);
		return result;
	}

}
