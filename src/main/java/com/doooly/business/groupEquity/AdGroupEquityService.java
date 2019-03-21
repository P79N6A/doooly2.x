package com.doooly.business.groupEquity;

import com.doooly.entity.reachad.AdGroupEquityLevel;

import java.util.List;

/**
 * @author sfc
 * @date 2019/3/18 18:07
 */
public interface AdGroupEquityService {
    String adGroupEquityLevelList(String groupId);
    String adEquityByEquityId(String equityId);
}
