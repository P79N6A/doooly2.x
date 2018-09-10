package com.doooly.common.constants;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * @Description: App常量
 * @author: qing.zhang
 * @date: 2017-07-24
 */
public class AppConstants {
	private static ResourceBundle appBundle = ResourceBundle.getBundle("prop/app");

	public static final String MASTER_SECRET = appBundle.getString("master_secret");// app秘钥

	public static final String APP_KEY = appBundle.getString("app_key");// appkey

	public static final String TEST_MASTER_SECRET = appBundle.getString("test_master_secret");// app秘钥

	public static final String TEST_APP_KEY = appBundle.getString("test_app_key");// appkey

	public static final Boolean IOS_ENVIRONMENT = appBundle.getString("ios_environment").equals("true");// ios环境区分 true 生产 false 测试

	public static final int IOS_SEND = 1;// ios发送

	public static final int ANDROID_SEND = 2;// ios安卓发送

	/** 手机型号缓存时间类型 */
	public final static TimeUnit TIME_UNIT = TimeUnit.DAYS;

	/** 手机型号缓存时间 */
	public final static long MOBILE_NUM_REDIS_TIME = 30L;

	/** 手机型号redis-key前缀 */
	public final static String TOKEN = "token";
}
