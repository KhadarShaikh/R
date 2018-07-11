package com.ojas.ra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.RequirementDAO;
import com.ojas.ra.domain.Requirement;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.RequirementService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;
import com.ojas.ra.util.MongoUtil;

/**
 * 
 * @author skkhadar
 *
 */
public class RequirementServiceImpl implements RequirementService {

	@Autowired
	RequirementDAO requirementDAO;

	@Autowired
	private MongoDBClient mongoDBClient;

	@Override
	public boolean createRequirement(Requirement requirement) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			b = requirementDAO.insert(requirement);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;

	}

	@Override
	public boolean post(Requirement requirement) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("postedResource", db);
			b = requirementDAO.insert(requirement);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveRequirement(Requirement requirement) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();
			requirementDAO.getCollection("requirement", db);
			requirementDAO.setPojo(new Requirement());
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", requirement.get_id());
			b = requirementDAO.updateMapByCondition(condition, MongoUtil.getObjectByDBObject(requirement));
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
			requirementDAO.getCollection("requirement", db);
			requirementDAO.setPojo(new Requirement());
			count = requirementDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return count;
	}

	@Override
	public boolean deleteRequirement(ObjectId objId) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();
			requirementDAO.getCollection("requirement", db);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", objId);

			b = requirementDAO.removeByCondition(condition);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();

			throw new RAException(e.getMessage());
		}
		return b;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Requirement> findAllByCondition(Map<String, Object> condition, MongoSortVO sort) throws RAException {
		List<Requirement> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			try {
				list = requirementDAO.findAllByCondition(condition, sort);
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
	public boolean removeByPrimaryId(String value) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);

			b = requirementDAO.removeByPrimaryId(value);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public Requirement findOneByCondition(Map<String, Object> condition) throws RAException {
		Requirement req;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			try {
				req = requirementDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return req;
	}

	@Override
	public Requirement findOneByPrimaryId(String value) throws RAException {
		Requirement req;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			try {
				req = requirementDAO.findOneByPrimaryId(value);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return req;
	}

	@Override
	public List<Requirement> getAllObjects(MongoSortVO sort, int page, int size) throws RAException {
		List<Requirement> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.getCollection("requirement", db);
			requirementDAO.setPojo(new Requirement());
			try {
				list = requirementDAO.getAllObjects(sort, page, size);
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
		requirementDAO.setPojo(new Requirement());
		return db;

	}

	@Override
	public List<Requirement> advancedFindByCondition(Map<String, MongoAdvancedQuery> requementMappingcondition,
			MongoSortVO sort, int pageNo, int pageSize) {
		List<Requirement> requirement = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			try {
				requirement = requirementDAO.advancedFindByCondition(requementMappingcondition, sort, pageNo, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}

		return requirement;

	}

	@Override
	public boolean updateMapByCondition(Map<String, Object> condition, Map<String, Object> target) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);

			b = requirementDAO.updateMapByCondition(condition, target);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<Requirement> getAllObjects(MongoSortVO sort) throws RAException {
		List<Requirement> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.getCollection("requirement", db);
			requirementDAO.setPojo(new Requirement());
			try {
				list = requirementDAO.getAllObjects(sort);
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
	public List<Requirement> advancedFindByCondition(String name, String value, MongoSortVO sort, int pageNo,
			int pageSize) {
		List<Requirement> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put(name, new BasicDBObject("$regex", value).append("$options", "i"));
			try {
				list = requirementDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
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
	public List<Requirement> findByCondition(Map<String, Object> requirementMappingcondition, MongoSortVO sort,
			int pageNo, int pageSize) {
		List<Requirement> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.getCollection("requirement", db);
			requirementDAO.setPojo(new Requirement());
			try {
				list = requirementDAO.findByCondition(requirementMappingcondition, sort, pageNo, pageSize);
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
	public List<Requirement> advancedFindByConditions(String skills, String jobCategory, String location,
			String experience, MongoSortVO sort, int pageNo, int pageSize) {
		List<Requirement> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);

			BasicDBObject regexQuery = new BasicDBObject();
			String[] s = skills.split(",");

			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			for (int i = 0; i < s.length; i++) {
				obj.add(new BasicDBObject("primarySkills",
						new BasicDBObject("$regex", ".*" + s[i] + ".*").append("$options", "i")));
			}
			if (!skills.equalsIgnoreCase("undefined")) {
				regexQuery.put("$or", obj);
			}
			if (!jobCategory.equalsIgnoreCase("undefined")) {
				regexQuery.put("jobCategory",
						new BasicDBObject("$regex", ".*" + jobCategory + ".*").append("$options", "i"));
			}
			if (!location.equalsIgnoreCase("undefined")) {
				regexQuery.put("jobLocation",
						new BasicDBObject("$regex", ".*" + location + ".*").append("$options", "i"));
			}
			if (!experience.equalsIgnoreCase("undefined")) {
				regexQuery.put("yearsOfExperience",
						new BasicDBObject("$regex", ".*" + experience + ".*").append("$options", "i"));
			}
			try {
				list = requirementDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
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
	public List<Requirement> advancedFindByConditions(String skills, String jobCategory, String location,
			String experience, String vendors, String budget, MongoSortVO sort, int pageNo, int pageSize) {
		List<Requirement> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);

			BasicDBObject regexQuery = new BasicDBObject();
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
						obje = new BasicDBObject("jobLocation",
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
			list = requirementDAO.getAllByRegex(sort, regexQuery, pageNo, pageSize);
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException("Data Not Found !!");
		}
		mongoDBClient.closeMongoClient();
		return list;
	}

	@Override
	public int getPages(MongoSortVO sort, int pageSize) {
		int pages;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.getCollection("requirement", db);
			requirementDAO.setPojo(new Requirement());
			try {
				pages = requirementDAO.getPages(sort, pageSize);
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
	public List<Requirement> advancedFindByCondition(String name, String value, MongoSortVO sort) {
		List<Requirement> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put(name, new BasicDBObject("$regex", value).append("$options", "i"));
			try {
				list = requirementDAO.getAllByRegex(sort, regexQuery);
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
	public List<Requirement> findAllByCondition(BasicDBObject andQuery, MongoSortVO sort, int pageNo, int pageSize) {
		List<Requirement> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			requirementDAO.setPojo(new Requirement());
			requirementDAO.getCollection("requirement", db);
			try {
				list = requirementDAO.findAllByCondition(andQuery, sort, pageNo, pageSize);
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