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

import com.ojas.ra.domain.RegistrationType;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.RegistrationTypeMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.RegistrationTypeService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/registrationType")
public class RegistrationTypeResource {
	@Autowired
	private RegistrationTypeService registrationTypeService;

	/**
	 * 
	 * @param registrationTypeMapper
	 * @return
	 */
	@Path("/createRegistrationType")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRegistrationType(@RequestBody RegistrationTypeMapper registrationTypeMapper) {
		int count = 0;
		Boolean result = false;
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			try {
				for (RegistrationType registration : registrationTypeService.getAllRegistrationType(sort)) {
					if (registration.getRegistrationType()
							.equalsIgnoreCase(registrationTypeMapper.getRegistrationType()) == false) {
						count++;
					}
				}
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			try {
				if (count == registrationTypeService.getAllRegistrationType(sort).size()) {
					RegistrationType registrationType = new RegistrationType();
					BeanUtils.copyProperties(registrationTypeMapper, registrationType);
					result = registrationTypeService.createRegistrationType(registrationType);
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
	 * @param registrationTypeMapper
	 * @param id
	 * @return
	 */
	@Path("/updateRegistrationType/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateJobType(@RequestBody RegistrationTypeMapper registrationTypeMapper,
			@PathParam("id") ObjectId id) {

		try {
			RegistrationType registrationType = new RegistrationType();
			BeanUtils.copyProperties(registrationTypeMapper, registrationType);
			registrationType.set_id(id);
			Boolean result = registrationTypeService.updateRegistrationType(registrationType);
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
	@Path("/listRegistrationType/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAlltRegistrationType(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<RegistrationType> list;
			List<RegistrationTypeMapper> mapperList;
			try {
				list = registrationTypeService.getAllRegistrationType(sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = registrationTypeService.getCount(sort);
			int pages = registrationTypeService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new Response(mapperList, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(mapperList, pages, count, HttpStatus.OK, "records found");
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
			RegistrationType registrationType;
			RegistrationTypeMapper mapper;
			try {
				registrationType = registrationTypeService.getOneByCondition(condition);
				mapper = convertDomainToMappar(registrationType);
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
	 * @param registrationType
	 * @return
	 */
	private RegistrationTypeMapper convertDomainToMappar(RegistrationType registrationType) {
		try {
			RegistrationTypeMapper registrationTypeMapper = new RegistrationTypeMapper();
			BeanUtils.copyProperties(registrationType, registrationTypeMapper);
			registrationTypeMapper.set_id(registrationType.get_id().toString());

			return registrationTypeMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param registrationTypes
	 * @return
	 */
	public List<RegistrationTypeMapper> convertDomainToMapperList(List<RegistrationType> registrationTypes) {
		try {
			List<RegistrationTypeMapper> list = new ArrayList<RegistrationTypeMapper>();
			for (RegistrationType registrationType : registrationTypes) {
				RegistrationTypeMapper registrationTypeMapper = new RegistrationTypeMapper();
				BeanUtils.copyProperties(registrationType, registrationTypeMapper);
				registrationTypeMapper.set_id(registrationType.get_id().toString());

				list.add(registrationTypeMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
