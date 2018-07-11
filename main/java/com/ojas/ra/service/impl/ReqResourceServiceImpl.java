package com.ojas.ra.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.ReqResourceDAO;
import com.ojas.ra.dao.ResourceDAO;
import com.ojas.ra.domain.RequestResources;
import com.ojas.ra.domain.Resource;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.ReqResourceService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class ReqResourceServiceImpl implements ReqResourceService {

	@Autowired
	private ReqResourceDAO reqResourceDAO;
	@Autowired
	private ResourceDAO resourceDAO;
	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean create(RequestResources requestResource) {
		boolean b;
		try {
			DB db = initializeDB();

			reqResourceDAO.setPojo(new RequestResources());
			reqResourceDAO.getCollection("requestedResource", db);
			b = reqResourceDAO.insert(requestResource);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		reqResourceDAO.setPojo(new RequestResources());
		return db;

	}

	@Override
	public List<Resource> getByMapObjects(MongoSortVO sort, int pageNo, int pageSize, Map<String, Object> condition) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			String mappedPojo = "requestedResource";
			try {
				list = resourceDAO.getByMapObjects(sort, pageNo, pageSize, mappedPojo, condition);
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
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);

			count = resourceDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return count;
	}

	@Override
	public int getPages(MongoSortVO sort, int pageSize) {
		int pages;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			try {
				pages = resourceDAO.getPages(sort, pageSize);
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

	@Override
	public List<RequestResources> advancedFindByCondition(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort,
			int pageNo, int pageSize) {
		List<RequestResources> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			reqResourceDAO.setPojo(new RequestResources());
			reqResourceDAO.getCollection("requestedResource", db);
			try {
				list = reqResourceDAO.advancedFindByCondition(condition, sort, pageNo, pageSize);
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
