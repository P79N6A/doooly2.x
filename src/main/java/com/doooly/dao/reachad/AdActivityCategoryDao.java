package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.persistence.annotation.MyBatisDao;
import com.doooly.entity.reachad.AdActivityCategory;

/**
 * 活动类目管理DAO
 * 
 * @author 赵一鹏
 * @version 2018年3月1日
 */
@MyBatisDao
public interface AdActivityCategoryDao  {

	List<AdActivityCategory> findList(AdActivityCategory adActivityCategory);

}
