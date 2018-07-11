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

import com.ojas.ra.domain.JobCategory;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.JobCategoryMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.JobCategoryService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/jobCategory")
public class JobCategoryResource {

	@Autowired
	private JobCategoryService jobCategoryService;

	/**
	 * 
	 * @param jobCategoryMapper
	 * @return
	 */
	@Path("/createJobCategory")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createJobCategory(@RequestBody JobCategoryMapper jobCategoryMapper) {
		int count = 0;
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);

			for (JobCategory jobCat : jobCategoryService.getAllJobCategory(sort)) {
				if (jobCat.getJobCategory().equalsIgnoreCase(jobCategoryMapper.getJobCategory()) == false) {
					count++;
				}
			}
			if (count == jobCategoryService.getAllJobCategory(sort).size()) {
				JobCategory jobCategory = new JobCategory();
				BeanUtils.copyProperties(jobCategoryMapper, jobCategory);
				Boolean result = jobCategoryService.createJobCategory(jobCategory);
				if (result == true) {
					return new Response("Active", result, HttpStatus.OK, "Success");
				} else {
					return new Response("InActive ", result, HttpStatus.CONFLICT, "Failed ");
				}
			}
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
		return new Response("InActive ", HttpStatus.CONFLICT, "Failed ");
	}

	/**
	 * 
	 * @param jobCategoryMapper
	 * @param id
	 * @return
	 */
	@Path("/updateJobCategory/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateJobCategory(@RequestBody JobCategoryMapper jobCategoryMapper, @PathParam("id") ObjectId id) {
		try {
			JobCategory jobCategory = new JobCategory();
			BeanUtils.copyProperties(jobCategoryMapper, jobCategory);
			jobCategory.set_id(id);
			boolean result = jobCategoryService.updateJobCategory(jobCategory);
			if (result == true) {
				return new Response("Active", result, HttpStatus.OK, "Success");
			} else {
				return new Response("InActive ", result, HttpStatus.CONFLICT, "Failed ");
			}
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
	@Path("/listJobCategory/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllJobCategory(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<JobCategory> list;
			List<JobCategoryMapper> result;
			try {
				list = jobCategoryService.getAllJobCategory(sort, pageNo, pageSize);
				result = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = jobCategoryService.getCount(sort);
			int pages = jobCategoryService.getPages(sort, pageSize);
			if (result == null || result.size() == 0) {
				return new Response(result, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(result, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	@Path("/listJobCategory")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAll(@Context ServletContext context) {

		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<JobCategory> list;
			List<JobCategoryMapper> result;
			try {
				list = jobCategoryService.getAllJobCategory(sort);
				result = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (result == null) {
				return new Response("InActive ", result, HttpStatus.CONFLICT, "Failed ");
			}
			return new Response("Active", result, HttpStatus.OK, "Success");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param jobCategories
	 * @return
	 */
	public List<JobCategoryMapper> convertDomainToMapperList(List<JobCategory> jobCategories) {
		try {
			List<JobCategoryMapper> list = new ArrayList<JobCategoryMapper>();
			for (JobCategory jobCategory : jobCategories) {
				JobCategoryMapper jobCategoryMapper = new JobCategoryMapper();
				BeanUtils.copyProperties(jobCategory, jobCategoryMapper);
				jobCategoryMapper.set_id(jobCategory.get_id().toString());

				list.add(jobCategoryMapper);
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
			JobCategory jobCategory;
			JobCategoryMapper result;
			try {
				jobCategory = jobCategoryService.getOneByCondition(condition);
				result = convertDomainToMappar(jobCategory);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (result == null) {
				return new Response("InActive ", result, HttpStatus.CONFLICT, "Failed ");
			}
			return new Response("Active", result, HttpStatus.OK, "Success");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param jobCategory
	 * @return
	 */
	private JobCategoryMapper convertDomainToMappar(JobCategory jobCategory) {
		try {
			JobCategoryMapper jobCategoryMapper = new JobCategoryMapper();
			BeanUtils.copyProperties(jobCategory, jobCategoryMapper);
			jobCategoryMapper.set_id(jobCategory.get_id().toString());
			return jobCategoryMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

}
