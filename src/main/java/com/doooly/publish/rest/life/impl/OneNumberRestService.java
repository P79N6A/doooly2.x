package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.oneNumber.service.OneNumberServiceI;
import com.doooly.common.util.RequestUtils;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.OneNumberRestServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2017-08-28
 */
@Component
@Path("/oneNumber")
public class OneNumberRestService implements OneNumberRestServiceI{

    private static final Logger logger = LoggerFactory.getLogger(OneNumberRestService.class);

    @Autowired
    private OneNumberServiceI oneNumberServiceI;

    @POST
    @Path(value = "/getTargetUrl")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getTargetUrl(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        logger.info("请求参数为---->"+ json);
        try {
            String userId = json.getString("userId");
            String businessId = json.getString("businessId");
            String targetUrl = json.getString("targetUrl");
            messageDataBean = oneNumberServiceI.getTargetUrl(userId,businessId,targetUrl);
        } catch (Exception e) {
            logger.error("获取1号通跳转链接出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    @GET
    @Path(value = "/login/authorization")
    public String authorization(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        MessageDataBean messageDataBean = new MessageDataBean();
        JSONObject json = RequestUtils.getJsonParam(request);
        logger.info("请求参数为---->"+ json);
        try {
            messageDataBean = oneNumberServiceI.authorization(json);
        } catch (Exception e) {
            logger.error("第三方跳转doooly免登陆验证出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }


    // 上线福特项目导致跳转京东的链接不成功，url后面多了token 和userId,代码回滚， add by pual 2019/1/25
    /*@POST
    @Path(value = "/getTargetUrl")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getTargetUrl(JSONObject json, @Context HttpServletRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        logger.info("getTargetUrl请求参数为---->"+ json);
        try {
            String userId = json.getString("userId");
            String businessId = json.getString("businessId");
            String targetUrl = json.getString("targetUrl");
            String token = request.getHeader("token");
            messageDataBean = oneNumberServiceI.getTargetUrl(userId,businessId,targetUrl);
            Map<String,Object> map = messageDataBean.getData();
            String resultUrl = String.valueOf(map.get("resultUrl"));
            if (!resultUrl.contains("userId=")) {
                resultUrl +="&userId="+userId;
            }
            if (!resultUrl.contains("token=")) {
                resultUrl +="&token="+token;
            }
            map.put("resultUrl",resultUrl);
            messageDataBean.setData(map);
        } catch (Exception e) {
            logger.error("获取1号通跳转链接出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }*/
}
