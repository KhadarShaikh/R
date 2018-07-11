package com.ojas.ra.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.RoleDAO;
import com.ojas.ra.domain.Role;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.RoleService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDAO roleDAO;

	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean createRole(Role role) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();
			roleDAO.getCollection("role", db);

			roleDAO.setPojo(new Role());
			roleDAO.getCollection("role", db);
			b = roleDAO.insert(role);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;

	}

	@Override
	public Role findOneByCondition(Map<String, Object> condition) throws RAException {
		Role role = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			roleDAO.setPojo(new Role());
			roleDAO.getCollection("role", db);
			try {
				role = roleDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return role;
	}

	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		roleDAO.setPojo(new Role());
		return db;

	}

	public List<Role> getByMapObjects(MongoSortVO sort, int page, int size, Map<String, Object> mapCondition)
			throws RAException {
		List<Role> roles = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			roleDAO.setPojo(new Role());
			roleDAO.getCollection("role", db);
			String mappedPojo = "roleMapping";
			try {
				roles = roleDAO.getByMapObjects(sort, page, size, mappedPojo, mapCondition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return roles;
	}

	@Override
	public Role getByRoleName(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo, int pageSize) {
		try {

		} catch (RAException e) {
			System.out.println(e);
		}
		return null;
	}

}
