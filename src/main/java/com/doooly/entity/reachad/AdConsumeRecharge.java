package com.doooly.entity.reachad;

import java.util.Date;

/**
 *
 * @author
 */
public class AdConsumeRecharge {

    private String mainTitle; //主标题
    private String subTitle; //副标题
    private String iconUrl;  //图标地址
    private String linkUrl; //链接地址
    private String subUrl; //跳转链接后缀
    private Date serverEndTime; // 服务结束时间
    /** 是否支持积分*/
    private String isSupportIntegral;
    //角标
    private String cornerMark;
    //楼层Id
    private Integer floorId;
    //
    private String guideIconUrl;

    public Date getServerEndTime() {
        return serverEndTime;
    }

    public void setServerEndTime(Date serverEndTime) {
        this.serverEndTime = serverEndTime;
    }

    public String getGuideIconUrl() {
		return guideIconUrl;
	}

	public void setGuideIconUrl(String guideIconUrl) {
		this.guideIconUrl = guideIconUrl;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public String getCornerMark() {
		return cornerMark;
	}

	public void setCornerMark(String cornerMark) {
		this.cornerMark = cornerMark;
	}

	public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

	public String getIsSupportIntegral() {
		return isSupportIntegral;
	}

	public void setIsSupportIntegral(String isSupportIntegral) {
		this.isSupportIntegral = isSupportIntegral;
	}
    
}