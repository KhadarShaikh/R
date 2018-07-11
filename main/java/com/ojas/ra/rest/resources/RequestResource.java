package com.ojas.ra.rest.resources;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ojas.ra.domain.Registration;
import com.ojas.ra.domain.RequestResources;
import com.ojas.ra.domain.Resource;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.ResourceMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.RegistrationService;
import com.ojas.ra.service.ReqResourceService;
import com.ojas.ra.service.ResourceService;
import com.ojas.ra.util.MongoAdvancedQuery;
import com.ojas.ra.util.MongoEqualQuery;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;
import com.ojas.ra.util.SendMail;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/requestResource")
public class RequestResource {

	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ReqResourceService reqResourceService;
	@Autowired
	private RegistrationService registrationService;
	@Autowired
	SendMail sendMail;

	/**
	 * 
	 * @param requestResourceMapper
	 * @return
	 */
	@Path("/reqResource/{requirementId}/{resourceId}/{registrationId}/{vendorId}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@Context ServletContext context, @PathParam("requirementId") String requirementId,
			@PathParam("resourceId") String resourceId, @PathParam("registrationId") String registrationId,
			@PathParam("vendorId") String vendorId) {
		boolean result = false;
		try {
			RequestResources requestResource = new RequestResources();
			requestResource.setStatus("Active");
			requestResource.setRequirementId(new ObjectId(requirementId));
			requestResource.setResourceId(new ObjectId(resourceId));
			requestResource.setRegistrationId(new ObjectId(registrationId));
			requestResource.setVendorId(new ObjectId(vendorId));
			result = reqResourceService.create(requestResource);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", requestResource.getResourceId());
			Resource resource;
			try {
				resource = resourceService.findOneByCondition(condition);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			resource.setSoftLock("Yes");
			resourceService.saveResource(resource);
			Map<String, Object> condition1 = new HashMap<String, Object>();
			condition.put("_id", requestResource.getRegistrationId());
			Registration registration;
			try {
				registration = registrationService.findOneByCondition(condition1);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			String subject = "Resource Request for " + resource.getJobCategory() + " at "
					+ resource.getCurrentLocation();
			String msg = "Dear " + registration.getCompanyName() + ",\n" + "The below resource match with our "
					+ resource.getJobCategory()
					+ " position, hence looking forward for his availability to schedule the interview process.\nThanks&Regards"
					+ registration.getCompanyName() + "\n" + registration.getCity();

			// sendMail.sendMail(registration.getMailId(), subject, msg);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "not created");
			}
			return new Response("Success", result, HttpStatus.OK, "created");

		} catch (

		RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	@Path("/findResourcesByVendorId/{registrationId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findResourcesByVendorId(@Context ServletContext context,
			@PathParam("registrationId") String registrationId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		Map<String,Map<String,Integer>>	allLists = null;
		try {
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(new ObjectId(registrationId));
			MongoEqualQuery equal1 = new MongoEqualQuery();
			equal1.setEqualto("Active");
			MongoEqualQuery equal2 = new MongoEqualQuery();
			equal2.setEqualto("No");
			Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
			condition.put("registrationId", equal);
			condition.put("status", equal1);
			condition.put("hardLock", equal2);

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Resource> list = new ArrayList<Resource>();
			List<Resource> resourcelist;
			try {
				resourcelist = resourceService.advancedFindByCondition(condition, sort, pageNo, pageSize);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}

			Map<String, MongoAdvancedQuery> condition2 = new HashMap<String, MongoAdvancedQuery>();
			condition2.put("vendorId", equal);
			List<RequestResources> reqList;
			try {
				reqList = reqResourceService.advancedFindByCondition(condition2, sort, pageNo, pageSize);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			for (Resource resources : resourcelist) {
				for (RequestResources requestResource : reqList) {
					if (resources.get_id().toString().equals(requestResource.getResourceId().toString())) {
						list.add(resources);
					}
				}
			}
			
			if(null!=list){
			allLists= allTLists(list);
			}
			List<ResourceMapper> mapperList;
			try {
				mapperList = convertDomainToMapperList(list);
				if(null!=mapperList){
					mapperList = addSowStatus(mapperList, registrationId);
				}
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = reqResourceService.getCount(sort);

			int pages = reqResourceService.getPages(sort, pageSize);
			if (mapperList == null || mapperList.size() == 0) {
				return new Response(mapperList,allLists, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(mapperList,allLists, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param resources
	 * @return
	 */
	private List<ResourceMapper> convertDomainToMapperList(List<Resource> resources) {
		try {
			List<ResourceMapper> list = new ArrayList<ResourceMapper>();
			for (Resource resource : resources) {
				ResourceMapper resourceMapper = new ResourceMapper();
				BeanUtils.copyProperties(resource, resourceMapper);
				resourceMapper.set_id(resource.get_id().toString());
				if (null != resource.getRegistrationId()) {
					resourceMapper.setRegistrationId(resource.getRegistrationId().toString());
				}
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("_id", resource.getRegistrationId());
				try {
					Registration reg = registrationService.findOneByCondition(condition);
					resourceMapper.setCompanyName(reg.getCompanyName());
					list.add(resourceMapper);
				} catch (RAException e) {
					throw new RAException("Data Not Found !!");
				}
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
	
	
	// sow
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
					resourceMapper.setCompanyName(regstration.getCompanyName());
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

}
