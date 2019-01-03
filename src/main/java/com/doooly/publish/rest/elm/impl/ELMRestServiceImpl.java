package com.doooly.publish.rest.elm.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.publish.rest.elm.ELMRestServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Description: 饿了吗
 * @author: qing.zhang
 * @date: 2019-01-03
 */
@Component
@Path("/doooly/elm")
public class ELMRestServiceImpl implements ELMRestServiceI{

    @Autowired
    private ELMServiceI elmServiceI;


    @POST
    @Path(value = "/orderAmountPush")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String orderAmountPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = elmServiceI.orderAmountPush(obj,httpServletRequest);
        return resultModel.toELMString();
    }

    @POST
    @Path(value = "/orderStatusPush")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String orderStatusPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        return null;
    }
}
