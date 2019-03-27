package com.doooly.service.doooly.impl;

import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.doooly.DlTemplateFloorItemDao;
import com.doooly.entity.doooly.DlTemplateFloorItem;
import com.doooly.service.doooly.DlTemplateFloorItemServiceI;
import com.reach.redis.annotation.Cacheable;
import com.reach.redis.annotation.EnableCaching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 模版item service接口实现
 * @Author: Mr.Wu
 * @Date: 2019/3/21
 */
@Service
@EnableCaching
public class DlTemplateFloorItemServiceImpl implements DlTemplateFloorItemServiceI {
    @Autowired
    private DlTemplateFloorItemDao dlTemplateFloorItemDao;

    @Cacheable(module = "TEMPLATE", event = "getAllByFloorId", key = "floorId",
            expires= RedisConstants.REDIS_CACHE_EXPIRATION_DATE, required = true)
    @Override
    public List<DlTemplateFloorItem> getAllByFloorId(Map<String, Object> map) {
        String floorId = (String) map.get("floorId");
        return dlTemplateFloorItemDao.getAllByFloorId(floorId);
    }
}
