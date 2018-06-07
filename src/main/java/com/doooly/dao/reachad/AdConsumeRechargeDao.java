package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBasicType;
import com.doooly.entity.reachad.AdConsumeRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 活动类目，类型
 * @author: zhaoyipeng
 * 
 */
public interface AdConsumeRechargeDao extends BaseDaoI<AdConsumeRecharge> {

    List<AdConsumeRecharge> getConsumeRecharges(@Param("floorId") int floorId);

 }
