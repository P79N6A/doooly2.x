package com.doooly.entity.doooly;

public class ErrorLog {
	//错误日志串
	private String logStr;
	//渠道H5/iOS/Android
	private String channel;
	//终端机型
	private String terminalModel;
	//app版本，如v2.1.6
	private String appVersion;
	//页面url
	private String pageUrl;
	private Long userId;
	
	public ErrorLog(String logStr, String channel, String terminalModel, String appVersion, String pageUrl,
			Long userId) {
		super();
		this.logStr = logStr;
		this.channel = channel;
		this.terminalModel = terminalModel;
		this.appVersion = appVersion;
		this.pageUrl = pageUrl;
		this.userId = userId;
	}
	public ErrorLog() {
		super();
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLogStr() {
		return logStr;
	}
	public void setLogStr(String logStr) {
		this.logStr = logStr;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTerminalModel() {
		return terminalModel;
	}
	public void setTerminalModel(String terminalModel) {
		this.terminalModel = terminalModel;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
}
