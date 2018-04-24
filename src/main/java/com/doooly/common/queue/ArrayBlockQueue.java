package com.doooly.common.queue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 
 * @author 赵清江
 * @date 2016年12月20日
 * @version 1.0
 */
public class ArrayBlockQueue<E> extends ArrayBlockingQueue<E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8686364680496091004L;

	public ArrayBlockQueue(int capacity) {
		super(capacity);
	}

}
