package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.JobCategory;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface JobCategoryService {
	/**
	 * 
	 * @param jobCategory
	 * @return
	 */
	public boolean createJobCategory(JobCategory jobCategory);

	/**
	 * 
	 * @param jobCategory
	 * @return
	 */
	public boolean updateJobCategory(JobCategory jobCategory);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<JobCategory> getAllJobCategory(MongoSortVO sort, int page, int size);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public JobCategory getOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<JobCategory> getAllJobCategory(MongoSortVO sort);

	/**
	 * 
	 * @param sort
	 * @param pageSize
	 * @return
	 */
	public int getPages(MongoSortVO sort, int pageSize);

	public int getCount(MongoSortVO sort);

}
