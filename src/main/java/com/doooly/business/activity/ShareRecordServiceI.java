package com.doooly.business.activity;

      	import com.alibaba.fastjson.JSONObject;
        import com.doooly.dto.activity.ActivityOrderReq;
        import com.doooly.dto.activity.ActivityOrderRes;
        import com.doooly.dto.common.MessageDataBean;

/**
 *
 * @author yangwenwei
 * @date 2016年12月16日
 * @version 1.0
 */
public interface ShareRecordServiceI {

	MessageDataBean isSetShareRecord(String userId, String telephone);

	MessageDataBean sendByShareRecord(String userId, String telephone, String telephoneUserId);


}
