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

import com.ojas.ra.domain.Address;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.AddressMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.AddressService;
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
@Path("/address")
public class AddressResource {

	@Autowired
	private AddressService addressService;

	/**
	 * 
	 * @param addressMapper
	 * @return
	 */
	@POST
	@Path("/createAddress")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAddress(@RequestBody AddressMapper addressMapper) {
		try {
			Address address = new Address();
			BeanUtils.copyProperties(addressMapper, address);
			address.setRegistrationId(new ObjectId(addressMapper.getRegistrationId()));
			Boolean result = addressService.createAddress(address);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "Address not created");
			}
			return new Response("Success", result, HttpStatus.OK, "Address created");

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param addressMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/saveAddress/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAddress(@RequestBody AddressMapper addressMapper, @PathParam("id") ObjectId id) {
		try {
			Address address = new Address();
			BeanUtils.copyProperties(addressMapper, address);
			address.set_id(id);
			Boolean result = addressService.saveAddress(address);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "Address not saved");
			}
			return new Response("Success", result, HttpStatus.OK, "Address saved");
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
	@Path("/deleteAddress/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAddressById(@Context ServletContext context, @PathParam("id") ObjectId id) {
		try {
			Boolean result = addressService.deleteAddress(id);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "Address id not available");
			}
			return new Response("Success", result, HttpStatus.OK, "successfully deleted");

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
	@Path("/listAddresses/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByCondition(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Address> list;
			List<AddressMapper> result;
			try {
				list = addressService.getAllObjects(sort, pageNo, pageSize);
				result = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = addressService.getCount(sort);
			int pages = addressService.getPages(sort, pageSize);
			if (result == null || result.size() == 0) {
				return new Response(result, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(result, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * @Path("/removeByPrimaryId/{id}")
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeByPrimaryId(@Context ServletContext context, @PathParam("id") String id) {
		try {
			Boolean result = addressService.removeByPrimaryId(id);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "Id not found ");
			}
			return new Response("Success", result, HttpStatus.OK, "Successfully removed");
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
			Address address;
			AddressMapper addressMapper;
			try {
				address = addressService.findOneByCondition(condition);
				addressMapper = convertDomainToMappar(address);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (addressMapper == null) {
				return new Response("Failed", addressMapper, HttpStatus.CONFLICT, "Not Found ");
			}
			return new Response(addressMapper, HttpStatus.OK, "success");

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
			Address address;
			AddressMapper addressMapper;
			try {
				address = addressService.findOneByCondition(condition);
				addressMapper = convertDomainToMappar(address);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (addressMapper == null) {
				return new Response(addressMapper, HttpStatus.CONFLICT, "failed ");
			}
			return new Response(addressMapper, HttpStatus.OK, "success");

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
			MongoEqualQuery equal = new MongoEqualQuery();

			equal.setEqualto(valueOftheProperty);
			Map<String, MongoAdvancedQuery> addressMappingcondition = new HashMap<String, MongoAdvancedQuery>();
			addressMappingcondition.put(nameOftheProperty, equal);
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Address> reglist;
			List<AddressMapper> addressMapper;
			try {
				reglist = addressService.advancedFindByCondition(addressMappingcondition, sort, pageNo, pageSize);
				addressMapper = convertDomainToMapperList(reglist);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = addressService.getCount(sort);
			int pages = addressService.getPages(sort, pageSize);
			if (addressMapper == null || addressMapper.size() == 0) {
				return new Response(addressMapper, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(addressMapper, pages, count, HttpStatus.OK, "records found");
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
	@Path("/findAddressesByRegistrationId/{registrationId}/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAddressesByregistrationId(@Context ServletContext context,
			@PathParam("registrationId") String objectId, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoEqualQuery equal = new MongoEqualQuery();

			equal.setEqualto(new ObjectId(objectId));
			Map<String, MongoAdvancedQuery> addressMappingcondition = new HashMap<String, MongoAdvancedQuery>();
			addressMappingcondition.put("registrationId", equal);

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("registrationId");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Address> addresslist;
			List<AddressMapper> addressMapper;
			try {
				addresslist = addressService.advancedFindByCondition(addressMappingcondition, sort, pageNo, pageSize);
				addressMapper = convertDomainToMapperList(addresslist);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = addressService.getCount(sort);
			int pages = addressService.getPages(sort, pageSize);
			if (addressMapper == null || addressMapper.size() == 0) {
				return new Response(addressMapper, pages, count, HttpStatus.CONFLICT, "No records found");
			}
			return new Response(addressMapper, pages, count, HttpStatus.OK, "records found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param addressMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/inactiveOrActive/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inactiveOrActive(@RequestBody AddressMapper addressMapper, @PathParam("id") ObjectId id) {
		try {
			Address address = new Address();
			BeanUtils.copyProperties(addressMapper, address);
			address.set_id(id);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Map<String, Object> target = new HashMap<String, Object>();
			target.put("status", address.getStatus());
			boolean result = addressService.updateMapByCondition(condition, target);
			if (result == false) {
				return new Response("InActive ", result, HttpStatus.CONFLICT, "Failed ");
			}
			return new Response("Active", result, HttpStatus.OK, "Success");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param addresss
	 * @return
	 */
	private List<AddressMapper> convertDomainToMapperList(List<Address> addresss) {
		try {
			List<AddressMapper> list = new ArrayList<AddressMapper>();
			for (Address address : addresss) {
				AddressMapper addressMapper = new AddressMapper();
				BeanUtils.copyProperties(address, addressMapper);
				addressMapper.set_id(address.get_id().toString());
				list.add(addressMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param address
	 * @return
	 */
	private AddressMapper convertDomainToMappar(Address address) {
		try {
			AddressMapper addressMapper = new AddressMapper();
			BeanUtils.copyProperties(address, addressMapper);
			addressMapper.set_id(address.get_id().toString());
			return addressMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
