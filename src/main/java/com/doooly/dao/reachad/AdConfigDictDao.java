package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @date: 2018-04-17
 */
public interface AdConfigDictDao {

	String getValueByKey(String accessToken);

	String getValueByTypeAndKey(@Param("type") String dictType, @Param("key") String dictKey);

	List<String> getValueListByTypeAndKey(@Param("type") String dictType, @Param("key") String dictKey);
}
