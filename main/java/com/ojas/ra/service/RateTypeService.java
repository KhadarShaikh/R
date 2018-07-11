package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.RateType;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface RateTypeService {
	/**
	 * 
	 * @param sort
	 * @return
	 */
	List<RateType> getAllObjects(MongoSortVO sort);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	RateType findOneByCondition(Map<String, Object> condition);
}
