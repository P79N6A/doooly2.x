package com.doooly.business.home.v2.servcie;

import java.util.List;
import java.util.Map;

/**
 * @Author: wanghai
 * @Date:2019/3/14 15:44
 * @Copyright:reach-life
 * @Description:
 */
public interface LifehomeService {

    List<Map<String, Object>> getLifeFloors(String groupId, int pageNum, int pageSize,String channel);

    List<Map<String,Object>> getGuideCategory(String groupId);

    List<Map<String,Object>> getGuideCategoryBusi(String guideCategoryId,int pageNum,int pageSize);

}
