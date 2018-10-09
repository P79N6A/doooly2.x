package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdSystemNotice;
import com.doooly.entity.reachad.AdSystemNoticeRead;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdSystemNoticeDao extends BaseDaoI<AdSystemNotice> {

	List<AdSystemNotice> getSystemNoticeList(@Param("userId") String userId,@Param("target") String target, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	List<AdSystemNotice> getSystemNoticeByTypeList(@Param("userId") String userId,@Param("target") String target, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,@Param("noticeType") String noticeType);

    int getSystemNoticeNum(@Param("userId") String userId,@Param("target") String target);//获取总数

    int getSystemNoticeByTypeNum(@Param("userId") String userId,@Param("target") String target,@Param("noticeType") String noticeType);//获取总数

    int getNoReadNum(@Param("userId") String userId,@Param("target") String target);

    int getNoReadNumByType(@Param("userId") String userId,@Param("target") String target,@Param("noticeType") String noticeType);

    List<AdSystemNotice> getNoReadList(@Param("userId") String userId,@Param("target") String target);

    List<AdSystemNotice> getNoReadListByType(@Param("userId") String userId,@Param("target") String target,@Param("noticeType") String noticeType);

    void batchInsert(List<AdSystemNoticeRead> adSystemNoticeReads);

}
