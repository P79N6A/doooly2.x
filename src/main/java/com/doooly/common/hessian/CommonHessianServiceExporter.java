package com.doooly.common.hessian;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.caucho.HessianExporter;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.util.NestedServletException;

public class CommonHessianServiceExporter extends HessianServiceExporter implements HttpRequestHandler {  
  
    private static final Logger logger = LoggerFactory.getLogger(HessianServiceExporter.class);  
  
    @Override  
    public void handleRequest(HttpServletRequest request,   
                              HttpServletResponse response)   
                              throws ServletException, IOException {  
//        if (!"POST".equals(request.getMethod())) {  
//            throw new HttpRequestMethodNotSupportedException(  
//                      request.getMethod(),  
//                      new String[]{"POST"},   
//                     "HessianServiceExporter only supports POST requests");  
//        }  
  
        response.setContentType(CONTENT_TYPE_HESSIAN);  
        try {  
            HessianContextUtils.setRequest(request); //保存Request到Hessian线程上下文  
            invoke(request.getInputStream(), response.getOutputStream());  
        }  
        catch (Throwable ex) {  
            logger.error("Hessian skeleton invocation failed");  
            throw new NestedServletException("Hessian skeleton invocation failed", ex);  
        }  
        finally {  
            HessianContextUtils.clear();  
        }  
    }  
}  