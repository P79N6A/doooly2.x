package com.doooly.common.exception;
/**
 * 自定义异常类
 *     
    * @Package com.doooly.common.exception  
    * @author hutao  
    * @date 2017年7月20日  
    * @version V1.0
 */
public class GlobalException extends RuntimeException {
	private static final long serialVersionUID = 6173855747626340068L;
	public static String TOKEN_CODE = "40001";
	public static String TOKEN_DESC = "token验证失败或token不存在";
	private String code="-1";//未捕获异常
    private String msg;

    public GlobalException() {
    }
    
	public GlobalException(String msg) {
		this.msg=msg;
	}

	public GlobalException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}