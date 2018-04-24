package com.doooly.publish.rest.life;


import javax.servlet.ServletContext;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


/**
 * App下载接口
 * @author 赵清江
 * @date 2016年7月20日
 * @version 1.0
 */
public interface AppRestServiceI {
	/**
	 * 下载APP接口
	 * @return
	 */
	public Response download(@Context ServletContext application);
	/**
	 * 上传APP接口(后台管理系统使用)
	 * @return
	 */
	public String upload();
	/**
	 * 更新APP接口
	 * @return
	 */
	public Response update(@PathParam("version") String version);
	
}
