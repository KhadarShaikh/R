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

import com.ojas.ra.domain.PrimarySkills;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.PrimarySkillsMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.PrimarySkillsService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/primarySkills")
public class PrimarySkillsResource {
	@Autowired
	private PrimarySkillsService primarySkillsService;

	/**
	 * 
	 * @param primarySkillsMapper
	 * @return
	 */
	@Path("/createPrimarySkills")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@RequestBody PrimarySkillsMapper primarySkillsMapper) {
		int count = 0;
		Boolean result = false;
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			for (PrimarySkills primSkills : primarySkillsService.getAll(sort)) {
				if (primSkills.getPrimarySkills().equalsIgnoreCase(primarySkillsMapper.getPrimarySkills()) == false) {
					count++;
				}
			}
			if (count == primarySkillsService.getAll(sort).size()) {
				PrimarySkills primarySkills = new PrimarySkills();
				BeanUtils.copyProperties(primarySkillsMapper, primarySkills);
				result = primarySkillsService.create(primarySkills);
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
	 * @param primarySkillsMapper
	 * @param id
	 * @return
	 */
	@Path("/updatePrimarySkills/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@RequestBody PrimarySkillsMapper primarySkillsMapper, @PathParam("id") ObjectId id) {

		try {
			PrimarySkills primarySkills = new PrimarySkills();
			BeanUtils.copyProperties(primarySkillsMapper, primarySkills);
			primarySkills.set_id(id);
			Boolean result = primarySkillsService.update(primarySkills);
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
	 */
	@Path("/listPrimarySkills/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAll(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<PrimarySkills> list;
			List<PrimarySkillsMapper> mapperList;
			try {
				list = primarySkillsService.getAll(sort, pageNo, pageSize);
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

	@Path("/listPrimarySkills")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getList(@Context ServletContext context) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<PrimarySkills> list;
			List<PrimarySkillsMapper> mapperList;
			try {
				list = primarySkillsService.getAll(sort);
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
	 * @param primarySkills
	 * @return
	 */
	public List<PrimarySkillsMapper> convertDomainToMapperList(List<PrimarySkills> primarySkills) {
		try {
			List<PrimarySkillsMapper> list = new ArrayList<PrimarySkillsMapper>();
			for (PrimarySkills primarySkillss : primarySkills) {
				PrimarySkillsMapper primarySkillsMapper = new PrimarySkillsMapper();
				BeanUtils.copyProperties(primarySkillss, primarySkillsMapper);
				primarySkillsMapper.set_id(primarySkillss.get_id().toString());

				list.add(primarySkillsMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param value
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
			PrimarySkills primarySkills;
			PrimarySkillsMapper mapper;
			try {
				primarySkills = primarySkillsService.getOneByCondition(condition);
				mapper = convertDomainToMappar(primarySkills);
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
	 * @param primarySkills
	 * @return
	 */
	private PrimarySkillsMapper convertDomainToMappar(PrimarySkills primarySkills) {
		try {
			PrimarySkillsMapper primarySkillsMapper = new PrimarySkillsMapper();
			BeanUtils.copyProperties(primarySkills, primarySkillsMapper);
			primarySkillsMapper.set_id(primarySkills.get_id().toString());

			return primarySkillsMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

}
