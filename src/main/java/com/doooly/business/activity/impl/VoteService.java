package com.doooly.business.activity.impl;

import com.doooly.business.activity.VoteServiceI;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdVoteOptionDao;
import com.doooly.dao.reachad.AdVoteRecordDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdVoteOption;
import com.doooly.entity.reachad.AdVoteRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 活动报名
 * @author: qing.zhang
 * @date: 2017-05-31
 */
@Service
public class VoteService implements VoteServiceI{

    @Autowired
    private AdCouponActivityDao adCouponActivityDao;
    @Autowired
    private AdVoteOptionDao adVoteOptionDao;
    @Autowired
    private AdVoteRecordDao adVoteRecordDao;

    private static final int MAX_VOTE_COUNT = 10;//最大投票数


    /**
     * 活动详情
     *
     * @param activityId
     * @return
     */
    public MessageDataBean getActivityDetail(Integer activityId, String userWechatOpenId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        // 活动详情
        AdCouponActivity adCouponActivity = adCouponActivityDao.getActivityDetail(activityId);
        if (adCouponActivity != null) {
            adCouponActivity.setEndDateStr(adCouponActivity.getEndDate().before(new Date())?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(adCouponActivity.getEndDate()));
            map.put("activityDetail", adCouponActivity);
            messageDataBean.setCode(MessageDataBean.success_code);
        } else {
            messageDataBean.setCode(MessageDataBean.no_data_code);
        }
        messageDataBean.setData(map);
        return messageDataBean;
    }

    /**
     * 首页投票选项
     *
     * @param userWechatOpenId
     * @return
     */
    public MessageDataBean findVoteOptionList( String userWechatOpenId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        //投票选项
        List<AdVoteOption> adVoteOptionList = adVoteOptionDao.findList(0);
        for (AdVoteOption adVoteOption : adVoteOptionList) {
            AdVoteRecord byOptionIdAndOpenId = adVoteRecordDao.findByOptionIdAndOpenId(adVoteOption.getId(), userWechatOpenId);
            if(null != byOptionIdAndOpenId){
                adVoteOption.setIsVote(1);//说明该用户给该选项投过票了
            }
        }
        messageDataBean.setCode(MessageDataBean.success_code);
        map.put("adVoteOptionList",adVoteOptionList);
        messageDataBean.setData(map);
        return messageDataBean;
    }

    @Override
    public MessageDataBean browserCount(Integer activityId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        adCouponActivityDao.updateBrowserCount(activityId);// 修改浏览次数
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(new HashMap<String,Object>());
        return messageDataBean;
    }

    @Override
    public MessageDataBean clickVote(Integer activityId,Integer optionId, String userWechatOpenId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        AdVoteRecord byOptionIdAndOpenId = adVoteRecordDao.findByOptionIdAndOpenId(optionId, userWechatOpenId);
        if(null != byOptionIdAndOpenId){
            messageDataBean.setCode(MessageDataBean.failure_code);//说明该用户给该选项投过票了
            messageDataBean.setMess("已投票");
        }else {
            // 活动详情
            AdCouponActivity adCouponActivity = adCouponActivityDao.getActivityDetail(activityId);
            if(adCouponActivity.getEndDate().before(new Date())){
                //说明超过了投票时间
                messageDataBean.setCode(MessageDataBean.failure_code);//说明该用户给该选项投过票了
                messageDataBean.setMess("投票已经结束");
            }else {
                int voteCountByOpenId = adVoteRecordDao.findVoteCountByOpenId(userWechatOpenId);// 查询投票数
                if(voteCountByOpenId>=MAX_VOTE_COUNT){
                    messageDataBean.setCode(MessageDataBean.failure_code);//说明该用户投满了
                    messageDataBean.setMess("投票完成");
                }else {
                    AdVoteRecord adVoteRecord = new AdVoteRecord();
                    adVoteRecord.setOptionId(optionId);
                    adVoteRecord.setUserWechatOpenId(userWechatOpenId);
                    adVoteRecord.setCreateDate(new Date());
                    adVoteRecordDao.insert(adVoteRecord);
                    adCouponActivityDao.updateVoteCount(activityId);//更新活动投票数
                    adVoteOptionDao.updateVoteCount(optionId);//更新活动投票数
                    messageDataBean.setCode(MessageDataBean.success_code);//设置状态值
                    if(voteCountByOpenId == MAX_VOTE_COUNT-1){
                        messageDataBean.setMess("投票完成");
                    }else {
                        messageDataBean.setMess("还有"+(MAX_VOTE_COUNT-voteCountByOpenId-1)+"次投票机会");
                    }
                    messageDataBean.setData(new HashMap<String,Object>());
                }
            }

        }
        return messageDataBean;
    }

    @Override
    public MessageDataBean findVoteCountByOpenId(String userWechatOpenId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        int voteCountByOpenId = adVoteRecordDao.findVoteCountByOpenId(userWechatOpenId);// 查询投票数
        HashMap<String, Object> map = new HashMap<>();
        map.put("voteCount",voteCountByOpenId);
        messageDataBean.setData(map);
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }

    @Override
    public MessageDataBean findVoteOption() {
        MessageDataBean messageDataBean = new MessageDataBean();
        List<AdVoteOption> adVoteOptionList = adVoteOptionDao.findList(1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("adVoteOptionList",adVoteOptionList);
        messageDataBean.setData(map);
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }
}
