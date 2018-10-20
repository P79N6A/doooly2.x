package com.doooly.business.freeCoupon.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.impl.AdUserService;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.business.freeCoupon.service.task.GetCouponTask;
import com.doooly.business.freeCoupon.service.thread.impl.MyThreadPoolServiceImpl;
import com.doooly.business.redisUtil.RedisUtilService;
import com.doooly.common.constants.ConstantsV2.ActivityCode;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.dao.reachad.AdAvailablePointsDao;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdCouponDao;
import com.doooly.dao.reachad.AdIntegralAcquireRecordDao;
import com.doooly.dao.reachad.AdIntegralActivityConnDao;
import com.doooly.dao.reachad.AdIntegralActivityDao;
import com.doooly.dao.reachad.AdRegisterRecordDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAvailablePoints;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdIntegralAcquireRecord;
import com.doooly.entity.reachad.AdIntegralActivityConn;
import com.doooly.entity.reachad.AdRegisterRecord;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.doooly.entity.reachad.AdCouponCode.ISRECEIVED_NUMBER;

@Service
@Transactional
public class FreeCouponBusinessService implements FreeCouponBusinessServiceI {

    private static Logger logger = Logger.getLogger(FreeCouponBusinessService.class);
    @Autowired
    private AdCouponActivityConnDao adCouponActivityConnDao;
    @Autowired
    private AdCouponCodeDao adCouponCodeDao;
    @Autowired
    private AdCouponDao adCouponDao;
    @Autowired
    private AdRegisterRecordDao adRegisterRecordDao;
    @Autowired
    private AdUserService adUserService;
    /**
     * redis工具类
     */
    @Autowired
    private RedisUtilService redisUtilService;
    @Autowired
    protected StringRedisTemplate redisTemplate;
    @Autowired
    private AdCouponActivityDao adCouponActivityDao;
    @Autowired
    private AdIntegralAcquireRecordDao adIntegralAcquireRecordDao;
    @Autowired
    private AdIntegralActivityDao adIntegralActivityDao;
    @Autowired
    private AdIntegralActivityConnDao adIntegralActivityConnDao;
    @Autowired
    private AdAvailablePointsDao adAvailablePointsDao;
    @Autowired
    private MyThreadPoolServiceImpl myThreadPoolService;

    // 会员coupon_code，唯一标识，放入缓存；如未领取设置值为4个0（0000），如已领取直接返回缓存值；
    private static String COUPON_CODE_KEY = "coupon_code:%s";
    // 会员coupon_code，唯一标识，缓存值4个0（0000）
    private static String COUPON_CODE_VALUE = "0000";
    ReentrantLock lock = new ReentrantLock();
    private static final Integer DEFAULT_MAX_ACTIVE = 10;

    @Override
    public MessageDataBean receiveCoupon(Integer adId, Integer couponId, Integer activityId, String productSn) {
        MessageDataBean messageDataBean = new MessageDataBean();
        Long startTime = System.currentTimeMillis();
        String businessId = adCouponDao.findBusinessIdByCouponId(couponId);
        logger.info("====查询businessId耗时" + (System.currentTimeMillis()-startTime));
        AdCouponCode adCouponCode = this.sendCoupon(businessId, activityId, adId, couponId);
        logger.info("====发送券码耗时" + (System.currentTimeMillis()-startTime));
        logger.info("==========获取券码：" + adCouponCode.getCode());
        if (adCouponCode.getCode() != null && !"".equals(adCouponCode.getCode())) {
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(MessageDataBean.success_mess);
            // 用户已领取过券码
            if (adCouponCode.getIsReceived() > 0) {
                messageDataBean.setCode(MessageDataBean.already_receive_code);
                messageDataBean.setMess(MessageDataBean.already_receive_mess);
            }
        } else {
            if (adCouponCode.getCode() == null) {
                messageDataBean.setCode(MessageDataBean.coupon_stock_zero_code);
                messageDataBean.setMess(MessageDataBean.coupon_stock_zero_msg);
            } else {
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess(MessageDataBean.failure_mess);
            }
        }
        return messageDataBean;
    }

