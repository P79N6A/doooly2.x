package com.doooly.publish.rest.life;

import com.doooly.dto.user.CheckVerifyCodeReq;
import com.doooly.dto.user.GetVerifyCodeReq;

/**
 * 验证码功能接口
 * @author Albert(赵清江)
 * @date 2016-07-11
 * @version 1.0
 */
public interface AuthenticationServiceI {
	/**
	 * 请求验证码接口
	 * @param GetVerifyCodeReq req
	 * @return VerifyCodeRes 
	 */
	public String demandVerifyCode(GetVerifyCodeReq req);
	/**
	 * 验证验证码接口
	 * @param req
	 * @return
	 */
	public String checkVerifyCode(CheckVerifyCodeReq req);
}
