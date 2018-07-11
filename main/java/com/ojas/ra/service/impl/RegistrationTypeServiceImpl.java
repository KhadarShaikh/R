package com.ojas.ra.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.RegistrationTypeDAO;
import com.ojas.ra.domain.RegistrationType;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.RegistrationTypeService;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class RegistrationTypeServiceImpl implements RegistrationTypeService {
	@Autowired
	RegistrationTypeDAO registrationTypeDAO;
	@Autowired
	MongoDBClient mongoDBClient;

	@Override
	public boolean createRegistrationType(RegistrationType registrationType) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationTypeDAO.setPojo(new RegistrationType());
			registrationTypeDAO.getCollection("registrationType", db);

			b = registrationTypeDAO.insert(registrationType);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean updateRegistrationType(RegistrationType registrationType) {
		boolean b;
		try {

			DB db = mongoDBClient.getReadMongoDB();
			registrationTypeDAO.setPojo(new RegistrationType());
			registrationTypeDAO.getCollection("registrationType", db);
			b = registrationTypeDAO.save(registrationType);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public int getCount(MongoSortVO sort) {
		int count;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationTypeDAO.setPojo(new RegistrationType());
			registrationTypeDAO.getCollection("registrationType", db);

			count = registrationTypeDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return count;
	}

	@Override
	public List<RegistrationType> getAllRegistrationType(MongoSortVO sort, int page, int size) {
		List<RegistrationType> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationTypeDAO.getCollection("registrationType", db);
			registrationTypeDAO.setPojo(new RegistrationType());
			try {
				list = registrationTypeDAO.getAllObjects(sort, page, size);
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

	public List<RegistrationType> getAllRegistrationType(MongoSortVO sort) {
		List<RegistrationType> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationTypeDAO.getCollection("registrationType", db);
			registrationTypeDAO.setPojo(new RegistrationType());
			try {
				list = registrationTypeDAO.getAllObjects(sort);
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
	public RegistrationType getOneByCondition(Map<String, Object> condition) {
		RegistrationType type = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationTypeDAO.setPojo(new RegistrationType());
			registrationTypeDAO.getCollection("registrationType", db);
			try {
				type = registrationTypeDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return type;
	}

	@Override
	public int getPages(MongoSortVO sort, int pageSize) {
		int pages;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			registrationTypeDAO.setPojo(new RegistrationType());
			registrationTypeDAO.getCollection("registrationType", db);
			try {
				pages = registrationTypeDAO.getPages(sort, pageSize);
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
