package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.ojas.ra.domain.RoleMapping;
import com.ojas.ra.exception.RAException;

/**
 * 
 * @author skkhadar
 *
 */
public interface RoleMappingService {
	/**
	 * 
	 * @param roleMapping
	 * @return
	 * @throws RAException
	 */
	boolean createRoleMapping(RoleMapping roleMapping) throws RAException;

	/**
	 * 
	 * @param condition
	 * @return
	 * @throws RAException
	 */
	RoleMapping findOneByCondition(Map<String, Object> condition) throws RAException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws RAException
	 */
	List<RoleMapping> findOneRoleMappingByUserId(ObjectId id) throws RAException;

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

}
