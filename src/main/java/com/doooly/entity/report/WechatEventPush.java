package com.doooly.entity.report;

import java.util.Date;

/**
 * 微信事件推送Entity
 * 
 * @author linking
 * @version 2018-04-17
 */
public class WechatEventPush {

	private String ToUserName; // 开发者微信号
	private String FromUserName; // 发送方帐号（一个OpenID）
	private Date CreateTime; // 消息创建时间
	private String MsgType; // 消息类型，event
	private String Event; // 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
	private String EventKey; // 事件参数（多个参数用逗号隔开）
	private Date createDate; // 数据存储创建时间
	private String channel; // 公众号渠道(doooly-兜礼,wugang-武钢)

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}