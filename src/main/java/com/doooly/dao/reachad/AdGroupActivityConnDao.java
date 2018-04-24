package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdGroupActivityConn;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 活动企业权限DAO接口
 * @author qing.zhang
 * @version 2017-04-25
 */
public interface AdGroupActivityConnDao extends BaseDaoI<AdGroupActivityConn> {


    List<AdGroupActivityConn> findGroup(Integer activityId);

    List<AdGroupActivityConn> findSecondGroup(@Param("ids") String ids);

    List<Map> findDepartment(@Param("ids") String ids);

	AdGroupActivityConn getRealGIft(@Param("activityId")Integer activityId, @Param("groupNum")Long groupNum);
}