package com.doooly.business.home.v2.servcie;

import com.doooly.entity.reachad.AdConsumeRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动类目，类型，楼层
 * @Author: Mr.Wu
 * @Date: 2018/12/6
 */
public interface AdConsumeRechargeServiceI {

    /**
     * 根据模版id 及 楼层id 获得楼层对应内容
     * @param templateId
     * @param floorId
     * @return
     */
    List<AdConsumeRecharge> getConsumeRecharges(@Param("templateId") int templateId, @Param("floorId") int floorId);
}
