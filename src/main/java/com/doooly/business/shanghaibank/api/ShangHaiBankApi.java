package com.doooly.business.shanghaibank.api;

import com.doooly.business.shanghaibank.api.impl.PDFAPI;
import com.doooly.business.shanghaibank.api.impl.VirAccountApi;
import com.doooly.business.shanghaibank.constants.ShangHaiBankConstans;

/**
 * @Description: 上海银行api接口
 * @author: qing.zhang
 * @date: 2018-05-25
 */
public interface ShangHaiBankApi {

    String host = ShangHaiBankConstans.HOST;
    int port = ShangHaiBankConstans.PORT;
    String customer = ShangHaiBankConstans.CUSTOMER;
    String key = ShangHaiBankConstans.KEY;
    String pfxPath = ShangHaiBankConstans.PFXPATH;
    String pfxPwd = ShangHaiBankConstans.PFXPWD;

    //static SMSAPI smsapi = new SMSAPI(host, port, customer, key, pfxPath, pfxPwd);
    //public static AccountApi accountApi = new AccountApi(host, port, customer, key, pfxPath, pfxPwd);
    //static TransactionSingleApi singleApi = new TransactionSingleApi(host, port, customer, key, pfxPath, pfxPwd);
    //static UpdateInfoApi updateInfoApi = new UpdateInfoApi(host, port, customer, key, pfxPath, pfxPwd);
    //static IntelDepositApi intelDepositApi = new IntelDepositApi(host, port, customer, key, pfxPath, pfxPwd);
    //static BaoLiApi baoLiApi = new BaoLiApi(host, port, customer, key, pfxPath, pfxPwd);
    //static YjlFundApi yjlFundApi = new YjlFundApi(host, port, customer, key, pfxPath, pfxPwd);
    //static GfFundApi gfFundApi = new GfFundApi(host, port, customer, key, pfxPath, pfxPwd);
    //static HtfFundApi htfFundApi = new HtfFundApi(host, port, customer, key, pfxPath, pfxPwd);
    static PDFAPI pdfApi = new PDFAPI(host, port, customer, key, pfxPath, pfxPwd);
    static VirAccountApi virAccountApi = new VirAccountApi(host, port, customer, key, pfxPath, pfxPwd);
    //static FundStoreApi fundStoreApi = new FundStoreApi(host, port, customer, key, pfxPath, pfxPwd);
    //static UnionPayLoanApi unionPayLoanApi = new UnionPayLoanApi(host, port, customer, key, pfxPath, pfxPwd);
}
