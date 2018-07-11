package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.SecondarySkills;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface SecondatySkillsService {
	/**
	 * 
	 * @param secondarySkills
	 * @return
	 */
	public boolean create(SecondarySkills secondarySkills);

	/**
	 * 
	 * @param secondarySkills
	 * @return
	 */
	public boolean update(SecondarySkills secondarySkills);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<SecondarySkills> getAll(MongoSortVO sort, int page, int size);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public SecondarySkills getOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<SecondarySkills> getAll(MongoSortVO sort);

}
