package com.doooly.publish.rest.meituan.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.bean.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghai on 2018/12/18.
 */
@Component
@Path("/test")
public class TestRestService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Path("testDelCacheKeys")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel testDelCacheKeys(JSONArray jsonArray) {
        for (Object object : jsonArray) {
            Map<String,String> mapitem = (Map<String,String>) object;
            String key = mapitem.get("key");
            Set<String> keys = stringRedisTemplate.keys("*" + key + "*");
            stringRedisTemplate.delete(keys);
        }
        ResultModel resultModel = new ResultModel(1000,"成功");
        return resultModel;
    }

    @Path("testAddCacheKeys")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel testAddCacheKeys(JSONObject jsonObject) {
        for (Map.Entry<String,Object> entry : jsonObject.entrySet()) {
            stringRedisTemplate.boundValueOps(entry.getKey()).set(entry.getValue().toString());
            stringRedisTemplate.expire(entry.getKey(),5, TimeUnit.MINUTES);
        }
        ResultModel resultModel = new ResultModel(1000,"成功");
        return resultModel;
    }

    @Path("testGetCacheKeys")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel testCacheKeys(JSONArray jsonArray) {
        Map<String,String> ret = new HashMap<>();
        for (Object object : jsonArray) {
            Map<String,String> mapitem = (Map<String,String>) object;
            String key = mapitem.get("key");
            String value = stringRedisTemplate.boundValueOps(key).get();
            ret.put(key,value);
        }
        ResultModel resultModel = new ResultModel(1000,"成功");
        resultModel.setData(ret);
        return resultModel;
    }

}
