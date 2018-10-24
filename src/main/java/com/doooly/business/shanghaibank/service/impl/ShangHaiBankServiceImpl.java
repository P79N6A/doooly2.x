package com.doooly.business.shanghaibank.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.shanghaibank.api.impl.ShanghaiBankApiImpl;
import com.doooly.business.shanghaibank.beans.api.res.C19BaseRes;
import com.doooly.business.shanghaibank.constants.ShangHaiBankConstans;
import com.doooly.business.shanghaibank.service.ShangHaiBankService;
import com.doooly.business.shanghaibank.utils.RSAUtil;
import com.doooly.business.utils.MD5Util;
import com.doooly.common.util.IdGeneratorUtil;
import com.doooly.dao.payment.AdBankBusinessOpenAccountDao;
import com.doooly.dao.payment.AdShanghaiBankAccountDao;
import com.doooly.dao.payment.AdShanghaiBankChargeRecordDao;
import com.doooly.dao.payment.AdShanghaiBankDao;
import com.doooly.dao.payment.AdShanghaiBankDrawNoticeRecordDao;
import com.doooly.dao.payment.AdShanghaiBankDrawRecordDao;
import com.doooly.dao.payment.AdShanghaiBankTrigserRecordDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.payment.AdBankBusinessOpenAccount;
import com.doooly.entity.payment.AdShanghaiBank;
import com.doooly.entity.payment.AdShanghaiBankAccount;
import com.doooly.entity.payment.AdShanghaiBankChargeRecord;
import com.doooly.entity.payment.AdShanghaiBankDrawNoticeRecord;
import com.doooly.entity.payment.AdShanghaiBankDrawRecord;
import com.doooly.entity.payment.AdShanghaiBankTrigserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Description: 上海银行接口实现类
 * @date: 2018-05-25
 */
