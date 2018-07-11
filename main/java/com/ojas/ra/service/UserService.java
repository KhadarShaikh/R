package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.ojas.ra.domain.AccessToken;
import com.ojas.ra.domain.Role;
import com.ojas.ra.domain.UserAccount;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface UserService {
	/**
	 * 
	 * @param role
	 * @return
	 * @throws RAException
	 */
	public Role getRole(String role) throws RAException;

	/**
	 * 
	 * @param user
	 * @return
	 * @throws RAException
	 */
	public boolean create(UserAccount user) throws RAException;

	/**
	 * 
	 * @param user
	 * @throws RAException
	 */
	public void save(UserAccount user) throws RAException;

	/**
	 * 
	 * @param username
	 * @return
	 * @throws RAException
	 */
	public UserAccount getByUsername(String username) throws RAException;

	/**
	 * 
	 * @param user
	 * @return
	 * @throws RAException
	 */
	public AccessToken createAccessToken(UserAccount user) throws RAException;

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws RAException
	 */
	public UserAccount loadUserByUsername(String arg0) throws RAException;

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws RAException
	 */
	public UserAccount getUserByToken(String arg0) throws RAException;

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public UserAccount findOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws RAException
	 */
	public List<UserAccount> findByRegistrationId(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort,
			int pageNo, int pageSize) throws RAException;

	/**
	 * 
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<UserAccount> getAllObjects(MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Role> getAllRoles(MongoSortVO sort, int pageNo, int pageSize);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(ObjectId id);

	/**
	 * 
	 * @param condition1
	 * @param target1
	 * @return
	 */
	public boolean updateMapByCondition(Map<String, Object> condition1, Map<String, Object> target1);

	/**
	 * 
	 * @param requirementMappingcondition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<UserAccount> advancedFindByCondition(Map<String, MongoAdvancedQuery> requirementMappingcondition,
			MongoSortVO sort, int pageNo, int pageSize);

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
