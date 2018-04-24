package com.doooly.entity.reachlife;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * Entity - 投诉 申诉 建议
 * 
 * @author Reach Team
 * @version 1.0
 */
public class LifeComplaint  {

	/**
	 * 删除标记（0：正常）（B系统基础字段）
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	
	/**
	 * 删除标记（1：删除）（B系统基础字段）
	 */
	public static final String DEL_FLAG_DELETE = "1";
	
	/**
	 * 删除标记（2：审核）（B系统基础字段）
	 */
	public static final String DEL_FLAG_AUDIT = "2";
	
	/** 投诉 申诉 建议的投诉类型：未记录订单申诉 */
	public static final String COMPLAINT_TYPE_UNRECORD_ORDER = "1";
	
	/** 投诉 申诉 建议的投诉类型：未记录积分申诉 */
	public static final String COMPLAINT_TYPE_UNRECORD_POINT = "2";
	
	/** 投诉 申诉 建议的投诉类型：其他申诉 */
	public static final String COMPLAINT_TYPE_OTHER = "3";
	
	/** 投诉 申诉 建议的申述状态：申诉中 */
	public static final String COMPLAINT_STATUS_COMPLAINTING = "0";
	
	/** 投诉 申诉 建议的申述状态：已受理 */
	public static final String COMPLAINT_STATUS_ACCEPTED = "1";
	
	/** 投诉 申诉 建议的申述类型：已驳回 */
	public static final String COMPLAINT_STATUS_REJECTED = "2";
	
	/** 投诉 申诉 建议的申述类型：审核中 */
	public static final String COMPLAINT_STATUS_AUDIT = "3";
	
	/** 投诉 申诉 建议的申述类型：积分返还中 */
	public static final String COMPLAINT_STATUS_POINT_RETURNING = "4";
	
	/** 投诉 申诉 建议的申述类型：已完成 */
	public static final String COMPLAINT_STATUS_FINISHED = "5";
	
	/** 申诉会员的会员号 */
	private String memberNum;
	
	/** 申诉会员所属企业番号 */
	private String groupNum;
	
	/** 申诉会员所属企业名称 */
	private String groupName;
	
	/** 内容 */
	private String content;

	/** 提出申诉者IP地址 */
	private String createIp;
	
	/** 投诉类型  (1.订单申诉  2.积分申诉  3.其他申诉) */
	private String type;
	
	/** 情况说明 */
	private String reply;
	
	/** 申诉编号  */
	private String complaintSn;
	
	/** 已记录订单申诉 或 已记录积分申诉 对应的订单编号  */
	private String orderSn;
	
	/** 商家名称 */
	private String supplierName;
	
	/** 购买商品时间 */
	private String buyDate;

	/** 订单金额 */
	private BigDecimal price;

	/** 图片路径（最多4张，已半角分号;分割） */
	private String imagePaths;

	/** 真实姓名 */
	private String name;
	
	/** 联系方式 */
	private String contactInfo;
	
	/** 备注（B系统基础字段） */
	private String remarks;
	
	/** 创建者（B系统基础字段） */
	private String createBy; //应设置为登录用户的会员号，游客则不设
	
	/** 更新者（B系统基础字段） */
	private String updateBy;
	
	/** 更新日期（B系统基础字段） */
	private Date updateDate;	// 另B系统有createDate基础字段，因与A系统重名，这里不再重复设置
	
	/** 更新日期（B系统基础字段） */
	private Date createDate;	// 另B系统有createDate基础字段，因与A系统重名，这里不再重复设置
	
	/** 最后审核人IP地址 */
	private String updateIp;
	
	/** 申述状态（0：申诉中；1：已受理；2：已驳回；3：审核中；4：积分返还中；5：已完成） */
	private String status;
	
	/** 应退积分（后台客服录入） */
	private BigDecimal shouldReturnPoint;
	
	/** 应退现金（后台客服录入） */
	private BigDecimal shouldReturnCash;
	
	/** 返还的积分（后台财务录入） */
	private BigDecimal returnPoint;
	
