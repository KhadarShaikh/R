package com.ojas.ra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.MenuDAO;
import com.ojas.ra.domain.Menu;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.MenuService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public class MenuServiceImpl implements MenuService {

	@Autowired
	MenuDAO menuDAO;
	@Autowired
	MongoDBClient mongoDBClient;

	@Override
	public boolean createMenu(Menu menu) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);

			b = menuDAO.insert(menu);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public boolean updateMenu(Menu menu) {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);

			b = menuDAO.save(menu);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<Menu> listMenu(MongoSortVO sort, int page, int size) {
		List<Menu> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);
			try {
				list = menuDAO.getAllObjects(sort, page, size);
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
	public Menu getOneByCondition(Map<String, Object> condition) {
		Menu menu;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);
			try {
				menu = menuDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return menu;
	}

	@Override
	public boolean deleteMenuById(ObjectId id) {
		boolean b;
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("_id", id);
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);

			b = menuDAO.removeByCondition(condition);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@Override
	public List<Menu> findAllByConditon(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo,
			int pageSize) {
		List<Menu> list = null;
		{
			try {
				DB db = mongoDBClient.getReadMongoDB();
				menuDAO.setPojo(new Menu());
				menuDAO.getCollection("menu", db);
				try {
					list = menuDAO.advancedFindByCondition(condition, sort, pageNo, pageSize);
				} catch (RAException e) {
					mongoDBClient.closeMongoClient();
					throw new RAException("Data Not Found !!");
				}
				mongoDBClient.closeMongoClient();
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException(e.getMessage());
			}
		}
		return list;
	}

	@Override
	public List<Menu> findAllByRoleId(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo,
			int pageSize) {
		List<Menu> list = null;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);
			try {
				list = menuDAO.advancedFindByCondition(condition, sort, pageNo, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			e.getMessage();
			mongoDBClient.closeMongoClient();
		}
		return list;
	}

	@Override
	public int getPages(MongoSortVO sort, int pageSize) {
		int pages = 0;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);
			try {
				pages = menuDAO.getPages(sort, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			e.getMessage();
			mongoDBClient.closeMongoClient();
		}
		return pages;
	}

	@Override
	public int getCount(MongoSortVO sort) {
		int count = 0;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			menuDAO.setPojo(new Menu());
			menuDAO.getCollection("menu", db);
			count = menuDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			e.getMessage();
			mongoDBClient.closeMongoClient();
		}
		return count;
	}
}
