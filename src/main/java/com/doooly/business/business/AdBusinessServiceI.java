package com.doooly.business.business;

import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessExpandInfo;

/**
 * @Description: ad_bussiness系列表
 * @author: qing.zhang
 * @date: 2018-12-27
 */
public interface AdBusinessServiceI {

    AdBusiness getBusiness(AdBusiness adBusiness);

    AdBusinessExpandInfo getBusinessExpandInfo(AdBusinessExpandInfo adBusinessExpandInfo);
}
