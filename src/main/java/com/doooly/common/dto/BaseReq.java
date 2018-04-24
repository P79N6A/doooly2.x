package com.doooly.common.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@XmlRootElement
public class BaseReq<T> implements Serializable {
	private static final long serialVersionUID = -2538200853842273723L;
	//时间戳
	private long timestamp;
	//客户端渠道 wechat/浏览器/IOS/Android
	private String clientChannel;
	//接口访问分配ID
	private String clientId;
	//接口访问凭证，暂忽略
	private String accessToken;
	//接口请求业务参数
	private T params;
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getClientChannel() {
		return clientChannel;
	}
	public void setClientChannel(String clientChannel) {
		this.clientChannel = clientChannel;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public T getParams() {
		return params;
	}
	public void setParams(T params) {
		this.params = params;
	}
	
}
