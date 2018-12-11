package com.doooly.business.myorder.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myorder.dto.OrderResult;
import com.doooly.business.myorder.service.MyOrderServiceI;
import com.doooly.business.pay.bean.AdOrderFlow;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.Pagelab;
import com.doooly.dao.reachad.AdOrderDetailDao;
import com.doooly.dao.reachad.AdOrderFlowDao;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdOrderDetail;
import com.doooly.entity.reachad.AdOrderReport;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserBusinessExpansion;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-07-11
 */
@Service
public class MyOrderService implements MyOrderServiceI {

    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(MyOrderService.class);

    @Autowired
    private AdOrderReportDao adOrderReportDao;
    @Autowired
    private AdOrderFlowDao adOrderFlowDao;
    @Autowired
    private AdOrderDetailDao adOrderDetailDao;
    @Autowired
    private AdUserDao adUserDao;

    @Override
    public OrderResult getOrders(String paramBody) {
        OrderResult orderResult = new OrderResult();
        Map<String,Object> status = new HashMap<>();
        status.put("code",0);// 返回码
        status.put("msg", "ok");// 返回信息
        logger.info("请求参数：{}",paramBody);
        JSONObject param = JSONObject.parseObject(paramBody).getJSONObject("body");
        Boolean isNeedPage = false;
        //分页参数
        JSONObject ipage = param.getJSONObject("ipage");
        Integer currentPage =  null;
        Integer pageSize = null;
        if (ipage != null) {
            currentPage = Integer.valueOf(ipage.getString("gotoPage"));
            pageSize = Integer.valueOf(ipage.getString("pageSize"));
            isNeedPage = true;
        }
        //查询参数
        JSONObject data = param.getJSONObject("data");
        String userId =  data.getString("userId");
        String startDate =  data.getString("startDate");
        String endDate =  data.getString("endDate");
        String sn =  data.getString("sn");
        String company =  data.getString("company");
        String type =  data.getString("type");
        String state =  data.getString("state");

        if(StringUtils.isBlank(userId)){
            status.put("code",1);// 返回码
            status.put("msg", "用户信息为空");// 返回信息
            orderResult.setStatus(status);
            return orderResult;
        }

        Calendar calendar = null;
        AdOrderReport adOrderReport = new AdOrderReport();
        if (StringUtils.isNotEmpty(company)) {
            AdBusiness adBusiness = new AdBusiness();
            adBusiness.setBusinessId(company);
            adOrderReport.setAdBusiness(adBusiness);
        }
        // 开始日期
        if (StringUtils.isNotEmpty(startDate)) {
            Date beginOrderDate = DateUtils.parseDate(startDate);
            calendar = Calendar.getInstance();
            calendar.setTime(beginOrderDate);
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
            adOrderReport.setBeginOrderDate(calendar.getTime());
        }
        // 结束日期
        if (StringUtils.isNotEmpty(endDate)) {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            Date endOrderDate = DateUtils.parseDate(endDate);
            calendar.setTime(endOrderDate);
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
            adOrderReport.setEndOrderDate(calendar.getTime());
        }
        // 第三方订单号
        if (StringUtils.isNotEmpty(sn)) {
            adOrderReport.setOrderNumber(sn);
        }

        // 订单类型（1：订单；5：退货单）
        if (StringUtils.isNotEmpty(type)) {
            adOrderReport.setType(new Integer(type));
        }

        // 订单完成状态（1：已完成；2：已取消）
        if (StringUtils.isNotBlank(state)) {
            adOrderReport.setState(state);
        }
        // 用户自己没有删除的订单，才显示，删除的不显示，设置删除为正常
        adOrderReport.setDelFlagUser("0");
        // 去除兜礼，吉祥航空
        Long[] ids = { new Long(6), new Long(11), new Long(18) };
        adOrderReport.setBusinessIds(ids);
        AdUser adUser = new AdUser();
        adUser.setId(Long.valueOf(userId));
        adOrderReport.setAdUser(adUser);

        Pagelab pagelab = new Pagelab(currentPage, pageSize);
        adOrderReport.setStartIndex(pagelab.getStartIndex());
        adOrderReport.setPageSize(pagelab.getPageSize());
        if (isNeedPage) {
            // 查询总数
            int totalNum = adOrderReportDao.getTotalNum(adOrderReport);
            if (totalNum > 0) {
                pagelab.setTotalNum(totalNum);// 这里会计算总页码
                // 查询详情
                List<AdOrderReport> list = adOrderReportDao.findOrderList(adOrderReport);
                if(CollectionUtils.isNotEmpty(list)){
                    for (AdOrderReport order : list) {
                        AdOrderReport detail = adOrderReportDao.getDetailByOrderReportId(order.getId());
                        if(detail != null){
                            order.setGoods(detail.getGoods());
                            order.setSpecification(detail.getSpecification());
                            order.setProductImg(detail.getProductImg());
                        }
                    }
                }
                orderResult.setData(getResult(list));
                orderResult.setOrderData(adOrderReportDao.findOrderSumByMonth(adOrderReport));
                Map<String,Object> page = new HashMap<>();
                page.put("countPage", pagelab.getCountPage());// 总页码
                page.put("totalSize", pagelab.getTotalNum());// 总数量
                orderResult.setiPage(page);
            } else {
                status.put("code",1);// 返回码
                status.put("msg", "没有查询到订单");// 返回信息
            }
        } else {
            List<Map> datas = adOrderReportDao.findOrderSum(adOrderReport);
            orderResult.setData(datas);
        }
        orderResult.setStatus(status);
        return orderResult;
    }

