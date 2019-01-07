package com.doooly.service.activity.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.impl.XingFuJiaoHangActivityService;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.common.constants.ActivityConstants;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.GenerateKeyUtil;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.activity.ActActivityCodeRecordDao;
import com.doooly.dao.activity.ActActivityRecordDao;
import com.doooly.dao.activity.ActLotteryResultDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.activity.ActActivityCodeRecord;
import com.doooly.entity.activity.ActActivityRecord;
import com.doooly.entity.activity.ActLotteryResult;
import com.doooly.entity.reachad.AdUser;
import com.doooly.service.activity.ActivityServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 活动服务接口实现类
 * @Author: Mr.Wu
 * @Date: 2018/12/30
 */
@Service
public class ActivityServiceImpl implements ActivityServiceI {
    private static Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private static final String PRJECT_ACTIVITY_URL = PropertiesConstants.dooolyBundle.getString("prject.activity.url");

    @Autowired
    private ActActivityRecordDao activityRecordDao;
    @Autowired
    private ActActivityCodeRecordDao activityCodeRecordDao;
    @Autowired
    private ActLotteryResultDao actLotteryResultDao;
    @Autowired
    private AdUserServiceI adUserService;
    @Autowired
    private XingFuJiaoHangActivityService jiaoHangActivityService;

    @Override
    public String generateKey(String activityId, Integer keyNum) {
        MessageDataBean messageDataBean = new MessageDataBean();
        if (activityId != null && !"".equals(activityId)) {
            ActActivityRecord activity = activityRecordDao.queryById(activityId);

            if (activity == null) {
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess("activityId not find activity");
            } else {
                // 生成key
                String[] keys = GenerateKeyUtil.generate(6, keyNum);

                if (keys == null) {
                    messageDataBean.setCode(MessageDataBean.failure_code);
                    messageDataBean.setMess("keys is null");
                } else {
                    ActActivityCodeRecord actCode = new ActActivityCodeRecord();
                    actCode.setActivityId(activity.getId());
                    actCode.setState(0);
                    actCode.setCreateDate(new Date());
                    actCode.setKeys(keys);

                    int row = activityCodeRecordDao.insertBatch(actCode);

                    cacheCode(activity.getId(), keys);
                    if (row > 0) {
                        messageDataBean.setCode(MessageDataBean.success_code);
                        messageDataBean.setMess(MessageDataBean.success_mess);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("row", row);
                        messageDataBean.setData(map);
                    } else {
                        messageDataBean.setCode(MessageDataBean.failure_code);
                        messageDataBean.setMess("Insert the failure");
                    }
                }
            }
        } else {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("activityId is null");
        }

        return messageDataBean.toJsonString();
    }

    /**
     *
     * @param obj
     *  activityId: 活动ID
     *  phone: 手机号码
     *  groupId: 企业ID
     * @return
     */
    @Override
    public String campaignByPhone(JSONObject obj) {
        String groupId = obj.getString("groupId");
        String phone = obj.getString("phone");
        String activityId = obj.getString("activityId");
        ActActivityRecord actActivityRecord = activityRecordDao.queryById(activityId);

        if (actActivityRecord == null) {
            return new MessageDataBean(MessageDataBean.failure_code, "活动未找到").toJSONString();
        }

        if (actActivityRecord.getEndDate().getTime() < new Date().getTime()) {
            return new MessageDataBean(MessageDataBean.failure_code, "活动已结束").toJSONString();
        }

        AdUser adUser = adUserService.getUserByPhone(phone);

        if (adUser == null) {
            // 用户不存在
            MessageDataBean resultMess = jiaoHangActivityService.doBefore(obj);

            if (resultMess == null) {
                return resultMess.toJsonString();
            }

            adUser = adUserService.getUserByPhone(phone);
        } else {
            // 存在时验证手机验证码
            JSONObject verificationReq = new JSONObject();
            verificationReq.put("businessId", WebService.BUSINESSID);
            verificationReq.put("storesId", WebService.STOREID);
            verificationReq.put("verificationCode", obj.getString("verificationCode"));
            verificationReq.put("cardNumber", phone);
            String result = HTTPSClientUtils.sendPost(verificationReq, Constants.MerchantApiConstants.CHECK_VERIFICATION_CODE_URL);

            // 验证码验证失败
            if (JSONObject.parseObject(result).getInteger("code") != 0) {
                logger.warn("交行活动-手机验证码验证失败，paramJsonReq={}, result={}", obj.toJSONString(), result);
                return new MessageDataBean(ActivityConstants.ActivityEnum.ACTIVITY_VERIFICATION_CODE_ERROR).toJsonString();
            }

        }

        ActActivityCodeRecord actCodeRecord = activityCodeRecordDao.findByActivityIdAndUserId(actActivityRecord.getId(), adUser.getId());

        if (actCodeRecord != null) {
            MessageDataBean messageDataBean = new MessageDataBean();
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("您已经领取过了");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("code", actCodeRecord.getActCode());
            messageDataBean.setData(map);
            return messageDataBean.toJsonString();
        }

        if (!groupId.equals(adUser.getGroupNum().toString())) {
            return new MessageDataBean(MessageDataBean.failure_code, "用户与当前企业不对应").toJsonString();
        }
        return getCode(actActivityRecord.getId().toString(), adUser);
    }

    @Override
    public String getWinningRecord(String activityId, Integer rowNum) {
        MessageDataBean messageDataBean = new MessageDataBean();
        List<ActLotteryResult> list = actLotteryResultDao.getListByActivityIdAndLimit(activityId, rowNum);
        if (list != null && list.size() > 0) {
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(MessageDataBean.success_mess);
            messageDataBean.setData((HashMap<String, Object>) new HashMap<String, Object>().put("date", list));
        } else {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 缓存活动生成的keys
     * @param activityId
     * @param keys
     */
    public void cacheCode(Long activityId, String[] keys) {
        JSONObject json = new JSONObject();
        json.put("activityId", activityId);
        List<Map<String, String>> keyList = new ArrayList<>();

        for (String str : keys) {
            Map<String, String> map = new HashMap<>();
            map.put("activityId", activityId.toString());
            map.put("actCode", str);
            keyList.add(map);
        }

        json.put("activityCodeRecords", keyList);
        JSONObject resultJson = HttpClientUtil.httpPost(PRJECT_ACTIVITY_URL + "lottery/batchAddLotteryCode", json);
        logger.info("缓存活动keys：" + resultJson.toJSONString());
    }

    /**
     * 根据活动获得券码
     * @param activityId
     * @param adUser
     * @return
     */
    public String getCode(String activityId, AdUser adUser) {
        MessageDataBean messageDataBean = new MessageDataBean();

        JSONObject json = new JSONObject();
        json.put("activityId", activityId);
        json.put("userId", adUser.getId());
        JSONObject resultJson = HttpClientUtil.httpPost(PRJECT_ACTIVITY_URL + "lottery/getLotteryCodeByUserId", json);

        logger.info("根据活动获得券码：" + resultJson.toJSONString());
        if (MessageDataBean.success_code.equals(resultJson.getString("code"))) {
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess("获取抽奖码成功");
            HashMap<String, Object> map = new HashMap<>();
            map.put("code", resultJson.getString("data"));
            messageDataBean.setData(map);
        } else {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("获取抽奖码失败");
        }
        return messageDataBean.toJsonString();
    }

}
