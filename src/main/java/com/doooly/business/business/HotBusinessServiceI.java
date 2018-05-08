package com.doooly.business.business;

import com.doooly.dto.common.MessageDataBean;

/**
 * @Description:
 * @author: 
 * @date: 2017-05-31
 */
public interface HotBusinessServiceI {

	MessageDataBean getIndexData(Integer userId, Integer type);
	MessageDataBean getHotMerchatData(Integer userId, String address, Integer type, Integer shopType);

	MessageDataBean getHotDatas(Integer userId, String address, Integer currentPage, Integer pageSize, String type, Integer shopType);

	MessageDataBean getBusinessInfo(Long userId);
	MessageDataBean getDictDatas(Integer userId, String address);
	MessageDataBean getBusinessServiceData(Long userId);

}
