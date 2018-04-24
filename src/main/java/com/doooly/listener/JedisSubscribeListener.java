package com.doooly.listener;

import org.smallz.smallz_jedis.JPubSubListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.order.service.CpsSummaryServiceI;

/**
 * redis消息队列 订阅监听器
 * 
 * @author 赵清江
 * @date 2016年12月15日
 * @version 1.0
 */
@Service
public class JedisSubscribeListener implements JPubSubListener {

	// 订单Cps通道
	private static final String ORDER_CPS_MSG_CHANNEL = "ORDER_CPS_MSG_CHANNEL";

	@Autowired
	private CpsSummaryServiceI cpsSummaryService;

	@Override
	public void onConsume(String channel, String message) {
		System.err.println("channel: " + channel + "\nmessage: " + message);

		if (channel.equals(ORDER_CPS_MSG_CHANNEL)) {
			cpsSummaryService.updateCpsFee(message);
		}
	}

	@Override
	public void onPConsume(String pattern, String channel, String message) {
		System.err.println("pattern: " + pattern + "\nchannel: " + channel + "\nmessage: " + message);
	}

	@Override
	public void onPSubDestroy(String pattern, int subChannel) {

	}

	@Override
	public void onPSubStart(String pattern, int subChannel) {

	}

	@Override
	public void onSubDestroy(String channel, int subChannel) {

	}

	@Override
	public void onSubStart(String channel, int subChannel) {

	}

}
