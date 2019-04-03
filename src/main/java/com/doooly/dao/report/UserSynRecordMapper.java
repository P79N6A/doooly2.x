package com.doooly.dao.report;


import com.doooly.entity.report.UserSynRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserSynRecordMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserSynRecord record);

    int insertSelective(UserSynRecord record);

    UserSynRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSynRecord record);

    int updateByPrimaryKey(UserSynRecord record);

    void batchInsertSynUserRecord(@Param("list") List<UserSynRecord> list);
}