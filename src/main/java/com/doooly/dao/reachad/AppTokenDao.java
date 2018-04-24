package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AppToken;

/**
 * 
 * @author Albert
 *
 */
public interface AppTokenDao {

	public AppToken findRecord(AppToken token);
	
	public AppToken findValidRecord(AppToken token);
	
	public int insert(AppToken token);
	
	public int update(AppToken token);
	
	public int clear();
	
}
