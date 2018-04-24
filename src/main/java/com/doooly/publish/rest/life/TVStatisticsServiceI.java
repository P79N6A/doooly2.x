package com.doooly.publish.rest.life;
/**
 * TV展示数据提供接口
 * @author 杨汶蔚
 * @date 2016年9月18日
 * @version 1.0
 */
public interface TVStatisticsServiceI {
	/**获取会员近两周和近六个月数据*/
	public String memberData();
	
	
	/**获取会员当天数据*/
	public String memberTodayData();
	
	
	/**获取订单近两周(包括今天)和近六个月数据*/
	public String orderData();
	
	/**获取站点统计近两周(包括今天)和近六个月数据*/
	public String SiteStatisticsData();
}
