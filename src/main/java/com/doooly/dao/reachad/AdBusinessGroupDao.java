package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBusinessGroup;

public interface AdBusinessGroupDao extends BaseDaoI<AdBusinessGroup> {
	
	List<AdBusinessGroup> selectByGid(String gid);
}
