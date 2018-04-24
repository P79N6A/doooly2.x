package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdAutoColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 报名表单dao
 * @author: qing.zhang
 * @date: 2017-04-26
 */
public interface AdAutoColumnDao {

    List<AdAutoColumn> getSignupForm(@Param("activityId") Integer activityId,@Param("dataType") Integer dataType);
}
