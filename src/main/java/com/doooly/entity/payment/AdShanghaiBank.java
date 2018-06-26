package com.doooly.entity.payment;

/**
 * 上海银行支持银行Entity
 * @author qing.zhang
 * @version 2018-05-28
 */
public class AdShanghaiBank {
	private Integer id;//主键
	private String accountBank;		// 上海银行支持银行行号
	private String accountName;		// 上海银行支持银行行名
	private String accountShortName;		// 上海银行支持银行行名简称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountShortName() {
        return accountShortName;
    }

    public void setAccountShortName(String accountShortName) {
        this.accountShortName = accountShortName;
    }
}