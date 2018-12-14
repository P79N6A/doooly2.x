package com.doooly.publish.rest.meituan.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.publish.rest.meituan.MeituanRestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/14.
 */
@Component
@Path("/meituan")
public class MeituanRestServiceImpl implements MeituanRestService {

    @GET
    @Path("/easyLogin")
    @Produces("application/json;charset=UTF-8")
    @Consumes("application/json;charset=UTF-8")
    @Override
    public Map<String, Object> easyLogin(@Context HttpServletRequest request) {
        String entToken = request.getParameter("entToken");
        Map<String,Object> retMap = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        int status = 0;
        String message = "调用失败";
        if (StringUtils.isNotEmpty(entToken)) {
            status = 1;
            message = "调用成功";
            data.put("loginStatus",1);
            data.put("staffPhoneNo","15711667875");
        }
        retMap.put("status",status);
        retMap.put("message",message);
        retMap.put("data",data);
        return retMap;
    }
}
