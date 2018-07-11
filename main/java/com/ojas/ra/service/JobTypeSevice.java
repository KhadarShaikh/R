package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.JobType;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface JobTypeSevice {
	/**
	 * 
	 * @param jobType
	 * @return
	 */
	public boolean createJobType(JobType jobType);

	/**
	 * 
	 * @param jobtype
	 * @return
	 */
	public boolean updateJobType(JobType jobtype);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<JobType> getAllJobType(MongoSortVO sort, int page, int size);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public JobType getOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<JobType> getAllJobType(MongoSortVO sort);

}
