package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券Entity
 * 
 * @author lxl
 * @version 2016-12-14
 */
public class AdCoupon {

	private Long id;
	String businessId; // 商户编号 // 未生成商户实体 暂时String代替
	private Date beginDate; // 生效开始时间
	private Date endDate; // 生效结束时间
	private String name; // 优惠券名称
	private String productSn; // 商品编号
	private String productId; // 商品id
	private String quantity; // 生成数量
	private String isOpen; // 是否开启
	private String productName; // 商品名称
	private String businessName; // 商户名称
	private String activityName; // 活动名称
	private String isCreat; // 是否生成
	private String couponType; // 红包类型 0-企业红包，1-员工邀请礼品,2-家属邀请礼品
	private int distributionQuantity;// 已分配数量，默认为0
	private int remindQuantity;// 剩余可分配数量，默认为0
	private int productSecondCategory;// 优惠券对应的商品二级分类
	private String firstShow;// 是否首页展示（热门推荐，热门商家，各分类...）
	private int productFirstCategory;// 首页展示，对应的一级分类
	private String receiveType;// 领取类型，默认为0（0-可直接领取，1-报名领取）
	private BigDecimal couponValue;// 优惠券面值
	private String couponUnit;// 优惠券单位
	private String type;// 分类类型(0-热门推荐，1-热门商家，2-品牌馆，3-普通分类)
	private String wechatDetailImage;// 微信端优惠券图片地址
	private String pcDetailImage;// PC端优惠券图片地址
	private String wechatIntroduction;// 微信端简介
	private String pcIntroduction;// PC端简介
	private String wechatConvertMethod;// 微信端兑换流程
	private String pcConvertMethod;// PC端兑换流程
	private String wechatAttention;// 微信端注意事项
	private String pcAttention;// PC端注意事项
	private String description;// 名称补充描述
	private String kind;// 优惠类型(0-优惠金额，1-折扣券)
	private String businessOnlineUrl; //线上商户链接
	private String businessAppUrl;//商户APP链接
	private String remarks;//优惠券备注说明(eg:满499使用)
	private String appDownload;//是否显示app下载栏（0-不显示，1-显示）
	private String businessActivity;//是否显示商户活动栏（0-不显示，1-显示）
	private String businessActivityUrl;//商户活动链接
	private String couponUserDetail;//卡券使用说明
	private String couponCategory;//卡券类型
    private String couponStatus;//卡券状态 1，待领取 ，2 ，待使用 3，敬请期待 4，已领完
    private String activityId;//活动id
    private String couponId;//卡券id
    private String couponDescription;// 下午茶卡券规则
    private String bussinessLogo;//商户logo
    private String receiveChannel;// 领取方式（0-兜礼领取，1-商家领取）
    private String couponValueStr;// 优惠券面值字符串
    private String categoryType;

    public static final String COUPONSTATUS_TOMORROW = "3";

    public static final int COUPON_NUM_LIMIT = 3;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getIsCreat() {
		return isCreat;
	}

