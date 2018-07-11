package com.ojas.ra.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.ojas.ra.domain.Menu;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
public interface MenuService {
	/**
	 * 
	 * @param menu
	 * @return
	 */
	public boolean createMenu(Menu menu);

	/**
	 * 
	 * @param menu
	 * @return
	 */
	public boolean updateMenu(Menu menu);

	/**
	 * 
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Menu> listMenu(MongoSortVO sort, int page, int size);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public Menu getOneByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteMenuById(ObjectId id);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Menu> findAllByRoleId(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo,
			int pageSize);

	/**
	 * 
	 * @param condition
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */

	public List<Menu> findAllByConditon(Map<String, MongoAdvancedQuery> condition, MongoSortVO sort, int pageNo,
			int pageSize);

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public int getCount(MongoSortVO sort);

	/**
	 * 
	 * @param sort
	 * @param pageSize
	 * @return
	 */
	public int getPages(MongoSortVO sort, int pageSize);

}
