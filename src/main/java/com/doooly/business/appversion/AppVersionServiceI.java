package com.doooly.business.appversion;


import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * @Description: app 版本
 * @author: qing.zhang
 * @date: 2017-07-31
 */
public interface AppVersionServiceI {
    MessageDataBean getVersionInfo(String uniqueIdentification, String versionCode, String deviceNumber, String deviceName, String type);

    MessageDataBean updateDownloadUrl(String downloadUrl, String type,String versionCode);

    MessageDataBean getDownloadUrl(String type);

    MessageDataBean getStartUpUrl(String groupNum,String versions);

    MessageDataBean saveRegistrationID(String uniqueIdentification, String registrationId, String userId, String type);
    /**
     * 保存错误日志信息
    * @author  hutao 
    * @date 创建时间：2018年11月16日 下午3:02:57 
    * @version 1.0 
    * @parameter  
    * @since  
    * @return
     */
	MessageDataBean saveErrorLog(JSONObject req, HttpServletRequest request);
    
    
}
