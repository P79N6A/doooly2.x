package com.doooly.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	public static String getRequstURLRootPath(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		return url.substring(0, url.indexOf("/jersey"));
	}


    /**
     * 将请求参数转为json格式
     * @param request
     * @return
     */
    public static JSONObject getJsonParam(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = request.getParameter(key);
            try {
                value = new String(value.getBytes("iso-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("将请求参数转为json格式出错", e);
            }
            jsonObject.put(key, value);
        }
        return jsonObject;
    }
}
