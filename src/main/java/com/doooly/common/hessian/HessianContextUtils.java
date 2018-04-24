package com.doooly.common.hessian;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.doooly.common.IPUtils;
/**
 * Hessian上下文工具类
* <p>Title: HessianContextUtils</p>
* <p>Description: </p>
* <p>Company: </p> 
* @author hutao
* @date 2016年6月20日 下午2:40:57
 */
public class HessianContextUtils {  
  
    private HttpServletRequest _request;  
    private static final ThreadLocal<HessianContextUtils> _localContext   
                                  = new ThreadLocal<HessianContextUtils>() {  
        @Override  
        public HessianContextUtils initialValue() {  
            return new HessianContextUtils();  
        }  
    };  
  
    private HessianContextUtils() {  
    }  
  
    public static void setRequest(HttpServletRequest request) {  
        _localContext.get()._request = request;  
    }  
  
    public static HttpServletRequest getRequest() {  
        return _localContext.get()._request;  
    }  
  
    public static void clear() {  
        _localContext.get()._request = null;  
    }  
    /**
     * 获取客户端IP地址
     * @return
     */
    public static String getClientIP(){
    	return IPUtils.getIpAddr(getRequest());
    }
}  