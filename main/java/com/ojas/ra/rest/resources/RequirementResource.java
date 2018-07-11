package com.ojas.ra.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.mongodb.BasicDBObject;
import com.ojas.ra.domain.Registration;
import com.ojas.ra.domain.Requirement;

import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.RequirementMapper;
import com.ojas.ra.mapper.ResourceMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.RegistrationService;
import com.ojas.ra.service.RequirementService;
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
@Path("/requirement")
public class RequirementResource {

	@Autowired
	private RequirementService requirementService;

	@Autowired
	private RegistrationService registrationService;

	/**
	 * 
	 * @param requirementMapper
	 * @return
	 * @throws IOException
	 */
	@POST
	@Path("/createRequirement")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRequirement(@RequestBody RequirementMapper requirementMapper) throws IOException {
		try {
			Requirement requirement = new Requirement();
			BeanUtils.copyProperties(requirementMapper, requirement);
			requirement.setRegistrationId(new ObjectId(requirementMapper.getRegistrationId()));
			requirement.setStatus("Active");
			requirement.setPostedDate(new Date());
			requirement.setJobId("REQ-" + randomNumber());
			Boolean result = requirementService.createRequirement(requirement);
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
	 * @param requirementMapper
	 * @return
	 */
	@Path("/postedRequirement")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postedRequirement(@RequestBody RequirementMapper requirementMapper) {
		try {
			Requirement requirement = new Requirement();
			BeanUtils.copyProperties(requirementMapper, requirement);
			requirement.setStatus("Active");
			requirement.setRegistrationId(new ObjectId(requirementMapper.getRegistrationId()));
			Boolean result = requirementService.post(requirement);
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
	 * @param uploadFile
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@Path("/bulkUpload/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response bulkUpload(@FormDataParam("uploadFile") InputStream uploadFile, @PathParam("id") ObjectId id)
			throws IOException {
		boolean result = false;
		try {
			Requirement requirement = new Requirement();
			requirement.setRegistrationId(id);

			XSSFWorkbook workbook = new XSSFWorkbook(uploadFile);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() != 0) {
					requirement.setJobCategory(row.getCell(0).toString());
					requirement.setJobType(row.getCell(1).toString());
					requirement.setDescription(row.getCell(2).toString());
					requirement.setYearsOfExperience(row.getCell(3).toString());
					requirement.setPrimarySkills(row.getCell(4).toString());
					requirement.setResources(row.getCell(5).toString());
					requirement.setJoinWithin(row.getCell(6).toString());
					result = requirementService.createRequirement(requirement);
				}
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
	 * @param requiementMapper
	 * @return
	 */
	@Path("/saveRequirement")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveRequirement(@RequestBody RequirementMapper requiementMapper) {
		try {
			Requirement requirement = new Requirement();
			BeanUtils.copyProperties(requiementMapper, requirement);
			requirement.set_id(new ObjectId(requiementMapper.get_id()));
			requirement.setRegistrationId(new ObjectId(requiementMapper.getRegistrationId()));
			requirement.setPostedDate(new Date());
			Boolean result = requirementService.saveRequirement(requirement);
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
	 * @param id
	 * @return
	 */
	@Path("/deleteRequirement/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteRequirement(@Context ServletContext context, @PathParam("id") ObjectId id) {
		try {
			Boolean result = requirementService.deleteRequirement(id);
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
	 * @param context
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/listRequirements/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByCondition(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Requirement> list;
			List<RequirementMapper> mapperList;
			try {
				list = requirementService.getAllObjects(sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
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
	 * @param objectId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findRequirementsByRegistrationId/{registrationId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRequirementsByRegistrationId(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		Map<String,Map<String,Integer>> allLists =null;
		
		try {
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(new ObjectId(objectId));
			Map<String, MongoAdvancedQuery> requirementMappingcondition = new HashMap<String, MongoAdvancedQuery>();
			requirementMappingcondition.put("registrationId", equal);

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("registrationId");
			sort.setPrimaryOrderBy(MongoOrderByEnum.ASC);
			List<Requirement> requirementlist;
			List<RequirementMapper> mapperList;
			try {
				requirementlist = requirementService.advancedFindByCondition(requirementMappingcondition, sort, pageNo,
						pageSize);
				if(null!=requirementlist){
					allLists = allTLists(requirementlist);
					}
				mapperList = convertDomainToMapperList(requirementlist);
				
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new Response(mapperList, allLists,pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(mapperList, allLists, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	@Path("/testrequirement/{jobCategory}/{jobType}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response testrequirement(@Context ServletContext context, @PathParam("jobCategory") String jobCategory,
			@PathParam("jobType") String jobType, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoEqualQuery equal1 = new MongoEqualQuery();
			MongoEqualQuery equal2 = new MongoEqualQuery();

			equal1.setEqualto(jobCategory);
			equal2.setEqualto(jobType);

			Map<String, Object> requirementMappingcondition = new HashMap<String, Object>();
			requirementMappingcondition.put("jobCategory", equal1);
			requirementMappingcondition.put("jobType", equal2);

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("registrationId");
			sort.setPrimaryOrderBy(MongoOrderByEnum.ASC);
			List<Requirement> requirementlist;
			List<RequirementMapper> mapperList;
			try {
				requirementlist = requirementService.findAllByCondition(requirementMappingcondition, sort);
				mapperList = convertDomainToMapperList(requirementlist);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new Response(mapperList, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(mapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	@Path("/findSowRequirementsByRegId/{registrationId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findSowRequirementsByRegistrationId(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		Map<String,Map<String,Integer>> allLists=null;
   		try {
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(new ObjectId(objectId));
			Map<String, MongoAdvancedQuery> requirementMappingcondition = new HashMap<String, MongoAdvancedQuery>();
			requirementMappingcondition.put("registrationId", equal);

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("registrationId");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Requirement> requirementlist;
			List<RequirementMapper> mapperList;
			try {
				requirementlist = requirementService.getAllObjects(sort);
				if(null!=requirementlist)
					allLists = allTLists(requirementlist);
				mapperList = convertDomainToMapperList(requirementlist);
				if(null!=mapperList)
					mapperList = addSowStatus(mapperList, objectId);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList,allLists, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList,allLists,pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}

	}

	/**
	 * 
	 * @param context
	 * @param objectId
	 * @param primarySkills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findRequirementsBySearch/{registrationId}/{primarySkills}/{jobCategory}/{location}/{experience}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRequirementsBySearch(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("primarySkills") String primarySkills,
			@PathParam("jobCategory") String jobCategory, @PathParam("location") String location,
			@PathParam("experience") String experience, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
	Map<String,Map<String,Integer>>	allLists= null;
		try {

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			String[] ar = primarySkills.split(",");
			Arrays.sort(ar);
			int i = 0;
			StringBuilder values = new StringBuilder();
			for (String string : ar) {
				values.append(string);
				i++;
				if (i != ar.length) {
					values.append(",");
				}
			}
			String ps = values.toString();
			List<Requirement> requirementlist = null;
			List<Requirement> list = null;
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> c = new ArrayList<BasicDBObject>();
			c.add(new BasicDBObject("primarySkills", ps));
			c.add(new BasicDBObject("jobCategory", jobCategory));
			c.add(new BasicDBObject("jobLocation", location));
			c.add(new BasicDBObject("yearsOfExperience", experience));
			c.add(new BasicDBObject("status", "Active"));
			andQuery.put("$and", c);
			try {
				requirementlist = requirementService.findAllByCondition(andQuery, sort, pageNo, pageSize);
				list = new ArrayList<Requirement>();
			} catch (RAException e) {
				e.getMessage();
			}
			if (null != requirementlist)
				for (Requirement requirement : requirementlist) {
					if (requirement.getRegistrationId().toString().equalsIgnoreCase(objectId)) {
						list.add(requirement);
					}
				}
			if(null!=list){
				allLists = allTLists(list);
				}
			List<RequirementMapper> mapperList = convertDomainToMapperList(list);
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
			if (null == mapperList || mapperList.size() == 0) {
				return new Response(mapperList, allLists, pages, count, HttpStatus.OK, "No records found");
			}
			return new Response(mapperList, allLists, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param primarySkills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findRequirementsBySearch/{primarySkills}/{jobCategory}/{location}/{experience}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRequirementsBySearchs(@Context ServletContext context,
			@PathParam("primarySkills") String primarySkills, @PathParam("jobCategory") String jobCategory,
			@PathParam("location") String location, @PathParam("experience") String experience,
			@PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) { 
		
		Map<String,Map<String,Integer>> allLists = null;
		try {

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			String[] ar = primarySkills.split(",");
			Arrays.sort(ar);
			int i = 0;
			StringBuilder values = new StringBuilder();
			for (String string : ar) {
				values.append(string);
				i++;
				if (i != ar.length) {
					values.append(",");
				}
			}
			String ps = values.toString();
			List<Requirement> requirementlist = null;
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> c = new ArrayList<BasicDBObject>();
			c.add(new BasicDBObject("primarySkills", ps));
			c.add(new BasicDBObject("jobCategory", jobCategory));
			c.add(new BasicDBObject("jobLocation", location));
			c.add(new BasicDBObject("yearsOfExperience", experience));
			c.add(new BasicDBObject("status", "Active"));
			andQuery.put("$and", c);
			try {
				requirementlist = requirementService.findAllByCondition(andQuery, sort, pageNo, pageSize);

			} catch (RAException e) {
				e.getMessage();
			}
			if(null!=requirementlist){
				allLists = allTLists(requirementlist);
			}
			List<RequirementMapper> mapperList = convertDomainToMapperList(requirementlist);
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
			if (null == mapperList || mapperList.size() == 0) {
				return new Response(mapperList, allLists, pages, count, HttpStatus.OK, "No records found");
			}
			return new Response(mapperList, allLists, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param objectId
	 * @param primarySkills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param vendors
	 * @param budget
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findRequirementsBySearchs/{registrationId}/{primarySkills}/{jobCategory}/{location}/{experience}/{vendors}/{budget}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRequirementsBySearchs(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("primarySkills") String primarySkills,
			@PathParam("jobCategory") String jobCategory, @PathParam("location") String location,
			@PathParam("experience") String experience, @PathParam("vendors") String vendors,
			@PathParam("budget") String budget, @PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			String[] ar = primarySkills.split(",");
			Arrays.sort(ar);
			int i = 0;
			StringBuilder values = new StringBuilder();
			for (String string : ar) {
				values.append(string);
				i++;
				if (i != ar.length) {
					values.append(",");
				}
			}
			String ps = values.toString();
			List<Requirement> requirementlist = null;
			List<Requirement> list = null;
			List<RequirementMapper> mapperList = null;
			try {
				requirementlist = requirementService.advancedFindByConditions(ps, jobCategory, location, experience,
						vendors, budget, sort, pageNo, pageSize);
				list = new ArrayList<Requirement>();
			} catch (RAException e) {
				e.getMessage();
			}
			if (null != requirementlist) {
				for (Requirement requirement : requirementlist) {
					if (requirement.getRegistrationId().toString().equalsIgnoreCase(objectId)) {
						list.add(requirement);
					}
				}
				mapperList = convertDomainToMapperList(list);
			}
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
			List<RequirementMapper> mapperList1 = null;
			if (null == mapperList || mapperList.size() == 0) {
				mapperList1 = optionalList(objectId);
				return new Response(mapperList1, pages, count, HttpStatus.OK, "No records found");
			}
			return new Response(mapperList, pages, count, HttpStatus.OK, "No records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param value
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findRequirementsByBudget/{budget}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRequirementsByBudget(@Context ServletContext context, @PathParam("budget") String value,
			@PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			String name = "budget";
			List<Requirement> requirementlist;
			List<RequirementMapper> mapperList;
			try {
				requirementlist = requirementService.advancedFindByCondition(name, value, sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(requirementlist);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
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
	 * @param id
	 * @return
	 */
	@Path("/removeByPrimaryId/{id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeByPrimaryId(@Context ServletContext context, @PathParam("id") String id) {
		try {
			Boolean result = requirementService.removeByPrimaryId(id);
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
			Requirement requirement;
			RequirementMapper mapper;
			try {
				requirement = requirementService.findOneByCondition(condition);
				mapper = convertDomainToMappar(requirement);
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
			Requirement req;
			RequirementMapper mapper;
			try {
				req = requirementService.findOneByCondition(condition);
				mapper = convertDomainToMappar(req);
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
	 * @param nameOftheProperty
	 * @param valueOftheProperty
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findOneAllByCondition/{nameOftheProperty}/{valueOftheProperty}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOneAllByCondition(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Requirement> requirementlist;
			List<RequirementMapper> mapperList;
			try {
				requirementlist = requirementService.advancedFindByCondition(nameOftheProperty, valueOftheProperty,
						sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(requirementlist);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
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
	 * @param requirementMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/inactiveOrActive/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inactiveOrActive(@RequestBody RequirementMapper requirementMapper, @PathParam("id") ObjectId id) {
		try {
			Requirement requirement = new Requirement();
			BeanUtils.copyProperties(requirementMapper, requirement);
			requirement.set_id(id);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Map<String, Object> target = new HashMap<String, Object>();
			target.put("status", requirement.getStatus());

			Boolean result = requirementService.updateMapByCondition(condition, target);
			if (result == false) {
				return new Response("Failed", HttpStatus.CONFLICT);
			}
			return new Response("Success", HttpStatus.OK);

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	@Path("/listRequirements")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAll(@Context ServletContext context) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Requirement> list;
			List<RequirementMapper> mapperList;
			try {
				list = requirementService.getAllObjects(sort);
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
	 * @param primarySkills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findRequirementsByFilter/{vendorRegId}/{primarySkills}/{jobCategory}/{location}/{experience}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRequirementsByFilter(@Context ServletContext context,
			@PathParam("vendorRegId") String vendorRegId, @PathParam("primarySkills") String primarySkills,
			@PathParam("jobCategory") String jobCategory, @PathParam("location") String location,
			@PathParam("experience") String experience, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			String[] ar = primarySkills.split(",");
			Arrays.sort(ar);
			int i = 0;
			StringBuilder values = new StringBuilder();
			for (String string : ar) {
				values.append(string);
				i++;
				if (i != ar.length) {
					values.append(",");
				}
			}
			String skills = values.toString();
			List<Requirement> requirementlist = null;
			try {
				requirementlist = requirementService.advancedFindByConditions(skills, jobCategory, location, experience,
						sort, pageNo, pageSize);
			} catch (RAException e) {
				e.getMessage();
			}
			int pages = requirementService.getPages(sort, pageSize);
			int count = requirementService.getCount(sort);

			List<RequirementMapper> list = null;
			try {
				list = convertDomainToMapperList(requirementlist);
			} catch (RAException e) {
				e.getMessage();
			}
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", new ObjectId(vendorRegId));
			Registration reg = null;
			try {
				reg = registrationService.findOneByCondition(condition);
			} catch (RAException e) {
				e.getMessage();
			}
			if (null != list) {
				for (RequirementMapper requirementMapper : list) {
					if (null != reg.getSowUser()) {
						String[] sow = reg.getSowUser().split(",");
						for (String regId : sow) {

							if (regId.equals(requirementMapper.getRegistrationId())) {
								requirementMapper.setIsSowUser("Yes");
							} else {
								requirementMapper.setIsSowUser("No");
							}
						}
					} else {
						requirementMapper.setIsSowUser("No");
					}
				}
			}
			List<RequirementMapper> mapperList1 = null;
			if (null == list || list.size() == 0) {
				mapperList1 = optionalList(vendorRegId);
				return new Response(mapperList1, pages, count, HttpStatus.OK, "No records found");
			}
			return new Response(list, pages, count, HttpStatus.OK, "No records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param primarySkills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param vendors
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findRequirementsByFilter/{primarySkills}/{jobCategory}/{location}/{experience}/{vendors}/{budget}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRequirementsByFilters(@Context ServletContext context,
			@PathParam("primarySkills") String primarySkills, @PathParam("jobCategory") String jobCategory,
			@PathParam("location") String location, @PathParam("experience") String experience,
			@PathParam("vendors") String vendors, @PathParam("budget") String budget, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			String[] ar = primarySkills.split(",");
			Arrays.sort(ar);
			int i = 0;
			StringBuilder values = new StringBuilder();
			for (String string : ar) {
				values.append(string);
				i++;
				if (i != ar.length) {
					values.append(",");
				}
			}
			String skills = values.toString();
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("companyName", vendors);
			Registration registration = null;
			try {
				registration = registrationService.findOneByCondition(condition);
			} catch (RAException e) {
				e.getMessage();
			}
			MongoEqualQuery equal = new MongoEqualQuery();
			try {
				equal.setEqualto(registration.get_id());
			} catch (NullPointerException e) {
				e.getMessage();
			}
			Map<String, MongoAdvancedQuery> condition1 = new HashMap<String, MongoAdvancedQuery>();
			condition1.put("registrationId", equal);
			List<Requirement> list = null;
			try {
				list = requirementService.advancedFindByCondition(condition1, sort, pageNo, pageSize);
			} catch (RAException e) {
				e.getMessage();
			}
			int count = requirementService.getCount(sort);
			int pages = requirementService.getPages(sort, pageSize);
			List<Requirement> requirementlist = null;
			try {
				requirementlist = requirementService.advancedFindByConditions(skills, jobCategory, location, experience,
						vendors, budget, sort, pageNo, pageSize);
			} catch (RAException e) {
				e.getMessage();
			}

			List<RequirementMapper> list1;
			try {
				requirementlist.addAll(list);
				list1 = convertDomainToMapperList(requirementlist);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			List<RequirementMapper> mapperList1 = null;
			if (null == list1 || list1.size() == 0) {
				mapperList1 = optionalList(registration.get_id().toString());
				return new Response(mapperList1, pages, count, HttpStatus.OK, "No records found");
			}
			return new Response(list1, pages, count, HttpStatus.OK, "No records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}



	/**
	 * 
	 * @param context
	 * @return
	 */
	@Path("/allCustomers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response allCustomers(@Context ServletContext context) {
		Set<String> vendorSet = new LinkedHashSet<String>();
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Requirement> list;
			try {
				list = requirementService.getAllObjects(sort);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			for (Requirement requirement : list) {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("_id", requirement.getRegistrationId());
				Registration reg;
				try {
					reg = registrationService.findOneByCondition(condition);
					vendorSet.add(reg.getCompanyName());
				} catch (RAException e) {
					return new Response(HttpStatus.CONFLICT, "No records found");
				}
				if (list == null || list.size() == 0) {
					return new Response(vendorSet, HttpStatus.CONFLICT, "failed");
				}
				return new Response(vendorSet, HttpStatus.OK, "success");
			}
		} catch (RAException e) {
			e.getMessage();
		}

		return null;
	}
	
	// sow user
		public List<RequirementMapper> addSowStatus(List<RequirementMapper> requirementMapperList, String objectId) {

			Registration regstration = null;
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", new ObjectId(objectId));
			try {
				regstration = registrationService.findOneByCondition(condition);
			} catch (RAException e) {
				e.getMessage();
			}

			if (null != requirementMapperList) {
				for (RequirementMapper requirementMapper: requirementMapperList) {
					if (null != regstration.getSowUser()) {
						String[] sow = regstration.getSowUser().split(",");
						for (String regId : sow) {
							if (regId.equals(requirementMapper.getRegistrationId())) {
								requirementMapper.setIsSowUser("Yes");
							} else {
								requirementMapper.setIsSowUser("No");
							}
						}
					} else {
						requirementMapper.setIsSowUser("No");
					}
				}
			}
			return requirementMapperList;
		}

		// All List New Method;

		public Map<String, Map<String, Integer>> allTLists(List<Requirement> requirementList) {

			Map<String, Integer> skillsMap = new HashMap<String, Integer>();
			Map<String, Integer> yearsOfExperiencMap = new HashMap<String, Integer>();
			Map<String, Integer> jobCategoryMap = new HashMap<String, Integer>();
			Map<String, Integer> budgetMap = new HashMap<String, Integer>();
			Map<String, Integer> locationMap = new HashMap<String, Integer>();
			Map<String, Integer> customerMap = new HashMap<String, Integer>();

			Map<String, Map<String, Integer>> allMap = new HashMap<String, Map<String, Integer>>();

			for (Requirement requirement : requirementList) {
				String[] primarySkills = requirement.getPrimarySkills().split(",");
				for (String primaryskill : primarySkills) {
					if (skillsMap.containsKey(primaryskill)) {
						skillsMap.put(primaryskill, skillsMap.get(primaryskill) + 1);
					} else {
						if (null != primaryskill)
							skillsMap.put(primaryskill, 1);
					}
				}

				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("_id", requirement.getRegistrationId());
				String companyName;

				companyName = registrationService.findOneByCondition(condition).getCompanyName();

				if (companyName != null) {
					if (customerMap.containsKey(companyName)) {

						customerMap.put(companyName, customerMap.get(companyName) + 1);
					} else {
						if (null != companyName)
							customerMap.put(companyName, 1);
					}
				}
				if (yearsOfExperiencMap.containsKey(requirement.getYearsOfExperience())) {
					yearsOfExperiencMap.put(requirement.getYearsOfExperience(),
							yearsOfExperiencMap.get(requirement.getYearsOfExperience()) + 1);
				} else {
					if (null != requirement.getYearsOfExperience())
						yearsOfExperiencMap.put(requirement.getYearsOfExperience(), 1);
				}

				if (jobCategoryMap.containsKey(requirement.getJobCategory())) {
					jobCategoryMap.put(requirement.getJobCategory(), jobCategoryMap.get(requirement.getJobCategory()) + 1);
				} else {
					if (null != requirement.getJobCategory())
						jobCategoryMap.put(requirement.getJobCategory(), 1);
				}

				if (budgetMap.containsKey(requirement.getBudget())) {
					budgetMap.put(requirement.getBudget(), budgetMap.get(requirement.getBudget()) + 1);
				} else {
					if (null != requirement.getBudget())
						budgetMap.put(requirement.getBudget(), 1);
				}

				if (locationMap.containsKey(requirement.getJobLocation())) {
					locationMap.put(requirement.getJobLocation(),
							locationMap.get(requirement.getJobLocation()) + 1);
				} else {
					if (null != requirement.getJobLocation())
						locationMap.put(requirement.getJobLocation(), 1);
				}
			}
			allMap.put("skillsMap", skillsMap);
			allMap.put("customerMap", customerMap);
			allMap.put("yearsOfExperiencMap", yearsOfExperiencMap);
			allMap.put("jobCategoryMap", jobCategoryMap);
			allMap.put("budgetMap", budgetMap);
			allMap.put("locationMap", locationMap);

			return allMap;
		}
	
	

	/**
	 * 
	 * @param requirements
	 * @return
	 */
	private List<RequirementMapper> convertDomainToMapperList(List<Requirement> requirements) {
		try {
			List<RequirementMapper> list = new ArrayList<RequirementMapper>();
			for (Requirement requirement : requirements) {
				RequirementMapper requirementMapper = new RequirementMapper();
				BeanUtils.copyProperties(requirement, requirementMapper);
				requirementMapper.set_id(requirement.get_id().toString());

				if (null != requirement.getPostedDate()) {

					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YY");
					requirementMapper.setPostedDate(sdf.format(requirement.getPostedDate()));
					Calendar calender = Calendar.getInstance();
					Date date1 = calender.getTime();
					Date date2 = requirement.getPostedDate();

					Long noOfDays = date1.getTime() - date2.getTime();
					noOfDays = TimeUnit.DAYS.convert(noOfDays, TimeUnit.MILLISECONDS);
					if (noOfDays <= 3) {
						requirementMapper.setJob("hot");

					} else {
						requirementMapper.setJob("premium");
					}

				}

				if (null != requirement.getRegistrationId()) {
					requirementMapper.setRegistrationId(requirement.getRegistrationId().toString());
				}
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("_id", requirement.getRegistrationId());
				try {
					Registration reg = registrationService.findOneByCondition(condition);
					if (null != reg)
						requirementMapper.setCompanyName(reg.getCompanyName());
					else
						throw new RAException("Data Not Found !!");
				} catch (RAException e) {
					throw new RAException("Data Not Found !!");
				}
				list.add(requirementMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param requirements
	 * @return
	 */
	private RequirementMapper convertDomainToMappar(Requirement requirements) {
		try {
			RequirementMapper requirementMapper = new RequirementMapper();
			BeanUtils.copyProperties(requirements, requirementMapper);
			requirementMapper.set_id(requirements.get_id().toString());
			if (null != requirements.getRegistrationId()) {
				requirementMapper.setRegistrationId(requirements.getRegistrationId().toString());
			}
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", requirements.getRegistrationId());
			try {
				Registration reg = registrationService.findOneByCondition(condition);
				if (null != reg)
					requirementMapper.setCompanyName(reg.getCompanyName());
				else
					throw new RAException("Data Not Found !!");
			} catch (RAException e) {
				throw new RAException("Data Not Found !!");
			}
			return requirementMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	public String randomNumber() {
		UUID refIdGen = UUID.randomUUID();

		long hi = refIdGen.getMostSignificantBits();
		long lo = refIdGen.getLeastSignificantBits();

		byte[] bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();

		BigInteger bigInteger = new BigInteger(bytes);
		String referenceNumber = bigInteger.toString();

		String refNumber = (referenceNumber.substring(2, 7));
		return refNumber;
	}

	public List<RequirementMapper> optionalList(String objectId) {
		List<Requirement> requirementlist1 = null;
		List<RequirementMapper> mapperList1 = null;
		MongoSortVO sort = new MongoSortVO();
		sort.setPrimaryKey("_id");
		sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
		MongoEqualQuery equal = new MongoEqualQuery();
		equal.setEqualto(new ObjectId(objectId));
		Map<String, MongoAdvancedQuery> requirementMappingcondition = new HashMap<String, MongoAdvancedQuery>();
		requirementMappingcondition.put("registrationId", equal);
		try {
			requirementlist1 = requirementService.getAllObjects(sort);
			mapperList1 = convertDomainToMapperList(requirementlist1);
		} catch (RAException e) {
			e.getMessage();
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("_id", new ObjectId(objectId));
		Registration reg = null;
		try {
			reg = registrationService.findOneByCondition(condition);
		} catch (RAException e) {
			e.getMessage();
		}
		if (null != mapperList1) {
			for (RequirementMapper requirementMapper : mapperList1) {
				if (null != reg.getSowUser()) {
					String[] sow = reg.getSowUser().split(",");
					for (String regId : sow) {
						if (regId.equals(requirementMapper.getRegistrationId())) {
							requirementMapper.setIsSowUser("Yes");
						} else {
							requirementMapper.setIsSowUser("No");
						}
					}
				} else {
					requirementMapper.setIsSowUser("No");
				}
			}
		}
		return mapperList1;
	}
}
