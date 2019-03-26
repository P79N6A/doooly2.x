package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.home.v2.servcie.LifehomeService;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.entity.reachad.AdBusiness;
import com.easy.mq.client.RocketClient;
import com.easy.mq.result.RocketProducerMessage;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reach.constrant.Group;
import com.reach.constrant.Topic;
import com.reach.dto.UserEventReq;
import com.reach.enums.EventType;
import com.reach.redis.utils.GsonUtils;
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
import java.util.*;

/**
 * @Author: wanghai
 * @Date:2019/3/7 9:44
 * @Copyright:reach-life
 * @Description:app生活频道
 */
@Component
@Path("lifehome")
public class LifehomeRestServiceImpl {

    private String BASE_URL = PropertiesHolder.getProperty("BASE_URL") + "/businessinfo/";

    private static Logger logger = LoggerFactory.getLogger(LifehomeRestServiceImpl.class);

    @Autowired
    private ConfigDictServiceI configDictServiceI;

    @Autowired
    private LifehomeService lifehomeService;

    @Resource(name = "rocketClient")
    private RocketClient rocketClient;

    @Autowired
    private AdBusinessDao adBusinessDao;

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
        calendar.add(Calendar.DAY_OF_MONTH,-30);
        Date startDate = calendar.getTime();
        JSONObject param = new JSONObject();
        param.put("startDate", DateUtils.dateFormatStr(startDate,"yyyy-MM-dd"));
        param.put("endDate",DateUtils.dateFormatStr(currentDate,"yyyy-MM-dd"));
        param.put("pageNo",1);
        param.put("pageSize",3);
        param.put("userId",userId);
        param.put("event","VISIT_BUSI");
        String actionUrl = configDictServiceI.getValueByTypeAndKey("actionUrl","actionUrl");
        JSONObject ret = HttpClientUtil.httpPost(actionUrl + "query/v1/",param);
        logger.info("action返回：{},{}",userId,ret);
        int code = ret.getInteger("code");
        List<String> businessIds = new ArrayList<>();
        List<Map<String,Object>> listData = new ArrayList<>();
        if (1000 == code) {
            String retData = ret.getString("data");
            Map<String,Object> mapData = GsonUtils.son.fromJson(retData,Map.class);
            String dataStr = JSONArray.toJSONString(mapData.get("data"));
            List<Map<String,Object>> mapList = GsonUtils.son.fromJson(dataStr,new TypeToken<List<Map<String,Object>>>(){}.getType());
            for (int i = 0; i < mapList.size(); i++) {
                Map<String,Object> itemMap = mapList.get(i);
                if (itemMap.get("businessId") != null) {
                    businessIds.add(String.valueOf(itemMap.get("businessId")));
                }
            }
            List<AdBusiness> adBusinessList = new ArrayList<>();
            if (businessIds.size() > 0) {
                adBusinessList = adBusinessDao.getListByBusinessIds(businessIds);
            }
            AdBusiness adBusinessItem = null;
            for (int i = 0; i < adBusinessList.size(); i++) {
                adBusinessItem = adBusinessList.get(i);
                Map<String, Object> mapItem = new HashMap<>();
                mapItem.put("subUrl",BASE_URL.substring(BASE_URL.indexOf("#") + 1, BASE_URL.length()) + adBusinessItem.getDealType()
                        + "/" + adBusinessItem.getId());
                mapItem.put("linkUrl",BASE_URL + adBusinessItem.getDealType() + "/" + adBusinessItem.getId());
                mapItem.put("iconUrl", adBusinessItem.getLogo());
                mapItem.put("mainTitle",adBusinessItem.getCompany());
                mapItem.put("serverEndTime",adBusinessItem.getServerEndTime());
                listData.add(mapItem);
            }
        } else {
            resultModel.setCode(code);
        }
        data.put("list",listData.size() > 0 ? listData : jsonArray);
        resultModel.setData(data);
        return resultModel;
    }


    @POST
    @Path("getGuideCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel getGuideCategory(JSONObject jsonObject,@Context HttpServletRequest request) {
        ResultModel resultModel = ResultModel.ok();
        Map<String,Object> data = Maps.newHashMap();
        String groupId = request.getHeader("groupId");
        resultModel.setData(lifehomeService.getGuideCategory(groupId));
        return resultModel;
    }


    @POST
    @Path("getGuideCategoryBusi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel getGuideCategoryBusi(JSONObject jsonObject,@Context HttpServletRequest request) {
        ResultModel resultModel = ResultModel.ok();
        String guideCategoryId = jsonObject.getString("guideCategoryId");
        int pageNum = jsonObject.getInteger("pageNum");
        int pageSize = jsonObject.getInteger("pageSize");
        resultModel.setData(lifehomeService.getGuideCategoryBusi(guideCategoryId,pageNum,pageSize));
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
        String city = jsonObject.getString("city");
        int pageNum = 1;
        int pageSize = 20;
        String groupId = request.getHeader("groupId");
        String channel = request.getHeader(Constants.CHANNEL);
        logger.info("getLifeFloors参数:{},{}",groupId,city);
        String floorStr = configDictServiceI.getValueByTypeAndKeyNoCache("getLifeFloors_data","getLifeFloors_data");
        JSONArray jsonArray = JSONArray.parseArray(floorStr);
        data.put("floors",lifehomeService.getLifeFloors(groupId,pageNum,pageSize,channel,city));
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
