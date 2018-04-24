package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

/**
 * @Description: 商户特权开通
 * @author: qing.zhang
 * @date: 2017-09-12
 */
public interface BusinessPrivilegeServiceI {
    MessageDataBean getActivityDetail(Integer activityId, Integer userId);

    MessageDataBean apply(Integer activityId, Integer userId);

}
