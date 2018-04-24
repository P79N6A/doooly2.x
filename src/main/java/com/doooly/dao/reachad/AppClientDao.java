package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.entity.reachad.AppClient;

/**
 * 
 * @author Albert
 *
 */
public interface AppClientDao {
	/**
	 * 根据用户ID和用户设备号查找id
	 * @param client
	 * @return
	 */
	public int getId(AppClient client);
	/**
	 * 根据用户ID查找用户设备信息
	 * @param client
	 * @return
	 */
	public List<AppClient> getClientByUserID(AppClient client); 
	/**
	 * 新增用户设备信息
	 * @param client
	 * @return
	 */
	public int insert(AppClient client);
	/**
	 * 更新用户设备信息
	 * @param client
	 * @return
	 */
	public int update(AppClient client);
	
}
