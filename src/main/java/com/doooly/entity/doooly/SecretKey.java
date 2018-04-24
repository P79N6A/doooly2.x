package com.doooly.entity.doooly;

import java.io.Serializable;
import java.util.Date;

import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyStatus;


/**
 * 密钥表secret_key POJO类
 * @author Albert(赵清江)
 * @date 2016-7-8
 * @version 1.0
 */
public class SecretKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8756859778722560284L;
	/**
	 * 编号
	 */
	private int id;
	/**
	 * 公钥
	 */
	private String publicKey;
	/**
	 * 私钥
	 */
	private String privateKey;
	/**
	 * 生成时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date modifyDate;
	/**
	 * 拥有者
	 */
	private SecretKeyOwner owner;
	/**
	 * 是否失效
	 */
	private SecretKeyStatus isValid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public SecretKeyOwner getOwner() {
		return owner;
	}

	public void setOwner(SecretKeyOwner owner) {
		this.owner = owner;
	}

	public SecretKeyStatus getIsValid() {
		return isValid;
	}

	public void setIsValid(SecretKeyStatus isValid) {
		this.isValid = isValid;
	}
}
