package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 家属邀请实体类
 * 
 * <p>
 * Title: AdInvitation
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author hutao
 * @date 2016年7月18日 下午2:42:03
 */
public class AdInvitation  {

	/**
	 * 
	 */
	
	// 未赠送
	public static Integer NO_GIVE_GIFT_STATE = 0;
	// 已赠送
	public static Integer GIVE_GIFT_STATE = 1;
	// 员工邀请家属类型
	public static Integer FAM_INVITATION_TYPE = 1;
	// 员工邀请员工
	public static Integer MEM_INVITATION_TYPE = 2;
	// 无邀请
	public static Integer NO_INVITATION_TYPE = 0;
	
	private String id;
	private String remarks;	// 备注
	private Date createDate;	// 创建日期
	private Date updateDate;	// 更新日期
	private String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）
	// 会员ID
	private Integer userId;
	// 会员邀请码
	private String invitationCode;
	// 邀请类型 1：家属邀请 0：主卡
	private Integer invitationType;
	// 还可邀请会员数量
	private Integer invitationAvail;
	// 可邀请最大人数
	private Integer invitationMaxNum;
	// 邀请达最小人数即送所选礼品
	private Integer invitationMinNum;
	// 邀请有效时间（与当前时间比较，默认7天）
	private Date validationTime;
	// 邀请人可获得礼品数
	private Integer giftNum;
	// 会员已选礼品列表（productSN），多个以逗号","分隔
	private String giftLists;
	// 0:未送 1:已送
	private Integer giftState;
	// 手机号
	private String telephone;
	// 卡号
	private String cardNumber;
	// 商品名
	private String productName;
	// 商品号
	private String productSn;
	//家属邀请活动标识
	private String flag;

	private String staffSn;

	private String familySn;

	public AdInvitation() {
	}

	
	public AdInvitation(Integer userId, Integer invitationType, Integer giftState) {
		super();
		this.userId = userId;
		this.invitationType = invitationType;
		this.giftState = giftState;
	}

	public AdInvitation(String invitationCode, Integer invitationType, Integer giftState) {
		super();
		this.invitationCode = invitationCode;
		this.invitationType = invitationType;
		this.giftState = giftState;
	}

	public AdInvitation(Integer userId, String giftList, Integer giftState, Integer invitationType,
			Date validationTime) {
		super();
		this.userId = userId;
		this.giftLists = giftList;
		this.giftState = giftState;
		this.invitationType = invitationType;
		this.validationTime = validationTime;
	}

	public AdInvitation(Integer userId, String invitationCode, Integer invitationType, Integer invitationAvail,
			Integer invitationMaxNum, Integer invitationMinNum, Integer giftNum, String giftList,String flag) {
		super();
		this.userId = userId;
		this.invitationCode = invitationCode;
		this.invitationType = invitationType;
		this.invitationAvail = invitationAvail;
		this.invitationMaxNum = invitationMaxNum;
		this.invitationMinNum = invitationMinNum;
		this.giftNum = giftNum;
		this.giftLists = giftList;
		this.flag=flag;
	}

	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public Integer getInvitationType() {
		return invitationType;
	}

	public void setInvitationType(Integer invitationType) {
		this.invitationType = invitationType;
	}

	public Integer getInvitationAvail() {
		return invitationAvail;
	}

	public void setInvitationAvail(Integer invitationAvail) {
		this.invitationAvail = invitationAvail;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getInvitationMaxNum() {
		return invitationMaxNum;
	}

	public void setInvitationMaxNum(Integer invitationMaxNum) {
		this.invitationMaxNum = invitationMaxNum;
	}

	public Date getValidationTime() {
		return validationTime;
	}

	public void setValidationTime(Date validationTime) {
		this.validationTime = validationTime;
	}

	public Integer getGiftState() {
		return giftState;
	}

	public void setGiftState(Integer giftState) {
		this.giftState = giftState;
	}

	public Integer getInvitationMinNum() {
		return invitationMinNum;
	}

	public void setInvitationMinNum(Integer invitationMinNum) {
		this.invitationMinNum = invitationMinNum;
	}

	public Integer getGiftNum() {
		return giftNum;
	}

	public void setGiftNum(Integer giftNum) {
		this.giftNum = giftNum;
	}

	public String getGiftLists() {
		return giftLists;
	}

	public void setGiftLists(String giftLists) {
		this.giftLists = giftLists;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public String getStaffSn() {
		return staffSn;
	}

	public void setStaffSn(String staffSn) {
		this.staffSn = staffSn;
	}

	public String getFamilySn() {
		return familySn;
	}

	public void setFamilySn(String familySn) {
		this.familySn = familySn;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
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


	public String getDelFlag() {
		return delFlag;
	}


	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	

}
