package com.ojas.ra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.UserAccountStatus;
import com.ojas.ra.dao.AccessTokenDAO;
import com.ojas.ra.dao.RoleDAO;
import com.ojas.ra.dao.RoleMappingDAO;
import com.ojas.ra.dao.UserAccountDAO;
import com.ojas.ra.domain.AccessToken;
import com.ojas.ra.domain.Role;
import com.ojas.ra.domain.UserAccount;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.UserService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class UserServiceImpl implements UserService {

	@Autowired
	UserAccountDAO userAccountDAO;

	@Autowired
	AccessTokenDAO accessTokenDAO;

	@Autowired
	RoleMappingDAO roleMappingDAO;

	@Autowired
	RoleDAO roleDAO;

	@Autowired
	private MongoDBClient mongoDBClient;

	public Role getRole(String role) {
		return null;
	}

	public boolean create(UserAccount user) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			user.setStatus(UserAccountStatus.STATUS_DISABLED.name());
			b = userAccountDAO.insert(user);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	public void save(UserAccount user) throws RAException {
		try {

			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			userAccountDAO.save(user);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
	}

	@Override
	public boolean delete(ObjectId id) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			b = userAccountDAO.removeByCondition(condition);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	public UserAccount getByUsername(String username) throws RAException {
		UserAccount userAccount = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			Map<String, Object> condition = new HashMap<String, Object>();
			userAccountDAO.getCollection("userAccount", db);
			userAccountDAO.setPojo(new UserAccount());
			condition.put("username", username);
			try {
				userAccount = userAccountDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return userAccount;
	}

	@Override
	public int getCount(MongoSortVO sort) {
		int count;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			count = userAccountDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return count;
	}

	public AccessToken createAccessToken(UserAccount user) throws RAException {
		AccessToken accessToken = null;
		try {
			DB db = mongoDBClient.getWriteMongoDB();

			accessToken = new AccessToken(user.getUsername(), UUID.randomUUID().toString());
			accessTokenDAO.setPojo(accessToken);
			accessTokenDAO.getCollection("accessToken", db);
			accessTokenDAO.insert(accessToken);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return accessToken;
	}

	@Override
	public UserAccount loadUserByUsername(String arg0) throws RAException {
		UserAccount userAccount = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();

			userAccountDAO.setPojo(new UserAccount());
			Map<String, Object> condition = new HashMap<String, Object>();
			userAccountDAO.getCollection("userAccount", db);
			userAccountDAO.setPojo(new UserAccount());
			condition.put("username", arg0);
			try {
				userAccount = userAccountDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return userAccount;
	}

	@SuppressWarnings("deprecation")
	@Override
	public UserAccount getUserByToken(String arg0) throws RAException {
		UserAccount userAccount = null;
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("token", arg0);
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("expiry");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			DB db = mongoDBClient.getWriteMongoDB();
			accessTokenDAO.setPojo(new AccessToken());
			accessTokenDAO.getCollection("accessToken", db);
			String userName = null;
			List<AccessToken> list;
			try {
				list = accessTokenDAO.findAllByCondition(condition, sort);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			if (null != list && 0 < list.size()) {
				for (AccessToken pojo : list) {
					userName = pojo.getUserName();
					break;
				}
			}

			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);

			Map<String, Object> usercondition = new HashMap<String, Object>();
			usercondition.put("username", userName);
			try {
				userAccount = userAccountDAO.findOneByCondition(usercondition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return userAccount;
	}

	public boolean removeByPrimaryId(ObjectId value) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", value);
			b = userAccountDAO.removeByCondition(condition);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	public List<UserAccount> findByRegistrationId(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort,
			int pageNo, int pageSize) throws RAException {
		List<UserAccount> list = null;
		try {

			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			try {
				list = userAccountDAO.advancedFindByCondition(condition, sort, pageNo, pageSize);
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

	public UserAccount findOneByCondition(Map<String, Object> condition) throws RAException {
		UserAccount userAccount = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			try {
				userAccount = userAccountDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return userAccount;
	}

	@Override
	public List<UserAccount> getAllObjects(MongoSortVO sort, int pageNo, int pageSize) {
		List<UserAccount> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			try {
				list = userAccountDAO.getAllObjects(sort, pageNo, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
		}
		return list;
	}

	@Override
	public List<Role> getAllRoles(MongoSortVO sort, int pageNo, int pageSize) {
		List<Role> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			roleDAO.setPojo(new Role());
			roleDAO.getCollection("role", db);
			try {
				list = roleDAO.getAllObjects(sort, pageNo, pageSize);
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
	public boolean updateMapByCondition(Map<String, Object> condition, Map<String, Object> target) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);

			b = userAccountDAO.updateMapByCondition(condition, target);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<UserAccount> advancedFindByCondition(Map<String, MongoAdvancedQuery> requementMappingcondition,
			MongoSortVO sort, int pageNo, int pageSize) {
		List<UserAccount> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			try {
				list = userAccountDAO.advancedFindByCondition(requementMappingcondition, sort, pageNo, pageSize);
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
			userAccountDAO.setPojo(new UserAccount());
			userAccountDAO.getCollection("userAccount", db);
			try {
				pages = userAccountDAO.getPages(sort, pageSize);
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
