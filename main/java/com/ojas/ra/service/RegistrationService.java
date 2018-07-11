package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.ojas.ra.domain.Registration;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface RegistrationService {
	/**
	 * 
	 * @param registration
	 * @return
	 * @throws RAException
	 */
	public boolean create(Registration registration) throws RAException;

	/**
	 * 
	 * @param registration
	 * @return
	 * @throws RAException
	 */
	public boolean save(Registration registration) throws RAException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws RAException
	 */
	public boolean delete(ObjectId id) throws RAException;

	/**
	 * 
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Registration> getAllObjects(MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean removeByPrimaryId(String id);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public Registration findOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @return
	 * @throws RAException
	 */
	public List<Registration> findAllByCondition(Map<String, Object> condition, MongoSortVO sort) throws RAException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws RAException
	 */
	Registration findOneByPrimaryId(String value) throws RAException;

	/**
	 * 
	 * @param condition
	 * @param target
	 * @return
	 */
	public boolean updateMapByCondition(Map<String, Object> condition, Map<String, Object> target);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<Registration> getAllObjects(MongoSortVO sort);

	/**
	 * 
	 * @param nameOftheProperty
	 * @param valueOftheProperty
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Registration> advancedFindByCondition(String nameOftheProperty, String valueOftheProperty, MongoSortVO sort,
			int pageNo, int pageSize);

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
