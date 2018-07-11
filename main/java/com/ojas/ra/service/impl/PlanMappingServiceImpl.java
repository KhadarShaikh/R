package com.ojas.ra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.PlanMappingDAO;
import com.ojas.ra.domain.PlanMapping;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.PlanMappingService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoEqualQuery;
import com.ojas.ra.util.MongoSortVO;
import com.ojas.ra.util.MongoUtil;

/**
 * 
 * @author skkhadar
 *
 */
public class PlanMappingServiceImpl implements PlanMappingService {
	@Autowired
	PlanMappingDAO planMappingDAO;

	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean create(PlanMapping planMapping) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();
			planMappingDAO.setPojo(new PlanMapping());
			planMappingDAO.getCollection("planMapping", db);
			b = planMappingDAO.insert(planMapping);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;

	}

	@Override
	public PlanMapping findOneByCondition(Map<String, Object> condition) throws RAException {
		PlanMapping planMapping = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			planMappingDAO.setPojo(new PlanMapping());
			planMappingDAO.getCollection("planMapping", db);
			try {
				planMapping = planMappingDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return planMapping;
	}

	@Override
	public List<PlanMapping> findOneRoleMappingByUserId(ObjectId id) throws RAException {
		List<PlanMapping> planMapping = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			planMappingDAO.setPojo(new PlanMapping());
			planMappingDAO.getCollection("planMapping", db);

			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(id);
			Map<String, MongoAdvancedQuery> planMappingcondition = new HashMap<String, MongoAdvancedQuery>();
			planMappingcondition.put("user_id", equal);
			try {
				planMapping = planMappingDAO.advancedFindByCondition(planMappingcondition, null, 1, 10);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return planMapping;
	}

	@Override
	public boolean delete(ObjectId id) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			planMappingDAO.setPojo(new PlanMapping());
			planMappingDAO.getCollection("planMapping", db);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			b = planMappingDAO.removeByCondition(condition);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean removeByPrimaryId(String value) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			planMappingDAO.setPojo(new PlanMapping());
			planMappingDAO.getCollection("planMapping", db);
			b = planMappingDAO.removeByPrimaryId(value);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		planMappingDAO.setPojo(new PlanMapping());
		return db;

	}

	@Override
	public void removeByCondition(Map<String, Object> condition) {
		DB db = mongoDBClient.getReadMongoDB();
		planMappingDAO.setPojo(new PlanMapping());
		planMappingDAO.getCollection("planMapping", db);
		planMappingDAO.removeByCondition(condition);
		mongoDBClient.closeMongoClient();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean updatePlanMapping(PlanMapping planMapping) {
		boolean b;
		try {

			DB db = initializeDB();
			planMappingDAO.getCollection("planMapping", db);
			planMappingDAO.setPojo(new PlanMapping());
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", planMapping.get_id());
			b = planMappingDAO.updateMapByCondition(condition, MongoUtil.getObjectByDBObject(planMapping));
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<PlanMapping> advancedFind(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo,
			int pageSize) {
		List<PlanMapping> list;
		try {
			DB db = initializeDB();
			planMappingDAO.getCollection("planMapping", db);
			planMappingDAO.setPojo(new PlanMapping());
			try {
				list = planMappingDAO.advancedFindByCondition(condition, sort, pageNo, pageSize);
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

}
