package com.doooly.business.appversion;

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
}
