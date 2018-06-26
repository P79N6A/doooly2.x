package com.doooly.business.shanghaibank.service;


import com.doooly.dto.common.MessageDataBean;

/**
 * @Description: 上海银行提供接口
 * @author: qing.zhang
 * @date: 2018-05-25
 */
public interface ShangHaiBankService {

    MessageDataBean c19SCrVirSingleAccount(String cNName,Integer type,String businessId,Long groupId);//虚账户单要素开户

    MessageDataBean c19SCrVirSingleAccountStInq(String cNName);//单要素虚账户开户状态查询

    MessageDataBean c19VirAcctBlanceInq(String cNName);//查询虚账户的余额

    MessageDataBean c19VirWithDrawalsInq(String channelFlowNo, String eAcctNo, String amount,
                                    String usage);//虚账户提款

    MessageDataBean c19VirAmtFrozOrNot(String channelFlowNo, String eAcctNo,
                                  String txnType, String applyAmount, String platformSummary, String notes);//虚账户冻结或解冻

    MessageDataBean c19SingleCharge(String channelFlowNo, String eAcctNo, String amount,
                               String recvAccount, String recvAccountName, String recvAccountBank,
                               String usage, String platformSummary);//虚账户单笔代缴

    MessageDataBean c19VirSReTrigSer(String amount, String businessId, String groupId,
                                String type);//虚账户单笔代发

    MessageDataBean c19VirSReTrigSer(String channelFlowNo, String eAcctNo, String amount,
                                String payAccount, String payAccountName,
                                String usage, String platformSummary);//虚账户单笔代发

    MessageDataBean c19VirAcctDeleCharSttInq(String channelFlowNo,String channelId);//虚账户交易指令状态查询

    MessageDataBean c19VirAcctPrintReceipt(String eAcctNo,String startDate,String endDate,String channelFlowNo);//打印代缴凭证

    String c19WithDrawalsNotice(String checkvalue,String encmsg ,String service);//虚账户提款通知

    MessageDataBean c19SingleCharge(String businessId, String eAcctNo, String amount, String sign);
}
