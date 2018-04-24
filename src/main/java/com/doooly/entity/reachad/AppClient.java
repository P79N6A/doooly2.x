package com.doooly.entity.reachad;

import com.doooly.common.constants.Constants.AppClientStatus;

/**
 * client信息POJO类
 * @author 赵清江
 * @date 2016-06-28
 * @version 1.0
 */
public class AppClient {
	
	/**
	 * 客服端类型
	 */
	private int clientType;
	
	private long userId;
	
	/**
	 * 客服端位置
	 */
	private String location;
	/**
	 * 客服端版本
	 */
	private String version;
	/**
	 * 语言类型
	 */
	private String language;
	/** 
	 * 发布渠道
	 */
	private String channel;
	/**
	 * Apk签名MD5值
	 */
	private String apkMd5;
	/**
	 * 设备型号
	 */
	private String deviceModel;
	/**
	 * 设备号码(唯一)
	 */
	private String deviceNumber;
	/**
	 * 操作系统版本
	 */
	private String osVersion;
	/**
	 * sdk版本
	 */
	private String sdkVersion;
	/**
	 * 是否正在使用
	 */
	private AppClientStatus isUsing;
	
	
	
	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}


	public AppClientStatus getIsUsing() {
		return isUsing;
	}


	public void setIsUsing(AppClientStatus isUsing) {
		this.isUsing = isUsing;
	}


	public int getClientType() {
		return clientType;
	}


	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}

	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getApkMd5() {
		return apkMd5;
	}


	public void setApkMd5(String apkMd5) {
		this.apkMd5 = apkMd5;
	}


	public String getDeviceModel() {
		return deviceModel;
	}


	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}


	public String getDeviceNumber() {
		return deviceNumber;
	}


	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}


	public String getOsVersion() {
		return osVersion;
	}


	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}


	public String getSdkVersion() {
		return sdkVersion;
	}


	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}


	@Override
	public String toString() {
		return "AppClient [clientType=" + clientType + ", userId=" + userId + ", location=" + location + ", version="
				+ version + ", language=" + language + ", channel=" + channel + ", apkMd5=" + apkMd5 + ", deviceModel="
				+ deviceModel + ", deviceNumber=" + deviceNumber + ", osVersion=" + osVersion + ", sdkVersion="
				+ sdkVersion + ", isUsing=" + isUsing + "]";
	}
	
	
}
