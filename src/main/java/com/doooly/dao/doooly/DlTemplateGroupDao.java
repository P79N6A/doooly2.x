/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.doooly;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.doooly.DlTemplateGroup;
import org.apache.ibatis.annotations.Param;

/**
 * 模版关联企业管理DAO接口
 * @author Mr.Wu
 * @version 2019-03-07
 */
public interface DlTemplateGroupDao extends BaseDaoI<DlTemplateGroup> {
    /**
     * 根据企业id和模版类型查询
     *
     * @param groupId
     * @param type
     * @return
     */
    DlTemplateGroup getByGroupIdAndType(@Param("groupId") String groupId, @Param("type") String type);
//
//    void batchUpdateByBatchId(@Param("list") List<DlTemplateGroup> list);
//
//    int batchInsert(@Param("list") List<DlTemplateGroup> list);
//
//    List<DlTemplateGroup> getGroupIdListByTemplate(@Param("templateId") String templateId);
//
//    int deleteByTemplateId(@Param("templateId") String templateId);

}