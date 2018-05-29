package com.doooly.business.familyInviteApp;

import com.alibaba.fastjson.JSONObject;

public interface AdInvitationService {

	JSONObject getInvitationDetail(JSONObject jsonReq);

	JSONObject getGroupInfo(String userId);

	JSONObject saveUserNotActive(String famMobile, String invitationCode,
			Integer invitationType, String famPassword, String name,String channel);

	JSONObject getMyFamilyInfo(String userId);

}
