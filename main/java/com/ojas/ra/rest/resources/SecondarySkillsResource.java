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
import javax.ws.rs.PUT;
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

import com.ojas.ra.domain.SecondarySkills;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.SecondarySkillsMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.SecondatySkillsService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/secondarySkills")
public class SecondarySkillsResource {
	@Autowired
	private SecondatySkillsService secondarySkillsService;

	/**
	 * 
	 * @param secondarySkillsMapper
	 * @return
	 */
	@Path("/createSecondarySkills")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createJobType(@RequestBody SecondarySkillsMapper secondarySkillsMapper) {
		Boolean result = false;
		int count = 0;
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);

			for (SecondarySkills secondarySkills : secondarySkillsService.getAll(sort)) {
				if (secondarySkills.getSecondarySkills()
						.equalsIgnoreCase(secondarySkillsMapper.getSecondarySkills()) == false) {
					count++;
				}
			}
			try {
				if (count == secondarySkillsService.getAll(sort).size()) {
					SecondarySkills secondarySkills = new SecondarySkills();
					BeanUtils.copyProperties(secondarySkillsMapper, secondarySkills);
					result = secondarySkillsService.create(secondarySkills);
				}
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
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
	 * @param secondarySkillsMapper
	 * @param id
	 * @return
	 */
	@Path("/updateSecondarySkill/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@RequestBody SecondarySkillsMapper secondarySkillsMapper, @PathParam("id") ObjectId id) {

		try {
			SecondarySkills secondarySkills = new SecondarySkills();
			BeanUtils.copyProperties(secondarySkillsMapper, secondarySkills);
			secondarySkills.set_id(id);
			Boolean result = secondarySkillsService.update(secondarySkills);
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
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @return
	 */

	@Path("/listSecondarySkills/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAll(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<SecondarySkills> list;
			List<SecondarySkillsMapper> mapperList;
			try {
				list = secondarySkillsService.getAll(sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(list);
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
	 * @param context
	 * @param value
	 * @return
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Path("/findOneByPrimaryId/{value}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOneByPrimaryId(@Context ServletContext context, @PathParam("value") ObjectId value)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", value);
			SecondarySkills secondarySkills;
			SecondarySkillsMapper mapper;
			try {
				secondarySkills = secondarySkillsService.getOneByCondition(condition);
				mapper = convertDomainToMappar(secondarySkills);
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
	 * @param secondarySkills
	 * @return
	 */
	public List<SecondarySkillsMapper> convertDomainToMapperList(List<SecondarySkills> secondarySkills) {
		try {
			List<SecondarySkillsMapper> list = new ArrayList<SecondarySkillsMapper>();
			for (SecondarySkills secondarySkillss : secondarySkills) {
				SecondarySkillsMapper secondarySkillsMapper = new SecondarySkillsMapper();
				BeanUtils.copyProperties(secondarySkillss, secondarySkillsMapper);
				secondarySkillsMapper.set_id(secondarySkillss.get_id().toString());
				list.add(secondarySkillsMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param secondarySkills
	 * @return
	 */
	private SecondarySkillsMapper convertDomainToMappar(SecondarySkills secondarySkills) {
		try {
			SecondarySkillsMapper secondarySkillsMapper = new SecondarySkillsMapper();
			BeanUtils.copyProperties(secondarySkills, secondarySkillsMapper);
			secondarySkillsMapper.set_id(secondarySkills.get_id().toString());

			return secondarySkillsMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
