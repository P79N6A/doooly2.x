package com.doooly.business.common.service;

import com.doooly.entity.reachad.AppClient;

/**
 * 
 * @author Albert
 *
 */
public interface AppClientServiceI {
	/**
	 * 根据用户ID和用户设备号查找id
	 * @param client
	 * @return
	 */
	public int getID(long userID,String deviceNumber);
	/**
	 * 更新或新增用户设备信息
	 * @param client
	 * @return
	 */
	public void addOrUpdate(AppClient client);
	
}
