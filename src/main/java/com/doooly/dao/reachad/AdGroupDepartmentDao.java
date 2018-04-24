package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdGroupDepartment;

/**
 * @Description: 部门
 * @author: wenwei.yang
 * @date: 2017-10-09
 */
public interface AdGroupDepartmentDao extends BaseDaoI<AdGroupDepartment> {

	List<AdGroupDepartment> getDepartmentDatasByGroupId(String companyId);


    
}