    @Override
    public MessageDataBean forWuGangCouponSend(Integer adId, Integer couponId, Integer activityId, String productSn) {
        MessageDataBean messageDataBean = new MessageDataBean();
        List<AdCouponActivityConn> activityConnByActivityId = adCouponActivityConnDao
                .getActivityConnByActivityId(activityId + "");
        for (AdCouponActivityConn adCouponActivityConn : activityConnByActivityId) {
            Integer counpon = adCouponActivityConn.getCoupon().getId().intValue();
            String businessId = adCouponDao.findBusinessIdByCouponId(counpon);
            AdCouponCode adCouponCode = this.sendCoupon(businessId, activityId, adId, counpon);
            logger.info("==========获取券码：" + adCouponCode.getCode());
            if (adCouponCode.getCode() != null && !"".equals(adCouponCode.getCode())) {
                messageDataBean.setCode(MessageDataBean.success_code);
                messageDataBean.setMess(MessageDataBean.success_mess);
                // 用户已领取过券码
                if (adCouponCode.getIsReceived() > 0) {
                    messageDataBean.setCode(MessageDataBean.already_receive_code);
                    messageDataBean.setMess(MessageDataBean.already_receive_mess);
                }
            } else {
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess(MessageDataBean.failure_mess);
            }
        }
        return messageDataBean;
    }

    // 发放卡券调用方法
    public AdCouponCode sendCoupon(String businessId, Integer activityId, Integer userId, Integer couponId) {
        // 是否已发放券码
        AdCouponCode adCouponCode = new AdCouponCode();
        // 方法进入时间
        Long startTime = System.currentTimeMillis();
        logger.info("====sendCoupon参数信息-activityId:" + activityId + ",couponId:" + couponId + ",userId:" + userId
                + ",方法进入时间：" + startTime);
        try {
            if (activityId > 0 && userId > 0 && couponId > 0) {
                adCouponCode.setUserId(Long.valueOf(userId));
                adCouponCode.setActivityId(Long.valueOf(activityId));
                adCouponCode.setCoupon(Long.valueOf(couponId));

                AdCouponActivity activity = adCouponActivityDao.getActivityById(activityId);
                Long activityTime = System.currentTimeMillis();
                logger.info("====查询活动耗时" + (activityTime-startTime));
                //查询已领取集合
                List<AdCouponCode> recCodeList = adCouponCodeDao.checkIfSendCodeNoPhone(adCouponCode);
                Long queryListTime = System.currentTimeMillis();
                logger.info("====查询领取卡券耗时" + (queryListTime-activityTime));
                if (CollectionUtils.isNotEmpty(recCodeList)) {
                    if (recCodeList.size() >= activity.getCouponCount()) {
                        //领取数量超过活动限制
                        adCouponCode.setIsReceived(ISRECEIVED_NUMBER);
                        adCouponCode.setCode(recCodeList.get(0).getCode());
                        logger.info("====用户已领取过券码超过活动限制");
                        return adCouponCode;
                    }
                    // 用户是否已领取券码
                    for (AdCouponCode couponCode : recCodeList) {
                        if (couponCode.getCoupon().equals(Long.valueOf(couponId))) {
                            //已经领取
                            adCouponCode.setIsReceived(ISRECEIVED_NUMBER);
                            adCouponCode.setCode(recCodeList.get(0).getCode());
                            logger.info("====用户已领取过券码");
                            return adCouponCode;
                        }
                    }
                    logger.info("====判读是否领取卡券耗时" + (System.currentTimeMillis()-queryListTime));
                }
                // ======redis检测用户是否已领取券码,如已操作中断操作并返回信息-start=====
                if (!redisTemplate.opsForValue().setIfAbsent(
                        String.format(COUPON_CODE_KEY, activityId + ":" + couponId + ":" + userId),
                        COUPON_CODE_VALUE)) {
                    // 已领取
                    adCouponCode.setIsReceived(ISRECEIVED_NUMBER);
                    adCouponCode.setCode(COUPON_CODE_VALUE);
                    logger.info("====redis用户已领取过券码-赋默认值COUPON_CODE_VALUE:" + COUPON_CODE_VALUE);
                    return adCouponCode;
                }
                //采用异步线程处理 发券操作
                //创建一个有返回值的任务
                JSONObject req = new JSONObject();
                req.put("businessId", businessId);
                req.put("userId", userId);
                req.put("couponId", couponId);
                req.put("activityId", activityId);
                GetCouponTask getCouponTask = new GetCouponTask(req, adCouponCode, redisUtilService, adCouponActivityConnDao, redisTemplate, adCouponCodeDao);
                Future submit = myThreadPoolService.submitTask(getCouponTask);
                logger.info("====另起线程发券耗时" + (System.currentTimeMillis()-queryListTime));
                return (AdCouponCode) submit.get();
            }
        } catch (Exception e) {
            redisTemplate.delete(String.format(COUPON_CODE_KEY, activityId + ":" + couponId + ":" + userId));
            logger.error("==========sendCoupon系统错误===============",e);
        }

        return adCouponCode;
    }

