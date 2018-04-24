package com.doooly.common.context;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.smallz.smallz_jedis.JClient;
import org.smallz.smallz_jedis.JPoolConfigWrapper;
import org.springframework.web.context.ContextLoaderListener;

import com.doooly.business.activity.impl.ActivityService;
import com.doooly.common.queue.ArrayBlockQueue;
import com.doooly.common.webservice.WebService;
import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.dto.activity.ActivityOrderRes;
import com.doooly.listener.JedisSubscribeListener;

/**
 * 
 * @author 赵清江
 * @date 2016年12月15日
 * @version 1.0
 */
public class SpringContextLoaderListener extends ContextLoaderListener{

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		WebService.initSSL();
		((new BasicThreadFactory.Builder().namingPattern("activity daemon thread").daemon(true).build()).newThread(new Runnable() {
			
			@Override
			public void run() {
				ArrayBlockQueue<ActivityOrderReq> activityQueue = getActivityQueue();
				ActivityService service = getActivityService();
				ActivityOrderReq req = null;
				while (true) {
					try {
						req = activityQueue.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (req == null) {
						continue;
					}
					ActivityOrderRes res = service.activityOrderService(req);
					System.out.println(res.toJsonString());
				}
			}
		})).start();
		
		((new BasicThreadFactory.Builder().namingPattern("cps daemon thread").daemon(true).build()).newThread(new Runnable() {
			
			@Override
			public void run() {
				JClient.init(getJPoolConfigWrapper());
				JClient.register(getJedisSubscribeListener());
				JClient.subcribe("ORDER_CPS_MSG_CHANNEL");
			}
		})).start();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
		
		super.contextDestroyed(event);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayBlockQueue<ActivityOrderReq> getActivityQueue(){
		return getCurrentWebApplicationContext().getBean(ArrayBlockQueue.class);
	}
	
	private ActivityService getActivityService(){
		return getCurrentWebApplicationContext().getBean(ActivityService.class);
	}
	
	private JPoolConfigWrapper getJPoolConfigWrapper(){
		return getCurrentWebApplicationContext().getBean(JPoolConfigWrapper.class);
	}
	
	private JedisSubscribeListener getJedisSubscribeListener(){
		return getCurrentWebApplicationContext().getBean(JedisSubscribeListener.class);
	}
}
