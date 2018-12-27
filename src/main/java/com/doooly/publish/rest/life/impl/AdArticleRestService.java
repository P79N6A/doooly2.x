package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.AdArticleRestServiceI;
import com.sun.jersey.spi.container.ContainerRequest;
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
 * @Description: 导购
 * @author: qing.zhang
 * @date: 2018-02-26
 */
@Component
@Path("/guide")
public class AdArticleRestService implements AdArticleRestServiceI {

    private static final Logger logger = LoggerFactory.getLogger(AdArticleRestService.class);

    @Autowired
    private AdArticleServiceI adArticleServiceI;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取导购列表
     */
    @POST
    @Path(value = "/getGuideProductList")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String getGuideProductList(JSONObject json, ContainerRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userId = json.getString("userId");
            String orderType = json.getString("orderType");//排序字段 1 最新 2，价格(低到高) 3，销量 4，价格(高到低)
            Integer currentPage = json.getInteger("currentPage");
            Integer pageSize = json.getInteger("pageSize");
            messageDataBean = adArticleServiceI.getGuideProductList(orderType, currentPage, pageSize,userId);
        } catch (Exception e) {
            logger.error("获取导购信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取导购列表v2
     */
    @POST
    @Path(value = "/getGuideProductList/v2")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String getGuideProductListv2(JSONObject json, @Context HttpServletRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
//            String userId = json.getString("userId");
//            String guideCategoryId = json.getString("guideCategoryId");//导购类目
//            String recommendHomepage = json.getString("recommendHomepage");//是否推荐到首页 0 不推荐，1 推荐
//            Integer currentPage = json.getInteger("currentPage");
//            Integer pageSize = json.getInteger("pageSize");

            Map<String, String> map = new HashMap<>();
            map.put("userId", json.getString("userId"));
            map.put("guideCategoryId", json.getString("guideCategoryId"));
            map.put("currentPage", json.getInteger("currentPage").toString());
            map.put("pageSize", json.getInteger("pageSize").toString());
            map.put("groupId", request.getHeader("groupId"));

            messageDataBean = adArticleServiceI.getGuideProductListv2(map);
        } catch (Exception e) {
            logger.error("获取导购信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toFormatJsonString();
    }


    /**
     * 获取导购文章列表
     */
    @POST
    @Path(value = "/getArticleProductList")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String getArticleProductList(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String articleId = json.getString("articleId");//文章id
            messageDataBean = adArticleServiceI.getArticleProductList(articleId);
        } catch (Exception e) {
            logger.error("获取导购文章信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取首页超值热卖列表
     */
    @POST
    @Path(value = "/getArticleList")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String getArticleList(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            messageDataBean = adArticleServiceI.getArticleList();
        } catch (Exception e) {
            logger.error("获取首页超值热卖列表出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取导购类目
     */
    @POST
    @Path(value = "/getGuideCategaryList")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String getGuideCategaryList(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            messageDataBean = adArticleServiceI.getGuideCategaryList();
        } catch (Exception e) {
            logger.error("获取导购类目出错出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 立即订购添加计数
     */
    @POST
    @Path(value = "/addSellCount")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String addSellCount(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String productId = json.getString("productId");//商品id
            messageDataBean = adArticleServiceI.addSellCount(productId);
        } catch (Exception e) {
            logger.error("立即订购添加计数出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }

}