@Service
public class ShangHaiBankServiceImpl implements ShangHaiBankService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShangHaiBankServiceImpl.class);

    @Autowired
    private ShanghaiBankApiImpl shanghaiBankApi;
    @Autowired
    private AdShanghaiBankAccountDao adShanghaiBankAccountDao;
    @Autowired
    private AdShanghaiBankChargeRecordDao adShanghaiBankChargeRecordDao;
    @Autowired
    private AdShanghaiBankDrawRecordDao adShanghaiBankDrawRecordDao;
    @Autowired
    private AdShanghaiBankTrigserRecordDao adShanghaiBankTrigserRecordDao;
    @Autowired
    private AdShanghaiBankDrawNoticeRecordDao adShanghaiBankDrawNoticeRecordDao;
    @Autowired
    private AdBankBusinessOpenAccountDao adBankBusinessOpenAccountDao;
    @Autowired
    private AdShanghaiBankDao adShanghaiBankDao;

    /**
     * 虚账户开户
     *
     * @param cNName 开户名称
     * @return
     */
    @Override
    public MessageDataBean c19SCrVirSingleAccount(String cNName, Integer type, String businessId, Long groupId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        try {
            //1,先在我方查询是否开过虚账户
            AdShanghaiBankAccount adShanghaiBankAccount = adShanghaiBankAccountDao.getAccount(cNName);
            if (adShanghaiBankAccount == null) {
                //没开过户，调用上海银行开户接口
                long startTime = System.currentTimeMillis();
                String result = shanghaiBankApi.c19SCrVirSingleAccount(cNName);
                LOGGER.info("上海银行开户接口耗时======" + (System.currentTimeMillis() - startTime));
                C19BaseRes c19BaseRes = JSONObject.parseObject(result, C19BaseRes.class);
                JSONObject data = (JSONObject) c19BaseRes.getData();
                adShanghaiBankAccount = JSONObject.parseObject(data.toJSONString(), AdShanghaiBankAccount.class);
                adShanghaiBankAccount.setAmount(new BigDecimal("0"));
                adShanghaiBankAccount.setFrozenAmount(new BigDecimal("0"));
                adShanghaiBankAccount.setType(type);
                adShanghaiBankAccount.setBusinessId(businessId);
                adShanghaiBankAccount.setGroupId(groupId);
                adShanghaiBankAccountDao.insert(adShanghaiBankAccount);
                messageDataBean.setCode(MessageDataBean.success_code);
                messageDataBean.setMess(MessageDataBean.success_mess);
                map.put("adShanghaiBankAccount", adShanghaiBankAccount);
                messageDataBean.setData(map);
            } else {
                messageDataBean.setCode(MessageDataBean.success_code);
                messageDataBean.setMess(MessageDataBean.success_mess);
                map.put("adShanghaiBankAccount", adShanghaiBankAccount);
                messageDataBean.setData(map);
            }
        } catch (Exception e) {
            LOGGER.error("虚账户开户失败,失败原因", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(e.getMessage());
        }
        return messageDataBean;
    }

    /**
     * 虚账户查询
     *
     * @param cNName 开户名称
     * @return
     */
    @Override
    public MessageDataBean c19SCrVirSingleAccountStInq(String cNName) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        AdShanghaiBankAccount adShanghaiBankAccount = adShanghaiBankAccountDao.getAccount(cNName);
        map.put("adShanghaiBankAccount", adShanghaiBankAccount);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setMess(MessageDataBean.success_mess);
        messageDataBean.setData(map);
        return messageDataBean;
    }

    /**
     * 查询虚账户的余额
     *
     * @param cNName 开户名
     * @return
     */
    @Override
    public MessageDataBean c19VirAcctBlanceInq(String cNName) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        try {
            AdShanghaiBankAccount account = adShanghaiBankAccountDao.getAccount(cNName);
            //调用上海银行徐账号余额查询接口
            long startTime = System.currentTimeMillis();
            String result =  shanghaiBankApi.c19VirAcctBlanceInq(account.getEacctNo());
            LOGGER.info("上海银行虚账号查询接口耗时======" + (System.currentTimeMillis() - startTime));
            C19BaseRes c19BaseRes = JSONObject.parseObject(result, C19BaseRes.class);
            JSONObject data = (JSONObject) c19BaseRes.getData();
            AdShanghaiBankAccount adShanghaiBankAccount = JSONObject.parseObject(data.toJSONString(), AdShanghaiBankAccount.class);
            //更新账户余额
            account.setAmount(adShanghaiBankAccount.getAmount());
            adShanghaiBankAccountDao.updateAmount(account);
            map.put("account",account);
            messageDataBean.setData(map);
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(MessageDataBean.success_mess);
        } catch (Exception e) {
            LOGGER.error("虚账户查询失败,失败原因", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(e.getMessage());
        }
        return messageDataBean;
    }

    /**
     * 虚账户提款
     *
     * @param channelFlowNo 兜礼流水号
     * @param eAcctNo       虚账户账号 暂时不用轧差账户就一个
     * @param amount        转账金额
     * @param usage         用途
     * @return
     */
    @Override
    public MessageDataBean c19VirWithDrawalsInq(String channelFlowNo, String eAcctNo, String amount, String usage) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            //查询轧差账户虚账户
            AdShanghaiBankAccount adShanghaiBankAccount = new AdShanghaiBankAccount();
            adShanghaiBankAccount.setType(3);
            AdShanghaiBankAccount bankAccount = adShanghaiBankAccountDao.getBankAccount(adShanghaiBankAccount);
            //调用上海银行虚账号提款接口
            long startTime = System.currentTimeMillis();
            shanghaiBankApi.C19ChannelVirWithDrawals(channelFlowNo, bankAccount.getEacctNo(), amount, usage);
            LOGGER.info("上海银行虚账号提款接口耗时======" + (System.currentTimeMillis() - startTime));
            AdShanghaiBankDrawRecord adShanghaiBankDrawRecord = new AdShanghaiBankDrawRecord();
            adShanghaiBankDrawRecord.setChannelFlowNo(channelFlowNo);
            adShanghaiBankDrawRecord.setEacctNo(bankAccount.getEacctNo());
            adShanghaiBankDrawRecord.setAmount(new BigDecimal(amount));
            adShanghaiBankDrawRecord.setUseage(usage);
            //插入提款记录表
            adShanghaiBankDrawRecordDao.insert(adShanghaiBankDrawRecord);
            //减少轧差账户徐账号余额
            bankAccount.setAmount(bankAccount.getAmount().subtract(new BigDecimal(amount)));
            adShanghaiBankAccountDao.updateAmount(bankAccount);
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(MessageDataBean.success_mess);
        } catch (Exception e) {
            LOGGER.error("虚账户提款失败,失败原因", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(e.getMessage());
        }
        return messageDataBean;
    }

    /**
     * 账户冻结或解冻
     *
     * @param channelFlowNo   兜礼流水号
     * @param eAcctNo         虚账户账号
     * @param txnType         交易类型(1虚账户冻结,2虚账户解冻)
     * @param applyAmount     冻结或解冻金额
     * @param platformSummary 平台摘要
     * @param notes           备注
     * @return
     */
    @Override
    public MessageDataBean c19VirAmtFrozOrNot(String channelFlowNo, String eAcctNo, String txnType, String applyAmount, String platformSummary, String notes) {
        return null;
    }

    /**
     * 虚账户单笔代缴
     *
     * @param channelFlowNo   兜礼流水号
     * @param eAcctNo         虚账户账号
     * @param amount          代缴金额
     * @param recvAccount     收费公司账号
     * @param recvAccountName 收费公司账号户名
     * @param recvAccountBank 收费公司账户行号
     * @param usage           用途
     * @param platformSummary 平台摘要
     * @return
     */
    @Override
    public MessageDataBean c19SingleCharge(String channelFlowNo, String eAcctNo, String amount, String recvAccount,
                                           String recvAccountName, String recvAccountBank, String usage, String platformSummary) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            //调用上海银行代缴接口
            long startTime = System.currentTimeMillis();
            String result = shanghaiBankApi.c19SingleCharge(channelFlowNo, eAcctNo, amount, recvAccount, recvAccountName, recvAccountBank, usage, platformSummary);
            LOGGER.info("上海银行代缴接口耗时======" + (System.currentTimeMillis() - startTime));
            C19BaseRes c19BaseRes = JSONObject.parseObject(result, C19BaseRes.class);
            JSONObject data = (JSONObject) c19BaseRes.getData();
            AdShanghaiBankChargeRecord adShanghaiBankChargeRecord = new AdShanghaiBankChargeRecord();
            JSONObject jsonObject = JSONObject.parseObject(data.toJSONString());
            adShanghaiBankChargeRecord.setBankFlowNo(jsonObject.getString("flowNo"));
            adShanghaiBankChargeRecord.setEacctNo(eAcctNo);
            adShanghaiBankChargeRecord.setAmount(new BigDecimal(amount));
            adShanghaiBankChargeRecord.setChannelFlowNo(channelFlowNo);
            adShanghaiBankChargeRecord.setRecvAccount(recvAccount);
            adShanghaiBankChargeRecord.setRecvAccountName(recvAccountName);
            adShanghaiBankChargeRecord.setRecvAccountBank(recvAccountBank);
            adShanghaiBankChargeRecord.setUseage(usage);
            adShanghaiBankChargeRecord.setPlatformSummary(platformSummary);
            //插入代缴记录
            adShanghaiBankChargeRecordDao.insert(adShanghaiBankChargeRecord);
            //更新虚账户余额,虚账户扣款
            adShanghaiBankAccountDao.updateAmountByEAcctNo(eAcctNo,new BigDecimal(amount),2);
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(MessageDataBean.success_mess);
        } catch (Exception e) {
            LOGGER.error("虚账户单笔代缴失败,失败原因", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(e.getMessage());
        }
        return messageDataBean;
    }

    /**
     * 虚账户单笔代发
     *
     * @param amount     金额
     * @param businessId 商户id
     * @param groupId    企业id
     * @param type       类型 //1表示从商户发到企业,2表示从企业到商户,3企业到轧差账户
     * @return
     */
    @Override
    public MessageDataBean c19VirSReTrigSer(String amount, String businessId, String groupId, String type) {
        MessageDataBean messageDataBean = new MessageDataBean();
        AdShanghaiBankAccount adShanghaiBankAccount = new AdShanghaiBankAccount();
        adShanghaiBankAccount.setBusinessId(businessId);
        adShanghaiBankAccount.setGroupId(Long.valueOf(groupId));
        //查询账户虚户
        AdShanghaiBankAccount offset = new AdShanghaiBankAccount();
        //查询商户虚户
        AdShanghaiBankAccount adBussinessAccount = new AdShanghaiBankAccount();
        //查询入账企业虚户
        AdShanghaiBankAccount adInGroupAccount = new AdShanghaiBankAccount();
        if ("3".equals(type)||"4".equals(type)) {
            adShanghaiBankAccount.setType(3);
            //查询轧差账户虚户
            offset = adShanghaiBankAccountDao.getBankAccount(adShanghaiBankAccount);
        } else if("5".equals(type)){
            adShanghaiBankAccount.setType(5);
            //查询商户虚户
            adInGroupAccount = adShanghaiBankAccountDao.getBankAccount(adShanghaiBankAccount);
            if (adInGroupAccount == null) {
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess("该商户未开通上海银行虚账户");
                return messageDataBean;
            }
        }else {
            adShanghaiBankAccount.setType(1);
            //查询商户虚户
            adBussinessAccount = adShanghaiBankAccountDao.getBankAccount(adShanghaiBankAccount);
            if (adBussinessAccount == null) {
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess("该商户未开通上海银行虚账户");
                return messageDataBean;
            }
        }
        adShanghaiBankAccount.setType(2);
        //查询企业虚户
        AdShanghaiBankAccount adGroupAccount = adShanghaiBankAccountDao.getBankAccount(adShanghaiBankAccount);
        if (adGroupAccount == null) {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("该企业未开通上海银行虚账户");
            return messageDataBean;
        }
        String eAcctNo = "";
        String payAccount = "";
        String payAccountName = "";
        String usage = "";
        String platformSummary = "";
        String channelFlowNo = IdGeneratorUtil.getOrderNumber(3);
        if ("1".equals(type)) {
            //1表示从商户发到企业
            eAcctNo = adGroupAccount.getEacctNo();
            payAccount = adBussinessAccount.getEacctNo();
            payAccountName = adBussinessAccount.getEacctName();
            usage = "商户退货积分退还代发记账";
        } else if ("2".equals(type)) {
            eAcctNo = adBussinessAccount.getEacctNo();
            payAccount = adGroupAccount.getEacctNo();
            payAccountName = adGroupAccount.getEacctName();
            usage = "企业消费积分代发记账";
        } else if ("3".equals(type)) {
            eAcctNo = offset.getEacctNo();
            payAccount = adGroupAccount.getEacctNo();
            payAccountName = adGroupAccount.getEacctName();
            usage = "企业退货积分代发记账";
        } else if ("4".equals(type)) {
            eAcctNo = adGroupAccount.getEacctNo();
            payAccount = offset.getEacctNo();
            payAccountName = offset.getEacctName();
            usage = "轧差账户转发到虚账户";
        } else if ("5".equals(type)) {
            //企业到企业
            eAcctNo = adInGroupAccount.getEacctNo();
            payAccount = adGroupAccount.getEacctNo();
            payAccountName = adGroupAccount.getEacctName();
            usage = "轧差账户转发到虚账户";
        }
        long startTime = System.currentTimeMillis();
        messageDataBean = c19VirSReTrigSer(channelFlowNo, eAcctNo, amount, payAccount, payAccountName, usage, platformSummary);
        LOGGER.info("上海银行单笔代发接口耗时======" + (System.currentTimeMillis() - startTime));
        return messageDataBean;
    }

    /**
     * 虚账户单笔代发
     *
     * @param channelFlowNo   兜礼流水号
     * @param eAcctNo         虚账户账号
     * @param amount          代缴金额
     * @param payAccount      代发账号
     * @param payAccountName  代发账户名
     * @param usage           用途
     * @param platformSummary 平台摘要
     * @return
     */
    @Override
    public MessageDataBean c19VirSReTrigSer(String channelFlowNo, String eAcctNo, String amount, String payAccount, String payAccountName, String usage, String platformSummary) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            //调用上海银行代发接口
            String result = shanghaiBankApi.c19VirSReTrigSer(channelFlowNo, eAcctNo, amount, payAccount, payAccountName, usage, platformSummary);
            C19BaseRes c19BaseRes = JSONObject.parseObject(result, C19BaseRes.class);
            JSONObject data = (JSONObject) c19BaseRes.getData();
            AdShanghaiBankTrigserRecord adShanghaiBankTrigserRecord = new AdShanghaiBankTrigserRecord();
            JSONObject jsonObject = JSONObject.parseObject(data.toJSONString());
            adShanghaiBankTrigserRecord.setBankFlowNo(jsonObject.getString("flowNo"));
            adShanghaiBankTrigserRecord.setChannelFlowNo(channelFlowNo);
            adShanghaiBankTrigserRecord.setEacctNo(eAcctNo);
            adShanghaiBankTrigserRecord.setAmount(new BigDecimal(amount));
            adShanghaiBankTrigserRecord.setPayAccount(payAccount);
            adShanghaiBankTrigserRecord.setPayAccountName(payAccountName);
            adShanghaiBankTrigserRecord.setUseage(usage);
            adShanghaiBankTrigserRecord.setPlatformSummary(platformSummary);
            //插入代发记录
            adShanghaiBankTrigserRecordDao.insert(adShanghaiBankTrigserRecord);
            //更新虚户余额
            //更新增加的
            adShanghaiBankAccountDao.updateAmountByEAcctNo(eAcctNo,new BigDecimal(amount),1);
            //更新减少的
            adShanghaiBankAccountDao.updateAmountByEAcctNo(payAccount,new BigDecimal(amount),2);
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(MessageDataBean.success_mess);
        } catch (Exception e) {
            LOGGER.error("虚账户单笔代发失败,失败原因", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("虚账户单笔代发失败");
        }
        return messageDataBean;
    }

    /**
     * 虚账户交易明细查询
     * @param eAcctNo 虚账号
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param otherAccNo 对方账号
     * @param otherAccName 对方户名
     * @param voucherNo 凭证编号
     * @param pageIndex 查询页码
     * @param pageSize  每页多少条记录
     * @return
     */
    @Override
    public MessageDataBean c19VirAcctTranDtlQry(String eAcctNo, String beginDate, String endDate,
                                                String otherAccNo, String otherAccName, String voucherNo, String pageIndex, String pageSize) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String,Object> map = new HashMap<>();
        try {
            //调用虚账户交易明细查询接口
            String result = shanghaiBankApi.c19VirAcctTranDtlQry(eAcctNo, beginDate, endDate, otherAccNo, otherAccName, voucherNo,pageIndex,pageSize);
            C19BaseRes c19BaseRes = JSONObject.parseObject(result, C19BaseRes.class);
            JSONObject data = (JSONObject) c19BaseRes.getData();
            //JSONObject jsonObject = JSONObject.parseObject(data.toJSONString());
            //处理参数
            map.put("result",data.toJSONString());
            messageDataBean.setData(map);
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(MessageDataBean.success_mess);
        } catch (Exception e) {
            LOGGER.error("虚账户交易明细查询失败,失败原因", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("虚账户交易明细查询失败");
        }
        return messageDataBean;
    }

    /**
     * 虚账户交易指令状态查询
     *
     * @param channelFlowNo 兜礼平台流水号
     * @param channelId     接入渠道id
     * @return
     */
    @Override
    public MessageDataBean c19VirAcctDeleCharSttInq(String channelFlowNo, String channelId) {
        return null;
    }

    /**
     * 打印代缴凭证
     *
     * @param eAcctNo       虚账户账号
     * @param startDate     查询开始时间
     * @param endDate       查询结束时间
     * @param channelFlowNo 交易流水号
     * @return
     */
    @Override
    public MessageDataBean c19VirAcctPrintReceipt(String eAcctNo, String startDate, String endDate, String channelFlowNo) {
        return null;
    }

    /**
     * 虚账户打款到账通知
     *
     * @param checkvalue 报文体签名
     * @param encmsg     加密后的报文体
     * @param service    请求的方法
     * @return
     */
    @Override
    public String c19WithDrawalsNotice(String checkvalue, String encmsg, String service) {
        JSONObject resultObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            //调用上海银行解密方法,返回解密字符串,为空验签失败
            String decMsg = shanghaiBankApi.c19WithDrawalsNotice(checkvalue, encmsg, service);
            if (decMsg == null) {
                jsonObject.put("retcode", "100000");
                jsonObject.put("retmsg", "验签失败");
            } else {
                if (ShangHaiBankConstans.VIRACCTNOTICE.equals(service)) {
                    jsonObject.put("retcode", "000000");
                    jsonObject.put("retmsg", "交易成功");
                    //插入通知记录表
                    AdShanghaiBankDrawNoticeRecord adShanghaiBankDrawNoticeRecord = JSONObject.parseObject(decMsg, AdShanghaiBankDrawNoticeRecord.class);
                    adShanghaiBankDrawNoticeRecordDao.insert(adShanghaiBankDrawNoticeRecord);
                    //更新虚账户余额
                    AdShanghaiBankAccount adShanghaiBankAccount = new AdShanghaiBankAccount();
                    adShanghaiBankAccount.setEacctNo(adShanghaiBankDrawNoticeRecord.getVirAccNo());
                    adShanghaiBankAccount.setAmount(new BigDecimal(adShanghaiBankDrawNoticeRecord.getAmt()));
                    adShanghaiBankAccountDao.updateAmountByNotice(adShanghaiBankAccount);
                } else {
                    jsonObject.put("retcode", "100000");
                    jsonObject.put("retmsg", "service参数有误");
                }
            }
        } catch (Exception e) {
            LOGGER.error("虚账户打款通知失败,失败原因", e);
            jsonObject.put("retcode", "100000");
            jsonObject.put("retmsg", "系统错误");
        }
        String[] result = RSAUtil.signData(ShangHaiBankConstans.PRIBKEY, JSON.toJSONString(jsonObject), ShangHaiBankConstans.ENCODING);
        resultObject.put("checkvalue", result[0]);
        resultObject.put("encmsg", result[1]);
        resultObject.put("service", ShangHaiBankConstans.VIRACCTNOTICE);
        return resultObject.toJSONString();
    }


    /**
     * 虚账户代缴
     *
     * @param businessId 代缴商户id
     * @param eAcctNo    虚账户账号
     * @param amount     代缴金额
     * @param sign       加密参数
     * @return
     */
    @Override
    public MessageDataBean c19SingleCharge(String businessId, String eAcctNo, String amount, String sign) {
        MessageDataBean messageDataBean = new MessageDataBean();
        String str = "businessId=" + businessId +
                "&eAcctNo=" + eAcctNo +
                "&amount=" + amount +
                "&signKey=" + ShangHaiBankConstans.SIGN_KEY;
        String sign1 = MD5Util.MD5Encode(str, "UTF-8");
        if (!sign.equals(sign1)) {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("验证参数错误");
            return messageDataBean;
        }
        //根据商户id获取商户绑定银行卡信息
        AdBankBusinessOpenAccount adBankBusinessOpenAccount = adBankBusinessOpenAccountDao.findByBusinessId(Integer.valueOf(businessId));
        AdShanghaiBank adShanghaiBank = adShanghaiBankDao.findByAccountBank(adBankBusinessOpenAccount.getAccountBank());
        if (adShanghaiBank == null) {
            //说明不支持改行号
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("暂不支持改行号");
            return messageDataBean;
        }

        String recvAccount = adBankBusinessOpenAccount.getBusinessEntityCard().replaceAll(" ", "");
        String recvAccountName = adBankBusinessOpenAccount.getCardholderName();
        String recvAccountBank = adBankBusinessOpenAccount.getAccountBank();
        String usage = "商户结算打款";
        String platformSummary = null;
        String channelFlowNo = IdGeneratorUtil.getOrderNumber(3);
        messageDataBean = c19SingleCharge(channelFlowNo, eAcctNo, amount, recvAccount, recvAccountName, recvAccountBank, usage, platformSummary);
        return messageDataBean;
    }

}
