package com.doooly.publish.rest.template.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.Constants;
import com.doooly.common.context.SpringContextHolder;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.template.TemplatePublishI;
import com.doooly.service.doooly.DlTemplateInfoServiceI;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 模版接口
 * @Author: Mr.Wu
 * @Date: 2019/3/8
 */
@Component
@Path("/template")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplatePublish implements TemplatePublishI {
    private static Logger logger = LoggerFactory.getLogger(TemplatePublish.class);
    @Autowired
    private DlTemplateInfoServiceI dlTemplateInfoService;
    /** 缓存对象 */
    private static StringRedisTemplate redisTemplate = (StringRedisTemplate) SpringContextHolder
            .getBean("redisTemplate");

    @POST
    @Override
    @Path(value = "/getTemplateByType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getTemplateByType(JSONObject json, @Context HttpServletRequest request) {
        String userToken = request.getHeader(Constants.TOKEN_NAME);
        logger.info("getTemplateByType() userToken={}", userToken);

        if (StringUtils.isEmpty(userToken)) {
            return new MessageDataBean("1001", "userToken is null").toJsonString();
        }

        logger.info("getTemplateByType() json = {}", json);
        String result = "";
        String userId = String.valueOf(redisTemplate.opsForValue().get(userToken));
        Integer tempType = json.getInteger("tempType");
        String groupId = request.getHeader("groupId");
        String address = json.getString("address");

        try {
            if (tempType == null || tempType <= 0) {
                // 参数错误
                logger.info("getTemplateByType() 请求参数错误 tempType = {}", tempType);
                return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess).toJsonString();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("groupId", groupId);
            map.put("type", tempType);
            map.put("userId", "1162242");
            map.put("address", address);

            result = dlTemplateInfoService.getTemplateInfoByType(map);

        } catch (Exception e) {
            // 系统异常
            logger.info("getTemplateByType() 请求错误 json = {}", json);
            e.printStackTrace();
            return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess).toJsonString();
        }

        return result;
    }

}
