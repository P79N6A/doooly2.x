package com.doooly.dao.doooly;


import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.doooly.DlTemplateFloor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模版关联楼层DAO接口
 * @author Mr.Wu
 * @version 2019-03-07
 */
public interface DlTemplateFloorDao extends BaseDaoI<DlTemplateFloor> {

    List<DlTemplateFloor> getByTemplateId(@Param("templateId") String templateId);

}