package com.ojas.ra.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.JobCategoryDAO;
import com.ojas.ra.domain.JobCategory;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.JobCategoryService;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class JobCategoryServiceImpl implements JobCategoryService {
	@Autowired
	JobCategoryDAO jobCategoryDAO;
	@Autowired
	MongoDBClient mongoDBClient;

	@Override
	public boolean createJobCategory(JobCategory jobCategory) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			jobCategoryDAO.setPojo(new JobCategory());
			jobCategoryDAO.getCollection("jobCategory", db);

			b = jobCategoryDAO.insert(jobCategory);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean updateJobCategory(JobCategory jobCategory) {
		boolean b;
		try {

			DB db = mongoDBClient.getReadMongoDB();
			jobCategoryDAO.setPojo(new JobCategory());
			jobCategoryDAO.getCollection("jobCategory", db);

			b = jobCategoryDAO.save(jobCategory);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());

		}
		return b;
	}

	@Override
	public List<JobCategory> getAllJobCategory(MongoSortVO sort, int page, int size) {
		List<JobCategory> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			jobCategoryDAO.getCollection("jobCategory", db);
			jobCategoryDAO.setPojo(new JobCategory());
			try {
				list = jobCategoryDAO.getAllObjects(sort, page, size);
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
	public JobCategory getOneByCondition(Map<String, Object> condition) {
		JobCategory jobCategory;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			jobCategoryDAO.setPojo(new JobCategory());
			jobCategoryDAO.getCollection("jobCategory", db);
			try {
				jobCategory = jobCategoryDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return jobCategory;
	}

	public List<JobCategory> getAllJobCategory(MongoSortVO sort) {
		List<JobCategory> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			jobCategoryDAO.getCollection("jobCategory", db);
			jobCategoryDAO.setPojo(new JobCategory());
			try {
				list = jobCategoryDAO.getAllObjects(sort);
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
		int b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			jobCategoryDAO.setPojo(new JobCategory());
			jobCategoryDAO.getCollection("jobCategory", db);

			b = jobCategoryDAO.getCount(sort);
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
			jobCategoryDAO.setPojo(new JobCategory());
			jobCategoryDAO.getCollection("jobCategory", db);
			try {
				b = jobCategoryDAO.getPages(sort, pageSize);
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