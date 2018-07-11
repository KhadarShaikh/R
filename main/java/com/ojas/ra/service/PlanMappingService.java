package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.ojas.ra.domain.PlanMapping;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface PlanMappingService {
	/**
	 * 
	 * @param condition
	 * @return
	 */
	PlanMapping findOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws RAException
	 */
	List<PlanMapping> findOneRoleMappingByUserId(ObjectId id) throws RAException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws RAException
	 */
	public boolean removeByPrimaryId(String value) throws RAException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws RAException
	 */
	public boolean delete(ObjectId id) throws RAException;

	/**
	 * 
	 * @param condition
	 */
	void removeByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param planMapping
	 * @return
	 */
	boolean updatePlanMapping(PlanMapping planMapping);

	/**
	 * 
	 * @param planMapping
	 * @return
	 * @throws RAException
	 */
	boolean create(PlanMapping planMapping) throws RAException;

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<PlanMapping> advancedFind(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo,
			int pageSize);
}
