package com.doooly.entity.home;

/**
 * @className: SplashScreenDataContract
 * @description: 兜礼APP闪屏页信息
 * @author: wangchenyu
 * @date: 2018-03-28 13:27
 */
public class SplashScreenDataContract {
	/**
	 * ad_group表的主键
	 */
	private Long groupId;
	/**
	 * app开机启动图片链接地址
	 */
	private String appStartUpUrl;
	/**
	 * app开机启动图片链接地址版本号
	 */
	private String appStartUpVerions;
	/**
	 * 获取的时间戳
	 */
	private Long timestamp;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getAppStartUpUrl() {
		return appStartUpUrl;
	}

	public void setAppStartUpUrl(String appStartUpUrl) {
		this.appStartUpUrl = appStartUpUrl;
	}

	public String getAppStartUpVerions() {
		return appStartUpVerions;
	}

	public void setAppStartUpVerions(String appStartUpVerions) {
		this.appStartUpVerions = appStartUpVerions;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
