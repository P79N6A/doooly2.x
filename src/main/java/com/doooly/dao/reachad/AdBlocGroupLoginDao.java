package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdBlocGroupLogin;

public interface AdBlocGroupLoginDao {
	
	AdBlocGroupLogin selectByBlocChannel(@Param("blocChannel")String blocChannel);
}
