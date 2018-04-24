package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 轮播广告表Entity
 * 
 * @version 1.0
 */
public class AdAd {

	private int id;
	private String title;// 标题
	private String imagePath;// 图片路径
	private String type;// 广告显示类别，默认为0(0-微信端，1-PC端)
	private int sort;// 排序号
	private String state;// 显示状态，默认为0（0-隐藏，1-显示）
	private String imageLinkUrl;// 链接url
	private Date createDate;
	private Date beginDate;// 开始时间
	private Date endDate;// 结束时间
	private String content;// 如果为文本时的内容
	private String mediaType;// 媒介类型(0-图片，1-文本，3-flash)

	private int createUser;
	private String linkType;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public String getImageLinkUrl() {
		return imageLinkUrl;
	}

	public void setImageLinkUrl(String imageLinkUrl) {
		this.imageLinkUrl = imageLinkUrl;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

}