	/** 删除标记（0：正常；1：删除；2：审核）（B系统基础字段） */
	private String delFlag;
	
	private String userId;//用户ID
	
	private Date modifyDate;
	
	private Date completeDate;
	
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取申诉会员的会员号
	 * 
	 * @return 申诉会员的会员号
	 */
	public String getMemberNum() {
		return memberNum;
	}

	/**
	 * 设置申诉会员的会员号
	 * 
	 * @param memberNum
	 * 				申诉会员的会员号
	 */
	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}

	/**
	 * 获取申诉会员所属企业番号
	 * 
	 * @return 申诉会员所属企业番号
	 */
	public String getGroupNum() {
		return groupNum;
	}

	/**
	 * 设置申诉会员所属企业番号
	 * 
	 * @param groupNum
	 * 				申诉会员所属企业番号
	 */
	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	/**
	 * 获取申诉会员所属企业名称
	 * 
	 * @return 申诉会员所属企业名称
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * 设置申诉会员所属企业名称
	 * 
	 * @param groupName
	 * 				申诉会员所属企业名称
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 获取提出申诉者IP地址
	 * 
	 * @return 提出申诉者IP地址
	 */
	public String getCreateIp() {
		return createIp;
	}

	/**
	 * 设置提出申诉者IP地址
	 * 
	 * @param createIp
	 *            提出申诉者IP地址
	 */
	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	/**
	 * 获取商家名称
	 * 
	 * @return 商家名称
	 */
	public String getSupplierName() {
		return supplierName;
	}

	/**
	 * 设置商家名称
	 * 
	 * @param supplierName
	 * 			商家名称
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/**
	 * 获取订单金额
	 * 
	 * @return 订单金额
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置订单金额
	 * 
	 * @param price
	 * 				订单金额
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取图片路径（最多3张，已半角分号;分割）
	 * 
	 * @return 图片路径（最多3张，已半角分号;分割）
	 */
	public String getImagePaths() {
		return imagePaths;
	}

	/**
	 * 设置图片路径（最多3张，已半角分号;分割）
	 * 
	 * @param imagePaths
	 *            图片路径（最多3张，已半角分号;分割）
	 */
	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}

	/**
	 * 获取真实姓名
	 * 
	 * @return 真实姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置真实姓名
	 * 
	 * @param name
	 *            真实姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取投诉类型
	 * 
	 * @return 投诉类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置投诉类型
	 * 
	 * @param type
	 *            投诉类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取情况说明
	 * 
	 * @return 情况说明
	 */
	public String getReply() {
		return reply;
	}

	/**
	 * 设置情况说明
	 * 
	 * @param reply
	 * 				情况说明
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}

	/**
	 * 获取申诉编号
	 * 
	 * @return 申诉编号
	 */
	public String getComplaintSn() {
		return complaintSn;
	}

	/**
	 * 设置申诉编号
	 * 
	 * @param complaintSn
	 * 				申诉编号
	 */
	public void setComplaintSn(String complaintSn) {
		this.complaintSn = complaintSn;
	}

	/**
	 * 获取已记录订单申诉 或 已记录积分申诉 对应的订单编号
	 * 
	 * @return 已记录订单申诉 或 已记录积分申诉 对应的订单编号
	 */
	public String getOrderSn() {
		return orderSn;
	}

	/**
	 * 设置已记录订单申诉 或 已记录积分申诉 对应的订单编号
	 * 
	 * @param orderSn
	 *            已记录订单申诉 或 已记录积分申诉 对应的订单编号
	 */
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	/**
	 * 获取订单总价
	 * 
	 * @return 订单总价
	 */
	public String getBuyDate() {
		return buyDate;
	}

	/**
	 * 设置订单总价
	 * 
	 * @param buyDate
	 *            订单总价
	 */
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	/**
	 * 获取联系方式
	 * 
	 * @return 联系方式
	 */
	public String getContactInfo() {
		return contactInfo;
	}

	/**
	 * 设置联系方式
	 * 
	 * @param contactInfo
	 *            联系方式
	 */
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	/**
	 * 获取备注（B系统基础字段）
	 * 
	 * @return 备注（B系统基础字段）
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置备注（B系统基础字段）
	 * 
	 * @param remarks
	 *            备注（B系统基础字段）
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	/**
	 * 获取创建者（B系统基础字段）
	 * 
	 * @return 创建者（B系统基础字段）
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * 设置创建者（B系统基础字段）
	 * 
	 * @param createBy
	 *            创建者（B系统基础字段）
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 获取更新者（B系统基础字段）
	 * 
	 * @return 更新者（B系统基础字段）
	 */
	public String getUpdateBy() {
		return updateBy;
	}

	/**
	 * 设置更新者（B系统基础字段）
	 * 
	 * @param updateBy
	 *            更新者（B系统基础字段）
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 获取更新日期（B系统基础字段）
	 * 
	 * @return 更新日期（B系统基础字段）
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * 设置更新日期（B系统基础字段）
	 * 
	 * @param updateDate
	 *            更新日期（B系统基础字段）
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 获取最后审核人IP地址
	 * 
	 * @return 最后审核人IP地址
	 */
	public String getUpdateIp() {
		return updateIp;
	}

	/**
	 * 设置最后审核人IP地址
	 * 
	 * @param updateIp
	 *            最后审核人IP地址
	 */
	public void setUpdateIp(String updateIp) {
		this.updateIp = updateIp;
	}

	/**
	 * 获取申述状态（0：申诉中；1：已受理；2：已驳回；3：审核中；4：积分返还中；5：已完成）
	 * 
	 * @return 申述状态（0：申诉中；1：已受理；2：已驳回；3：审核中；4：积分返还中；5：已完成）
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置申述状态（0：申诉中；1：已受理；2：已驳回；3：审核中；4：积分返还中；5：已完成）
	 * 
	 * @param status
	 *            申述状态（0：申诉中；1：已受理；2：已驳回；3：审核中；4：积分返还中；5：已完成）
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取应退积分（后台客服录入）
	 * 
	 * @return 应退积分（后台客服录入）
	 */
	public BigDecimal getShouldReturnPoint() {
		return shouldReturnPoint;
	}

	/**
	 * 设置应退积分（后台客服录入）
	 * 
	 * @param shouldReturnPoint
	 *            应退积分（后台客服录入）
	 */
	public void setShouldReturnPoint(BigDecimal shouldReturnPoint) {
		this.shouldReturnPoint = shouldReturnPoint;
	}

	/**
	 * 获取应退现金（后台客服录入）
	 * 
	 * @return 应退现金（后台客服录入）
	 */
	public BigDecimal getShouldReturnCash() {
		return shouldReturnCash;
	}

	/**
	 * 设置应退现金（后台客服录入）
	 * 
	 * @param shouldReturnCash
	 *            应退现金（后台客服录入）
	 */
	public void setShouldReturnCash(BigDecimal shouldReturnCash) {
		this.shouldReturnCash = shouldReturnCash;
	}

	/**
	 * 获取返还的积分（后台财务录入）
	 * 
	 * @return 返还的积分（后台财务录入）
	 */
	public BigDecimal getReturnPoint() {
		return returnPoint;
	}

	/**
	 * 设置返还的积分（后台财务录入）
	 * 
	 * @param returnPoint
	 *            返还的积分（后台财务录入）
	 */
	public void setReturnPoint(BigDecimal returnPoint) {
		this.returnPoint = returnPoint;
	}

	/**
	 * 获取删除标记（0：正常；1：删除；2：审核）（B系统基础字段）
	 * 
	 * @return 删除标记（0：正常；1：删除；2：审核）（B系统基础字段）
	 */
	public String getDelFlag() {
		return delFlag;
	}

	/**
	 * 设置删除标记（0：正常；1：删除；2：审核）（B系统基础字段）
	 * 
	 * @param delFlag
	 *            删除标记（0：正常；1：删除；2：审核）（B系统基础字段）
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}

