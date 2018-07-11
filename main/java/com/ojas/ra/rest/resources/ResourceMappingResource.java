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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.ra.domain.ResourceMapping;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.ResourceMappingMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.RequirementService;
import com.ojas.ra.service.ResourceMappingService;
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
@Path("/resourceMapping")
public class ResourceMappingResource {

	@Autowired
	ResourceMappingService resourceMappingService;
	@Autowired
	RequirementService requirementService;

	/**
	 * 
	 * @param resourceMappingMapper
	 * @return
	 */
	@Path("/createResourceMapping")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createResourceMapping(@RequestBody ResourceMappingMapper resourceMappingMapper) {
		try {
			ResourceMapping resourceMapping = new ResourceMapping();
			resourceMapping.setCompanyId(new ObjectId(resourceMappingMapper.getCompanyId()));
			resourceMapping.setRequirementId(new ObjectId(resourceMappingMapper.getRequirementId()));
			resourceMapping.setVendorId(new ObjectId(resourceMappingMapper.getVendorId()));
			resourceMapping.setResourceId(new ObjectId(resourceMappingMapper.getResourceId()));
			Boolean result = resourceMappingService.createResourceMapping(resourceMapping);
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
	 */
	@Path("/deleteResourceMapping/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteRequirement(@Context ServletContext context, @PathParam("id") ObjectId id) {
		try {
			Boolean result = resourceMappingService.deleteResourceMapping(id);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "not deleted");
			}
			return new Response("Success", result, HttpStatus.OK, "deleted");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param resourceMappingMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/updateResourceMapping/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateResourceMapping(@RequestBody ResourceMappingMapper resourceMappingMapper,
			@PathParam("id") ObjectId id) {
		try {
			ResourceMapping resourceMapping = new ResourceMapping();
			resourceMapping.set_id(id);
			resourceMapping.setCompanyId(new ObjectId(resourceMappingMapper.getCompanyId()));
			resourceMapping.setRequirementId(new ObjectId(resourceMappingMapper.getRequirementId()));
			resourceMapping.setResourceId(new ObjectId(resourceMappingMapper.getResourceId()));
			resourceMapping.setVendorId(new ObjectId(resourceMappingMapper.getVendorId()));
			Boolean result = resourceMappingService.updateResourceMapping(resourceMapping);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "not updated");
			}
			return new Response("Success", result, HttpStatus.OK, "updated");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param nameOftheProperty
	 * @param valueOftheProperty
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findAll/{nameOftheProperty}/{valueOftheProperty}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByCondition(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(valueOftheProperty);
			Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
			condition.put(nameOftheProperty, equal);
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<ResourceMapping> list;
			List<ResourceMappingMapper> mapperList;
			try {
				list = resourceMappingService.advancedFind(condition, sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceMappingService.getCount(sort);
			int pages = resourceMappingService.getPages(sort, pageSize);
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
	 * @param resourceMappings
	 * @return
	 */
	private List<ResourceMappingMapper> convertDomainToMapperList(List<ResourceMapping> resourceMappings) {
		try {
			List<ResourceMappingMapper> list = new ArrayList<ResourceMappingMapper>();
			for (ResourceMapping resourceMapping : resourceMappings) {
				ResourceMappingMapper resourceMappingMapper = new ResourceMappingMapper();
				resourceMappingMapper.set_id(resourceMapping.get_id().toString());
				resourceMappingMapper.setCompanyId(resourceMapping.getCompanyId().toString());
				resourceMappingMapper.setRequirementId(resourceMapping.getRequirementId().toString());
				resourceMappingMapper.setResourceId(resourceMapping.getResourceId().toString());
				resourceMappingMapper.setVendorId(resourceMapping.getVendorId().toString());
				list.add(resourceMappingMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
