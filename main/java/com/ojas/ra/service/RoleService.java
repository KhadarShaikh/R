package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import com.ojas.ra.domain.Role;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface RoleService {
	/**
	 * 
	 * @param role
	 * @return
	 * @throws RAException
	 */
	boolean createRole(Role role) throws RAException;

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public Role findOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @param mapCondition
	 * @return
	 * @throws RAException
	 */
	public List<Role> getByMapObjects(MongoSortVO sort, int page, int size, Map<String, Object> mapCondition)
			throws RAException;

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Role getByRoleName(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo, int pageSize);
}
