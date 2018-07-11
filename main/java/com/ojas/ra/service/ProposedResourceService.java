package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.ProposedResource;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface ProposedResourceService {
	/**
	 * 
	 * @param postRequirement
	 * @return
	 */
	boolean create(ProposedResource proposedResource);

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
	List<ProposedResource> advancedFindByCondition(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort,
			int pageNo, int pageSize);

}
