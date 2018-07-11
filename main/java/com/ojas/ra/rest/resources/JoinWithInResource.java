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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ojas.ra.domain.JoinWithIn;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.JoinWithInMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.JoinWithInService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/joinWithIn")
public class JoinWithInResource {

	@Autowired
	private JoinWithInService joinWithInService;

	/**
	 * 
	 * @param joinWithInMapper
	 * @return
	 */
	@Path("/createjoinWithIn")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody Response createjoinWithIn(@RequestBody JoinWithInMapper joinWithInMapper) {
		boolean result = false;
		int count = 0;
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			for (JoinWithIn joinWithIn : joinWithInService.getAllJoinWithIn(sort)) {
				if (joinWithIn.getJoinWithIn().equalsIgnoreCase(joinWithInMapper.getJoinWithIn()) == false) {
					count++;
				}
			}
			if (count == joinWithInService.getAllJoinWithIn(sort).size()) {
				JoinWithIn joinWithIn = new JoinWithIn();
				BeanUtils.copyProperties(joinWithInMapper, joinWithIn);
				result = joinWithInService.createJoinWithIn(joinWithIn);
			}
			if (result == false) {
				return new Response(result, HttpStatus.CONFLICT, "failed");
			}
			return new Response(result, HttpStatus.OK, "sucess");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param joinWithInMapper
	 * @param id
	 * @return
	 */
	@Path("/updatejoinWithIn/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody Response updatejoinWithIn(@RequestBody JoinWithInMapper joinWithInMapper,
			@PathParam("id") ObjectId id) {
		try {
			JoinWithIn joinWithIn = new JoinWithIn();
			BeanUtils.copyProperties(joinWithInMapper, joinWithIn);
			joinWithIn.set_id(id);
			boolean result = joinWithInService.updateJoinWithIn(joinWithIn);
			if (result == false) {
				return new Response(result, HttpStatus.CONFLICT, "failed");
			}
			return new Response(result, HttpStatus.OK, "sucess");
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
	@Path("/listJoinWithIn/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public @ResponseBody Response getAlljoinWithIn(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<JoinWithIn> list;
			List<JoinWithInMapper> joinWithInMapper;
			try {
				list = joinWithInService.getAllJoinWithIn(sort, pageNo, pageSize);
				joinWithInMapper = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = joinWithInService.getCount(sort);
			int pages = joinWithInService.getPages(sort, pageSize);
			if (joinWithInMapper == null || joinWithInMapper.size() == 0) {
				return new Response(joinWithInMapper, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(joinWithInMapper, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param joinWithIns
	 * @return
	 */
	public List<JoinWithInMapper> convertDomainToMapperList(List<JoinWithIn> joinWithIns) {
		try {
			List<JoinWithInMapper> list = new ArrayList<JoinWithInMapper>();
			for (JoinWithIn joinWithIn : joinWithIns) {
				JoinWithInMapper joinWithInMapper = new JoinWithInMapper();
				BeanUtils.copyProperties(joinWithIn, joinWithInMapper);
				joinWithInMapper.set_id(joinWithIn.get_id().toString());
				list.add(joinWithInMapper);
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
		JoinWithInMapper joinWith;
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", value);
			JoinWithIn joinWithIn;
			try {
				joinWithIn = joinWithInService.getOneByCondition(condition);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			joinWith = convertDomainToMappar(joinWithIn);
		} catch (RAException e) {
			throw new RAException("Data Not Found !!");
		}
		return new Response(joinWith, HttpStatus.CONFLICT, "No records found");
	}

	/**
	 * 
	 * @param JoinWithIn
	 * @return
	 */
	private JoinWithInMapper convertDomainToMappar(JoinWithIn JoinWithIn) {
		try {
			JoinWithInMapper joinWithInMapper = new JoinWithInMapper();
			BeanUtils.copyProperties(JoinWithIn, joinWithInMapper);
			joinWithInMapper.set_id(JoinWithIn.get_id().toString());

			return joinWithInMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
