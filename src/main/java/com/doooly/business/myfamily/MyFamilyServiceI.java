package com.doooly.business.myfamily;

import com.doooly.dto.common.MessageDataBean;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 我的家属
 * @author: qing.zhang
 * @date: 2017-07-25
 */
public interface MyFamilyServiceI {

    MessageDataBean getMyFamily(Integer userId);

    MessageDataBean initMyFamily(Integer userId, List<HashMap<String, Object>> invitationFamilyList);

    MessageDataBean getMyFamilyList(Integer userId);

    MessageDataBean confirmSharePoint(Integer userId, Integer isPointShare);

}
