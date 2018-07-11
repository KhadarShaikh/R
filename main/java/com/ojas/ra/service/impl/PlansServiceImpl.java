package com.ojas.ra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.PlansDAO;
import com.ojas.ra.domain.Plans;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.PlansService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;
import com.ojas.ra.util.MongoUtil;

/**
 * 
 * @author skkhadar
 *
 */
public class PlansServiceImpl implements PlansService {

	@Autowired
	PlansDAO plansDAO;

	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean createPlans(Plans plans) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();

			plansDAO.setPojo(new Plans());
			plansDAO.getCollection("plans", db);
			b = plansDAO.insert(plans);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean updatePlans(Plans plans) throws RAException {
		boolean b;
		try {

			DB db = initializeDB();
			plansDAO.getCollection("plans", db);
			plansDAO.setPojo(new Plans());
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", plans.get_id());
			b = plansDAO.updateMapByCondition(condition, MongoUtil.getObjectByDBObject(plans));
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean deletePlansById(ObjectId ObjId) throws RAException {
		boolean b;
		try {

			DB db = initializeDB();
			plansDAO.getCollection("plans", db);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", ObjId);

			b = plansDAO.removeByCondition(condition);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public Plans getPlansByPrimaryId(Map<String, Object> condition) throws RAException {
		Plans plans;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			plansDAO.setPojo(new Plans());
			plansDAO.getCollection("plans", db);
			try {
				plans = plansDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return plans;
	}

	@Override
	public List<Plans> advancedFind(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int page, int size) {
		List<Plans> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			plansDAO.getCollection("plans", db);
			plansDAO.setPojo(new Plans());
			try {
				list = plansDAO.advancedFindByCondition(condition, sort, page, size);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return list;
	}

	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		plansDAO.setPojo(new Plans());
		return db;

	}

	@Override
	public List<Plans> getAllPlans(MongoSortVO sort, int page, int size) {
		List<Plans> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			plansDAO.getCollection("plans", db);
			plansDAO.setPojo(new Plans());
			try {
				list = plansDAO.getAllObjects(sort, page, size);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return list;
	}

	@Override
	public boolean updatePlans(Map<String, Object> condition, Map<String, Object> target) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			plansDAO.setPojo(new Plans());
			plansDAO.getCollection("plans", db);

			b = plansDAO.updateMapByCondition(condition, target);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public int getCount(MongoSortVO sort) {
		int b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			plansDAO.setPojo(new Plans());
			plansDAO.getCollection("plans", db);

			b = plansDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public int getPages(MongoSortVO sort, int pageSize) {
		int b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			plansDAO.setPojo(new Plans());
			plansDAO.getCollection("plans", db);
			try {
				b = plansDAO.getPages(sort, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}
}
