package com.ojas.ra.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.ProposedResourceDAO;
import com.ojas.ra.domain.ProposedResource;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.ProposedResourceService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class ProposedResourceServiceeImpl implements ProposedResourceService {

	@Autowired
	private ProposedResourceDAO proposedResourceDAO;
	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean create(ProposedResource proposedResource) {
		boolean b;
		try {
			DB db = initializeDB();

			proposedResourceDAO.setPojo(new ProposedResource());
			proposedResourceDAO.getCollection("proposedResource", db);
			b = proposedResourceDAO.insert(proposedResource);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;

	}

	@Override
	public List<ProposedResource> advancedFindByCondition(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort,
			int pageNo, int pageSize) {
		List<ProposedResource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			proposedResourceDAO.setPojo(new ProposedResource());
			proposedResourceDAO.getCollection("proposedResource", db);
			try {
				list = proposedResourceDAO.advancedFindByCondition(condition, sort, pageNo, pageSize);
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
		proposedResourceDAO.setPojo(new ProposedResource());
		return db;

	}

	@Override
	public int getPages(MongoSortVO sort, int pageSize) {
		int pages;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			proposedResourceDAO.setPojo(new ProposedResource());
			proposedResourceDAO.getCollection("proposedResource", db);
			try {
				pages = proposedResourceDAO.getPages(sort, pageSize);
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
