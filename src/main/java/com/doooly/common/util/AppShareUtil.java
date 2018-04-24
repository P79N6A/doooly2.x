package com.doooly.common.util;

import com.doooly.common.constants.PropertiesHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分享相关辅助类
 * 
 * @author john
 */
public class AppShareUtil {
	
	private static Logger log = LoggerFactory.getLogger(AppShareUtil.class);

	/**
	 * 读取指定前缀的配置信息<br>
	 * 包括标题,描述,等<br>
	 * 
	 * @param appType
	 * @param prefix
	 * @param params
	 * @return
	 */
	public static Map<String, Object> getShareInfo(String appType, String prefix, String[] params) {
		log.info("appType = {},prefix = {}, params = {}", appType, prefix, params);
		// 判断APP类型
//		String title = PropertiesConstants.shareBundle.getString(prefix + ".title");
//		String desc = PropertiesConstants.shareBundle.getString(prefix + ".desc");
//		String link = PropertiesConstants.shareBundle.getString(prefix + ".link");
//		String imgUrl = PropertiesConstants.shareBundle.getString(prefix + ".imgUrl");
//		String type = PropertiesConstants.shareBundle.getString(prefix + ".type");
//		String dataUrl = PropertiesConstants.shareBundle.getString(prefix + ".dataUrl");
		
		String title = PropertiesHolder.getProperty(prefix + ".title");
		String desc = PropertiesHolder.getProperty(prefix + ".desc");
		String link = PropertiesHolder.getProperty(prefix + ".link");
		String imgUrl = PropertiesHolder.getProperty(prefix + ".imgUrl");
		String type = PropertiesHolder.getProperty(prefix + ".type");
		String dataUrl = PropertiesHolder.getProperty(prefix + ".dataUrl");
		
		if(title == null ||desc == null || link == null){
			log.info("no paramters found for prefix = {}" , prefix);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
        String[] str = replace(params, title, desc, link,imgUrl);
        map.put("title", str[0]);
        map.put("desc", str[1]);
        map.put("link", str[2]);
        map.put("imgUrl", str[3]);
		map.put("type", type);
		map.put("dataUrl", dataUrl);
		log.info("map = {}" , map);
		return map;
	}

	/**
	 * 使用参数替换配置文件里的占位符
	 * 
	 * @param params
	 * @param strs
	 * @return
	 */
	public static String[] replace(String[] params, String... strs) {
		if(params != null){
			int index = 0;
			String[] newStrs = new String[strs.length];
			for (int i = 0; i < strs.length; i++) {
				String str = strs[i];
				if (str == null) {
					continue;
				}
				str = str.trim();
				Pattern p = Pattern.compile("\\{\\d+\\}");
				Matcher m = p.matcher(str);
				while (m.find() && index < params.length) {
					String s = m.group();
					String param = params[index++];
					try {
						param =URLDecoder.decode(param, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					str = str.replace(s, param);
				}
				newStrs[i] = str;
			}
			log.info("params = {}", Arrays.asList(params));
			log.info("newStrs = {}", Arrays.asList(newStrs));
			return newStrs;
		}
		return strs;
	}
}
