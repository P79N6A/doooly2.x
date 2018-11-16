package com.doooly.business.appversion.impl;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.appversion.AppVersionServiceI;
import com.doooly.dao.doooly.AdAppVersionUrlDao;
import com.doooly.dao.doooly.ErrorLogDao;
import com.doooly.dao.reachad.AdAppAuroraPushBindDao;
import com.doooly.dao.reachad.AdAppVersionDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.doooly.AdAppVersionUrl;
import com.doooly.entity.doooly.ErrorLog;
import com.doooly.entity.reachad.AdAppAuroraPushBind;
import com.doooly.entity.reachad.AdAppVersion;
import com.doooly.entity.reachad.AdGroup;

/**
 * @Description: app 版本信息
 * @author: qing.zhang
 * @date: 2017-07-31
 */
@Service
public class AppVersionService implements AppVersionServiceI {
	private static final Logger logger = LoggerFactory.getLogger(AppVersionService.class);

	@Autowired
	private ErrorLogDao errorLogDao;

	@Autowired
	private AdAppVersionDao adAppVersionDao;
	@Autowired
	private AdAppVersionUrlDao adAppVersionUrlDao;
	@Autowired
	private AdGroupDao adGroupDao;
	@Autowired
	private AdAppAuroraPushBindDao adAppAuroraPushBindDao;

	private static final int NEED_UPDATE_STATUS = 1;// 1需要更新
	private static final int NO_NEED_UPDATE_STATUS = 0;// 1需要更新

	@Override
	public MessageDataBean getVersionInfo(String uniqueIdentification, String versionCode, String deviceNumber,
			String deviceName, String type) {
		// 由于用户没开权限获取不到唯一标识这里指定默认值
		if (StringUtils.isBlank(uniqueIdentification)) {
			uniqueIdentification = "0";
		}
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<>();
		AdAppVersion adAppVersion = adAppVersionDao.getVersionInfo(uniqueIdentification, type);
		// 查询更新地址
		AdAppVersionUrl versionInfo = adAppVersionUrlDao.getVersionInfo(type);
		if (adAppVersion == null) {
			adAppVersion = new AdAppVersion();
			adAppVersion.setVersionCode(versionCode);
			adAppVersion.setType(type);
			adAppVersion.setUniqueIdentification(uniqueIdentification);
			adAppVersion.setDeviceName(deviceName);
			adAppVersion.setDeviceNumber(deviceNumber);
			adAppVersionDao.insert(adAppVersion);
			map.put("status", NO_NEED_UPDATE_STATUS);
		} else if (!versionInfo.getVersionCode().equals(versionCode)) {
			// 传入的版本号和存的版本号不一致
			map.put("status", NEED_UPDATE_STATUS);
			// 更新数据库版本号
			adAppVersion.setVersionCode(versionCode);
			adAppVersionDao.update(adAppVersion);
			map.put("downloadUrl", versionInfo.getDownloadUrl());
		} else {
			map.put("status", NO_NEED_UPDATE_STATUS);// 版本号一致无需更新
		}
		messageDataBean.setData(map);
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@Override
	public MessageDataBean updateDownloadUrl(String downloadUrl, String type, String versionCode) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<>();
		// 查询更新地址
		AdAppVersionUrl versionInfo = adAppVersionUrlDao.getVersionInfo(type);
		if (versionInfo == null) {
			versionInfo = new AdAppVersionUrl();
			versionInfo.setDownloadUrl(downloadUrl);
			versionInfo.setType(type);
			versionInfo.setVersionCode(versionCode);
			adAppVersionUrlDao.insert(versionInfo);
		} else if (!versionCode.equals(versionInfo.getVersionCode())) {
			versionInfo.setDownloadUrl(downloadUrl);
			versionInfo.setType(type);
			versionInfo.setVersionCode(versionCode);
			adAppVersionUrlDao.insert(versionInfo);
		}
		messageDataBean.setData(map);
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getDownloadUrl(String type) {
		MessageDataBean messageDataBean = null;
		try {
			messageDataBean = new MessageDataBean();
			HashMap<String, Object> map = new HashMap<>();
			// 查询更新地址
			AdAppVersionUrl versionInfo = adAppVersionUrlDao.getVersionInfo(type);
			logger.info("=====================versionInfo :" + versionInfo + "======================");
			if (versionInfo != null) {
				map.put("downloadUrl", versionInfo.getDownloadUrl());
				map.put("domainName", versionInfo.getDomainName());
				messageDataBean.setCode(MessageDataBean.success_code);
			} else {
				messageDataBean.setCode(MessageDataBean.failure_code);
				messageDataBean.setMess("暂无下载地址");
			}
			messageDataBean.setData(map);
			logger.info("=====================messageDataBean :" + messageDataBean + "======================");
		} catch (Exception e) {
			logger.error("获取下载链接出错", e);
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean getStartUpUrl(String groupNum, String versions) {
		MessageDataBean messageDataBean = null;
		try {
			messageDataBean = new MessageDataBean();
			messageDataBean.setCode(MessageDataBean.success_code);
			HashMap<String, Object> map = new HashMap<>();
			// 查询更新地址
			AdGroup group = adGroupDao.findGroupByGroupNum(groupNum);
			if (group != null && !versions.equals(group.getAppStartUpVerions())) {
				map.put("status", NEED_UPDATE_STATUS);
				map.put("appStartUpUrl", group.getAppStartUpUrl());
				map.put("appStartUpVerions", group.getAppStartUpVerions());
			} else {
				map.put("status", NO_NEED_UPDATE_STATUS);
			}
			messageDataBean.setData(map);
			logger.info("=====================messageDataBean :" + messageDataBean + "======================");
		} catch (Exception e) {
			messageDataBean.setCode(MessageDataBean.failure_code);
			logger.error("获取启动图片链接出错", e);
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean saveRegistrationID(String uniqueIdentification, String registrationId, String userId,
			String type) {
		MessageDataBean messageDataBean = null;
		try {
			messageDataBean = new MessageDataBean();
			messageDataBean.setCode(MessageDataBean.success_code);
			AdAppAuroraPushBind adAppAuroraPushBind = adAppAuroraPushBindDao.findByUserId(userId, registrationId);
			if (adAppAuroraPushBind == null) {
				// 说明没绑定过
				adAppAuroraPushBind = new AdAppAuroraPushBind();
				adAppAuroraPushBind.setRegistrationId(registrationId);
				adAppAuroraPushBind.setUserId(userId);
				adAppAuroraPushBind.setType(type);
				adAppAuroraPushBind.setUniqueIdentification(uniqueIdentification);
				adAppAuroraPushBindDao.saveAuroraPushBind(adAppAuroraPushBind);
			}
			logger.info("=====================messageDataBean :" + messageDataBean + "======================");
		} catch (Exception e) {
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("绑定极光唯一id出错");
			logger.error("保存绑定信息出错", e);
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean saveErrorLog(JSONObject req) {
		String channel = req.getString("clientChannel");
		JSONObject param = req.getJSONObject("param");
		String logStr = param.getString("logStr");
		String terminalModel = param.getString("terminalModel");
		String appVersion = param.getString("appVersion");
		String pageUrl = param.getString("pageUrl");
		Long userId = param.getLong("userId");
		ErrorLog errorLog = new ErrorLog(logStr, channel, terminalModel, appVersion, pageUrl, userId);
		try {
			int result = errorLogDao.insert(errorLog);
		} catch (Exception e) {
			logger.error("保存app异常日志，error=" + e.getMessage(), e);
			return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess);
		}
		return new MessageDataBean(MessageDataBean.success_code, MessageDataBean.success_mess);
	}
}
