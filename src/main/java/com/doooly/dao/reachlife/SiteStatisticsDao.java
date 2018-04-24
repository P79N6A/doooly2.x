package com.doooly.dao.reachlife;

public interface SiteStatisticsDao {

	public Integer getPVCountByWeek(String name, int i);

	public Integer getUVCountByWeek(String name, int i);

}
