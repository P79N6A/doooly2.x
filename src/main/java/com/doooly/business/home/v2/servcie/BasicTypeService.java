package com.doooly.business.home.v2.servcie;

import com.doooly.entity.reachad.AdBasicType;

import java.util.List;

public interface BasicTypeService {
    List<AdBasicType> getFloors(String userId, Integer type, Integer templateType);

    public void getAll(String str);
}
