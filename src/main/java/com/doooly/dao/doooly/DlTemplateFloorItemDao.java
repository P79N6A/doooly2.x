/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.doooly;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.doooly.DlTemplateFloorItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 楼层关联itemDAO接口
 * @author Mr.Wu
 * @version 2019-03-07
 */
public interface DlTemplateFloorItemDao extends BaseDaoI<DlTemplateFloorItem> {

    List<DlTemplateFloorItem> getAllByFloorId(@Param("floorId") String floorId);

}