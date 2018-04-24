package com.doooly.service.doooly.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.doooly.common.service.impl.BaseServiceImpl;
import com.doooly.dao.doooly.DLChannelMapper;
import com.doooly.entity.doooly.DLChannel;
import com.doooly.service.doooly.DLChannelServiceI;

@Service
public class DLChannelServiceImpl extends BaseServiceImpl<DLChannelMapper, DLChannel> implements DLChannelServiceI {
	@Resource
	private DLChannelMapper dlChannelDao;

}
