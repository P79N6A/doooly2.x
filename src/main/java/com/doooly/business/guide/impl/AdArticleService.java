package com.doooly.business.guide.impl;

import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.business.utils.Pagelab;
import com.doooly.dao.reachad.AdArticleDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdPortRecordDao;
import com.doooly.dao.reachad.AdProductDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdArticle;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdPortRecord;
import com.doooly.entity.reachad.AdProduct;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description: 导购
 * @author: qing.zhang
 * @date: 2018-02-26
 */
@Service
public class AdArticleService implements AdArticleServiceI {
    private static final Logger logger = LoggerFactory.getLogger(AdArticleService.class);

    @Autowired
    private AdProductDao adProductDao;
    @Autowired
    private AdBusinessDao adBusinessDao;
    @Autowired
    private AdArticleDao adArticleDao;
    @Autowired
    private AdPortRecordDao adPortRecordDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String GUIDE_RECORD_KEY = "getGuideProductList";

    @Override
    public MessageDataBean getGuideProductList(String orderType, Integer currentPage, Integer pageSize, String userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Pagelab pagelab = new Pagelab(currentPage, pageSize);
        // 查询总数
        int totalNum = adProductDao.getTotalNum();
        if (totalNum > 0) {
            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
            Object o = hashOperations.get(GUIDE_RECORD_KEY, userId);
            if(o==null){
                hashOperations.put(GUIDE_RECORD_KEY,userId,"1");
                AdPortRecord adPortRecord = new AdPortRecord();
                adPortRecord.setPortName("getGuideProductList");
                adPortRecord.setUserId(Long.valueOf(userId));
                adPortRecordDao.insert(adPortRecord);
                map.put("isNew",0);
            }else {
                map.put("isNew",1);
            }
            pagelab.setTotalNum(totalNum);// 这里会计算总页码
            // 查询详情
            List<AdProduct> adProducts = adProductDao.getGuideProductList(orderType,
                    pagelab.getStartIndex(), pagelab.getPageSize());
            for (AdProduct adProduct : adProducts) {
                calculate(adProduct);
            }
            map.put("adProducts", adProducts);// 数据
            map.put("countPage", pagelab.getCountPage());// 总页码
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setData(map);
        } else {
            messageDataBean.setCode(MessageDataBean.no_data_code);
            messageDataBean.setMess("查询导购商品数据为空");
        }
        return messageDataBean;
    }

    @Override
    public MessageDataBean getArticleProductList(String articleId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<AdProduct> adProducts = adArticleDao.getArticleProductList(articleId);
        if (CollectionUtils.isNotEmpty(adProducts)) {
            for (AdProduct adProduct : adProducts) {
                calculate(adProduct);
            }
            map.put("adProducts", adProducts);
            messageDataBean.setData(map);
            messageDataBean.setCode(MessageDataBean.success_code);
        } else {
            messageDataBean.setCode(MessageDataBean.no_data_code);
            messageDataBean.setMess("暂无数据");
        }
        return messageDataBean;
    }

    @Override
    public MessageDataBean getArticleList() {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        AdArticle adArticle = adArticleDao.getNewArticle();
        if(adArticle == null){
            messageDataBean.setCode(MessageDataBean.no_data_code);
            messageDataBean.setMess("暂无数据");
            return messageDataBean;
        }
        List<Map> adProducts = adArticleDao.getArticleList(String.valueOf(adArticle.getId()));
        if (CollectionUtils.isNotEmpty(adProducts)) {
            for (Map map1 : adProducts) {
                BigDecimal marketPrice = (BigDecimal) map1.get("marketPrice");
                String maxUserRebate = String.valueOf(map1.get("maxUserRebate"));
                BigDecimal discount = (BigDecimal) map1.get("discount");
                BigDecimal factPrice;
                //如果价格算出来为0
                if(discount.equals(BigDecimal.ZERO)){
                    factPrice = marketPrice;
                }else {
                    factPrice = marketPrice.multiply(discount).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
                }
                if(StringUtils.isBlank(maxUserRebate)){
                    maxUserRebate = "0";
                }
                map1.put("price", factPrice.doubleValue());
                map1.put("maxUserRebate", maxUserRebate);
            }
            map.put("adProducts", adProducts);
            messageDataBean.setData(map);
            messageDataBean.setCode(MessageDataBean.success_code);
        } else {
            messageDataBean.setCode(MessageDataBean.no_data_code);
            messageDataBean.setMess("暂无数据");
        }
        return messageDataBean;
    }

    @Override
    public MessageDataBean addSellCount(String productId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        Random random = new Random();
        int num = random.nextInt(4);
        AdProduct adProduct = new AdProduct();
        adProduct.setId(Integer.parseInt(productId));
        adProduct.setSellCount(num);
        adProductDao.updateSellCount(adProduct);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setMess(MessageDataBean.success_mess);
        return messageDataBean;
    }

    /**
     * 计算兜礼价和用户返利积分
     *
     * @param adProduct   商品
     * @return
     */
    public void calculate(AdProduct adProduct) {
        if(StringUtils.isBlank(adProduct.getMaxUserRebate())){
            adProduct.setMaxUserRebate("0");
        }
        Double rebate;
        BigDecimal factPrice;//实付
        BigDecimal chu = new BigDecimal("10000");
        AdBusiness adBusiness = adBusinessDao.get(adProduct.getBusinessId());
        if(adBusiness != null){
            if ( adBusiness.getBussinessRebate() != null && adBusiness.getUserRebate() != null) {
                BigDecimal userRebate = new BigDecimal(adBusiness.getUserRebate());
                //前折计算兜礼价 折扣0保持原价
                if(new BigDecimal(adProduct.getDiscount()).equals(BigDecimal.ZERO)){
                    factPrice = adProduct.getMarketPrice();
                }else {
                    factPrice = adProduct.getMarketPrice().multiply(new BigDecimal(adProduct.getDiscount())).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
                }
                if(adProduct.getUserRebate() !=null){
                    //京东开普勒订单实际分层比例
                    rebate = factPrice.multiply(adProduct.getBussinesRebate()).multiply(userRebate).multiply(adProduct.getUserRebate())
                            .divide(new BigDecimal("1000000"), 2, BigDecimal.ROUND_DOWN).doubleValue();
                }else {
                    rebate = factPrice.multiply(adProduct.getBussinesRebate()).multiply(userRebate)
                            .divide(chu, 2, BigDecimal.ROUND_DOWN).doubleValue();
                }
                //前折计算兜礼价
                adProduct.setPrice(factPrice);
                //设置返利
                adProduct.setRebate(String.valueOf(rebate));
            }
        }else{
            adProduct.setPrice(adProduct.getMarketPrice());
            adProduct.setRebate("0.00");
        }
    }
}
