package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 标签
 * 
 * @version 1.0
 */
public class AdProduct {

	private int id;

	private String name;// 商品名称

	private String nameSpell;// 名称拼音

	private String businessId;// 商家ID

	private boolean isMarketable;// 是否上架(1-上架，0-下架)，默认为0

	private BigDecimal marketPrice;// 市场价

	private BigDecimal dooolyPrice;		// 兜礼价格

	private Double discount;// 折扣

	private BigDecimal price;// 折后价

	private boolean availableCoupon;// 有可用券，默认为0（1-有可用券，0-无可用券）

	private String linkUrlWechat;// 第三方链接地址

	private int tag;// 商品标签

	private String isHot;// 是否热门首页展示（热门推荐）

	private int firstTag;// 首页展示所属标签

	private String unit;// 单位

	private int firstCategory;// 商品一级分类

	private int secondCategory;// 商品二级分类

	private int buyUpperLimit;// 购买上限

	private Date buyStartTime;// 购买开始时间

	private Date buyEndTime;// 购买结束时间

	private String imageWechat;// 微信端商品图片地址

	private int totalStock;// 总库存

	private int usedStock;// 已用库存

	private int remindStock;// 剩余库存

	private String description;// 商品描述、备注

	private Date createDate;

	private int createUser;

	private Date updateDate;

	private int updateUser;

	private String attribute0;

	private String guideTag;

	private String categoryId;
	private String tagName;
	private String recommendReason;// 推荐理由
	private String businessName;// 商家名称
	private String rebate;
	private String lightenType;

	private Integer hitsCount;
    private BigDecimal bussinesRebate;//商户返佣比例
    private BigDecimal userRebate;//用户返利比例
    private BigDecimal layeredRebate;//实际分成比例
    private Integer sellCount;//已售多少件
    private String imagePath;//文章主图
    private String content;//文章内容
    private String articleId;//文章主表id
    private String title;//文章标题
    private String businessLogo;//商家logo
    private String dealType;//商家类型线上线下
    private String digest;//文章摘要
    private String imgUrl;//分享图标
    private String maxUserRebate;//最高返利
	private String shippingMethod;      // 发货方式
	private Integer guideCategoryId;//导购类别
	private Integer recommendLife;//推荐到生活

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameSpell() {
		return nameSpell;
	}

	public String getGuideTag() {
		return guideTag;
	}

	public void setGuideTag(String guideTag) {
		this.guideTag = guideTag;
	}

	public void setNameSpell(String nameSpell) {
		this.nameSpell = nameSpell;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public boolean isMarketable() {
		return isMarketable;
	}

	public void setMarketable(boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getGuideCategoryId() {
		return guideCategoryId;
	}

	public void setGuideCategoryId(Integer guideCategoryId) {
		this.guideCategoryId = guideCategoryId;
	}

	public Integer getRecommendLife() {
		return recommendLife;
	}

	public void setRecommendLife(Integer recommendLife) {
		this.recommendLife = recommendLife;
	}

	public boolean isAvailableCoupon() {
		return availableCoupon;
	}

	public void setAvailableCoupon(boolean availableCoupon) {
		this.availableCoupon = availableCoupon;
	}

	public String getLinkUrlWechat() {
		return linkUrlWechat;
	}

	public void setLinkUrlWechat(String linkUrlWechat) {
		this.linkUrlWechat = linkUrlWechat;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public int getFirstTag() {
		return firstTag;
	}

	public void setFirstTag(int firstTag) {
		this.firstTag = firstTag;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(int firstCategory) {
		this.firstCategory = firstCategory;
	}

	public int getSecondCategory() {
		return secondCategory;
	}

	public void setSecondCategory(int secondCategory) {
		this.secondCategory = secondCategory;
	}

	public int getBuyUpperLimit() {
		return buyUpperLimit;
	}

	public void setBuyUpperLimit(int buyUpperLimit) {
		this.buyUpperLimit = buyUpperLimit;
	}

	public Date getBuyStartTime() {
		return buyStartTime;
	}

	public void setBuyStartTime(Date buyStartTime) {
		this.buyStartTime = buyStartTime;
	}

	public Date getBuyEndTime() {
		return buyEndTime;
	}

	public void setBuyEndTime(Date buyEndTime) {
		this.buyEndTime = buyEndTime;
	}

	public String getImageWechat() {
		return imageWechat;
	}

	public void setImageWechat(String imageWechat) {
		this.imageWechat = imageWechat;
	}

	public int getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}

	public int getUsedStock() {
		return usedStock;
	}

	public void setUsedStock(int usedStock) {
		this.usedStock = usedStock;
	}

	public int getRemindStock() {
		return remindStock;
	}

	public void setRemindStock(int remindStock) {
		this.remindStock = remindStock;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(int updateUser) {
		this.updateUser = updateUser;
	}

	public String getAttribute0() {
		return attribute0;
	}

	public void setAttribute0(String attribute0) {
		this.attribute0 = attribute0;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getRecommendReason() {
		return recommendReason;
	}

	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

	public String getLightenType() {
		return lightenType;
	}

	public void setLightenType(String lightenType) {
		this.lightenType = lightenType;
	}

	public Integer getHitsCount() {
		return hitsCount;
	}

	public void setHitsCount(Integer hitsCount) {
		this.hitsCount = hitsCount;
	}

    public BigDecimal getBussinesRebate() {
        return bussinesRebate;
    }

    public void setBussinesRebate(BigDecimal bussinesRebate) {
        this.bussinesRebate = bussinesRebate;
    }

    public BigDecimal getUserRebate() {
        return userRebate;
    }

    public void setUserRebate(BigDecimal userRebate) {
        this.userRebate = userRebate;
    }

    public BigDecimal getLayeredRebate() {
        return layeredRebate;
    }

    public void setLayeredRebate(BigDecimal layeredRebate) {
        this.layeredRebate = layeredRebate;
    }

    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(String businessLogo) {
        this.businessLogo = businessLogo;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMaxUserRebate() {
        return maxUserRebate;
    }

    public void setMaxUserRebate(String maxUserRebate) {
        this.maxUserRebate = maxUserRebate;
    }

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public BigDecimal getDooolyPrice() {
		return dooolyPrice;
	}

	public void setDooolyPrice(BigDecimal dooolyPrice) {
		this.dooolyPrice = dooolyPrice;
	}
}