package com.doooly.service.doooly;

import com.doooly.entity.doooly.DlTemplateFloorItem;

import java.util.List;
import java.util.Map;

/**
 * 模版item service接口
 * @Author: Mr.Wu
 * @Date: 2019/3/21
 */
public interface DlTemplateFloorItemServiceI {

    /**
     * 根据floorId获得所有item
     * @param map
     *      floorId: 楼层id
     * @return
     */
    List<DlTemplateFloorItem> getAllByFloorId(Map<String, Object> map);
}
