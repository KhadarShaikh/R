package com.ojas.ra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.RegistrationDAO;
import com.ojas.ra.domain.Registration;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.RegistrationService;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	RegistrationDAO registrationDAO;

	@Autowired
	MongoDBClient mongoDBClient;

	@Override
	public boolean create(Registration registration) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);

			b = registrationDAO.insert(registration);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean save(Registration registration) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);

			b = registrationDAO.save(registration);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean delete(ObjectId id) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			b = registrationDAO.removeByCondition(condition);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Registration> findAllByCondition(Map<String, Object> condition, MongoSortVO sort) throws RAException {
		List<Registration> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);
			try {
				list = registrationDAO.findAllByCondition(condition, sort);
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
	public int getCount(MongoSortVO sort) {
		int count;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);
			count = registrationDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return count;
	}

	@Override
	public boolean removeByPrimaryId(String value) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);

			b = registrationDAO.removeByPrimaryId(value);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public Registration findOneByCondition(Map<String, Object> condition) throws RAException {
		Registration reg;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);
			try {
				reg = registrationDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return reg;
	}

	@Override
	public Registration findOneByPrimaryId(String value) throws RAException {
		Registration reg;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);
			try {
				reg = registrationDAO.findOneByPrimaryId(value);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return reg;
	}

	@Override
	public List<Registration> getAllObjects(MongoSortVO sort, int page, int size) throws RAException {
		List<Registration> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.getCollection("registration", db);
			registrationDAO.setPojo(new Registration());
			try {
				list = registrationDAO.getAllObjects(sort, page, size);
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
	public List<Registration> getAllObjects(MongoSortVO sort) throws RAException {
		List<Registration> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.getCollection("registration", db);
			registrationDAO.setPojo(new Registration());
			try {
				list = registrationDAO.getAllObjects(sort);
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

	@SuppressWarnings("unused")
	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		registrationDAO.setPojo(new Registration());
		return db;

	}

	@Override
	public boolean updateMapByCondition(Map<String, Object> condition, Map<String, Object> target) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);

			b = registrationDAO.updateMapByCondition(condition, target);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<Registration> advancedFindByCondition(String nameOftheProperty, String valueOftheProperty,
			MongoSortVO sort, int pageNo, int pageSize) {
		List<Registration> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);
			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put(nameOftheProperty, new BasicDBObject("$regex", valueOftheProperty).append("$options", "i"));
			try {
				list = registrationDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
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
	public int getPages(MongoSortVO sort, int pageSize) {
		int pages;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationDAO.setPojo(new Registration());
			registrationDAO.getCollection("registration", db);
			try {
				pages = registrationDAO.getPages(sort, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return pages;
	}
}