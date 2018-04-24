package com.doooly.publish.rest.life;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

/**
 * 分享朋友,分享朋友圈 接口
 * 
 * @author wang
 */
public interface ShareWithFriendsServiceI {

	/***
	 * 
	 * 分享标题  title:   "滴滴出行-300元礼包大放送" 
	 * 分享描述  desc:    "关爱出行，全家共享。分秒就接驾，送你300元打车大礼包，即刻出发吧！"  
	 * 分享链接  link:    "igushi-sh.com/"  
	 * 分享图标  imgUrl:  "http://igushi-sh.com/images/shareImg.jpg"  
	 * 
	 * 分享类型  type:    "link"  (music、video或link，不填默认为link) 
	 * 数据链接  dataUrl: ""      (如果type是music或video，则要提供数据链接，默认为空)
	 * 
	 * @param request
	 * @param sharInfo
	 * @return
	 */
	public String commonShareConfig( HttpServletRequest request,HttpServletResponse response);
	
	public String commonShareJSONPConfig(@Context HttpServletRequest request,@Context HttpServletResponse response);

}
