package com.doooly.business.home.v2.servcie;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IndexServiceI {

	/**
	 * 兜礼权益接口查询楼层v2.2
	 * 
	* @author  hutao 
	* @date 创建时间：2018年11月1日 下午3:35:48 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	String selectFloorsByV2_2(Map<String, String> map);

	/**
	 * 根据接口版本查询首页楼层信息
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月23日 下午3:36:00
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	String selectFloorsByVersion(JSONObject params, HttpServletRequest request, String version);

	/**
	 * 获得花积分楼层信息
	 *
	 * @author 吴章义
	 * @date 创建时间：2018-11-11
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	String listSpendIntegralFloors(Map<String, String> map);


	/**
	 * 获取id
	 * @param userId
	 * @return
	 */
	String getUserGroupInfo(String userId);

	/**
	 * 兜礼权益接口查询楼层v.3_3
	 * 内部价增加兜里价格字段、是否是品牌馆
	 * add paul 2019/2/19
	 * @param map
	 * @return
	 */
	String selectFloorsByV3_3(Map<String, String> map);
}
