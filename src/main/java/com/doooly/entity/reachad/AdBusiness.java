package com.doooly.entity.reachad;

import java.util.Date;

/**
 * ad_business商户信息表pojo类
 * 
 * @author tangzhiyuan
 *
 */
public class AdBusiness {
	private Long id;
	private String company; // 商户名称
	private String type; // 商户类型
	private String logo; // 商户图标
	private String miniLogo;// 地图小图标
	private String url; // 链接地址
	private String appUrl; //app 链接地址
	private String businessId; // 商户编号
	private String userName; // 权限帐号
	private String password; // 权限密码
	private String userRebate; // 返利比例
	private String bussinessRebate; // 返佣比例
	private String unionLogin; // 联合登录回调地址
	private String dataSynchronization; // 同步开关
	private String bussinessRebateType; // 返佣类型
	private String userRebateType; // 返利类型
	private String memo; // 备注
	private String isActive; // 账户状态
	private Date createDate;
	private Date updateDate;
	private int delFlag;
	private String isHot;// 是否热门商家，默认为0（0-非热门，1-热门）
	private Double discount;// 折扣
	private String dealType;// 商家交易类型（0-线上，1-线下，2-线上及线下）
	private String benefitWay;// 如何乐惠
	private String benefitContent;// 乐惠内容
	private String noticeInfo;// 注意事项
	private Date serverStartTime; // 服务起始时间
	private Date serverEndTime; // 服务结束时间
	private String listImageWechat;// 商户微信列表图
	private String lightenType;//是否点亮,0未点亮,1已点亮
	private String businessIntroduce;//商家介绍（卡券详情，商家介绍）
	private String remarks;//商家介绍（卡券详情，商家介绍）
	private String maxUserRebate;
	private Integer openOffLineScan;// 是否开启线下扫码优惠(0，未开启；1，已开启)
	private String scanImageUrl;//扫描优惠商家图片
    private Integer openOneNnumber;// 商户是否开通1号通 0， 未开通 1，已开通
    private Integer isRebateApply;// 商户是否开通1号通 0， 未开通 1，已开通
	private String wechatJumpStyle;//微信端跳转样式(0:H5,1:APP) 默认为0
	private String appJumpStyle;//APP端跳转样式(0:H5,1:APP) 默认为1
    private String isSupportIntegral;//是否支持积分(0.不支持,1支持)
    private Date isNewEndTime; // 上新结束时间
	private Date isNewBeginTime; // 上新开始时间
	public AdBusiness() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserRebate() {
		return userRebate;
	}

	public void setUserRebate(String userRebate) {
		this.userRebate = userRebate;
	}

	public String getBussinessRebate() {
		return bussinessRebate;
	}

	public void setBussinessRebate(String bussinessRebate) {
		this.bussinessRebate = bussinessRebate;
	}

	public String getUnionLogin() {
		return unionLogin;
	}

	public void setUnionLogin(String unionLogin) {
		this.unionLogin = unionLogin;
	}

	public String getDataSynchronization() {
		return dataSynchronization;
	}

	public void setDataSynchronization(String dataSynchronization) {
		this.dataSynchronization = dataSynchronization;
	}

	public String getBussinessRebateType() {
		return bussinessRebateType;
	}

	public void setBussinessRebateType(String bussinessRebateType) {
		this.bussinessRebateType = bussinessRebateType;
	}

	public String getUserRebateType() {
		return userRebateType;
	}

	public void setUserRebateType(String userRebateType) {
		this.userRebateType = userRebateType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getMiniLogo() {
		return miniLogo;
	}

	public void setMiniLogo(String miniLogo) {
		this.miniLogo = miniLogo;
	}

	public String getBenefitWay() {
		return benefitWay;
	}

	public void setBenefitWay(String benefitWay) {
		this.benefitWay = benefitWay;
	}

	public String getBenefitContent() {
		return benefitContent;
	}

	public void setBenefitContent(String benefitContent) {
		this.benefitContent = benefitContent;
	}

	public String getNoticeInfo() {
		return noticeInfo;
	}

	public void setNoticeInfo(String noticeInfo) {
		this.noticeInfo = noticeInfo;
	}

	public Date getServerStartTime() {
		return serverStartTime;
	}

	public void setServerStartTime(Date serverStartTime) {
		this.serverStartTime = serverStartTime;
	}

	public Date getServerEndTime() {
		return serverEndTime;
	}

	public void setServerEndTime(Date serverEndTime) {
		this.serverEndTime = serverEndTime;
	}

	public String getListImageWechat() {
		return listImageWechat;
	}

	public void setListImageWechat(String listImageWechat) {
		this.listImageWechat = listImageWechat;
	}

	public String getLightenType() {
		return lightenType;
	}

	public void setLightenType(String lightenType) {
		this.lightenType = lightenType;
	}
	public String getBusinessIntroduce() {
		return businessIntroduce;
	}

	public void setBusinessIntroduce(String businessIntroduce) {
		this.businessIntroduce = businessIntroduce;
	}


	public String getMaxUserRebate() {
		return maxUserRebate;
	}

	public void setMaxUserRebate(String maxUserRebate) {
		this.maxUserRebate = maxUserRebate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Integer getOpenOffLineScan() {
		return openOffLineScan;
	}

	public void setOpenOffLineScan(Integer openOffLineScan) {
		this.openOffLineScan = openOffLineScan;
	}

	public String getScanImageUrl() {
		return scanImageUrl;
	}

	public void setScanImageUrl(String scanImageUrl) {
		this.scanImageUrl = scanImageUrl;
	}

    public Integer getOpenOneNnumber() {
        return openOneNnumber;
    }

    public void setOpenOneNnumber(Integer openOneNnumber) {
        this.openOneNnumber = openOneNnumber;
    }

    public Integer getIsRebateApply() {
        return isRebateApply;
    }

    public void setIsRebateApply(Integer isRebateApply) {
        this.isRebateApply = isRebateApply;
    }

	public String getWechatJumpStyle() {
		return wechatJumpStyle;
	}

	public void setWechatJumpStyle(String wechatJumpStyle) {
		this.wechatJumpStyle = wechatJumpStyle;
	}

	public String getAppJumpStyle() {
		return appJumpStyle;
	}

	public void setAppJumpStyle(String appJumpStyle) {
		this.appJumpStyle = appJumpStyle;
	}

	public String getIsSupportIntegral() {
		return isSupportIntegral;
	}

	public void setIsSupportIntegral(String isSupportIntegral) {
		this.isSupportIntegral = isSupportIntegral;
	}

	public Date getIsNewEndTime() {
		return isNewEndTime;
	}

	public void setIsNewEndTime(Date isNewEndTime) {
		this.isNewEndTime = isNewEndTime;
	}

	public Date getIsNewBeginTime() {
		return isNewBeginTime;
	}

	public void setIsNewBeginTime(Date isNewBeginTime) {
		this.isNewBeginTime = isNewBeginTime;
	}
	
}
