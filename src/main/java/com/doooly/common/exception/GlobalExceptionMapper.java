package com.doooly.common.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.doooly.common.dto.BaseRes;
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
	private Logger log = Logger.getLogger(this.getClass());
	
    @Override
    public Response toResponse(Exception e) {
        Response.ResponseBuilder ResponseBuilder = null;
        BaseRes<Object> res = new BaseRes<Object>();
        GlobalException exp = null;
        if (e instanceof GlobalException){
        	exp = (GlobalException) e;
        }else {
        	exp = new GlobalException(e.getMessage());
        }
        res.setCode(exp.getCode());
    	res.setMsg(exp.getMsg());
        ResponseBuilder = Response.ok(res, MediaType.APPLICATION_JSON);
        e.printStackTrace();
        log.warn("执行自定义异常,全局异常="+e.getMessage());
        return ResponseBuilder.build();
    }
}