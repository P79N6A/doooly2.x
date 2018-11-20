package com.doooly.dao.reachad;

import com.doooly.business.pay.bean.AdOrderSource;

public interface AdOrderSourceDao {

	int insert(AdOrderSource adOrderSource);

    AdOrderSource get(AdOrderSource adOrderSource);

}