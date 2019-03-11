package com.doooly.publish.rest.giftBag.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.publish.rest.giftBag.GiftBagPublishI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * 模版接口
 * @Author: Mr.Wu
 * @Date: 2019/3/8
 */
@Component
@Path("/giftBag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GiftBagPublish implements GiftBagPublishI {
    private static Logger logger = LoggerFactory.getLogger(GiftBagPublish.class);


    @POST
    @Override
    @Path(value = "/getAllGiftBagByGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllGiftBagByGroup(JSONObject json, @Context HttpServletRequest request) {
        String groupId = request.getHeader("groupId");
        Integer pageNo = json.getInteger("pageNo");
        Integer pageSize = json.getInteger("pageSize");

        JSONObject result = new JSONObject();
        result.put("code", 1000);
        result.put("msg", "请求成功");
        List<Object> list = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            JSONObject gift = new JSONObject();
            gift.put("id", i * 100000);
            gift.put("name", "礼包" + i);
            gift.put("image", "https://admin.doooly.com/image/201902/30326135-2640-443a-b5ed-6aafeea9f400.png");
            list.add(gift);
        }
        result.put("gifts", list);
        result.put("hasMore", true);
        return result.toJSONString();
    }

    @POST
    @Override
    @Path(value = "/getGiftBagProduct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getGiftBagProduct(JSONObject json) {
        Integer pageNo = json.getInteger("pageNo");
        Integer pageSize = json.getInteger("pageSize");
        String giftId = json.getString("giftId");
        JSONObject result = new JSONObject();
        result.put("code", 1000);
        result.put("msg", "请求成功");
        result.put("hasMore", true);
        result.put("giftId", giftId);
        List<Object> list = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            JSONObject product = new JSONObject();
            product.put("productId", i * 100);
            product.put("skuId", 1000 + i);
            product.put("title", "商品名称" + i);
            product.put("introduction", "商品简述" + i);
            product.put("image", "https://admin.doooly.com/image/201902/30326135-2640-443a-b5ed-6aafeea9f400.png");
            product.put("price", 100 + 1);
            product.put("originalPiect", 200 + 1);
            list.add(product);
        }
        result.put("products", list);


        return result.toJSONString();
    }
}
