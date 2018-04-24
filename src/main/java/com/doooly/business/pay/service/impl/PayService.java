package com.doooly.business.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.WSServiceI;
import com.doooly.business.common.service.impl.WSService;
import com.doooly.business.common.utils.GenerateImageCodeUtil;
import com.doooly.business.pay.service.PayServiceI;
import com.doooly.common.constants.Constants.ResponseCode;
import com.doooly.dto.user.GetPayVerifyCodeReq;
import com.doooly.dto.user.GetPayVerifyCodeRes;

/**
 * 支付服务类
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
@Service
public class PayService implements PayServiceI {

	@Autowired
	private WSServiceI wsService;
	
	/**
	 * 获取积分支付验证码
	 */
	@Override
	public GetPayVerifyCodeRes getPayVerifyCode(GetPayVerifyCodeReq req) {
		GetPayVerifyCodeRes response = new GetPayVerifyCodeRes();
		
		try {
			response = wsService.getIntegralPayVerifyCode(req);
			if (response.getCode().equals(ResponseCode.SUCCESS.getCode())) {
				String QR_code_Uri = GenerateImageCodeUtil.generateQRCode(response.getVerifyCode18(), null);
				String BR_code_Uri = GenerateImageCodeUtil.generateBarCode(response.getVerifyCode18(), null);
				response.setQRCodeUri(QR_code_Uri);
				response.setBarCodeUri(BR_code_Uri);
			}
		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			e.printStackTrace();
		}
		
		return response;
	}

	
	
	
	
}
