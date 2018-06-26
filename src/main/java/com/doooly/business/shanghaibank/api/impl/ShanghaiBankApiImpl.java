package com.doooly.business.shanghaibank.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.shanghaibank.api.ShangHaiBankApi;
import com.doooly.business.shanghaibank.constants.ShangHaiBankConstans;
import com.doooly.business.shanghaibank.utils.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Description: 上海银行内部调用接口
 * @author: qing.zhang
 * @date: 2018-05-25
 */
@Service
public class ShanghaiBankApiImpl implements ShangHaiBankApi {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShanghaiBankApiImpl.class);


    /**
     * 虚账户开户
     *
     * @param cNName
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public String c19SCrVirSingleAccount(String cNName) throws IOException {
        Object o = virAccountApi.c19SCrVirSingleAccount(cNName);
        LOGGER.info("虚账户开户接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
    }

    /**
     * 虚账户开户状态查询
     *
     * @param cNName
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public String c19SCrVirSingleAccountStInq(String cNName) throws IOException {
        Object o = virAccountApi.c19SCrVirSingleAccountStInq(cNName);
        LOGGER.info("虚账户开户状态查询接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
    }

    /**
     * 虚账户余额查询
     *
     * @param eAcctNo
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public String c19VirAcctBlanceInq(String eAcctNo) throws IOException {
        Object o = virAccountApi.c19VirAcctBlanceInq(eAcctNo);
        LOGGER.info("虚账户余额查询接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
    }

    /**
     * 虚账户提款
     *
     * @param channelFlowNo 兜礼流水号
     * @param eAcctNo       虚账户账号
     * @param amount        转账金额
     * @param usage         用途
     * @return
     */
    public String c19VirWithDrawalsInq(String channelFlowNo, String eAcctNo, String amount,
                                       String usage) throws IOException {
        Object o = virAccountApi.c19VirWithDrawalsInq(channelFlowNo, eAcctNo, amount, usage);
        LOGGER.info("虚账户提款接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
    }

    /**
     * 平台虚账户提款
     *
     * @param channelFlowNo 兜礼流水号
     * @param eAcctNo       虚账户账号
     * @param amount        转账金额
     * @param usage         用途
     * @return
     */
    public String C19ChannelVirWithDrawals(String channelFlowNo, String eAcctNo, String amount,
                                       String usage) throws IOException {
        Object o = virAccountApi.C19ChannelVirWithDrawals(channelFlowNo, eAcctNo, amount, usage);
        LOGGER.info("虚账户提款接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
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
    public String c19VirAmtFrozOrNot(String channelFlowNo, String eAcctNo, String txnType, String applyAmount, String platformSummary, String notes) throws IOException {
        Object o = virAccountApi.c19VirAmtFrozOrNot(channelFlowNo, eAcctNo, txnType, applyAmount, platformSummary, notes);
        LOGGER.info("账户冻结或解冻接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
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
    public String c19SingleCharge(String channelFlowNo, String eAcctNo, String amount, String recvAccount, String recvAccountName, String recvAccountBank, String usage, String platformSummary) throws IOException {
        Object o = virAccountApi.c19SingleCharge(channelFlowNo, eAcctNo, amount, recvAccount, recvAccountName, recvAccountBank, usage, platformSummary);
        LOGGER.info("虚账户单笔代缴接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
    }

    /**
     * 虚账户单笔代发
     * @param channelFlowNo 兜礼流水号
     * @param eAcctNo 虚账户账号
     * @param amount 代缴金额
     * @param payAccount 代发账号
     * @param payAccountName 代发账户名
     * @param usage 用途
     * @param platformSummary 平台摘要
     * @return
     */
    public String c19VirSReTrigSer(String channelFlowNo, String eAcctNo, String amount, String payAccount, String payAccountName, String usage, String platformSummary) throws IOException {
        Object o = virAccountApi.c19VirSReTrigSer(channelFlowNo, eAcctNo, amount, payAccount, payAccountName, usage, platformSummary);
        LOGGER.info("虚账户单笔代发接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
    }

    /**
     * 虚账户交易指令状态查询
     * @param channelFlowNo 兜礼平台流水号
     * @param channelId 接入渠道id
     * @return
     */
    public String c19VirAcctDeleCharSttInq(String channelFlowNo, String channelId) throws IOException {
        Object o = virAccountApi.c19VirAcctDeleCharSttInq(channelFlowNo, channelId);
        LOGGER.info("虚账户交易指令状态查询接口返回结果========="+JSON.toJSONString(o, true));
        return JSON.toJSONString(o, true);
    }

    /**
     * 打印代缴凭证
     * @param eAcctNo 虚账户账号
     * @param startDate 查询开始时间
     * @param endDate 查询结束时间
     * @param channelFlowNo 交易流水号
     * @return
     */
    public void c19VirAcctPrintReceipt(String eAcctNo, String startDate, String endDate, String channelFlowNo) throws IOException {
        JSONObject o = pdfApi.c19VirAcctPrintReceipt(eAcctNo,startDate,endDate,channelFlowNo);
        String ss= o.getString("data");
        byte[] b= Base64.decodeBase64(ss);
        File f= new File("D:/aa/test3.pdf");
        FileUtils.writeByteArrayToFile(f, b);
        LOGGER.info("打印代缴凭证接口返回结果========="+JSON.toJSONString(o, true));
    }


    public String c19WithDrawalsNotice(String checkvalue,String encmsg ,String service) throws UnsupportedEncodingException {
        // 获取报文体签名
        String msgSign = URLDecoder.decode(checkvalue, "UTF-8");
        // 获取报文体
        String encMsg =  URLDecoder.decode(encmsg, "UTF-8");

        // 执行验签操作
        boolean verifyFlag = RSAUtil.rsaVerify(ShangHaiBankConstans.PUBKEY, msgSign, encMsg, ShangHaiBankConstans.ENCODING);
        // 验签通过的场合，查看报文内容
        String decMsg;
        if(verifyFlag){
            // 使用base64解码报文体
            decMsg = RSAUtil.decodeContent(encMsg, ShangHaiBankConstans.ENCODING);
            LOGGER.info("验签成功。报文内容是："+decMsg);
            return decMsg;
        } else {
            LOGGER.info("验签失败");
            return null;
        }
    }
}
