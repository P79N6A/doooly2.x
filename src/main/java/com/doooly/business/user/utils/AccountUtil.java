package com.doooly.business.user.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 账户判定工具类
 * 
 * @author 赵清江
 * @date 2016年7月13日
 * @version 1.0
 */
public class AccountUtil {
	private static Logger log = Logger.getLogger(AccountUtil.class);

	/**
	 * 判断是否手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(177)|(18[0,5-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(mobile);
		return matcher.matches();
	}

	/**
	 * MD5签名验证
	 * 
	 * @author hutao
	 * @date 创建时间：2017年10月20日 上午9:03:12
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	public static boolean checkMD5Sign(String orignStr, String sign) {
		if (StringUtils.isBlank(orignStr) || StringUtils.isBlank(sign)) {
			return false;
		}
		log.info(String.format("签名前-orignStr=%s,验证签名sign=%s", orignStr, sign));
		String signHex = DigestUtils.md5Hex(orignStr);
		log.info(String.format("签名后-原始签名结果signHex=%s,验证签名sign=%s", signHex, sign));
		if (signHex.equals(sign)) {
			return true;
		}
		return false;
	}
}
