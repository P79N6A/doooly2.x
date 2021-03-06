package com.doooly.publish.rest.reachad;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: DailyActivityServiceI
 * @Description: 日常运营活动接口
 * @author hutao
 * @date 2018年10月9日
 *
 */
public interface DailyActivityServiceI {

	/**
	 * 标准（普通）日常运营活动发放礼品券
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午10:49:51
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
    String commonActivitySendCopuon(JSONObject jsonReq);
	/**
	 * 标准（普通）日常运营活动查询可领取优惠券
	 *
	 * @author zhangq
	 * @date 创建时间：2018年10月15日 14:49:51
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
    String commonActivityQueryCopuon(JSONObject jsonReq);

    /**
     * 查看是否领取过
     * @param jsonReq
     * @return
     */
    String checkIfSendCode(JSONObject jsonReq);

	/**
	 * 交行活动（定制）发放礼品券
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午10:50:24
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
    String jiaoHangActivitySendCopuon(JSONObject jsonReq);

	/**
	 * 交行活动（定制）抽奖活动
	 * @param jsonReq
	 * @return
	 */
	String jiaoHanLotteryActivitySendCopuon(JSONObject jsonReq);
}
