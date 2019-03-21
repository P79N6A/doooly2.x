package com.doooly.business.groupEquity.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.groupEquity.AdGroupEquityService;
import com.doooly.dao.reachad.AdEquityDao;
import com.doooly.dao.reachad.AdGroupEquityLevelDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdEquity;
import com.doooly.entity.reachad.AdGroupEquityLevel;
import com.doooly.publish.rest.reachad.AdGroupEquityPubService;
import com.doooly.publish.rest.reachad.impl.AdGroupEquityPub;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sfc
 * @date 2019/3/18 18:08
 */
@Service
public class AdGroupEquityServiceImpl implements AdGroupEquityService {

    private static Logger logger = Logger.getLogger(AdGroupEquityPub.class);

    @Autowired
    private AdGroupEquityLevelDao adGroupEquityLevelDao;
    @Autowired
    private AdEquityDao adEquityDao;

    @Override
    public String adGroupEquityLevelList(String groupId) {
        JSONArray jArr = new JSONArray();
        try{
            List<AdGroupEquityLevel> adGroupEquityList = adGroupEquityLevelDao.getAdGroupEquityList(groupId);
            for(int i=0;i<adGroupEquityList.size();i++){
                JSONObject jo = new JSONObject();
                AdGroupEquityLevel adGroupEquityLevel = adGroupEquityList.get(i);
                jo.put("adGroupId",adGroupEquityLevel.getAdGroupId());
                jo.put("adEquityId",adGroupEquityLevel.getAdEquityId());
                jo.put("adEquityName",adGroupEquityLevel.getEquityName());
                jo.put("adEquityLogo",adGroupEquityLevel.getEquityLogo());
                jo.put("equityDesc",adGroupEquityLevel.getEquityDesc());
                jo.put("userService",adGroupEquityLevel.getUserService());
                jo.put("instructions",adGroupEquityLevel.getInstructions());
                jArr.add(jo);
            }
        }catch (Exception e){
            logger.error("%%%AdGroupEquityServiceImpl error is " + e.getMessage(),e);
            throw e;
        }
        return jArr.toString();
    }

    @Override
    public String adEquityByEquityId(String equityId) {
        String jsonStr = null;
        try{
            jsonStr = JSONObject.toJSONString(adEquityDao.findEquity(equityId));
        }catch (Exception e){
            logger.error("%%%adEquityByEquityId error is " + e.getMessage(),e);
            throw e;
        }
        return jsonStr;
    }
}
