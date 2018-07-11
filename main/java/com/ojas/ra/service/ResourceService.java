package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.ojas.ra.domain.Resource;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface ResourceService {
	/**
	 * 
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Resource> getAllObjects(MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public Resource findOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public boolean createResource(Resource resource);

	/**
	 * 
	 * @param condition
	 * @param target
	 * @return
	 */
	public boolean updateMapByCondition(Map<String, Object> condition, Map<String, Object> target);

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public boolean saveResource(Resource resource);

	/**
	 * 
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @param condition
	 * @return
	 */
	public List<Resource> getByMapObjects(MongoSortVO sort, int pageNo, int pageSize, Map<String, Object> condition);

	/**
	 * 
	 * @param resource
	 * @return
	 * @throws RAException
	 */
	boolean request(Resource resource) throws RAException;

	/**
	 * 
	 * @param resourceMappingcondition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Resource> requestFindByCondition(Map<String, MongoAdvancedQuery> resourceMappingcondition, MongoSortVO sort,
			int pageNo, int pageSize);

	/**
	 * 
	 * @param nameOftheProperty
	 * @param valueOftheProperty
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Resource> advancedFindByCondition(String nameOftheProperty, String valueOftheProperty, MongoSortVO sort,
			int pageNo, int pageSize);

	/**
	 * 
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Resource> advancedFindByCondition(MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param primarySkills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param vendors
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Resource> advancedFindByConditions(String primarySkills, String jobCategory, String location,
			String experience, MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param resourceMappingcondition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Resource> advancedFindByCondition(Map<String, MongoAdvancedQuery> resourceMappingcondition,
			MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param skills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param vendors
	 * @param sort
	 * @return
	 */
	public List<Resource> advancedFindByConditions(String skills, String jobCategory, String location,
			String experience, String vendors, String budget, MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public List<Resource> getAllObjects(MongoSortVO sort);

	/**
	 * 
	 * @param sort
	 * @param pageSize
	 * @return
	 */
	public int getPages(MongoSortVO sort, int pageSize);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @return
	 */
	public List<Resource> findAllByCondition(Map<String, Object> condition, MongoSortVO sort);

	/**
	 * 
	 * @param name
	 * @param value
	 * @param sort
	 * @return
	 */
	List<Resource> advancedFindByCondition(String name, String value, MongoSortVO sort);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public int getCount(MongoSortVO sort);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Resource> findByCondition(Map<String, Object> condition, MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Resource> findAllByCondition(BasicDBObject condition, MongoSortVO sort, int pageNo, int pageSize);

}
