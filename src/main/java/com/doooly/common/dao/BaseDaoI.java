package com.doooly.common.dao;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

public interface BaseDaoI<T> {
	/**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	T get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	T get(T entity);
	
	/**
	 * 查询数据列表，查询条件为非id的信息
	 * @param entity
	 * @return
	 */
	List<T> getAll(T entity);
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	int insert(T entity);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	int update(T entity);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param entity
	 * @return
	 */
	int delete(T entity);
	/**
	 * 分页查询
	 * @param entity
	 * @param page
	 * @return
	 */
	List<T> findPage(T entity, PageBounds page);
}
