package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdArea;

public interface AdAreaDao extends BaseDaoI<AdArea> {

	List<AdArea> findServicedAreaList();


}
