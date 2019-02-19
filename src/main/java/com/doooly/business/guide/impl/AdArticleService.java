package com.doooly.business.guide.impl;

import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.business.utils.Pagelab;
import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.reachad.AdProductExtend;
import com.doooly.entity.reachad.*;
import com.reach.redis.annotation.Cacheable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Autowired
    private AdGuideCategoryDao adGuideCategoryDao;

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
            if (o == null) {
                hashOperations.put(GUIDE_RECORD_KEY, userId, "1");
                AdPortRecord adPortRecord = new AdPortRecord();
                adPortRecord.setPortName("getGuideProductList");
                adPortRecord.setUserId(Long.valueOf(userId));
                adPortRecordDao.insert(adPortRecord);
                map.put("isNew", 0);
            } else {
                map.put("isNew", 1);
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

    @Cacheable(module = "GUIDEPRODUCT", event = "getGuideProductListv2",
            key = "groupId, recommendHomepage, guideCategoryId, currentPage",
            expiresKey = "expires")
    @Override
    public MessageDataBean getGuideProductListv2(Map<String, String> paramMap) {
        MessageDataBean messageDataBean = new MessageDataBean();
        String userId = paramMap.get("userId");
        String guideCategoryId = paramMap.get("guideCategoryId");
        String recommendHomepage = paramMap.get("recommendHomepage");
        Integer currentPage = Integer.valueOf(paramMap.get("currentPage"));
        Integer pageSize = Integer.valueOf(paramMap.get("pageSize"));

        paramMap.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE + "");

        HashMap<String, Object> map = new HashMap<String, Object>();
        Pagelab pagelab = new Pagelab(currentPage, pageSize);
        // 查询总数
        int totalNum = adProductDao.getTotalNumv2(guideCategoryId,recommendHomepage);
        if (totalNum > 0) {
            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
            Object o = hashOperations.get(GUIDE_RECORD_KEY, userId);
            if (o == null) {
                hashOperations.put(GUIDE_RECORD_KEY, userId, "1");
                AdPortRecord adPortRecord = new AdPortRecord();
                adPortRecord.setPortName("getGuideProductList");
                adPortRecord.setUserId(Long.valueOf(userId));
                adPortRecordDao.insert(adPortRecord);
                map.put("isNew", 0);
            } else {
                map.put("isNew", 1);
            }
            pagelab.setTotalNum(totalNum);// 这里会计算总页码
            // 查询详情
            List<AdProduct> adProducts = adProductDao.getGuideProductListv2(guideCategoryId,
                    pagelab.getStartIndex(), pagelab.getPageSize(), recommendHomepage);
            for (AdProduct adProduct : adProducts) {
                calculate(adProduct);
            }
            map.put("adProducts", adProducts);// 数据
            map.put("countPage", pagelab.getCountPage());// 总页码
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setData(map);

            // 失效时间
            Long date = (System.currentTimeMillis() / 1000) + RedisConstants.REDIS_CACHE_EXPIRATION_DATE;

            for (AdProduct adProduct : adProducts) {
                // 如果失效时间大于商品结束时间，则修改失效时间为商品结束时间
                if (date > (adProduct.getBuyEndTime().getTime()) / 1000) {
                    date = (adProduct.getBuyEndTime().getTime()) / 1000;
                }
            }

            date = date - (System.currentTimeMillis() / 1000);

            logger.info("getGuideProductListv2>>失效时间(实际会减去1s)===" + date);
            paramMap.put("expires", date + "");

            map.put("expires", (date - 1) + "");
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
        if (adArticle == null) {
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
                if (discount.equals(BigDecimal.ZERO)) {
                    factPrice = marketPrice;
                } else {
                    factPrice = marketPrice.multiply(discount).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
                }
                if (StringUtils.isBlank(maxUserRebate)) {
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
    public MessageDataBean getGuideCategaryList() {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        List<AdGuideCategory> adGuideCategoryList = adGuideCategoryDao.findList();
        map.put("adGuideCategoryList", adGuideCategoryList);
        messageDataBean.setData(map);
        messageDataBean.setCode(MessageDataBean.success_code);
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

    @Cacheable(module = "GUIDEPRODUCT", event = "getGuideProductListv3",
            key = "groupId, recommendHomepage, guideCategoryId, currentPage",
            expiresKey = "expires")
    @Override
    public MessageDataBean getGuideProductListv3(Map<String, String> paramMap) {
        MessageDataBean messageDataBean = new MessageDataBean();
        String userId = paramMap.get("userId");
        String guideCategoryId = paramMap.get("guideCategoryId");
        String recommendHomepage = paramMap.get("recommendHomepage");
        Integer currentPage = Integer.valueOf(paramMap.get("currentPage"));
        Integer pageSize = Integer.valueOf(paramMap.get("pageSize"));

        paramMap.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE + "");

        HashMap<String, Object> map = new HashMap<String, Object>();
        Pagelab pagelab = new Pagelab(currentPage, pageSize);
        // 查询总数
        int totalNum = adProductDao.getTotalNumv2(guideCategoryId,recommendHomepage);
        if (totalNum > 0) {
            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
            Object o = hashOperations.get(GUIDE_RECORD_KEY, userId);
            if (o == null) {
                hashOperations.put(GUIDE_RECORD_KEY, userId, "1");
                AdPortRecord adPortRecord = new AdPortRecord();
                adPortRecord.setPortName("getGuideProductList");
                adPortRecord.setUserId(Long.valueOf(userId));
                adPortRecordDao.insert(adPortRecord);
                map.put("isNew", 0);
            } else {
                map.put("isNew", 1);
            }
            pagelab.setTotalNum(totalNum);// 这里会计算总页码
            // 查询详情
            List<AdProductExtend> adProducts = adProductDao.getGuideProductListv3(guideCategoryId,
                    pagelab.getStartIndex(), pagelab.getPageSize(), recommendHomepage);
            for (AdProductExtend adProduct : adProducts) {
                calculate(adProduct);
                isStar(adProduct); //品牌馆判断
            }
            map.put("adProducts", adProducts);// 数据
            map.put("countPage", pagelab.getCountPage());// 总页码
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setData(map);

            // 失效时间
            Long date = (System.currentTimeMillis() / 1000) + RedisConstants.REDIS_CACHE_EXPIRATION_DATE;

            for (AdProduct adProduct : adProducts) {
                // 如果失效时间大于商品结束时间，则修改失效时间为商品结束时间
                if (date > (adProduct.getBuyEndTime().getTime()) / 1000) {
                    date = (adProduct.getBuyEndTime().getTime()) / 1000;
                }
            }

            date = date - (System.currentTimeMillis() / 1000);

            logger.info("getGuideProductListv3>>失效时间(实际会减去1s)===" + date);
            paramMap.put("expires", date + "");

            map.put("expires", (date - 1) + "");
        } else {
            messageDataBean.setCode(MessageDataBean.no_data_code);
            messageDataBean.setMess("查询导购商品数据为空");
        }
        return messageDataBean;
    }

    /**
     * 目前-南京祖祖祖祖租是品牌馆
     * @param adProduct
     */
    private void isStar(AdProductExtend adProduct) {
        if (StringUtils.equals("Test_nanjingzuzu", adProduct.getBusinessNum())) {
            adProduct.setIsStar("1");
        } else {
            adProduct.setIsStar("0");
        }
    }

    /**
     * 计算兜礼价和用户返利积分
     *
     * @param adProduct 商品
     * @return
     */
    public void calculate(AdProduct adProduct) {
        if (StringUtils.isBlank(adProduct.getMaxUserRebate())) {
            adProduct.setMaxUserRebate("0");
        }
        adProduct.setShippingMethod(adProduct.getShippingMethod() != null ? adProduct.getShippingMethod() : "");
        Double rebate;
        BigDecimal factPrice;//前台根据折扣计算价格
        BigDecimal marketPrice = adProduct.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN);//后台商品配置导购价
        BigDecimal chu = new BigDecimal("10000");
        AdBusiness adBusiness = adBusinessDao.get(adProduct.getBusinessId());
        if (adBusiness != null) {
            if (adBusiness.getBussinessRebate() != null && adBusiness.getUserRebate() != null) {
                BigDecimal userRebate = adProduct.getUserRebate();
                //前折计算兜礼价 折扣0保持原价
                if (new BigDecimal(adProduct.getDiscount()).equals(BigDecimal.ZERO)) {
                    factPrice = marketPrice;
                } else {
                    factPrice = marketPrice.multiply(new BigDecimal(adProduct.getDiscount())).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
                }
                if ("京东返利".equals(adBusiness.getCompany())) {
                    //京东开普勒订单实际分层比例
                    rebate = marketPrice.multiply(adProduct.getBussinesRebate()).multiply(adProduct.getLayeredRebate())
                            .divide(chu, 2, BigDecimal.ROUND_DOWN).doubleValue();
                } else {
                    rebate = marketPrice.multiply(userRebate)
                            .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN).doubleValue();
                }
                //前折计算兜礼价
                adProduct.setPrice(factPrice.setScale(2, BigDecimal.ROUND_DOWN));
                //设置返利
                adProduct.setRebate(String.valueOf(new BigDecimal(rebate).setScale(2, BigDecimal.ROUND_DOWN)));
            }
        } else {
            adProduct.setPrice(marketPrice);
            adProduct.setRebate("0.00");
        }
    }
}
