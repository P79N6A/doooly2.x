package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: app版本信息service
 * @author: qing.zhang
 * @date: 2017-07-31
 */
public interface AppVersionRestServiceI {

    String getVersionInfo(JSONObject json);//更新版本信息

    String updateDownloadUrl(JSONObject json);//更新版本下载链接信息

    String getDownloadUrl(JSONObject json);//获取下载链接信息

    String getStartUpUrl(JSONObject json);//获取启动图片链接信息

    String saveRegistrationID(JSONObject json);//保存用户和极光绑定信息
}
