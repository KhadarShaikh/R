package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.RequestResources;
import com.ojas.ra.domain.Resource;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface ReqResourceService {
	/**
	 * 
	 * @param requestResource
	 * @return
	 */
	boolean create(RequestResources requestResource);

	/**
	 * 
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @param condition
	 * @return
	 */
	List<Resource> getByMapObjects(MongoSortVO sort, int pageNo, int pageSize, Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @param pageSize
	 * @return
	 */
	int getPages(MongoSortVO sort, int pageSize);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<RequestResources> advancedFindByCondition(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort,
			int pageNo, int pageSize);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	int getCount(MongoSortVO sort);

}
