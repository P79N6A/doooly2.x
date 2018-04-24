package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdSystemNotice;
import com.doooly.entity.reachad.AdSystemNoticeRead;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdSystemNoticeDao extends BaseDaoI<AdSystemNotice> {

	List<AdSystemNotice> getSystemNoticeList(@Param("userId") String userId,@Param("target") String target, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    int getSystemNoticeNum(@Param("userId") String userId,@Param("target") String target);//获取总数

    int getNoReadNum(@Param("userId") String userId,@Param("target") String target);

    int updateReadType(@Param("userId") String userId,@Param("target") String target);

    List<AdSystemNotice> getNoReadList(@Param("userId") String userId,@Param("target") String target);

    void batchInsert(List<AdSystemNoticeRead> adSystemNoticeReads);

}
