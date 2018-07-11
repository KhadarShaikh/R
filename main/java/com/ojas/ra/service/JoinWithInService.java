package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.JoinWithIn;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface JoinWithInService {
	/**
	 * 
	 * @param joinWithIn
	 * @return
	 */
	public boolean createJoinWithIn(JoinWithIn joinWithIn);

	/**
	 * 
	 * @param joinWithIn
	 * @return
	 */
	public boolean updateJoinWithIn(JoinWithIn joinWithIn);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<JoinWithIn> getAllJoinWithIn(MongoSortVO sort, int page, int size);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public JoinWithIn getOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<JoinWithIn> getAllJoinWithIn(MongoSortVO sort);

	/**
	 * 
	 * @param sort
	 * @param pageSize
	 * @return
	 */
	public int getPages(MongoSortVO sort, int pageSize);

	public int getCount(MongoSortVO sort);


}
