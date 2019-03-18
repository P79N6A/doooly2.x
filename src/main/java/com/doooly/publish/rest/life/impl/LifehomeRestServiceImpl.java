package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.home.v2.servcie.LifehomeService;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.util.HttpClientUtil;
import com.easy.mq.client.RocketClient;
import com.easy.mq.result.RocketProducerMessage;
import com.google.common.collect.Maps;
import com.reach.constrant.Group;
import com.reach.constrant.Topic;
import com.reach.dto.UserEventReq;
import com.reach.enums.EventType;
import com.reach.redis.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Calendar;
import java.util.Date;
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

    private static Logger logger = LoggerFactory.getLogger(LifehomeRestServiceImpl.class);

    @Autowired
    private ConfigDictServiceI configDictServiceI;

    @Autowired
    private LifehomeService lifehomeService;

    @Resource(name = "rocketClient")
    private RocketClient rocketClient;

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

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        Date startDate = calendar.getTime();
        JSONObject param = new JSONObject();
        param.put("startDate", DateUtils.dateFormatStr(startDate,"yyyy-MM-dd"));
        param.put("endDate",DateUtils.dateFormatStr(currentDate,"yyyy-MM-dd"));
        param.put("pageNo",1);
        param.put("pageSize",3);
        String actionUrl = configDictServiceI.getValueByTypeAndKey("actionUrl","actionUrl");
        JSONObject ret = HttpClientUtil.httpPost(actionUrl + "query/v1/",param);
        data.put("list",ret);
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
        int pageNum = jsonObject.getInteger("pageNum");
        int pageSize = jsonObject.getInteger("pageSize");
        String groupId = request.getHeader("groupId");
        String floorStr = configDictServiceI.getValueByTypeAndKeyNoCache("getLifeFloors_data","getLifeFloors_data");
        JSONArray jsonArray = JSONArray.parseArray(floorStr);
        data.put("floors",lifehomeService.getLifeFloors(groupId,pageNum,pageSize));
        resultModel.setData(data);
        return resultModel;
    }

    @POST
    @Path("addUerRecentVisitBusiness")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel addUerRecentViewBusiness(JSONObject jsonObject,@Context HttpServletRequest request) {
        ResultModel resultModel = ResultModel.ok();
        String userId = jsonObject.getString("userId");
        String businessId = jsonObject.getString("businessId");
        try{
            RocketProducerMessage message = new RocketProducerMessage();
            message.setTopic(Topic.ACTION_TOPIC);
            message.setGroup(Group.ACTION_GROUP);

            UserEventReq req = new UserEventReq();
            req.setEvent(EventType.VISIT);
            if(request.getHeader("deviceNo") != null) {
                req.setDeviceId(request.getHeader("deviceNo"));
            }else if(request.getHeader("deviceId") != null){
                req.setDeviceId(request.getHeader("deviceId"));
            }
            req.setUserId(userId);
            req.setBusinessId(businessId);
            req.setChannel(request.getHeader("appSource"));
            message.setBody(SerializationUtils.serialize(req));
            rocketClient.send(message, true);
            logger.info("action data:{}", JsonUtil.bean2Json(req));
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultModel;
    }

}
