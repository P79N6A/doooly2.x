package com.doooly.publish.rest.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myorder.service.OrderService;
import com.doooly.common.constants.Constants;
import com.doooly.common.context.SpringContextHolder;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.common.CommonPublishI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @Author: Mr.Wu
 * @Date: 2019/3/18
 */
@Component
@Path("/common")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommonPublish implements CommonPublishI {
    private static Logger logger = LoggerFactory.getLogger(CommonPublish.class);
    /** 缓存对象 */
    private static StringRedisTemplate redisTemplate = (StringRedisTemplate) SpringContextHolder
            .getBean("redisTemplate");
    @Autowired
    private OrderService orderService;

    @POST
    @Override
    @Path(value = "/cannelUserFlag")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String cannelUserFlag(JSONObject json,  @Context HttpServletRequest request) {
        String userToken = request.getHeader(Constants.TOKEN_NAME);
        logger.info("cannelUserFlag() userToken={}", userToken);
        if (StringUtils.isEmpty(userToken)) {
            return new MessageDataBean("1001", "userToken is null").toJsonString();
        }

        logger.info("cannelUserFlag() json = {}", json);
        String result = "";
        String userId = String.valueOf(redisTemplate.opsForValue().get(userToken));

        try {
            String flags = json.getString("userFlag");
            orderService.cannelUserFlag(userId, flags);
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageDataBean("1001", "userToken is null").toJsonString();
        }

        return new MessageDataBean("1000", "请求成功").toJsonString();
    }
}
