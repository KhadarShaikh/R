package com.ojas.ra.rest.resources;

import java.io.IOException;
import java.util.HashMap;
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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.ra.domain.RoleMapping;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.RoleMappingMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.RoleMappingService;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/rolemapper")
public class RoleMappingResource {

	@Autowired
	private RoleMappingService roleMappingService;

	/**
	 * 
	 * @param roleMapping
	 * @return
	 */
	@POST
	@Path("/createRoleMapping")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRoleMapping(@RequestBody RoleMapping roleMapping) {
		try {
			Boolean result = roleMappingService.createRoleMapping(roleMapping);
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
	 * @param id
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Path("/findRoleMappingByName/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOneByPrimaryId(@Context ServletContext context, @PathParam("id") String id)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			RoleMapping roleMapping;
			RoleMappingMapper mapper;
			try {
				roleMapping = roleMappingService.findOneByCondition(condition);
				mapper = convertDomainToMappar(roleMapping);
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
	 * @param roleMapping
	 * @return
	 */
	private RoleMappingMapper convertDomainToMappar(RoleMapping roleMapping) {
		try {
			RoleMappingMapper roleMappingMapper = new RoleMappingMapper();
			BeanUtils.copyProperties(roleMapping, roleMappingMapper);
			roleMappingMapper.set_id(roleMapping.get_id().toString());

			return roleMappingMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
