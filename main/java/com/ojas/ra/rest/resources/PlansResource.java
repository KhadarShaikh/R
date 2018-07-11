package com.ojas.ra.rest.resources;

import java.io.IOException;
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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.ra.domain.PlanMapping;
import com.ojas.ra.domain.Plans;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.PlansMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.PlanMappingService;
import com.ojas.ra.service.PlansService;
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
@Path("/plans")
public class PlansResource {

	@Autowired
	private PlansService plansService;

	@Autowired
	PlanMappingService planMappingService;

	/**
	 * 
	 * @param plansMapper
	 * @return
	 */
	@Path("/createPlans")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPlans(@RequestBody PlansMapper plansMapper) {
		try {
			Plans plans = new Plans();
			BeanUtils.copyProperties(plansMapper, plans);
			plans.setStatus("Active");
			plans.setPlus("+");
			Boolean result = plansService.createPlans(plans);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "plan not created");
			}
			return new Response("Success", result, HttpStatus.OK, "plan created");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param plansMapper
	 * @param id
	 * @return
	 */
	@Path("/updatePlans/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePlans(@RequestBody PlansMapper plansMapper, @PathParam("id") ObjectId id) {
		try {
			Plans plans = new Plans();
			BeanUtils.copyProperties(plansMapper, plans);
			plans.set_id(id);
			Boolean result = plansService.updatePlans(plans);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "plan not updated");
			}
			return new Response("Success", result, HttpStatus.OK, "plan updated");
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
	@DELETE
	@Path("/deletePlans/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlansById(@Context ServletContext context, @PathParam("id") ObjectId id) {
		try {
			Boolean result = plansService.deletePlansById(id);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "plan not deleted");
			}
			return new Response("Success", result, HttpStatus.OK, "plan deleted");
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
	@Path("/getplansByPrimaryId/{value}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findPlansByPrimaryId(@Context ServletContext context, @PathParam("value") ObjectId value)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", value);
			Plans plan;
			PlansMapper plans;
			try {
				plan = plansService.getPlansByPrimaryId(condition);
				plans = convertDomainToMappar(plan);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (plans == null) {
				return new Response("Failed", plans, HttpStatus.CONFLICT, "plan not found");
			}
			return new Response("Success", plans, HttpStatus.OK, "plan found");
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
	@Path("/listActivePlans/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllActivePlans(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto("Active");
			Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
			condition.put("status", equal);
			List<Plans> list;
			List<PlansMapper> planMappers;
			try {
				list = plansService.advancedFind(condition, sort, pageNo, pageSize);
				planMappers = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = plansService.getCount(sort);
			int pages = plansService.getPages(sort, pageSize);
			if (planMappers == null) {
				return new Response(planMappers, pages, count, HttpStatus.CONFLICT, "plans not found");
			}
			return new Response(planMappers, pages, count, HttpStatus.OK, "plans found");
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
	@Path("/listPlans/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllPlans(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Plans> list;
			List<PlansMapper> planMappers;
			try {
				list = plansService.getAllPlans(sort, pageNo, pageSize);
				planMappers = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = plansService.getCount(sort);
			int pages = plansService.getPages(sort, pageSize);
			if (planMappers == null) {
				return new Response(planMappers, pages, count, HttpStatus.CONFLICT, "plans not found");
			}
			return new Response(planMappers, pages, count, HttpStatus.OK, "plans found");

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
	@Path("/findAllByCondition/{nameOftheProperty}/{valueOftheProperty}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response advanceFind(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(valueOftheProperty);
			Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
			condition.put(nameOftheProperty, equal);
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Plans> list;
			List<PlansMapper> planMappers;
			try {
				list = plansService.advancedFind(condition, sort, pageNo, pageSize);
				planMappers = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = plansService.getCount(sort);
			int pages = plansService.getPages(sort, pageSize);
			if (planMappers == null) {
				return new Response(planMappers, pages, count, HttpStatus.CONFLICT, "plans not found");
			}
			return new Response(planMappers, pages, count, HttpStatus.OK, "plans found");

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param plansMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/inactiveOrActive/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inactiveOrActive(@RequestBody PlansMapper plansMapper, @PathParam("id") ObjectId id) {
		try {
			Plans plans = new Plans();
			BeanUtils.copyProperties(plansMapper, plans);
			plans.set_id(id);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Map<String, Object> target = new HashMap<String, Object>();
			target.put("status", plans.getStatus());

			Boolean result = plansService.updatePlans(condition, target);
			if (result == false) {
				return new Response("Failed", HttpStatus.CONFLICT);
			}
			return new Response("Success", HttpStatus.OK);
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	@Path("/getPreviousPlansByRegId/{id}/{pageNo}/{pageSize}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPreviousPlansByRegId(@Context ServletContext context, @PathParam("id") ObjectId id,
			@PathParam("pageNo") int pageNo, @PathParam("pageSize") int pageSize) {
		List<Plans> plansList = new ArrayList<Plans>();
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);

			Map<String, MongoAdvancedQuery> condition = new HashMap<String, MongoAdvancedQuery>();
			MongoEqualQuery equal = new MongoEqualQuery();
			equal.setEqualto(id);
			condition.put("registrationId", equal);
			List<PlanMapping> planMappingList;
			try {
				planMappingList = planMappingService.advancedFind(condition, sort, pageNo, pageSize);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			for (PlanMapping planMapping : planMappingList) {
				String status=planMapping.getStatus();
				if(null!=status)
				if (status.equals("InActive")) {
					ObjectId planId = planMapping.getPlan_id();
					Map<String, Object> condition1 = new HashMap<String, Object>();
					condition1.put("_id", planId);
					try {
						Plans plan = plansService.getPlansByPrimaryId(condition1);
						plansList.add(plan);
					} catch (RAException e) {
						return new Response(HttpStatus.CONFLICT, "No records found");
					}
				}
			}
			List<PlansMapper> plansMapperList;
			try {
				plansMapperList = convertDomainToMapperList(plansList);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (plansMapperList == null) {
				return new Response("Failed", plansMapperList, HttpStatus.CONFLICT, "No record found");
			}
			return new Response("Success", plansMapperList, HttpStatus.OK, "record found");

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param plans
	 * @return
	 */
	private PlansMapper convertDomainToMappar(Plans plans) {
		try {
			PlansMapper plansMapper = new PlansMapper();
			BeanUtils.copyProperties(plans, plansMapper);
			plansMapper.set_id(plans.get_id().toString());
			return plansMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param plans
	 * @return
	 */
	private List<PlansMapper> convertDomainToMapperList(List<Plans> plans) {
		try {
			List<PlansMapper> list = new ArrayList<PlansMapper>();
			for (Plans planss : plans) {
				PlansMapper plansMapper = new PlansMapper();
				BeanUtils.copyProperties(planss, plansMapper);
				plansMapper.set_id(planss.get_id().toString());
				list.add(plansMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
