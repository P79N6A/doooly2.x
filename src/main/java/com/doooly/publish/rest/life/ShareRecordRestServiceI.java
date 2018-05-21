package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.spi.container.ContainerRequest;

/**
 * @Description: 分享记录校验接口
 * @author: yangwenwei
 * @date: 2018-02-26
 */
public interface ShareRecordRestServiceI {

    String isSetShareRecord(JSONObject json);//判断是否可以放入记录
    
    String sendByShareRecord(JSONObject json);//当第一次被激活的时候,进行发券
}
