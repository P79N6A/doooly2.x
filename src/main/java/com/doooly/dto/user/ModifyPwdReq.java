package com.doooly.dto.user;

import com.doooly.common.dto.BaseReq;

/**
 * 修改密码请求DTO
 * @author 赵清江
 * @date 2016年7月14日
 * @version 1.0
 */
public class ModifyPwdReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3802008165109838087L;

	private String mobile;
	
	private String newPwd;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	@Override
	public String toString() {
		return "ModifyPwdReq [mobile=" + mobile + ", newPwd=" + newPwd + "]";
	}
}