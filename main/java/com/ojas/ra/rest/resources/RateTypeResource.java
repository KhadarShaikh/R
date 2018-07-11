
package com.ojas.ra.rest.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ojas.ra.domain.RateType;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.RateTypeMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.RateTypeService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Path("/rateType")
@Component
public class RateTypeResource {

	@Autowired
	private RateTypeService rateTypeService;

	/**
	 * 
	 * @param context
	 * @return
	 */
	@Path("/listExperience")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByCondition(@Context ServletContext context) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<RateType> list;
			List<RateTypeMapper> mapperList;
			try {
				list = rateTypeService.getAllObjects(sort);
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
	 * @param nameOftheProperty
	 * @param valueOftheProperty
	 * @return
	 */
	@Path("/findOneByCondition/{nameOftheProperty}/{valueOftheProperty}}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOneByCondition(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put(nameOftheProperty, valueOftheProperty);
			RateType rateType = rateTypeService.findOneByCondition(condition);
			RateTypeMapper mapper = convertDomainToMappar(rateType);
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
	 * @param rateTypes
	 * @return
	 */
	private List<RateTypeMapper> convertDomainToMapperList(List<RateType> rateTypes) {
		try {
			List<RateTypeMapper> list = new ArrayList<RateTypeMapper>();
			for (RateType rateType : rateTypes) {
				RateTypeMapper rateTypeMapper = new RateTypeMapper();
				BeanUtils.copyProperties(rateType, rateTypeMapper);
				rateTypeMapper.set_id(rateType.get_id().toString());
				list.add(rateTypeMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param rateType
	 * @return
	 */
	private RateTypeMapper convertDomainToMappar(RateType rateType) {
		try {
			RateTypeMapper rateTypeMapper = new RateTypeMapper();
			BeanUtils.copyProperties(rateType, rateTypeMapper);
			rateTypeMapper.set_id(rateType.get_id().toString());

			return rateTypeMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
