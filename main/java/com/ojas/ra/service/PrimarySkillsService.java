package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.PrimarySkills;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface PrimarySkillsService {
	/**
	 * 
	 * @param primarySkills
	 * @return
	 */
	public boolean create(PrimarySkills primarySkills);

	/**
	 * 
	 * @param primarySkills
	 * @return
	 */
	public boolean update(PrimarySkills primarySkills);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<PrimarySkills> getAll(MongoSortVO sort, int page, int size);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public PrimarySkills getOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<PrimarySkills> getAll(MongoSortVO sort);

}
