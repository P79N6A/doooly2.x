package com.doooly.business.activity;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * Created by john on 18/1/2.
 */
public interface EnrollService {

    public static int OPTION_ID = 1;

    public MessageDataBean signUp(JSONObject json);
}
