package com.doooly.publish.rest.app;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
/**
 * 
    * @ClassName: AppErrorLogService  
    * @Description: 记录app(ios/android/h5)错误日志信息
    * 
    * @author hutao  
    * @date 2018年11月16日  
    *
 */
public interface AppErrorLogService {

	/**
	 * 保存错误日志信息
	 * 
	* @author  hutao 
	* @date 创建时间：2018年11月16日 下午2:53:05 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	MessageDataBean insert(JSONObject params,@Context HttpServletRequest request);
}
