package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.payment.bean.ResultModel;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @Author: wanghai
 * @Date:2019/3/7 9:44
 * @Copyright:reach-life
 * @Description:app生活频道
 */
@Component
@Path("lifehome")
public class LifehomeRestServiceImpl {

    @Autowired
    private ConfigDictServiceI configDictServiceI;

    @POST
    @Path("/getUserRecentView")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel getUserRecentView(JSONObject jsonObject) {
        long userId = jsonObject.getLongValue("userId");
        ResultModel resultModel = ResultModel.ok();
        Map<String,Object> data = Maps.newHashMap();
        String listStr = configDictServiceI.getValueByTypeAndKeyNoCache("getUserRecentView_data","getUserRecentView_data");
        JSONArray jsonArray = JSONArray.parseArray(listStr);
        data.put("list",jsonArray);
        resultModel.setData(data);
        return resultModel;
    }


    @POST
    @Path("getLifeFloors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel getLifeFloors(JSONObject jsonObject,@Context HttpServletRequest request) {
        ResultModel resultModel = ResultModel.ok();
        Map<String,Object> data = Maps.newHashMap();
        long userId = jsonObject.getLongValue("userId");
        String groupId = request.getHeader("groupId");
        String floorStr = configDictServiceI.getValueByTypeAndKeyNoCache("getLifeFloors_data","getLifeFloors_data");
        JSONArray jsonArray = JSONArray.parseArray(floorStr);
        data.put("floors",jsonArray);
        resultModel.setData(data);
        return resultModel;
    }

}
