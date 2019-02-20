package com.doooly.common.meituan;

import java.util.Date;

/**
 * Created by Administrator on 2018/12/11.
 */
public class ContentParams {

    //时间戳
    protected long ts = new Date().getTime()/1000;

    //签名
    protected String sign = MeituanConstants.MSIGN;

    //企业在商企通的id
    protected int entId = MeituanConstants.ENT_ID;

    public long getTs() {
        return ts;
    }

    public String getSign() {
        return sign;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getEntId() {
        return entId;
    }

    public void setEntId(int entId) {
        this.entId = entId;
    }
}
