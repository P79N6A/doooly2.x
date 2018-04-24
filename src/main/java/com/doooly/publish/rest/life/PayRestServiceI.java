package com.doooly.publish.rest.life;

import com.doooly.dto.user.GetPayVerifyCodeReq;

/**
 * 支付功能接口
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
public interface PayRestServiceI {

	/**
	 * 获取积分支付验证码接口
	 * @param req
	 * @return
	 */
	public String getPayCode(GetPayVerifyCodeReq req);
	
}
