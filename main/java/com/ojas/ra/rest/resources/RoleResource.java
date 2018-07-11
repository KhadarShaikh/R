package com.ojas.ra.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.ra.domain.Role;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.RoleMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.RoleService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/role")
public class RoleResource {

	@Autowired
	private RoleService roleService;

	/**
	 * 
	 * @param role
	 * @return
	 */
	@POST
	@Path("/createRole")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRole(@RequestBody Role role) {
		try {
			Boolean result = roleService.createRole(role);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "not created");
			}
			return new Response("Success", result, HttpStatus.OK, "created");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param roleName
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Path("/findRoleByName/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOneByPrimaryId(@Context ServletContext context, @PathParam("name") String roleName)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("roleName", roleName);
			Role role;
			RoleMapper mapper;
			try {
				role = roleService.findOneByCondition(condition);
				mapper = convertDomainToMappar(role);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (mapper == null) {
				return new Response("Failed", mapper, HttpStatus.CONFLICT, "No record found");
			}
			return new Response("Success", mapper, HttpStatus.OK, "record found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param id
	 * @param page
	 * @param size
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Path("/findRolesByRoleMapping/{id}/{page}/{size}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRolesByRoleMapping(@Context ServletContext context, @PathParam("id") String id,
			@PathParam("page") int page, @PathParam("size") int size)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("user_id", new ObjectId(id));
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Role> role;
			List<RoleMapper> mapperList;
			try {
				role = roleService.getByMapObjects(sort, size, page, condition);
				mapperList = convertDomainToMapperList(role);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (mapperList == null || mapperList.size() == 0) {
				return new Response("Failed", mapperList, HttpStatus.CONFLICT, "No records found");
			}
			return new Response("Success", mapperList, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param roleMappers
	 * @return
	 */
	private List<RoleMapper> convertDomainToMapperList(List<Role> roleMappers) {
		try {
			List<RoleMapper> list = new ArrayList<RoleMapper>();
			for (Role roleMapping : roleMappers) {
				RoleMapper roleMapper = new RoleMapper();
				BeanUtils.copyProperties(roleMapping, roleMapper);
				roleMapper.set_id(roleMapping.get_id().toString());

				list.add(roleMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	private RoleMapper convertDomainToMappar(Role role) {
		try {
			RoleMapper roleMapper = new RoleMapper();
			BeanUtils.copyProperties(role, roleMapper);
			roleMapper.set_id(role.get_id().toString());

			return roleMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
