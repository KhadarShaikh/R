package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.ojas.ra.domain.ResourceMapping;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface ResourceMappingService {
	/**
	 * 
	 * @param rm
	 * @return
	 */
	public boolean createResourceMapping(ResourceMapping rm);

	/**
	 * 
	 * @param objId
	 * @return
	 */
	public boolean deleteResourceMapping(ObjectId objId);

	/**
	 * 
	 * @param rm
	 * @return
	 */
	public boolean updateResourceMapping(ResourceMapping rm);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<ResourceMapping> advancedFind(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int page,
			int size);

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
