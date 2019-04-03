package com.doooly.service.doooly;

import java.util.Map;

/**
 * 模版服务接口
 * @Author: Mr.Wu
 * @Date: 2019/3/13
 */
public interface DlTemplateInfoServiceI {
    /**
     * 获取模版信息
     * @param map
     * @return
     */
    public String getTemplateInfoByType(Map<String, Object> map);
}
