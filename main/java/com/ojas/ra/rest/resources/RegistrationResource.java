package com.ojas.ra.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ojas.ra.domain.PlanMapping;
import com.ojas.ra.domain.Plans;
import com.ojas.ra.domain.Registration;
import com.ojas.ra.domain.Role;
import com.ojas.ra.domain.RoleMapping;
import com.ojas.ra.domain.UserAccount;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.RegistrationMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.PlanMappingService;
import com.ojas.ra.service.PlansService;
import com.ojas.ra.service.RegistrationService;
import com.ojas.ra.service.RoleMappingService;
import com.ojas.ra.service.RoleService;
import com.ojas.ra.service.UserService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;
import com.ojas.ra.util.SendMail;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/registration")
public class RegistrationResource {

	@Autowired
	private RegistrationService registrationService;
	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;
	@Autowired
	PlansService plansService;
	@Autowired
	RoleMappingService roleMappingService;
	@Autowired
	PlanMappingService planMappingService;
	@Autowired
	SendMail sendMail;

	/**
	 * 
	 * @param registration
	 * @return
	 */
	@POST
	@Path("/createRegistration")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createResgister(@RequestBody RegistrationMapper registration) {
		boolean result = false;
		Registration reg = new Registration();
		BeanUtils.copyProperties(registration, reg);
		try {
			UserAccount accounts;
			try {
				accounts = userService.loadUserByUsername(registration.getMailId());
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (null == accounts) {
				reg.setStatus("Active");
				// setting dates
				Calendar calender = Calendar.getInstance();
				reg.setRegisteredDate(calender.getTime());
				calender.setTime(calender.getTime());
				calender.add(calender.DATE, 90);
				reg.setExpiryDate(calender.getTime());
				result = registrationService.create(reg);
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("mailId", registration.getMailId());
				Registration regs;
				try {
					regs = registrationService.findOneByCondition(condition);
				} catch (RAException e) {
					return new Response(HttpStatus.CONFLICT, "No records found");
				}
				if (result) {
					UserAccount userAccount = new UserAccount();
					userAccount.setUsername(registration.getMailId());
					MessageDigest md;
					try {
						md = MessageDigest.getInstance("MD5");

						byte[] messageDigest = md.digest(registration.getMailId().getBytes());
						BigInteger number = new BigInteger(1, messageDigest);
						String hashtext = number.toString(16);
						userAccount.setPassword(hashtext);
						userAccount.setRegistrationId(regs.get_id());
						userAccount.setStatus("active");
						userAccount.setRegistrationType((registration.getRegistrationType()));
						userService.save(userAccount);
						UserAccount account;
						try {
							account = userService.loadUserByUsername(userAccount.getUsername());
							condition.put("username", account.getUsername());
						} catch (RAException e) {
							return new Response(HttpStatus.CONFLICT, "No records found");
						}
						Map<String, Object> rolecondition = new HashMap<String, Object>();

						rolecondition.put("roleName", "SUPERADMIN");
						Role role;
						try {
							role = roleService.findOneByCondition(rolecondition);
						} catch (RAException e) {
							return new Response(HttpStatus.CONFLICT, "No records found");
						}
						RoleMapping mapping = new RoleMapping();
						mapping.setRole_id(role.get_id());
						mapping.setUser_id(account.get_id());
						roleMappingService.createRoleMapping(mapping);
						if (null != registration.getPlanId()) {
							Map<String, Object> plancondition = new HashMap<String, Object>();
							plancondition.put("_id", new ObjectId(registration.getPlanId()));
							Plans plan;
							try {
								plan = plansService.getPlansByPrimaryId(plancondition);
							} catch (RAException e) {
								return new Response(HttpStatus.CONFLICT, "No records found");
							}
							PlanMapping mapp = new PlanMapping();
							mapp.setPlan_id(plan.get_id());
							mapp.setRegistrationId(regs.get_id());
							planMappingService.create(mapp);
						}
						String subject = "Successfully Registered..";
						String msg = "your username is: " + registration.getMailId() + "and password is"
								+ userAccount.getPassword();
						// sendMail.sendMail(registration.getMailId(), subject,
						// msg);

					} catch (NoSuchAlgorithmException e) {
						throw new RAException(e.getMessage());
					}
				}
			} else {
				return new Response("Already Registerd with given Email Id " + accounts.getUsername());
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
	 * @param uploadFile
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@Path("/uploadFile/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("uploadFile") InputStream uploadFile, @PathParam("id") ObjectId id)
			throws IOException {
		try {
			Registration registration = new Registration();
			registration.set_id(id);
			registration.setUploadFile(IOUtils.toByteArray(uploadFile));
			Boolean result = registrationService.save(registration);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "not uploaded");
			}
			return new Response("Success", result, HttpStatus.OK, "uploaded");

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param registrationMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/saveRegistration/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveRegister(@RequestBody RegistrationMapper registrationMapper, @PathParam("id") ObjectId id) {
		try {
			Registration registration = new Registration();
			BeanUtils.copyProperties(registrationMapper, registration);
			registration.set_id(id);
			registration.setRegisteredDate(registration.getRegisteredDate());
			Boolean result = registrationService.save(registration);
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
	@DELETE
	@Path("/deleteRegistration/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRegisterById(@Context ServletContext context, @PathParam("id") ObjectId id) {
		try {
			Boolean result = registrationService.delete(id);
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
	@Path("/listRegistrations/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByCondition(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Registration> list;
			List<RegistrationMapper> mapperList;
			try {
				list = registrationService.getAllObjects(sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = registrationService.getCount(sort);
			int pages = registrationService.getPages(sort, pageSize);
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
	 * @return
	 */
	@Path("/listRegistrations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByCondition(@Context ServletContext context) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Registration> list;
			List<RegistrationMapper> mapperList;
			try {
				list = registrationService.getAllObjects(sort);
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
	 * @param id
	 * @return
	 */
	@Path("/removeByPrimaryId/{id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeByPrimaryId(@Context ServletContext context, @PathParam("id") String id) {
		try {
			Boolean result = registrationService.removeByPrimaryId(id);
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
	@Path("/findOneByCondition/{nameOftheProperty}/{valueOftheProperty}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOneByCondition(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put(nameOftheProperty, valueOftheProperty);
			Registration registration;
			RegistrationMapper mapper;
			try {
				registration = registrationService.findOneByCondition(condition);
				mapper = convertDomainToMappar(registration);
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
			Registration reg;
			RegistrationMapper mapper;
			try {
				reg = registrationService.findOneByCondition(condition);
				mapper = convertDomainToMappar(reg);
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
			List<Registration> reglist;
			List<RegistrationMapper> mapperList;
			try {
				reglist = registrationService.advancedFindByCondition(nameOftheProperty, valueOftheProperty, sort,
						pageNo, pageSize);
				mapperList = convertDomainToMapperList(reglist);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = registrationService.getCount(sort);
			int pages = registrationService.getPages(sort, pageSize);
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
	 * @param registrationMapper
	 * @param id
	 * @return
	 */
	@PUT
	@Path("/inactiveOrActive/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inactiveOrActive(@RequestBody RegistrationMapper registrationMapper, @PathParam("id") ObjectId id) {
		try {
			Registration registration = new Registration();
			BeanUtils.copyProperties(registrationMapper, registration);
			registration.set_id(id);
			registration.setRegisteredDate(registration.getRegisteredDate());
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", id);
			Map<String, Object> target = new HashMap<String, Object>();
			target.put("status", registration.getStatus());

			Map<String, Object> condition1 = new HashMap<String, Object>();
			condition1.put("registrationId", id);
			Map<String, Object> target1 = new HashMap<String, Object>();

			target1.put("status", registration.getStatus());
			userService.updateMapByCondition(condition1, target1);

			target.put("status", registration.getStatus());
			Boolean result = registrationService.updateMapByCondition(condition, target);
			if (result == false) {
				return new Response("Failed", HttpStatus.CONFLICT);
			}
			return new Response("Success", HttpStatus.OK);

		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	@GET
	@Path("/listRegisteredSowusersByRegId/{regId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listSowusers(@Context ServletContext context, @PathParam("regId") String regId) {
		String errormsg = null;
		Registration registration = null;
		String[] sowIds = null;
		Map<String, Object> condition = new HashMap<String, Object>();
		List<RegistrationMapper> sowRegistrationMapperList = null;
		List<Registration> sowList = null;
		condition.put("_id", new ObjectId(regId));

		registration = registrationService.findOneByCondition(condition);

		if (registration != null) {
			try {
				sowList = new ArrayList<Registration>();
				sowIds = registration.getSowUser().split(",");
			} catch (Exception e) {
				errormsg = "no sow feild in Registration withId ";
				return new Response(HttpStatus.CONFLICT, sowList, errormsg);
			}

			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);

			List<Registration> registrationList = registrationService.getAllObjects(sort);
			if (registrationList.size() != 0) {
				for (Registration registration1 : registrationList) {

					for (String sowId : sowIds) {
						if (sowId.equals(registration1.get_id().toString()))
							sowList.add(registration1);
					}

				}
				sowRegistrationMapperList = convertDomainToMapperList(sowList);

			} else {
				errormsg = " sowregistrations not found";
				return new Response(HttpStatus.CONFLICT, sowList, errormsg);
			}

			return new Response(HttpStatus.OK, sowRegistrationMapperList, errormsg);
		} else {

			errormsg = "no registraions Found";
			return new Response(HttpStatus.CONFLICT, sowRegistrationMapperList, errormsg);
		}
	}

	/**
	 * 
	 * @param registrations
	 * @return
	 */
	private List<RegistrationMapper> convertDomainToMapperList(List<Registration> registrations) {
		try {
			List<RegistrationMapper> list = new ArrayList<RegistrationMapper>();
			for (Registration registration : registrations) {
				RegistrationMapper registrationMapper = new RegistrationMapper();
				BeanUtils.copyProperties(registration, registrationMapper);
				registrationMapper.set_id(registration.get_id().toString());
				list.add(registrationMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param registration
	 * @return
	 */
	private RegistrationMapper convertDomainToMappar(Registration registration) {
		try {
			RegistrationMapper registrationMapper = new RegistrationMapper();
			BeanUtils.copyProperties(registration, registrationMapper);
			registrationMapper.set_id(registration.get_id().toString());
			return registrationMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
