package com.ojas.ra.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.ResourceDAO;
import com.ojas.ra.domain.Resource;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.ResourceService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	ResourceDAO resourceDAO;

	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean createResource(Resource resource) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();

			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			b = resourceDAO.insert(resource);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean request(Resource resource) throws RAException {

		boolean b;
		try {
			DB db = initializeDB();

			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("requestedresource", db);
			b = resourceDAO.insert(resource);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;

	}

	@Override
	public boolean saveResource(Resource resource) {
		boolean b;
		try {
			DB db = initializeDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			b = resourceDAO.save(resource);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<Resource> advancedFindByCondition(String name, String value, MongoSortVO sort, int pageNo,
			int pageSize) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put(name, new BasicDBObject("$regex", value).append("$options", "i"));
			regexQuery.put("hardLock", new BasicDBObject("$regex", "No").append("$options", "i"));
			regexQuery.put("status", new BasicDBObject("$regex", "Active").append("$options", "i"));
			try {
				list = resourceDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
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
	public List<Resource> requestFindByCondition(Map<String, MongoAdvancedQuery> resourceMappingcondition,
			MongoSortVO sort, int pageNo, int pageSize) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("requestedresource", db);
			try {
				list = resourceDAO.advancedFindByCondition(resourceMappingcondition, sort, pageNo, pageSize);
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
	public List<Resource> getAllObjects(MongoSortVO sort, int page, int size) throws RAException {
		List<Resource> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.getCollection("resource", db);
			resourceDAO.setPojo(new Resource());
			try {
				list = resourceDAO.getAllObjects(sort, page, size);
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
	public Resource findOneByCondition(Map<String, Object> condition) throws RAException {
		Resource res;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			try {
				res = resourceDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return res;
	}

	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		resourceDAO.setPojo(new Resource());
		return db;

	}

	@Override
	public boolean updateMapByCondition(Map<String, Object> condition, Map<String, Object> target) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);

			b = resourceDAO.updateMapByCondition(condition, target);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<Resource> getByMapObjects(MongoSortVO sort, int pageNo, int pageSize, Map<String, Object> condition) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			String mappedPojo = "resourceMapping";
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
	public List<Resource> advancedFindByCondition(MongoSortVO sort, int pageNo, int pageSize) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put("hardLock", new BasicDBObject("$regex", "No").append("$options", "i"));
			regexQuery.put("status", new BasicDBObject("$regex", "Active").append("$options", "i"));
			try {
				list = resourceDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
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
	public List<Resource> advancedFindByConditions(String primarySkills, String jobCategory, String location,
			String experience, MongoSortVO sort, int pageNo, int pageSize) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);

			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put("hardLock", new BasicDBObject("$regex", "No").append("$options", "i"));
			regexQuery.put("status", new BasicDBObject("$regex", "Active").append("$options", "i"));

			String[] s = primarySkills.split(",");

			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			for (int i = 0; i < s.length; i++) {
				obj.add(new BasicDBObject("primarySkills",
						new BasicDBObject("$regex", ".*" + s[i] + ".*").append("$options", "i")));
			}
			if (!primarySkills.equalsIgnoreCase("undefined")) {
				regexQuery.put("$or", obj);
			}
			if (!jobCategory.equalsIgnoreCase("undefined")) {
				regexQuery.put("jobCategory",
						new BasicDBObject("$regex", ".*" + jobCategory + ".*").append("$options", "i"));
			}
			if (!location.equalsIgnoreCase("undefined")) {
				regexQuery.put("currentLocation",
						new BasicDBObject("$regex", ".*" + location + ".*").append("$options", "i"));
			}
			if (!experience.equalsIgnoreCase("undefined")) {
				regexQuery.put("yearsOfExperience",
						new BasicDBObject("$regex", ".*" + experience + ".*").append("$options", "i"));
			}
			try {
				list = resourceDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return list;
	}

	@Override
	public List<Resource> advancedFindByCondition(Map<String, MongoAdvancedQuery> resourceMappingcondition,
			MongoSortVO sort, int pageNo, int pageSize) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			try {
				list = resourceDAO.advancedFindByCondition(resourceMappingcondition, sort, pageNo, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				e.getMessage();
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			e.getMessage();
		}
		return list;
	}

	@Override
	public List<Resource> advancedFindByConditions(String skills, String jobCategory, String location,
			String experience, String vendors, String budget, MongoSortVO sort, int pageNo, int pageSize) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);

			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put("hardLock", new BasicDBObject("$regex", "No").append("$options", "i"));
			regexQuery.put("status", new BasicDBObject("$regex", "Active").append("$options", "i"));
			BasicDBObject obje = null;
			BasicDBList or = null;

			DBObject firstOr = null;
			DBObject secondOr = null;
			DBObject thirdOr = null;
			DBObject fourthOr = null;
			DBObject fifthOr = null;
			try {
				String[] skill = skills.split(",");
				String[] jobCat = jobCategory.split(",");
				String[] loc = location.split(",");
				String[] exp = experience.split(",");
				String[] bud = budget.split(",");

				if (!skills.equalsIgnoreCase("undefined")) {
					or = new BasicDBList();
					for (int i = 0; i < skill.length; i++) {
						obje = new BasicDBObject("primarySkills",
								new BasicDBObject("$regex", ".*" + skill[i] + ".*").append("$options", "i"));
						or.add(obje);
					}
					firstOr = new BasicDBObject("$or", or);
				}
				if (!jobCategory.equalsIgnoreCase("undefined")) {
					or = new BasicDBList();
					for (int i = 0; i < jobCat.length; i++) {
						obje = new BasicDBObject("jobCategory",
								new BasicDBObject("$regex", ".*" + jobCat[i] + ".*").append("$options", "i"));
						or.add(obje);
					}
					secondOr = new BasicDBObject("$or", or);
				}

				if (!location.equalsIgnoreCase("undefined")) {
					or = new BasicDBList();
					for (int i = 0; i < loc.length; i++) {
						obje = new BasicDBObject("currentLocation",
								new BasicDBObject("$regex", ".*" + loc[i] + ".*").append("$options", "i"));
						or.add(obje);
					}
					thirdOr = new BasicDBObject("$or", or);
				}

				if (!experience.equalsIgnoreCase("undefined")) {
					or = new BasicDBList();
					for (int i = 0; i < exp.length; i++) {
						obje = new BasicDBObject("yearsOfExperience",
								new BasicDBObject("$regex", ".*" + exp[i] + ".*").append("$options", "i"));
						or.add(obje);
					}
					fourthOr = new BasicDBObject("$or", or);
				}
				if (!budget.equalsIgnoreCase("undefined")) {
					or = new BasicDBList();
					for (int i = 0; i < bud.length; i++) {
						obje = new BasicDBObject("budget", new BasicDBObject("$regex", bud[i]).append("$options", "i"));
						or.add(obje);
					}
					fifthOr = new BasicDBObject("$or", or);
				}
			} catch (RAException e) {
				throw new RAException("Data Not Found !!");
			}
			BasicDBList andValues = new BasicDBList();
			if (null != firstOr) {
				andValues.add(firstOr);
			}
			if (null != secondOr) {
				andValues.add(secondOr);
			}
			if (null != thirdOr) {
				andValues.add(thirdOr);
			}
			if (null != fourthOr) {
				andValues.add(fourthOr);
			}
			if (null != fifthOr) {
				andValues.add(fifthOr);
			}
			regexQuery.put("$and", andValues);
			list = resourceDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException("Data Not Found !!");
		}
		return list;
	}

	@Override
	public List<Resource> getAllObjects(MongoSortVO sort) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			try {
				list = resourceDAO.getAllObjects(sort);
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
	public List<Resource> advancedFindByCondition(String name, String value, MongoSortVO sort) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put(name, new BasicDBObject("$regex", value).append("$options", "i"));
			regexQuery.put("hardLock", new BasicDBObject("$regex", "No").append("$options", "i"));
			regexQuery.put("status", new BasicDBObject("$regex", "Active").append("$options", "i"));
			try {
				list = resourceDAO.getAllByRegex(sort, regexQuery);
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

	@SuppressWarnings("deprecation")
	@Override
	public List<Resource> findAllByCondition(Map<String, Object> condition, MongoSortVO sort) {
		List<Resource> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.setPojo(new Resource());
			resourceDAO.getCollection("resource", db);
			try {
				list = resourceDAO.findAllByCondition(condition, sort);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return list;
	}

	@Override
	public List<Resource> findByCondition(Map<String, Object> condition, MongoSortVO sort, int pageNo, int pageSize) {
		List<Resource> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.getCollection("resource", db);
			resourceDAO.setPojo(new Resource());
			try {
				list = resourceDAO.findByCondition(condition, sort, pageNo, pageSize);
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
	public List<Resource> findAllByCondition(BasicDBObject condition, MongoSortVO sort, int pageNo,
			int pageSize) {
		List<Resource> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			resourceDAO.getCollection("resource", db);
			resourceDAO.setPojo(new Resource());
			try {
				list = resourceDAO.findAllByCondition(condition, sort, pageNo, pageSize);
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