package com.doooly.publish.rest.template.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.template.TemplatePublishI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

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

    @POST
    @Override
    @Path(value = "/getTemplateByType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getTemplateByType(JSONObject json) {
        logger.info("getTemplateByType() json = {}", json);
        Integer tempType = json.getInteger("tempType");

        //
        if (tempType == null || tempType <= 0) {
            // 参数错误
            logger.info("getTemplateByType() 请求参数错误 tempType = {}", tempType);
            return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess).toJsonString();
        }
        JSONObject result = new JSONObject();
        List<Object> floorList = new ArrayList<>();

        try {
            for (int i = 1; i <= 6; i++) {
                JSONObject floor = new JSONObject();
                floor.put("type", i);
                floor.put("title", "主标题" + i);
                floor.put("subTitle", "副标题" + i);
                if (i == 5) {
                    floor.put("couponCount", 4); // 礼券数量
                    floor.put("linkUrl", "www.baidu.com"); // 礼券数量
                }
                List<Object> itemList = new ArrayList<>();

                for (int j = 1; j <= 8; j++) {
                    JSONObject item = new JSONObject();
                    if (i == 1) { // 导航栏
                        item.put("type", i);
                        item.put("title", "主标题" + j);
                        item.put("subTitle", "副标题" + j);
                        item.put("iconUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("cornerMark", "角标" + i);
                        item.put("linkUrl", "www.baidu.com");
                    } else if (i == 2) {    // 礼包
                        item.put("type", i);
                        item.put("title", "主标题" + j);
                        item.put("subTitle", "副标题" + j);
                        item.put("iconUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("imageUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("cornerMark", "角标" + i);
                        item.put("linkUrl", "www.baidu.com");
                    } else if (i == 3) { // 广告
                        item.put("type", i);
                        item.put("title", "主标题" + j);
                        item.put("subTitle", "副标题" + j);
                        item.put("iconUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("imageUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("cornerMark", "角标" + i);
                        item.put("linkUrl", "www.baidu.com");
                    } else if (i == 4) { // 热门商户
                        item.put("type", i);
                        item.put("title", "主标题" + j);
                        item.put("subTitle", "副标题" + j);
                        item.put("iconUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("imageUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("cornerMark", "角标" + i);
                        item.put("linkUrl", "www.baidu.com");
                        item.put("isSupportIntegral", (j % 2));
                    } else if (i == 5) { // 卡券专区
                        item.put("type", i);
                        item.put("title", "主标题" + j);
                        item.put("subTitle", "副标题" + j);
                        item.put("iconUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("imageUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("cornerMark", "角标" + i);
                        item.put("linkUrl", "www.baidu.com");
                        item.put("price", 77.222);            // 原价
                        item.put("discountsPrice", 50.02);    // 优惠价格
                        item.put("discountsDesc", "抢购价");   // 优惠描述
                        item.put("sellCount", 100 + j);       // 已售数量

                    } else if (i == 6) { // 活动专区
                        item.put("type", i);
                        item.put("title", "主标题" + j);
                        item.put("subTitle", "副标题" + j);
                        item.put("iconUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("imageUrl", "https://admin.doooly.com/image/201811/038c1753-69ec-4682-aeca-a9661af2bead.png");
                        item.put("cornerMark", "角标" + i);
                        item.put("linkUrl", "www.baidu.com");
                    }
                    itemList.add(item);
                }
                floor.put("list", itemList);
                floorList.add(floor);

            }

            JSONObject floors = new JSONObject();
            floors.put("floors", floorList);
            result.put("code", "1000");
            result.put("msg", "操作成功");
            result.put("date", floors);
        } catch (Exception e) {
            // 系统异常
            logger.info("getTemplateByType() 请求参数错误 tempType = {}", tempType);
            return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess).toJsonString();
        }

        return result.toJSONString();
    }

}
