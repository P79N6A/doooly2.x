package com.doooly.business.myaccount.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.AliasDeviceListResult;
import cn.jpush.api.device.DeviceClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.doooly.common.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description: app消息中心service
 * @author: qing.zhang
 * @date: 2017-07-18
 */
@Service
public class AppMessageService {


    private static final Logger LOG = LoggerFactory.getLogger(AppMessageService.class);
    private static final String ALERT = "你在xxx店消费xx元,一共获得xxx积分";
    private static final String TITLE = "推送App消息";
    private static final String MSG_CONTENT = "你在xxx店消费xx元,一共获得xxx积分";

    /**
     * 推送消息
     *
     * @param content 发送内容
     * @param alias   发送别名 app预先配置别名为登录的userId方便开发
     * @return 0 失败 ，1成功
     */
    public int sendMessage(String content, String alias) {
        JPushClient jpushClient = new JPushClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());

        // 通过别名推送
        PushPayload payload = buildPushObject_all_alias_alert(alias, content);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            if (result.getResponseCode() == 200) {
                return 1;
            } else {
                return 0;
            }
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);
            return 0;

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            return 0;
        }
    }

    /**
     * 推送消息
     *
     * @param content 发送内容
     * @param alias   发送别名 app预先配置别名为登录的userId方便开发
     * @param type    发送平台
     * @param flag    发送环境
     * @return 0 失败 ，1成功
     */
    public int sendMessage(String content, String alias, Integer type, Boolean flag) {
        LOG.info("send==start==发送内容{},别名{},类型{},环境{}", content, alias, type, flag);
        JPushClient jpushClient;
        DeviceClient deviceClient;
        PushPayload payload;
        if (AppConstants.IOS_SEND == type) {
            //iso 设置环境
            deviceClient = new DeviceClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());
            if (!ifBindAlias(deviceClient, alias,"ios")) {
                //该别名没有绑定过 不推送
                return 0;
            }
            jpushClient = new JPushClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());
            payload = buildPushObject_ios_alias_alert(alias, content, flag);
        } else {
            //安卓全平台
            if (flag) {
                //正式环境
                deviceClient = new DeviceClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());
                if (!ifBindAlias(deviceClient, alias,"android")) {
                    //该别名没有绑定过 不推送
                    return 0;
                }
                jpushClient = new JPushClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());
            } else {
                //测试环境
                deviceClient = new DeviceClient(AppConstants.TEST_MASTER_SECRET, AppConstants.TEST_APP_KEY, null, ClientConfig.getInstance());
                if (!ifBindAlias(deviceClient, alias,"android")) {
                    //该别名没有绑定过 不推送
                    return 0;
                }
                jpushClient = new JPushClient(AppConstants.TEST_MASTER_SECRET, AppConstants.TEST_APP_KEY, null, ClientConfig.getInstance());
            }
            payload = buildPushObject_android_alias_alert(alias, content);
        }

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            if (result.getResponseCode() == 200) {
                return 1;
            } else {
                return 0;
            }
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);
            return 0;

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            return 0;
        }
    }

    /**
     * 推送消息 全平台
     *
     * @param content 发送内容
     * @return 0 失败 ，1成功
     */
    public int sendMessage(String content) {
        JPushClient jpushClient = new JPushClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());

        // 通过别名推送
        PushPayload payload = buildPushObject_all_all_alert(content);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            if (result.getResponseCode() == 200) {
                return 1;
            } else {
                return 0;
            }
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);
            return 0;

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            return 0;
        }
    }

    /**
     * 推送消息 全平台
     *
     * @param content 发送内容
     * @param type    发送平台
     * @param flag    发送环境
     * @return 0 失败 ，1成功
     */
    public int sendMessage(String content, Integer type, Boolean flag) {
        LOG.info("send==start==发送内容{},类型{},环境{}", content, type, flag);
        JPushClient jpushClient;
        PushPayload payload;
        if (AppConstants.IOS_SEND == type) {
            //iso 设置环境
            jpushClient = new JPushClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());
            payload = buildPushObject_ios_all_alert(content, flag);
        } else {
            //安卓全平台
            if (flag) {
                jpushClient = new JPushClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());
            } else {
                jpushClient = new JPushClient(AppConstants.TEST_MASTER_SECRET, AppConstants.TEST_APP_KEY, null, ClientConfig.getInstance());
            }
            payload = buildPushObject_android_all_alert(content);
        }

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            if (result.getResponseCode() == 200) {
                return 1;
            } else {
                return 0;
            }
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);
            return 0;

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            return 0;
        }
    }

    /**
     * 快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
     *
     * @return
     */
    public static PushPayload buildPushObject_all_all_alert(String content) {
        return new PushPayload.Builder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.alert(content)).build();
    }

    /**
     * 快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
     *
     * @return
     */
    public static PushPayload buildPushObject_ios_all_alert(String content, Boolean flag) {
        return new PushPayload.Builder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setNotification(Notification.alert(content))
                .setOptions(Options.newBuilder().setApnsProduction(flag).build())
                .build();
    }

    /**
     * 快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
     *
     * @return
     */
    public static PushPayload buildPushObject_android_all_alert(String content) {
        return new PushPayload.Builder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.alert(content))
                .build();
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为 "alias"，通知内容为 content。
     *
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String alias, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content))
                .build();
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为 "alias"，通知内容为 content。
     *
     * @return
     */
    public static PushPayload buildPushObject_ios_alias_alert(String alias, String content, Boolean flag) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content))
                .setOptions(Options.newBuilder().setApnsProduction(flag).build())
                .build();
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为 "alias"，通知内容为 content。
     *
     * @return
     */
    public static PushPayload buildPushObject_android_alias_alert(String alias, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content))
                .build();
    }

    /**
     * 构建推送对象：平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
     *
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(ALERT, TITLE, null))
                .build();
    }

    /**
     * 构建推送对象：平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 -
     * 通知信息是 ALERT，角标数字为 5，通知声音为 "happy"，并且附加字段 from = "JPush"；消息内容是 MSG_CONTENT。
     * 通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
     *
     * @return
     */
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(MSG_CONTENT))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    /**
     * 构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）交（"alias1" 与 "alias2" 的并集），
     * 推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush。
     *
     * @return
     */
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(MSG_CONTENT)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }

    /**
     * 判断别名是否绑定过
     * deviceClient 查询别名客户端
     * alias 别名
     * platform 平台 默认所有
     */
    public static Boolean ifBindAlias(DeviceClient deviceClient, String alias,String platform) {
        try {
            AliasDeviceListResult result = deviceClient.getAliasDeviceList(alias, platform);
            LOG.info("Got result - " + result);
            if (result.getResponseCode() == 200) {
                List<String> registration_ids = result.registration_ids;
                if (registration_ids.size() > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            LOG.error("判断别名是否绑定过出错", e);
            return false;
        }
    }

}