	/*
	 * // 发放卡券调用方法 public int sendCoupon(Integer userId, String productSn) {
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("userId", userId); map.put("productSN", productSn);
	 * map.put("couponCodeId", 0); // 发放卡券存储过程
	 * adCouponCodeDao.excuteSendGiftCouponProc(map); int couponCodeId =
	 * Integer.valueOf(map.get("couponCodeId").toString()); return couponCodeId;
	 * 
	 * // redis中获取code AdCouponCode adCouponCode =
	 * redisUtilService.getOneCodeFromRedis(businessId, couponId, activityId);
	 */

    @Override
    public HashMap<String, Object> findAvailableCoupons(String userId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            // 获取有效的卡券活动关联
            List<AdCouponActivityConn> actConnList = adCouponActivityConnDao.findAvailableCoupons(userId);
            if (actConnList != null && !actConnList.isEmpty()) {
                map.put("actConnList", actConnList);
            }
        } catch (Exception e) {
            logger.error("获取有效的卡券活动关联异常！！！");
        }
        return map;
    }

    @Override
    public HashMap<String, Object> saveRegisterRecord(JSONObject obj) {
        logger.info("保存报名记录jsonStr:" + obj.toJSONString());
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            // 保存报名记录
            AdRegisterRecord adRegisterRecord = JSONObject.toJavaObject(obj, AdRegisterRecord.class);
            Integer result = adRegisterRecordDao.insert(adRegisterRecord);
            map.put("saveResult", result);
            // 获取该卡券活动关联报名总数
            Integer totalRegisterNum = adRegisterRecordDao.getTotalRegisterNum(adRegisterRecord);
            map.put("totalRegisterNum", totalRegisterNum);
            logger.info(String.format("保存报名记录result:%s ||获取该卡券活动关联报名总数totalRegisterNum:%s", result, totalRegisterNum));
        } catch (Exception e) {
            logger.error("保存报名记录  and 获取报名人数 异常！！！");
        }
        return map;
    }

    @Override
    public HashMap<String, Object> getCouponDetailsByConnId(String actConnId, String userId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            AdCouponActivityConn activityConn = adCouponActivityConnDao.getCouponDetailsByConnId(actConnId, userId);
            map.put("activityConn", activityConn);
        } catch (Exception e) {
            logger.error("根据卡券活动关联id查询卡券详情!!!");
        }
        return map;
    }

    @Override
    public List<String> sendCoupons(String businessId, Integer activityId, List<String> userIds, Integer couponId) {
        List<String> returnUserList = new ArrayList<String>();
        // 方法进入时间
        Long startTime = System.currentTimeMillis();
        logger.info("====sendCoupon参数信息-activityId:" + activityId + ",couponId:" + couponId + ",userIds:" + userIds
                + ",方法进入时间：" + startTime);
        try {
            if (activityId > 0 && userIds.size() > 0 && couponId > 0) {
                List<HashMap<String, Object>> userCodeList = new ArrayList<HashMap<String, Object>>();
                // 用户是否已领取券码
                List<Long> hadCodeUsers = adCouponCodeDao.checkIsHadCode(activityId.toString(), couponId.longValue(),
                        userIds);
                for (Long userId : hadCodeUsers) {
                    if (userIds.contains(userId.toString())) {
                        userIds.remove(userId.toString());
                    }
                }
                // ==============发放券码-start==============
                if (userIds.size() > 0) {
                    List<String> codeList = redisUtilService
                            .PopDataFromRedis(businessId + "+" + couponId + "+" + activityId, userIds.size());
                    logger.info("====codeList:" + codeList + ",codeList == null:" + (codeList == null));
                    // 验证库存是否足够
                    if (codeList.size() == 0) {
                        return returnUserList;
                    }
                    for (int i = 0; i < codeList.size(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("code", codeList.get(i));
                        map.put("userId", Long.valueOf(userIds.get(i)));
                        map.put("activityId", activityId);
                        userCodeList.add(map);
                    }
                    // 同步数据库code
                    int count = adCouponCodeDao.batchUpdateCouponCode(userCodeList, businessId);
                    if (count > 0) {
                        // 修改关联表中库存,直接领取时点击之后库存减一
                        adCouponActivityConnDao.BatchreduceRemindQuantity(couponId, activityId, userCodeList.size());
                        // 发券成功后将券的信息放入redis队列-->微信端发送领券成功通知
                        for (int i = 0; i < codeList.size(); i++) {
                            JSONObject data = new JSONObject();
                            data.put("userId", userIds.get(i));
                            data.put("activityId", activityId);
                            data.put("couponId", couponId);
                            redisTemplate.convertAndSend("COUPON_CHANNEL", data.toString());
                            logger.info("====activityId:" + activityId + ",couponId:" + couponId + ",userId:"
                                    + userIds.get(i) + ",成功发券执行耗时：" + (System.currentTimeMillis() - startTime));
                            returnUserList.add(userIds.get(i));
                        }
                        String mobils = "";
                        List<String> telephones = adUserService.getIds(returnUserList);
                        for (String telephone : telephones) {
                            mobils = mobils + "," + telephone;
                        }
                        mobils = mobils.substring(1);
                        logger.info("=========短信推送手机号码：" + mobils + "==============");
                        JSONObject reqJSON = new JSONObject();
                        // 手机号，多个以英文逗号分隔
                        // 切换开关 true：使用阿里大鱼平台 false:嘉怡短信平台
                        AdUser user = new AdUser();
                        // 严选活动ID =SMS_25595369
                        JSONObject paramSMSJSON = new JSONObject();
                        reqJSON.put("smsContent", null);
                        adUserService.batchSendSms(user, paramSMSJSON, mobils, "SMS_122140024", null, true);
                        logger.info("========网易严选活动符合发送短信条件========");
                    } else {
                        logger.info("========发券失败=======");
                        return returnUserList;
                    }
                }
            } else {
                return returnUserList;
            }
        } catch (Exception e) {
            // redisTemplate.delete(String.format(COUPON_CODE_KEY, activityId +
            // ":" + couponId + ":" + userId));
            logger.info("==========sendCoupons系统错误===============");
            e.printStackTrace();
            return null;
        }
        return returnUserList;
    }

    /**
     * 回收卡券
     */
    @Override
    public void recyclingCoupon() {
        // 查询活动卡券的结束时间小于当天的20条数据进行回收卡券
        try {
            // List<AdCouponActivityConn> adCouponActivityConnList =
            // adCouponActivityConnDao.getNowActivityCoupon();
            List<Map<String, Object>> adCouponActivityList = adCouponActivityDao.getNowActivityCoupon();
            for (Map<String, Object> adCouponInfo : adCouponActivityList) {
                String activityId = adCouponInfo.get("activityId") + "";
                String couponId = adCouponInfo.get("couponId") + "";
                String businessId = adCouponInfo.get("businessId") + "";
                logger.info("businessId:" + businessId + "couponId:" + couponId + "activityId:" + activityId);
                this.recoveryRedisRemindCode(businessId, couponId, activityId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> recoveryRedisRemindCode(String businessId, String couponId, String activityId) {
        List<String> popCodeList = new ArrayList<String>();
        try {
            // 取出code
            popCodeList = redisUtilService.PopDataFromRedis(businessId + "+" + couponId + "+" + activityId, 0);
            if (popCodeList != null && popCodeList.size() > 0) {
                logger.info("===============券码回收recoveryRedisRemindCode取出数量popCodeList:" + popCodeList.size());
                // 重新放入
                if (redisUtilService.PushDataToRedis(businessId + "+" + couponId, popCodeList)) {
                    // 回滚卡券库存
                    HashMap<String, Object> param = new HashMap<String, Object>();
                    param.put("couponId", couponId);
                    param.put("count", popCodeList.size());
                    // 回滚卡券库存
                    adCouponDao.updateCouponCount(param);
                    // 冲减卡券活动关联库存
                    // AdCouponActivityConn adCouponActivityConn = new
                    // AdCouponActivityConn();
                    // adCouponActivityConn.setBusinessId(businessId);
                    // adCouponActivityConn.setCouponId(Integer.valueOf(couponId));
                    // adCouponActivityConn.setActivityId(Integer.valueOf(activityId));
                    // adCouponActivityConn.setCount(popCodeList.size());
                    adCouponActivityConnDao.updateCouponActivityConnCount(businessId, Integer.valueOf(activityId),
                            Integer.valueOf(couponId), popCodeList.size());
                }
                logger.info("===============券码回收recoveryRedisRemindCode取出数据popCodeList:" + popCodeList);
            } else {
                logger.info("===============券码回收recoveryRedisRemindCode取出数据popCodeList:" + popCodeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return popCodeList;
    }

    @Override
    public MessageDataBean getIntegralActivityData(Long userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> data = new HashMap<String, Object>();
        try {
            // adIntegralAcquireRecordDao;adIntegralActivityDao;adIntegralActivityConnDao;
            // 判断是否领取过
            AdIntegralAcquireRecord record = adIntegralAcquireRecordDao.checkIsHadProvided(userId);
            if (record != null) {
                // 已领取
                messageDataBean.setCode(ActivityCode.HAD_ALREADY.getCode() + "");
                messageDataBean.setMess(ActivityCode.HAD_ALREADY.getMsg());
                logger.info("====用户已发放过积分=====积分活动id为" + record.getIntegralActivityId() + "===获取的积分数量为==:"
                        + record.getIntegral());
                // return messageDataBean;
            } else {
                // 判断是否有积分活动
                AdIntegralActivityConn activityConn = adIntegralActivityConnDao.checkIsHadActivity(userId);
                if (activityConn == null) {
                    messageDataBean.setCode(ActivityCode.NOT_STARTED.getCode() + "");
                    messageDataBean.setMess(ActivityCode.NOT_STARTED.getMsg());
                    logger.info("====用户所属企业未有正在进行的积分活动====用户id为:==" + userId);
                    // return messageDataBean;
                } else {
                    // 判断是否可以发放积分(积分是否充足)(update ad_integral_activity set
                    // available_integral - 10 where id =id and
                    // available_integral>=10 )
                    if (activityConn.getAvailableIntegral().compareTo(activityConn.getIntegralForEach()) == -1) {
                        messageDataBean.setCode(ActivityCode.HAD_NONE.getCode() + "");
                        messageDataBean.setMess(ActivityCode.HAD_NONE.getMsg());
                        logger.info("====用户所属企业积分活动余额不足====用户id为:==" + userId);
                        // return messageDataBean;
                    } else {
                        messageDataBean.setCode(SystemCode.SUCCESS.getCode() + "");
                        messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
                        DecimalFormat df1 = new DecimalFormat("0.00");
                        data.put("integral", df1.format(activityConn.getIntegralForEach()));
                        messageDataBean.setData(data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode() + "");
            messageDataBean.setMess(SystemCode.SYSTEM_ERROR.getMsg());
        }
        return messageDataBean;
    }

    @Override
    @Transactional
    public MessageDataBean sendIntegralActivity(Long userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> data = new HashMap<String, Object>();
        boolean isLocked = false;
        try {
            if (redisTemplate.opsForValue().setIfAbsent("integral_activity_" + userId,
                    String.valueOf(System.currentTimeMillis() + 5 * 60 * 1000))) {
                logger.info("integral_activity_" + userId + " - tryLock success.");
                // 设置锁的有效期，防止因异常情况无法释放锁而造成死锁情况的发生
                redisTemplate.expire("integral_activity_" + userId, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
                isLocked = true;
                // adIntegralAcquireRecordDao;adIntegralActivityDao;adIntegralActivityConnDao;
                // 判断是否领取过
                AdIntegralAcquireRecord record = adIntegralAcquireRecordDao.checkIsHadProvided(userId);
                if (record != null) {
                    // 已领取
                    messageDataBean.setCode(ActivityCode.HAD_ALREADY.getCode() + "");
                    messageDataBean.setMess(ActivityCode.HAD_ALREADY.getMsg());
                    logger.info("====用户已发放过积分=====积分活动id为" + record.getIntegralActivityId() + "===获取的积分数量为==:"
                            + record.getIntegral());
                    // return messageDataBean;
                } else {
                    // 判断是否有积分活动
                    AdIntegralActivityConn activityConn = adIntegralActivityConnDao.checkIsHadActivity(userId);
                    // 判断是否可以发放积分(积分是否充足)(update ad_integral_activity set
                    // available_integral - 10 where id =id and
                    // available_integral>=10 )
                    int update = adIntegralActivityDao.updateIntegralGiveOut(activityConn.getIntegralId(),
                            activityConn.getIntegralForEach());
                    if (update == 0) {
                        messageDataBean.setCode(ActivityCode.HAD_NONE.getCode() + "");
                        messageDataBean.setMess(ActivityCode.HAD_NONE.getMsg());
                        logger.info("====用户所属企业积分活动余额不足====用户id为:==" + userId);
                        // return messageDataBean;
                    } else {
                        // 更新available和user以及插入ad_integral_acquire_record表
                        AdAvailablePoints adAvailablePoints = new AdAvailablePoints();
                        adAvailablePoints.setUserId(userId + "");
                        adAvailablePoints.setBusinessRebateAmount(activityConn.getIntegralForEach());
                        adAvailablePoints.setType(AdAvailablePoints.TYPE_INTEGRAL_ACTIVITY);
                        adAvailablePoints.setStatus(AdAvailablePoints.STATUS_OBTAINED);
                        adAvailablePointsDao.insert(adAvailablePoints);
                        adUserService.addIntegral(userId, activityConn.getIntegralForEach());
                        AdIntegralAcquireRecord adIntegralAcquireRecord = new AdIntegralAcquireRecord();
                        adIntegralAcquireRecord.setUserId(userId);
                        adIntegralAcquireRecord.setIntegral(activityConn.getIntegralForEach());
                        adIntegralAcquireRecord.setIntegralActivityId(activityConn.getIntegralId());
                        adIntegralAcquireRecord.setCreateDate(new Date());
                        adIntegralAcquireRecordDao.insert(adIntegralAcquireRecord);
                        messageDataBean.setCode(SystemCode.SUCCESS.getCode() + "");
                        messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
                        DecimalFormat df1 = new DecimalFormat("0.00");
                        data.put("integral", df1.format(activityConn.getIntegralForEach()));
                        messageDataBean.setData(data);
                    }
                }
            } else {
                messageDataBean.setCode(ActivityCode.HAD_ALREADY.getCode() + "");
                messageDataBean.setMess(ActivityCode.HAD_ALREADY.getMsg());
                logger.info("====当前用户二次请求,userId为===" + userId);
            }
            // lock.lock();
        } catch (Exception e) {
            e.printStackTrace();
            messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode() + "");
            messageDataBean.setMess(SystemCode.SYSTEM_ERROR.getMsg());
            redisTemplate.delete("integral_activity_" + userId);
        }
        // finally {
        // if(isLocked) {
        // isLocked = false;
        // redisTemplate.delete("integral_activity_"+userId);
        // }
        // }
        return messageDataBean;
    }

}
