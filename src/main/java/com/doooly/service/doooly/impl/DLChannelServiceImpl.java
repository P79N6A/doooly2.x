package com.doooly.service.doooly.impl;

import com.doooly.common.service.impl.BaseServiceImpl;
import com.doooly.dao.doooly.DLChannelMapper;
import com.doooly.entity.doooly.DLChannel;
import com.doooly.service.doooly.DLChannelServiceI;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DLChannelServiceImpl extends BaseServiceImpl<DLChannelMapper, DLChannel> implements DLChannelServiceI {
	@Resource
	private DLChannelMapper dlChannelDao;

}
