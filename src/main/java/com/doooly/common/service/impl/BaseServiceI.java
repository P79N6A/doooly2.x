package com.doooly.common.service.impl;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

public interface BaseServiceI<D extends BaseDaoI<T>,T>{
	public T get(String id);

	public T get(T entity);

	public List<T> getAll(T entity) ;

	public int insert(T entity) ;

	public int update(T entity) ;

	public int delete(T entity) ;

	public List<T> findPage(T entity, PageBounds page) ;

}
