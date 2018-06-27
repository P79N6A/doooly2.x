package com.doooly.business.business;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * @Description:
 * @author:
 * @date: 2017-05-31
 */
public interface HotBusinessServiceI {

	MessageDataBean getIndexData(Integer userId, Integer type);

	MessageDataBean getHotMerchatData(Integer userId, String address, Integer type, Integer shopType);

	MessageDataBean getHotDatas(Integer userId, String address, Integer currentPage, Integer pageSize, String type,
			Integer shopType);

	MessageDataBean getBusinessInfo(Long userId);

	/**
	 * 可用积分服务-商户详情
	 */
	MessageDataBean getBusinessInfoService(JSONObject params);

	MessageDataBean getDictDatas(Integer userId, String address);

	MessageDataBean getBusinessServiceData(Long userId);

	MessageDataBean getBusinessServiceDataV21(Long userId);

}
