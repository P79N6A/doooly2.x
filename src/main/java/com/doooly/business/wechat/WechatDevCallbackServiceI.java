package com.doooly.business.wechat;

import java.util.List;

public interface WechatDevCallbackServiceI {
	/**
	 * 开发者模式配置签名验证
	 * 
	 * @author hutao
	 * @date 创建时间：2018年4月12日 下午4:53:35
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	boolean checkSignature(String signature, String timestamp, String nonce);

	/**
	 * 处理微信回调
	 * 
	 * @author hutao
	 * @date 创建时间：2018年4月12日 下午4:25:44
	 * @version 1.0
	 * @parameter reqStr
	 * @param channel - 渠道(doooly/wugang)
	 * @since
	 * @return
	 */
	List<String> dealCallback(String reqStr, String channel);
}
