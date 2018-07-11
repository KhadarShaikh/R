package com.ojas.ra.rest.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.ra.domain.Menu;
import com.ojas.ra.domain.Role;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.MenuMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.MenuService;
import com.ojas.ra.service.RoleService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoEqualQuery;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/menu")
public class MenuResource {

	@Autowired
	private MenuService menuService;

	@Autowired
	private RoleService roleService;

	/**
	 * 
	 * @param menuMapper
	 * @return
	 */
	@POST
	@Path("/createMenu")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMenu(@RequestBody MenuMapper menuMapper) {
		Role role = null;
		ObjectId roleId;
		try {
			Menu menu = new Menu();
			String roleName = menuMapper.getModuleName();
			Map<String, Object> roleCondition = new HashMap<String, Object>();
			roleCondition.put("roleName", roleName);
			try {
				role = roleService.findOneByCondition(roleCondition);
			} catch (RAException e) {
				throw new RAException("Data Not Found !!");
			}
			roleId = role.get_id();
			BeanUtils.copyProperties(menuMapper, menu);
			menu.setRoleId(roleId);
			Boolean result = menuService.createMenu(menu);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "menu not created");
			}
			return new Response("Success", result, HttpStatus.OK, "menu created");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param menuMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/updateMenu/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMenu(@RequestBody MenuMapper menuMapper, @PathParam("id") ObjectId id) {
		try {
			Menu menu = new Menu();
			BeanUtils.copyProperties(menuMapper, menu);
			menu.set_id(id);
			menu.setRoleId(new ObjectId(menuMapper.getRoleId()));
			Boolean result = menuService.updateMenu(menu);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "menu not saved");
			}
			return new Response("Success", result, HttpStatus.OK, "menu saved");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GET
	@Path("/listMenus/{pageNo}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllMenus(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.ASC);
			List<Menu> list;
			List<MenuMapper> menuList;
			try {
				list = menuService.listMenu(sort, pageNo, pageSize);
				menuList = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = menuService.getCount(sort);
			int pages = menuService.getPages(sort, pageSize);
			if (menuList == null || menuList.size() == 0) {
				return new Response(menuList, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(menuList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	@GET
	@Path("/getOneById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOneById(@Context ServletContext context, @PathParam("id") ObjectId id) {
		Menu menu = null;
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			MenuMapper menuMapper;
			try {
				menu = menuService.getOneByCondition(condition);
				menuMapper = convertDomainToMappar(menu);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (menuMapper == null) {
				return new Response("Failed", menuMapper, HttpStatus.CONFLICT, "No records found");
			}
			return new Response("Success", menuMapper, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/deleteById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteById(@Context ServletContext context, @PathParam("id") ObjectId id) {
		try {
			Boolean result = menuService.deleteMenuById(id);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "Id not found ");
			}
			return new Response("Success", result, HttpStatus.OK, "Successfully removed");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	@GET
	@Path("/findAllByCondition/{roleName}/{registrationType}/{pageNo}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllBycondtion(@Context ServletContext context, @PathParam("roleName") String roleName,
			@PathParam("registrationType") String registrationType, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		Role role = null;
		ObjectId roleId = null;
		List<Menu> menu = null;
		List<MenuMapper> menuList = null;
		int count = 0;
		int pages = 0;
		try {
			Map<String, Object> roleCondition = new HashMap<String, Object>();
			roleCondition.put("roleName", roleName);
			try {
				role = roleService.findOneByCondition(roleCondition);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (null != role) {
				roleId = role.get_id();
				MongoEqualQuery equal1 = new MongoEqualQuery();
				equal1.setEqualto(roleId);
				MongoEqualQuery equal2 = new MongoEqualQuery();
				equal2.setEqualto(registrationType);
				Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
				condition.put("roleId", equal1);
				condition.put("moduleName", equal2);
				MongoSortVO sort = new MongoSortVO();
				sort.setPrimaryKey("_id");
				sort.setPrimaryOrderBy(MongoOrderByEnum.ASC);
				try {
					menu = menuService.findAllByConditon(condition, sort, pageNo, pageSize);
				} catch (RAException e) {
					return new Response(HttpStatus.CONFLICT, "No records found");
				}
				if (menu != null) {
					menuList = convertDomainToMapperList(menu);
					count = menuService.getCount(sort);
					pages = menuService.getPages(sort, pageSize);
					if (menuList == null || menuList.size() == 0) {
						return new Response(menuList, pages, count, HttpStatus.CONFLICT, "No records found");
					}
				}
			}
			return new Response(menuList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param roleName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GET
	@Path("/findAllByRoleName/{roleName}/{pageNo}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllByRoleName(@Context ServletContext context, @PathParam("roleName") String roleName,
			@PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		Role role = null;
		ObjectId roleId;
		try {
			Map<String, Object> roleCondition = new HashMap<String, Object>();
			roleCondition.put("roleName", roleName);
			try {
				role = roleService.findOneByCondition(roleCondition);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			roleId = role.get_id();
			Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
			MongoSortVO mongoSortVo = new MongoSortVO();
			mongoSortVo.setPrimaryKey("_id");
			mongoSortVo.setPrimaryOrderBy(MongoOrderByEnum.ASC);
			MongoEqualQuery mongoEqualQuery = new MongoEqualQuery();
			mongoEqualQuery.setEqualto(roleId);
			condition.put("roleId", mongoEqualQuery);
			List<MenuMapper> menuMapperList;
			try {
				menuMapperList = convertDomainToMapperList(
						menuService.findAllByRoleId(condition, mongoSortVo, pageNo, pageSize));
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = menuService.getCount(mongoSortVo);
			int pages = menuService.getPages(mongoSortVo, pageSize);
			if (menuMapperList == null || menuMapperList.size() == 0) {
				return new Response(menuMapperList, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(menuMapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param roleId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GET
	@Path("/findAllByRoleId/{roleId}/{pageNo}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByRoleId(@PathParam("roleId") ObjectId roleId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		List<MenuMapper> menuMapperList = null;
		try {
			Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.ASC);
			MongoEqualQuery mongoEqualQuery = new MongoEqualQuery();
			mongoEqualQuery.setEqualto(roleId);
			condition.put("roleId", mongoEqualQuery);
			try {
				menuMapperList = convertDomainToMapperList(
						menuService.findAllByRoleId(condition, sort, pageNo, pageSize));
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = menuService.getCount(sort);
			int pages = menuService.getPages(sort, pageSize);
			if (menuMapperList == null || menuMapperList.size() == 0) {
				return new Response(menuMapperList, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(menuMapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param menus
	 * @return
	 */
	private List<MenuMapper> convertDomainToMapperList(List<Menu> menus) {
		try {
			List<MenuMapper> list = new ArrayList<MenuMapper>();
			for (Menu menu : menus) {
				MenuMapper menuMapper = new MenuMapper();
				BeanUtils.copyProperties(menu, menuMapper);
				menuMapper.set_id(menu.get_id().toString());
				menuMapper.setRoleId(menu.getRoleId().toString());
				list.add(menuMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param menu
	 * @return
	 */
	private MenuMapper convertDomainToMappar(Menu menu) {
		try {
			MenuMapper menuMapper = new MenuMapper();
			BeanUtils.copyProperties(menu, menuMapper);
			menuMapper.setRoleId(menu.getRoleId().toString());
			menuMapper.set_id(menu.get_id().toString());
			return menuMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

}
