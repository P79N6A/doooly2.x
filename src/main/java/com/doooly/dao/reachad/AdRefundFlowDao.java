package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdRefundFlow;

public interface AdRefundFlowDao {
    int deleteByPrimaryKey(Long id);

    int insert(AdRefundFlow record);

    int insertSelective(AdRefundFlow record);

    AdRefundFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdRefundFlow record);

    int updateByPrimaryKey(AdRefundFlow record);
}