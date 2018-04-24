package com.doooly.business.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.AppClientServiceI;
import com.doooly.common.constants.Constants.AppClientStatus;
import com.doooly.dao.reachad.AppClientDao;
import com.doooly.entity.reachad.AppClient;

/**
 * 客服端设备信息服务类
 * @author 赵清江
 * @date 2016-06-28
 * @version 1.0
 */
@Service
public class AppClientService implements AppClientServiceI {

	@Autowired
	private AppClientDao clientDao;
	

	@Override
	public int getID(long userID,String deviceNumber) {
		AppClient client = new AppClient();
		client.setUserId(userID);
		client.setDeviceNumber(deviceNumber);
		return clientDao.getId(client);
	}
	
	@Override
	public void addOrUpdate(AppClient client) {
		List<AppClient> list = clientDao.getClientByUserID(client);
		client.setIsUsing(AppClientStatus.Using);
		if (list == null) {
			clientDao.insert(client);
			return;
		}
		for (AppClient appClient : list) {
			if (appClient.getDeviceNumber().equals(client.getDeviceNumber())) {
				appClient.setIsUsing(AppClientStatus.NotUsing);
				clientDao.update(appClient);
				clientDao.update(client);
				return;
			}
		}
		clientDao.insert(client);
	}

}