	public void setIsCreat(String isCreat) {
		this.isCreat = isCreat;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public int getDistributionQuantity() {
		return distributionQuantity;
	}

	public void setDistributionQuantity(int distributionQuantity) {
		this.distributionQuantity = distributionQuantity;
	}

	public int getRemindQuantity() {
		return remindQuantity;
	}

	public void setRemindQuantity(int remindQuantity) {
		this.remindQuantity = remindQuantity;
	}

	public int getProductSecondCategory() {
		return productSecondCategory;
	}

	public void setProductSecondCategory(int productSecondCategory) {
		this.productSecondCategory = productSecondCategory;
	}

	public String getFirstShow() {
		return firstShow;
	}

	public void setFirstShow(String firstShow) {
		this.firstShow = firstShow;
	}

	public int getProductFirstCategory() {
		return productFirstCategory;
	}

	public void setProductFirstCategory(int productFirstCategory) {
		this.productFirstCategory = productFirstCategory;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public BigDecimal getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(BigDecimal couponValue) {
		this.couponValue = couponValue;
	}

	public String getCouponUnit() {
		return couponUnit;
	}

	public void setCouponUnit(String couponUnit) {
		this.couponUnit = couponUnit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWechatDetailImage() {
		return wechatDetailImage;
	}

	public void setWechatDetailImage(String wechatDetailImage) {
		this.wechatDetailImage = wechatDetailImage;
	}

	public String getPcDetailImage() {
		return pcDetailImage;
	}

	public void setPcDetailImage(String pcDetailImage) {
		this.pcDetailImage = pcDetailImage;
	}

	public String getWechatIntroduction() {
		return wechatIntroduction;
	}

	public void setWechatIntroduction(String wechatIntroduction) {
		this.wechatIntroduction = wechatIntroduction;
	}

	public String getPcIntroduction() {
		return pcIntroduction;
	}

	public void setPcIntroduction(String pcIntroduction) {
		this.pcIntroduction = pcIntroduction;
	}

	public String getWechatConvertMethod() {
		return wechatConvertMethod;
	}

	public void setWechatConvertMethod(String wechatConvertMethod) {
		this.wechatConvertMethod = wechatConvertMethod;
	}

	public String getPcConvertMethod() {
		return pcConvertMethod;
	}

	public void setPcConvertMethod(String pcConvertMethod) {
		this.pcConvertMethod = pcConvertMethod;
	}

	public String getWechatAttention() {
		return wechatAttention;
	}

	public void setWechatAttention(String wechatAttention) {
		this.wechatAttention = wechatAttention;
	}

	public String getPcAttention() {
		return pcAttention;
	}

	public void setPcAttention(String pcAttention) {
		this.pcAttention = pcAttention;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getBusinessOnlineUrl() {
		return businessOnlineUrl;
	}

	public void setBusinessOnlineUrl(String businessOnlineUrl) {
		this.businessOnlineUrl = businessOnlineUrl;
	}

	public String getBusinessAppUrl() {
		return businessAppUrl;
	}

	public void setBusinessAppUrl(String businessAppUrl) {
		this.businessAppUrl = businessAppUrl;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAppDownload() {
		return appDownload;
	}

	public void setAppDownload(String appDownload) {
		this.appDownload = appDownload;
	}

	public String getBusinessActivity() {
		return businessActivity;
	}

	public void setBusinessActivity(String businessActivity) {
		this.businessActivity = businessActivity;
	}

	public String getBusinessActivityUrl() {
		return businessActivityUrl;
	}

	public void setBusinessActivityUrl(String businessActivityUrl) {
		this.businessActivityUrl = businessActivityUrl;
	}

	public String getCouponUserDetail() {
		return couponUserDetail;
	}

	public void setCouponUserDetail(String couponUserDetail) {
		this.couponUserDetail = couponUserDetail;
	}

	
	public String getCouponCategory() {
		return couponCategory;
	}

	public void setCouponCategory(String couponCategory) {
		this.couponCategory = couponCategory;
	}

	@Override
	public String toString() {
		return "AdCoupon [id=" + id + ", businessId=" + businessId
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", name=" + name + ", productSn=" + productSn
				+ ", productId=" + productId + ", quantity=" + quantity
				+ ", isOpen=" + isOpen + ", productName=" + productName
				+ ", businessName=" + businessName + ", activityName="
				+ activityName + ", isCreat=" + isCreat + ", couponType="
				+ couponType + ", distributionQuantity=" + distributionQuantity
				+ ", remindQuantity=" + remindQuantity
				+ ", productSecondCategory=" + productSecondCategory
				+ ", firstShow=" + firstShow + ", productFirstCategory="
				+ productFirstCategory + ", receiveType=" + receiveType
				+ ", couponValue=" + couponValue + ", couponUnit=" + couponUnit
				+ ", type=" + type + ", wechatDetailImage=" + wechatDetailImage
				+ ", pcDetailImage=" + pcDetailImage + ", wechatIntroduction="
				+ wechatIntroduction + ", pcIntroduction=" + pcIntroduction
				+ ", wechatConvertMethod=" + wechatConvertMethod
				+ ", pcConvertMethod=" + pcConvertMethod + ", wechatAttention="
				+ wechatAttention + ", pcAttention=" + pcAttention
				+ ", description=" + description + ", kind=" + kind
				+ ", businessOnlineUrl=" + businessOnlineUrl
				+ ", businessAppUrl=" + businessAppUrl + ", remarks=" + remarks
				+ ", appDownload=" + appDownload + ", businessActivity="
				+ businessActivity + ", businessActivityUrl="
				+ businessActivityUrl + ", couponUserDetail="
				+ couponUserDetail + "]";
	}

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getBussinessLogo() {
        return bussinessLogo;
    }

    public void setBussinessLogo(String bussinessLogo) {
        this.bussinessLogo = bussinessLogo;
    }

    public String getReceiveChannel() {
        return receiveChannel;
    }

    public void setReceiveChannel(String receiveChannel) {
        this.receiveChannel = receiveChannel;
    }

    public String getCouponValueStr() {
        return couponValueStr;
    }

    public void setCouponValueStr(String couponValueStr) {
        this.couponValueStr = couponValueStr;
    }

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
}