    @Override
    public OrderResult getOrderDetail(String paramBody) {
        OrderResult orderResult = new OrderResult();
        Map<String,Object> status = new HashMap<>();
        status.put("code",0);// 返回码
        status.put("msg", "ok");// 返回信息
        JSONObject param = JSONObject.parseObject(paramBody).getJSONObject("body");
        //查询参数
        JSONObject data = param.getJSONObject("data");
        String userId =  data.getString("userId");
        String orderReportId =  data.getString("orderReportId");
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderReportId)) {
            status.put("code",100);// 返回码
            status.put("msg", "The data format error");// 返回信息
            orderResult.setStatus(status);
            return orderResult;
        }

        AdOrderReport adOrderReport = adOrderReportDao.getOrderDetailInfoById(orderReportId);
        if (adOrderReport == null) {
            status.put("code",124);// 返回码
            status.put("msg", "查无此订单记录");// 返回信息
            orderResult.setStatus(status);
            return orderResult;
        }
        if (!adOrderReport.getAdUser().getId().equals(Long.valueOf(userId))) { // 该订单明细不属于该会员
            status.put("code",125);// 返回码
            status.put("msg", "会员与此订单记录不匹配，不能查看");// 返回信息
            orderResult.setStatus(status);
            return orderResult;
        }
        Map map = new HashMap();
        putOrderReportToMapByOrderReportId(map, adOrderReport);
        List<Map> datas = new ArrayList<>();
        datas.add(map);
        orderResult.setData(datas);
        orderResult.setStatus(status);
        return orderResult;
    }

    private void putOrderReportToMapByOrderReportId(Map map, AdOrderReport adOrderReport) {
        AdUserBusinessExpansion adUserBusinessExpansion = null;
        //都市旅游卡订单信息
        if (adOrderReport.getProductType() == 5) {
            adUserBusinessExpansion = adOrderReportDao.findSctcdAccount(adOrderReport);
        }
        AdOrderFlow adOrderFlowQuery = new AdOrderFlow();
        adOrderFlowQuery.setAdOrderReport(adOrderReport);
        List<AdOrderFlow> adOrderFlowList = adOrderFlowDao.findListByAdOrderReport(adOrderFlowQuery);
        //根据流水获取支付方式
        if (CollectionUtils.isNotEmpty(adOrderFlowList)) {
            adOrderReport.setAdOrderFlowList(adOrderFlowList);
            String payTypeStr = "";
            Map<String,String> payTypeMap = new HashMap<>();
            for (AdOrderFlow adOrderFlow : adOrderFlowList) {
                if (adOrderFlow.getPayType() == AdOrderFlow.PAY_TYPE_PLATFORM_POINT) {
                    payTypeMap.put("0","兜礼积分");
                }else if(adOrderFlow.getPayType() == AdOrderFlow.PAY_TYPE_WECHAT){
                    payTypeMap.put("3","微信支付");
                }else {
                    payTypeMap.put("1","现金支付");
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : payTypeMap.values()) {
                stringBuilder.append(s).append("/");
            }
            if (StringUtils.isNotBlank(stringBuilder)) {
                payTypeStr = stringBuilder.toString().substring(0, stringBuilder.length()-1);
            }
            adOrderReport.setPayTypeStr(payTypeStr);
        }
        //查询订单详情
        AdOrderDetail adOrderDetailQuery = new AdOrderDetail();
        adOrderDetailQuery.setAdOrderReport(adOrderReport);
        List<AdOrderDetail> adOrderDetailList = adOrderDetailDao.findListByAdOrderReport(adOrderDetailQuery);
        if (adOrderDetailList != null) {
            adOrderReport.setAdOrderDetailList(adOrderDetailList);
        }
        // 认证会员并且开通了返利开关
        int resultNum = adUserDao.findOpenRebateSwitchNum(adOrderReport.getAdUser().getId());
        adOrderReport.setOpenRebateSwitch(resultNum > 0);
        this.putToMap(map, adOrderReport, adUserBusinessExpansion);
    }


    /**
     * 获得指定订单adOrderReport的map输出(接口使用)。
     * 要求adOrderReport中必须放入adOrderFlowList、adOrderDetailList
     *
     * @param adOrderReport
     * @return
     */
    private void putToMap(Map map, AdOrderReport adOrderReport, AdUserBusinessExpansion adUserBusinessExpansion) {
        Map<String, Object> orderMap = adOrderReport.toMap();
        logger.info("orderMap >>> " + JSONObject.toJSONString(orderMap));
        if (adOrderReport.getProductType() == 5) {
            logger.info("adOrderReport.getProductType() == 5");
            orderMap.put("sctcd_cardno", adOrderReport.getRemarks());
        }
        if (adUserBusinessExpansion != null) {
            orderMap.put("sctcd_cardno", adUserBusinessExpansion.getF1());
            orderMap.put("sctcd_account_mobile", adUserBusinessExpansion.getF2());
            orderMap.put("sctcd_account_id_card", adUserBusinessExpansion.getF5());
        }
        map.put("order", orderMap);
    }

    //获取结果集合
    private List<Map> getResult(List<AdOrderReport> list1) {
        List<Map> list = new ArrayList<>();
        if (list1 != null) {
            for (AdOrderReport adOrderReport : list1) {
                list.add((adOrderReport.toMap()));
            }
        }
        return list;
    }

	@Override
	public long getOrderReportIdByOrderNum(String orderNum) {
		AdOrderReport adOrderReport = new AdOrderReport();
		adOrderReport.setOrderNumber(orderNum);
		adOrderReport = adOrderReportDao.getOrderReportIdByOrderNum(adOrderReport);
		return adOrderReport.getId();
	}

    @Override
    public int orderBelongOneActivity(String activityName, String orderNum) {
        Map<String,Object> param = new HashMap<>();
        param.put("activityName",activityName);
        param.put("orderNum",orderNum);
        return adOrderReportDao.orderBelongOneActivity(param);
    }
}
