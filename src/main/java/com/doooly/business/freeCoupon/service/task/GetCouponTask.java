package com.doooly.business.freeCoupon.service.task;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.redisUtil.RedisUtilService;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.entity.reachad.AdCouponCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Description: redis 获取卡券任务
 * @author: qing.zhang
 * @date: 2018-10-18
 */
public class GetCouponTask implements Callable<AdCouponCode> {

    private static Logger logger = LoggerFactory.getLogger(GetCouponTask.class);
    private static String COUPON_CODE_KEY = "coupon_code:%s";

    private RedisUtilService redisUtilService;
    private AdCouponActivityConnDao adCouponActivityConnDao;
    protected StringRedisTemplate redisTemplate;
    private AdCouponCodeDao adCouponCodeDao;
    private AdCouponCode adCouponCode;
    private JSONObject req;
    private List<String> codeList;

    public GetCouponTask() {
    }

    public GetCouponTask(JSONObject req, AdCouponCode adCouponCode, RedisUtilService redisUtilService,
                         AdCouponActivityConnDao adCouponActivityConnDao, StringRedisTemplate redisTemplate,
                         AdCouponCodeDao adCouponCodeDao, List<String> codeList) {
        this.req = req;
        this.adCouponCode = adCouponCode;
        this.redisUtilService = redisUtilService;
        this.adCouponActivityConnDao = adCouponActivityConnDao;
        this.redisTemplate = redisTemplate;
        this.adCouponCodeDao = adCouponCodeDao;
        this.codeList = codeList;
    }

    @Override
    public AdCouponCode call() throws Exception {
        // 方法进入时间
        Long startTime = System.currentTimeMillis();
        String businessId = req.getString("businessId");
        String userId = req.getString("userId");
        Integer couponId = req.getInteger("couponId");
        Integer activityId = req.getInteger("activityId");
        int count = 0;
        try {
            // 同步数据库code
            count = adCouponCodeDao.updateCouponCode(adCouponCode, businessId);
        } catch (Exception e) {
            //将取出的券码放回redis
            // 重新放入
            redisUtilService.PushDataToRedis(businessId + "+" + couponId + "+" + activityId, codeList);
            adCouponCode.setCode(null);
            logger.info("========获取缓存卡券异常=======", e);
        } finally {
            redisTemplate.delete(String.format(COUPON_CODE_KEY, activityId + ":" + couponId + ":" + userId));
        }
        if (count > 0) {
            // 修改关联表中库存,直接领取时点击之后库存减一
            adCouponActivityConnDao.reduceRemindQuantity(couponId, activityId);
            // 发券成功后将券的信息放入redis队列-->微信端发送领券成功通知
            JSONObject data = new JSONObject();
            data.put("userId", adCouponCode.getUserId());
            data.put("activityId", adCouponCode.getActivityId());
            data.put("couponId", adCouponCode.getCoupon());
            redisTemplate.convertAndSend("COUPON_CHANNEL", data.toString());

            logger.info("====activityId:" + activityId + ",couponId:" + couponId + ",userId:" + userId
                    + ",成功发券执行耗时：" + (System.currentTimeMillis() - startTime));
        }

        return adCouponCode;
    }
}
