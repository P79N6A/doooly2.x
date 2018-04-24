package com.doooly.business.activity.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.EnrollService;
import com.doooly.business.utils.DateUtils;
import com.doooly.dao.reachad.AdVoteRecordDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdVoteRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * 我要听相声活动<br>
 * 活动仅限2017年-2018年在兜礼有消费的会员.
 */
@Service
public class EnrollServiceImpl implements EnrollService {

    private static Logger logger = LoggerFactory.getLogger(EnrollServiceImpl.class);

    private static Date END_DATE  = DateUtils.parseDate("2018-01-10");

    @Autowired
    private AdVoteRecordDao adVoteRecordDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    @Transactional
    public MessageDataBean signUp(JSONObject json) {
        try {
            String userId = json.getString("userId");
            logger.info("userId = {},end_date = {}",userId,END_DATE);
            //活动是否过期
            if(END_DATE.before(new Date())){
                return new MessageDataBean("10001","活动已结束");
            }

            //是否重复报名
            AdVoteRecord record = adVoteRecordDao.findByOptionIdAndOpenId(OPTION_ID,userId);
            if(record != null){
                return new MessageDataBean("10002","已经收到您的报名啦,无需重复点击");
            }

            //是否有具有抽奖条件(活动仅限2017年-2018年在兜礼有消费的会员.)
            if (orderDao.getFinishedOrderCnt(userId) == 0) {
                return new MessageDataBean("10003","对不起,您不符合抽奖条件,建议您消费后再尝试");
            }

            //记录报名用户数据
            AdVoteRecord adVoteRecord = new AdVoteRecord();
            adVoteRecord.setActivityId(1);
            adVoteRecord.setOptionId(1);
            adVoteRecord.setUserWechatOpenId(userId);
            adVoteRecord.setCreateDate(new Date());
            int rows = adVoteRecordDao.insert(adVoteRecord);
            if(rows > 0){
                logger.info("userId = {}, success.",userId);
                return new MessageDataBean("10000","您成功报名,我们将于下周公布中奖名单");
            }
        } catch (Exception e) {
            logger.error("EnrollServiceImpl e = {}",e.getMessage());
        }
        return new MessageDataBean("10099","报名失败");

    }
}
