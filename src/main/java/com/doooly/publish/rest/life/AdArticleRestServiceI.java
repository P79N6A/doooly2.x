package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.spi.container.ContainerRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * @Description: 导购
 * @author: qing.zhang
 * @date: 2018-02-26
 */
public interface AdArticleRestServiceI {

    String getGuideProductList(JSONObject json, ContainerRequest request);//获取导购信息

    String getGuideProductListv2(JSONObject json, @Context HttpServletRequest request);//获取导购信息v2

    String getArticleProductList(JSONObject json);//获取导购文章信息

    String getArticleList(JSONObject json);//获取超值热卖信息

    String getGuideCategaryList(JSONObject json);//获取导购类目分类信息

    String addSellCount(JSONObject json);//立即订购添加售出数
}
