package com.doooly.business.pay.service;

import com.doooly.dto.user.GetPayVerifyCodeReq;
import com.doooly.dto.user.GetPayVerifyCodeRes;

/**
 * 支付服务接口
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
public interface PayServiceI {
	
	/**
	 * 获取积分支付验证码(生成18位验证码的二维码和条形码,即被扫码)
	 * @param req
	 * @return
	 */
	public GetPayVerifyCodeRes getPayVerifyCode(GetPayVerifyCodeReq req) ;
	
}
