package com.doooly.entity.doooly;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模版关联楼层Entity
 * @author Mr.Wu
 * @version 2019-03-07
 */
public class DlTemplateFloor {

	private static final long serialVersionUID = 1L;
	private String id;
	private String templateId;		// 模版id
	private String title;		// 标题
	private String subTitle;		// 副标题
	private Integer isHide;		// 是否是隐藏楼层，隐藏楼层不显示编辑弹窗(0：否，1：是，默认0)(type=3、4为隐藏楼层)
	private Integer type;		// 楼层类型
	private Integer state;		// 显示状态，(0：显示，1：不显示，默认0)
	private String depict;		// 楼层描述
	private Integer sort;		// 排序
	private String delFlag; 	// 删除标记
	private String createBy;	// 创建者
	private Date createDate;	// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;	// 更新日期
	// ========== 关联字段 =======
	private List<DlTemplateFloorItem> items = new ArrayList<>(); 	// 楼层item集合
	private String couponCount;    // 卡券数量

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public List<DlTemplateFloorItem> getItems() {
		return items;
	}

	public void setItems(List<DlTemplateFloorItem> items) {
		this.items = items;
	}

	public String getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(String couponCount) {
		this.couponCount = couponCount;
	}
}