package com.ojas.ra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.ResourceExperienceDAO;
import com.ojas.ra.domain.ResourceExperience;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.ResourceExperienceService;

/**
 * 
 * @author skkhadar
 *
 */
public class ResourceExperienceServiceImpl implements ResourceExperienceService {

	@Autowired
	private ResourceExperienceDAO resourceExperienceDAO;

	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean create(ResourceExperience res) {
		boolean b;
		try {
			DB db = initializeDB();

			resourceExperienceDAO.setPojo(new ResourceExperience());
			resourceExperienceDAO.getCollection("resourceExperience", db);
			b = resourceExperienceDAO.insert(res);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		resourceExperienceDAO.setPojo(new ResourceExperience());
		return db;

	}
}
