package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.spi.container.ContainerRequest;

/**
 * @date: 2018-02-26
 */
public interface PicPushServiceI {


    String picPush(JSONObject json);//推送图片
    String textPush(JSONObject json);//推送链接
}
