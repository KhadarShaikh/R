package com.ojas.ra.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
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
import com.ojas.ra.domain.Resource;
import com.ojas.ra.domain.ResourceExperience;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.ResourceMapper;
import com.ojas.ra.service.RegistrationService;
import com.ojas.ra.service.ResourceExperienceService;
import com.ojas.ra.service.ResourceService;
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
@Path("/resource")
public class ResourceResource {

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ResourceExperienceService resourceExpService;
	@Autowired
	private RegistrationService registrationService;

	/**
	 * 
	 * @param resourceMapper
	 * @return
	 */
	@Path("/createResource")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response createResource(@RequestBody ResourceMapper resourceMapper) {
		boolean result;
		try {
			Resource resource = new Resource();
			BeanUtils.copyProperties(resourceMapper, resource);
			resource.setStatus("Active");
			resource.setRegistrationId(new ObjectId(resourceMapper.getRegistrationId()));
			resource.setResId("RES-" + randomNumber());
			String[] ar = resource.getPrimarySkills().split(",");
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
			resource.setPrimarySkills(values.toString());
			resource.setSoftLock("No");
			resource.setHardLock("No");
			result = resourceService.createResource(resource);
			ResourceExperience res = new ResourceExperience();
			res.setMonthsOfExperience(resource.getMonthsOfExperience());
			res.setYearsOfExperience(resource.getYearsOfExperience());
			result = resourceExpService.create(res);
			if (result == false) {
				return new com.ojas.ra.response.Response("Failed", result, HttpStatus.CONFLICT, "not created");
			}
			return new com.ojas.ra.response.Response("Success", result, HttpStatus.OK, "created");

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
	@Path("/uploadFile/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public com.ojas.ra.response.Response uploadFile(@FormDataParam("uploadFile") InputStream uploadFile,
			@PathParam("id") ObjectId id) throws IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Resource res;
			try {
				res = resourceService.findOneByCondition(condition);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			res.setUploadResume(IOUtils.toByteArray(uploadFile));
			Boolean result = resourceService.saveResource(res);
			if (result == false) {
				return new com.ojas.ra.response.Response("Failed", result, HttpStatus.CONFLICT, "not uploaded");
			}
			return new com.ojas.ra.response.Response("Success", result, HttpStatus.OK, "uploaded");

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@Path("/downloadFile/{id}")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("id") final ObjectId id) throws IOException {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("_id", id);
		Resource resource;
		try {
			resource = resourceService.findOneByCondition(condition);
		} catch (RAException e) {
			throw new RAException("Data Not Found !!");
		}
		final byte[] doc = resource.getUploadResume();
		String filename = resource.getFirstName();

		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
				try {
					output.write(doc);
					output.flush();
				} catch (Exception e) {
					throw new RAException("File Not Found !!");
				}
			}
		};
		return Response.ok(fileStream).header("content-disposition", "attachment; filename = " + filename + ".docx")
				.build();
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
	public com.ojas.ra.response.Response bulkUpload(@FormDataParam("uploadFile") InputStream uploadFile,
			@PathParam("id") ObjectId id) throws IOException {
		boolean result = false;
		try {
			Resource resource = new Resource();
			resource.setRegistrationId(id);
			XSSFWorkbook workbook = new XSSFWorkbook(uploadFile);

			XSSFSheet sheet = workbook.getSheetAt(0);
			int col_value = sheet.getRow(0).getLastCellNum();
			int row_num = sheet.getLastRowNum();
			List<String> DBheader = new ArrayList<String>();
			List<String> Data = new ArrayList<String>();

			for (int z = 1; z <= row_num; z++) {
				DBheader.clear();
				Data.clear();
				for (int j = 0; j < col_value; j++) {
					if (sheet.getRow(0).getCell(j) != null || sheet.getRow(0) != null) {
						String cel_value = sheet.getRow(0).getCell(j).toString();
						DBheader.add(cel_value.trim());
					} else {
						break;

					}
				}
				for (int k = 0; k < col_value; k++) {
					String data = " ";
					if (sheet.getRow(z).getCell(k) != null) {
						data = sheet.getRow(z).getCell(k).toString();
					}
					Data.add(data.trim());
				}
				BasicDBObject doc = new BasicDBObject();
				int l = 0;
				for (String headers : DBheader) {
					if (l > Data.size()) {
						break;
					}
					doc.append(headers, Data.get(l));
					l++;
				}
				resource.setBulkUpload(doc);
				result = resourceService.createResource(resource);
			}

			if (result == false) {
				return new com.ojas.ra.response.Response("Failed", result, HttpStatus.CONFLICT, "not uploaded");
			}
			return new com.ojas.ra.response.Response("Success", result, HttpStatus.OK, "uploaded");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param resourceMapper
	 * @param id
	 * @return
	 */
	@Path("/saveResource/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response saveResource(@RequestBody ResourceMapper resourceMapper,
			@PathParam("id") ObjectId id) {
		try {
			Resource resource = new Resource();
			BeanUtils.copyProperties(resourceMapper, resource);
			resource.set_id(id);
			resource.setRegistrationId(new ObjectId(resourceMapper.getRegistrationId()));
			String[] ar = resource.getPrimarySkills().split(",");
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
			resource.setPrimarySkills(values.toString());
			Boolean result = resourceService.saveResource(resource);
			if (result == false) {
				return new com.ojas.ra.response.Response("Failed", result, HttpStatus.CONFLICT, "not updated");
			}
			return new com.ojas.ra.response.Response("Success", result, HttpStatus.OK, "updated");

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
	public com.ojas.ra.response.Response findOneByPrimaryId(@Context ServletContext context,
			@PathParam("value") ObjectId value) throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", value);
			Resource resource;
			ResourceMapper mapper;
			try {
				resource = resourceService.findOneByCondition(condition);
				mapper = convertDomainToMappar(resource);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			if (mapper == null) {
				return new com.ojas.ra.response.Response("Failed", mapper, HttpStatus.CONFLICT, "No record found");
			}
			return new com.ojas.ra.response.Response("Success", mapper, HttpStatus.OK, "record found");
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
	@Path("/findResourcesByRegistrationId/{registrationId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesByRegistrationId(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		Map<String, Map<String, Integer>>  allLists= null;
		try {
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(new ObjectId(objectId));
			Map<String, MongoAdvancedQuery> resourceMappingcondition = new HashMap<String, MongoAdvancedQuery>();
			resourceMappingcondition.put("registrationId", equal);

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("registrationId");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			try {
				resourcelist = resourceService.advancedFindByCondition(resourceMappingcondition, sort, pageNo,
						pageSize);
				if(null!=resourcelist){
			  allLists = allTLists(resourcelist);
			  }
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response("error", pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			if (mapperList == null || mapperList.size() == 0) {
				
				return new com.ojas.ra.response.Response(mapperList,allLists,pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			
			return new com.ojas.ra.response.Response(mapperList, allLists,pages, count, HttpStatus.OK, "records found");
			
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
	@Path("/findSowResourcesByRegId/{registrationId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findSowResourcesByRegistrationId(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		Map<String, Map<String, Integer>> allLists = null;
	
		try {
	    	MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("registrationId");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourcelist = null;
			List<ResourceMapper> resourceMapperList = null;
			try {
				MongoEqualQuery equal1 = new MongoEqualQuery();
				equal1.setEqualto("No");
				MongoEqualQuery equal2 = new MongoEqualQuery();
				equal2.setEqualto("Active");
				Map<String, MongoAdvancedQuery> resourceMappingcondition = new HashMap<String, MongoAdvancedQuery>();
				resourceMappingcondition.put("hardLock", equal1);
				resourceMappingcondition.put("status", equal2);
				resourcelist = resourceService.advancedFindByCondition(resourceMappingcondition, sort, pageNo,
						pageSize);
			} catch (RAException e) {
				e.getMessage();
			}
			if(null!=resourcelist){
				allLists = allTLists(resourcelist);
			}
			resourceMapperList = convertDomainToMapperList(resourcelist);
			resourceMapperList	=addSowStatus(resourceMapperList, objectId);
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (resourceMapperList == null || resourceMapperList.size() == 0) {
				return new com.ojas.ra.response.Response(resourceMapperList, allLists, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(resourceMapperList, allLists, pages, count, HttpStatus.OK,
					"records found");
		
		}catch (RAException e) {
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
	@Path("/findResourcesBySearch/{registrationId}/{primarySkills}/{jobCategory}/{location}/{experience}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesBySearch(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("primarySkills") String primarySkills,
			@PathParam("jobCategory") String jobCategory, @PathParam("location") String location,
			@PathParam("experience") String experience, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		Map<String,Map<String,Integer>>	allLists = null;
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
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			List<Resource> resourcelist = null;
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> c = new ArrayList<BasicDBObject>();
			c.add(new BasicDBObject("primarySkills", ps));
			c.add(new BasicDBObject("jobCategory", jobCategory));
			c.add(new BasicDBObject("currentLocation", location));
			c.add(new BasicDBObject("yearsOfExperience",experience));
			c.add(new BasicDBObject("status", "Active"));
			c.add(new BasicDBObject("hardLock", "No"));
			andQuery.put("$and", c);
			try {
				resourcelist = resourceService.findAllByCondition(andQuery, sort, pageNo, pageSize);
				
			} catch (RAException e) {
				e.getMessage();
			}
			List<Resource> list = new ArrayList<Resource>();
			if (null != resourcelist)
				for (Resource resource : resourcelist) {
					if (resource.getRegistrationId().toString().equalsIgnoreCase(objectId)) {
						list.add(resource);
					}
				}
			if(null!=list){
				
 allLists=allTLists(list);
			}
			List<ResourceMapper> mapperList = null;
			try {
				mapperList = convertDomainToMapperList(list);
			} catch (RAException e) {
				e.getMessage();
			}
			if (null == mapperList || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList,allLists, pages, count, HttpStatus.OK, "No records found");
			}
			return new com.ojas.ra.response.Response(mapperList,allLists, pages, count, HttpStatus.OK, "records found");
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
	@Path("/findResourcesBySearch/{primarySkills}/{jobCategory}/{location}/{experience}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesBySearchs(@Context ServletContext context,
			@PathParam("primarySkills") String primarySkills, @PathParam("jobCategory") String jobCategory,
			@PathParam("location") String location, @PathParam("experience") String experience,
			@PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		Map<String,Map<String,Integer>>	 allLists =null;
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
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			List<Resource> resourcelist = null;

			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> c = new ArrayList<BasicDBObject>();
			c.add(new BasicDBObject("primarySkills", ps));
			c.add(new BasicDBObject("jobCategory", jobCategory));
			c.add(new BasicDBObject("currentLocation", location));
			c.add(new BasicDBObject("yearsOfExperience",experience));
			c.add(new BasicDBObject("status", "Active"));
			c.add(new BasicDBObject("hardLock", "No"));
			andQuery.put("$and", c);
			try {
				resourcelist = resourceService.findAllByCondition(andQuery, sort, pageNo, pageSize);
			} catch (RAException e) {
				e.getMessage();
			}
			
			if(null!=resourcelist){
				
		allLists = allTLists(resourcelist);
			}
			List<ResourceMapper> mapperList = null;
			try {
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				e.getMessage();
			}
			if (null == mapperList || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, allLists,pages, count, HttpStatus.OK, "No records found");
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
	 * @param vendors
	 * @param budget
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findResourcesBySearchs/{registrationId}/{primarySkills}/{jobCategory}/{location}/{experience}/{vendors}/{budget}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesBySearchs(@Context ServletContext context,
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

			int pages = resourceService.getPages(sort, pageSize);
			int count = resourceService.getCount(sort);
			List<Resource> resourcelist = null;
			try {
				resourcelist = resourceService.advancedFindByConditions(ps, jobCategory, location, experience, vendors,
						budget, sort, pageNo, pageSize);
			} catch (RAException e) {
				e.getMessage();
			}
			List<ResourceMapper> mapperList = null;
			if (null != resourcelist) {
				List<Resource> list = new ArrayList<Resource>();
				for (Resource resource : resourcelist) {
					if (resource.getRegistrationId().toString().equalsIgnoreCase(objectId)) {
						list.add(resource);
					}
				}
				try {
					mapperList = convertDomainToMapperList(list);
				} catch (RAException e) {
					e.getMessage();
				}
			}
			List<ResourceMapper> mapperList1 = null;
			if (mapperList == null || mapperList.size() == 0) {
				mapperList1 = optionList(objectId, pageNo, pageSize);
				return new com.ojas.ra.response.Response(mapperList1, pages, count, HttpStatus.OK, "records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
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
	@Path("/findResourcesByBudget/{budget}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findReqResourcesByBudget(@Context ServletContext context,
			@PathParam("budget") String value, @PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			String name = "budget";
			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			try {
				resourcelist = resourceService.advancedFindByCondition(name, value, sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
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
	public com.ojas.ra.response.Response findOneByCondition(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put(nameOftheProperty, valueOftheProperty);
			Resource resource;
			ResourceMapper mapper;
			try {
				resource = resourceService.findOneByCondition(condition);
				mapper = convertDomainToMappar(resource);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			if (mapper == null) {
				return new com.ojas.ra.response.Response("Failed", mapper, HttpStatus.CONFLICT, "No record found");
			}
			return new com.ojas.ra.response.Response("Success", mapper, HttpStatus.OK, "record found");
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
	@Path("/listResources/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response listResources(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> list;
			List<ResourceMapper> mapperList;
			try {
				list = resourceService.advancedFindByCondition(sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			Map<String, Map<String, Integer>> allList = allTLists(list);
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, allList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, allList, pages, count, HttpStatus.OK, "records found");
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
	public com.ojas.ra.response.Response findOneAllByCondition(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			try {
				resourcelist = resourceService.advancedFindByCondition(nameOftheProperty, valueOftheProperty, sort,
						pageNo, pageSize);
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
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
	@Path("/findResourcesByRequirementId/{requirementId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesByRequirementId(@Context ServletContext context,
			@PathParam("requirementId") String objectId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", new ObjectId(objectId));

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			try {
				resourcelist = resourceService.getByMapObjects(sort, pageNo, pageSize, condition);
				mapperList = convertDomainToMapperList(resourcelist);
				
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param reqId
	 * @param regId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findResourcesByReqIdAndVendorId/{requirementId}/{vendorId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesByReqIdAndVendorId(@Context ServletContext context,
			@PathParam("requirementId") String reqId, @PathParam("vendorId") String regId,
			@PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("requirementId", new ObjectId(reqId));
			condition.put("vendorId", new ObjectId(regId));

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			try {
				resourcelist = resourceService.getByMapObjects(sort, pageNo, pageSize, condition);
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param conditions
	 * @param companyId
	 * @param vendorId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findResourcesByTwoIds/{conditions}/{companyId}/{vendorId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesByComIdAndVendorId(@Context ServletContext context,
			@PathParam("conditions") String conditions, @PathParam("companyId") String companyId,
			@PathParam("vendorId") String vendorId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			if (conditions.equalsIgnoreCase("CompanyName")) {
				condition.put("companyId", new ObjectId(companyId));
			} else {
				condition.put("requirementId", new ObjectId(companyId));
			}
			condition.put("vendorId", new ObjectId(vendorId));

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			try {
				resourcelist = resourceService.getByMapObjects(sort, pageNo, pageSize, condition);
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param reqId
	 * @param regId
	 * @param vendorId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findResourcesByReqIdAndComIdAndVendorId/{requirementId}/{companyId}/{vendorId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesByReqIdAndComIdAndVendorId(@Context ServletContext context,
			@PathParam("requirementId") String reqId, @PathParam("companyId") String regId,
			@PathParam("vendorId") String vendorId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("requirementId", new ObjectId(reqId));
			condition.put("companyId", new ObjectId(regId));
			condition.put("vendorId", new ObjectId(vendorId));

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);

			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			try {
				resourcelist = resourceService.getByMapObjects(sort, pageNo, pageSize, condition);
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param resourceMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/inactiveOrActive/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response inactiveOrActive(@RequestBody ResourceMapper resourceMapper,
			@PathParam("id") ObjectId id) {
		try {
			Resource resource = new Resource();
			BeanUtils.copyProperties(resourceMapper, resource);
			resource.set_id(id);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Map<String, Object> target = new HashMap<String, Object>();
			target.put("status", resource.getStatus());
			Boolean result = resourceService.updateMapByCondition(condition, target);
			if (result == false) {
				return new com.ojas.ra.response.Response("Failed", HttpStatus.CONFLICT);
			}
			return new com.ojas.ra.response.Response("Success", HttpStatus.OK);

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param resourceMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/softLock/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response softLock(@RequestBody ResourceMapper resourceMapper,
			@PathParam("id") ObjectId id) {
		try {
			Resource resource = new Resource();
			BeanUtils.copyProperties(resourceMapper, resource);
			resource.set_id(id);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Map<String, Object> target = new HashMap<String, Object>();
			target.put("softLock", resource.getSoftLock());
			Boolean result = resourceService.updateMapByCondition(condition, target);
			if (result == false) {
				return new com.ojas.ra.response.Response("Failed", HttpStatus.CONFLICT);
			}
			return new com.ojas.ra.response.Response("Success", HttpStatus.OK);

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param resourceMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/hardLock/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response hardLock(@RequestBody ResourceMapper resourceMapper,
			@PathParam("id") ObjectId id) {
		try {
			Resource resource = new Resource();
			BeanUtils.copyProperties(resourceMapper, resource);
			resource.set_id(id);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Map<String, Object> target = new HashMap<String, Object>();
			target.put("hardLock", resource.getHardLock());
			Boolean result = resourceService.updateMapByCondition(condition, target);
			if (result == false) {
				return new com.ojas.ra.response.Response("Failed", HttpStatus.CONFLICT);
			}
			return new com.ojas.ra.response.Response("Success", HttpStatus.OK);

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
	@Path("/findAllByCondition/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findAllCondition(@Context ServletContext context,
			@PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourcelist;
			List<ResourceMapper> mapperList;
			try {
				resourcelist = resourceService.advancedFindByCondition(sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.CONFLICT,
						"No records found");
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}

	}

	/**
	 * 
	 * @param context
	 * @param skills
	 * @param jobCategory
	 * @param location
	 * @param experience
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Path("/findResourcesByFilter/{customerRegid}/{primarySkills}/{jobCategory}/{location}/{experience}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesByFilter(@Context ServletContext context,
			@PathParam("customerRegid") String customerRegid, @PathParam("primarySkills") String primarySkills,
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
			List<Resource> resourcelist = null;
			List<ResourceMapper> resourcemapperList = null;
			try {
				resourcelist = resourceService.advancedFindByConditions(skills, jobCategory, location, experience, sort,
						pageNo, pageSize);
			} catch (RAException e) {
				e.getMessage();
			}
			Registration reg = null;
			if (null != resourcelist) {
				resourcemapperList = convertDomainToMapperList(resourcelist);
				resourcemapperList	=addSowStatus(resourcemapperList, customerRegid);
				}
			
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			List<ResourceMapper> mapperList1 = null;
			if (resourcemapperList == null || resourcemapperList.size() == 0) {
				mapperList1 = optionList(customerRegid, pageNo, pageSize);
				return new com.ojas.ra.response.Response(mapperList1, pages, count, HttpStatus.OK, "records found");
			}
			return new com.ojas.ra.response.Response(resourcemapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @param location
	 * @param jobCategory
	 * @param primarySkills
	 * @param experience
	 * @param vendors
	 * @return
	 */
	@Path("/findResourcesByFilter/{primarySkills}/{jobCategory}/{location}/{experience}/{vendors}/{budget}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response findResourcesByFilters(@Context ServletContext context,
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
			List<Resource> list = null;
			List<Resource> resourcelist = null;
			Registration registration = null;
			List<ResourceMapper> mapperList = null;
			try {
				registration = registrationService.findOneByCondition(condition);
			} catch (RAException e) {
				e.getMessage();
			}
			if (null != registration) {
				MongoEqualQuery equal = new MongoEqualQuery();
				try {
					equal.setEqualto(registration.get_id());
				} catch (RAException e) {
					e.getMessage();
				}
				Map<String, MongoAdvancedQuery> resourceCondition = new HashMap<String, MongoAdvancedQuery>();
				resourceCondition.put("registrationId", equal);
				try {
					list = resourceService.advancedFindByCondition(resourceCondition, sort, pageNo, pageSize);
				} catch (RAException e) {
					e.getMessage();
				}
			}
			try {
				resourcelist = resourceService.advancedFindByConditions(skills, jobCategory, location, experience,
						vendors, budget, sort, pageNo, pageSize);
				if (null != list)
					resourcelist.addAll(list);
				mapperList = convertDomainToMapperList(resourcelist);
			} catch (RAException e) {
				e.getMessage();
			}
			int count = resourceService.getCount(sort);
			int pages = resourceService.getPages(sort, pageSize);
			List<ResourceMapper> mapperList1 = null;
			if (mapperList == null || mapperList.size() == 0) {
				MongoSortVO sort1 = new MongoSortVO();
				sort1.setPrimaryKey("registrationId");
				sort1.setPrimaryOrderBy(MongoOrderByEnum.DESC);
				List<Resource> resourcelist1;
				try {
					MongoEqualQuery equal1 = new MongoEqualQuery();
					equal1.setEqualto("NO");
					MongoEqualQuery equal2 = new MongoEqualQuery();
					equal2.setEqualto("Active");
					Map<String, MongoAdvancedQuery> resourceMappingcondition = new HashMap<String, MongoAdvancedQuery>();
					resourceMappingcondition.put("hardLock", equal1);
					resourceMappingcondition.put("status", equal2);
					resourcelist1 = resourceService.advancedFindByCondition(resourceMappingcondition, sort, pageNo,
							pageSize);
					mapperList1 = convertDomainToMapperList(resourcelist1);
					return new com.ojas.ra.response.Response(mapperList1, pages, count, HttpStatus.OK, "records found");
				} catch (RAException e) {
					e.getMessage();
				}
			}
			return new com.ojas.ra.response.Response(mapperList, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}


/**/

	/**
	 * 
	 * @param context
	 * @return
	 */
	@Path("/allVendors")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response getAllVendors(@Context ServletContext context) {
		Set<String> vendorSet = new LinkedHashSet<String>();
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> resourceList;
			try {
				resourceList = resourceService.getAllObjects(sort);
			} catch (RAException e) {
				return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
			}
			for (Resource resource : resourceList) {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("_id", resource.getRegistrationId());
				Registration reg;
				try {
					reg = registrationService.findOneByCondition(condition);
				} catch (RAException e) {
					return new com.ojas.ra.response.Response(HttpStatus.CONFLICT, "No records found");
				}
				vendorSet.add(reg.getCompanyName());
				if (resourceList == null || resourceList.size() == 0) {
					return new com.ojas.ra.response.Response(vendorSet, HttpStatus.CONFLICT, "failed");
				}
			}
		} catch (RAException e) {
			e.getMessage();
		}
		return new com.ojas.ra.response.Response(vendorSet, HttpStatus.OK, "success");
	}

	@Path("/graphs")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.ojas.ra.response.Response graphs(@Context ServletContext context){
		
		
		
		
		return null;
	}
	
	
	
	
	
	// sow and companyName
	public List<ResourceMapper> addSowStatus(List<ResourceMapper> resourceMapperList, String objectId) {

		Registration regstration = null;
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("_id", new ObjectId(objectId));
		try {
			regstration = registrationService.findOneByCondition(condition);
		} catch (RAException e) {
			e.getMessage();
		}

		if (null != resourceMapperList) {
			for (ResourceMapper resourceMapper : resourceMapperList) {
				if (null != regstration.getSowUser()) {
					String[] sow = regstration.getSowUser().split(",");
					for (String regId : sow) {
						if (regId.equals(resourceMapper.getRegistrationId())) {
							resourceMapper.setIsSowUser("Yes");
						} else {
							resourceMapper.setIsSowUser("No");
						}
					}
				} else {
					resourceMapper.setIsSowUser("No");
				}
			}
		}
		return resourceMapperList;
	}

	// All List New Method;

	public Map<String, Map<String, Integer>> allTLists(List<Resource> resourceList) {

		Map<String, Integer> skillsMap = new HashMap<String, Integer>();
		Map<String, Integer> yearsOfExperiencMap = new HashMap<String, Integer>();
		Map<String, Integer> jobCategoryMap = new HashMap<String, Integer>();
		Map<String, Integer> budgetMap = new HashMap<String, Integer>();
		Map<String, Integer> locationMap = new HashMap<String, Integer>();
		Map<String, Integer> vendorMap = new HashMap<String, Integer>();

		Map<String, Map<String, Integer>> allMap = new HashMap<String, Map<String, Integer>>();

		for (Resource resource : resourceList) {
			String[] primarySkills = resource.getPrimarySkills().split(",");
			for (String primaryskill : primarySkills) {
				if (skillsMap.containsKey(primaryskill)) {
					skillsMap.put(primaryskill, skillsMap.get(primaryskill) + 1);
				} else {
					if (null != primaryskill)
						skillsMap.put(primaryskill, 1);
				}
			}

			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", resource.getRegistrationId());
			String compaanyName;

			compaanyName = registrationService.findOneByCondition(condition).getCompanyName();

			if (compaanyName != null) {
				if (vendorMap.containsKey(compaanyName)) {

					vendorMap.put(compaanyName, vendorMap.get(compaanyName) + 1);
				} else {
					if (null != compaanyName)
						vendorMap.put(compaanyName, 1);
				}
			}
			if (yearsOfExperiencMap.containsKey(resource.getYearsOfExperience())) {
				yearsOfExperiencMap.put(resource.getYearsOfExperience(),
						yearsOfExperiencMap.get(resource.getYearsOfExperience()) + 1);
			} else {
				if (null != resource.getYearsOfExperience())
					yearsOfExperiencMap.put(resource.getYearsOfExperience(), 1);
			}

			if (jobCategoryMap.containsKey(resource.getJobCategory())) {
				jobCategoryMap.put(resource.getJobCategory(), jobCategoryMap.get(resource.getJobCategory()) + 1);
			} else {
				if (null != resource.getJobCategory())
					jobCategoryMap.put(resource.getJobCategory(), 1);
			}

			if (budgetMap.containsKey(resource.getBudget())) {
				budgetMap.put(resource.getBudget(), budgetMap.get(resource.getBudget()) + 1);
			} else {
				if (null != resource.getBudget())
					budgetMap.put(resource.getBudget(), 1);
			}

			if (locationMap.containsKey(resource.getCurrentLocation())) {
				locationMap.put(resource.getCurrentLocation(),
						locationMap.get(resource.getCurrentLocation()) + 1);
			} else {
				if (null != resource.getCurrentLocation())
					locationMap.put(resource.getCurrentLocation(), 1);
			}
		}
		allMap.put("skillsMap", skillsMap);
		allMap.put("vedorMap", vendorMap);
		allMap.put("yearsOfExperiencMap", yearsOfExperiencMap);
		allMap.put("jobCategoryMap", jobCategoryMap);
		allMap.put("budgetMap", budgetMap);
		allMap.put("locationMap", locationMap);

		return allMap;
	}
	
	
	
	
	
	
	
	
	
	

	/**
	 * 
	 * @param resources
	 * @return
	 */
	private List<ResourceMapper> convertDomainToMapperList(List<Resource> resources) {
		List<ResourceMapper> list = null;
		try {
			list = new ArrayList<ResourceMapper>();
			for (Resource resource : resources) {
				ResourceMapper resourceMapper = new ResourceMapper();
				BeanUtils.copyProperties(resource, resourceMapper);
				resourceMapper.set_id(resource.get_id().toString());
				if (null != resource.getRegistrationId()) {
					resourceMapper.setRegistrationId(resource.getRegistrationId().toString());
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("_id", resource.getRegistrationId());
					Registration reg;
					try {
						reg = registrationService.findOneByCondition(condition);
						if (null != reg) {
							resourceMapper.setCompanyName(reg.getCompanyName());
							resourceMapper.setVendorEmailId(reg.getMailId());
							resourceMapper.setVendorMobile(reg.getMobile());
						} else
							throw new RAException("Data Not Found !!");
					} catch (RAException e) {
						throw new RAException("Data Not Found !!");
					}
				}
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("_id", resource.getRegistrationId());
				try {
					Registration reg = registrationService.findOneByCondition(condition);
					if (null != reg) {
						resourceMapper.setCompanyName(reg.getCompanyName());
						resourceMapper.setVendorEmailId(reg.getMailId());
						resourceMapper.setVendorMobile(reg.getMobile());
					} else
						throw new RAException("Data Not Found !!");
				} catch (RAException e) {
					throw new RAException("Data Not Found !!");
				}
				list.add(resourceMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	private ResourceMapper convertDomainToMappar(Resource resources) {
		try {
			ResourceMapper resourceMapper = new ResourceMapper();
			BeanUtils.copyProperties(resources, resourceMapper);
			resourceMapper.set_id(resources.get_id().toString());
			if (null != resources.getRegistrationId()) {
				resourceMapper.setRegistrationId(resources.getRegistrationId().toString());
			}
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", resources.getRegistrationId());
			try {
				Registration reg = registrationService.findOneByCondition(condition);
				if (null != reg) {
					resourceMapper.setCompanyName(reg.getCompanyName());
					resourceMapper.setVendorEmailId(reg.getMailId());
					resourceMapper.setVendorMobile(reg.getMobile());
				} else
					throw new RAException("Data Not Found !!");
				if (reg.getSowUser() != null) {
					String[] sow = reg.getSowUser().split(",");
					for (String regId : sow) {
						if (regId.equals(resources.getRegistrationId().toString())) {
							resourceMapper.setIsSowUser("Yes");
						} else {
							resourceMapper.setIsSowUser("No");
						}
					}
				}
			} catch (RAException e) {
				throw new RAException("Data Not Found !!");
			}
			return resourceMapper;
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

	public List<ResourceMapper> optionList(String objectId, int pgNo, int PgSize) {
		MongoSortVO sort1 = new MongoSortVO();
		sort1.setPrimaryKey("registrationId");
		sort1.setPrimaryOrderBy(MongoOrderByEnum.DESC);
		List<ResourceMapper> mapperList1 = null;
		List<Resource> resourcelist1;
		try {
			MongoEqualQuery equal1 = new MongoEqualQuery();
			equal1.setEqualto("NO");
			MongoEqualQuery equal2 = new MongoEqualQuery();
			equal2.setEqualto("Active");
			Map<String, MongoAdvancedQuery> resourceMappingcondition = new HashMap<String, MongoAdvancedQuery>();
			resourceMappingcondition.put("hardLock", equal1);
			resourceMappingcondition.put("status", equal2);
			resourcelist1 = resourceService.advancedFindByCondition(resourceMappingcondition, sort1, pgNo, PgSize);
			mapperList1 = convertDomainToMapperList(resourcelist1);
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
			for (ResourceMapper resourceMapper : mapperList1) {
				resourceMapper.setCompanyName(reg.getCompanyName());
				if (null != reg.getSowUser()) {
					String[] sow = reg.getSowUser().split(",");
					for (String regId : sow) {
						if (regId.equals(resourceMapper.getRegistrationId())) {
							resourceMapper.setIsSowUser("Yes");
						} else {
							resourceMapper.setIsSowUser("No");
						}
					}
				} else {
					resourceMapper.setIsSowUser("No");
				}
			}
		}
		return mapperList1;
	}
}
