package com.doooly.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.doooly.common.dao.BaseDaoI;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

public class BaseServiceImpl<D extends BaseDaoI<T>,T>{
	@Resource
	private D dao;
	public T get(String id) {
		return dao.get(id);
	}

	public T get(T entity) {
		return dao.get(entity);
	}

	public List<T> getAll(T entity) {
		return dao.getAll(entity);
	}

	public int insert(T entity) {
		return dao.insert(entity);
	}

	public int update(T entity) {
		return dao.update(entity);
	}

	public int delete(T entity) {
		return dao.delete(entity);
	}

	public List<T> findPage(T entity, PageBounds page) {
		return dao.findPage(entity, page);
	}

}
