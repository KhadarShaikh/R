package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.RegistrationType;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface RegistrationTypeService {
	/**
	 * 
	 * @param registrationType
	 * @return
	 */
	public boolean createRegistrationType(RegistrationType registrationType);

	/**
	 * 
	 * @param registrationType
	 * @return
	 */
	public boolean updateRegistrationType(RegistrationType registrationType);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<RegistrationType> getAllRegistrationType(MongoSortVO sort, int page, int size);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<RegistrationType> getAllRegistrationType(MongoSortVO sort);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public RegistrationType getOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @param pageSize
	 * @return
	 */
	public int getPages(MongoSortVO sort, int pageSize);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public int getCount(MongoSortVO sort);
}
