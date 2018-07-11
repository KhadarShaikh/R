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

import com.ojas.ra.domain.Payment;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.mapper.PaymentMapper;
import com.ojas.ra.response.Response;
import com.ojas.ra.service.PaymentService;
import com.ojas.ra.util.MongoOrderByEnum;
import com.ojas.ra.util.MongoSortVO;

/**
 * 
 * @author skkhadar
 *
 */
@Component
@Path("/payment")
public class PaymentResource {

	@Autowired
	PaymentService paymentService;

	/**
	 * 
	 * @param paymentMapper
	 * @return
	 */
	@Path("/createPayment")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPayment(@RequestBody PaymentMapper paymentMapper) {
		try {
			Payment payment = new Payment();
			BeanUtils.copyProperties(paymentMapper, payment);
			payment.setRegistrationId(new ObjectId(paymentMapper.getRegistrationId()));
			Boolean result = paymentService.createPayment(payment);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "payment not created");
			}
			return new Response("Success", result, HttpStatus.OK, "payment created");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param paymentMapper
	 * @param id
	 * @return
	 */
	@Path("/savePayment/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response savePayment(@RequestBody PaymentMapper paymentMapper, @PathParam("id") ObjectId id) {
		try {
			Payment payment = new Payment();
			BeanUtils.copyProperties(paymentMapper, payment);
			payment.set_id(id);
			payment.setRegistrationId(new ObjectId(paymentMapper.getRegistrationId()));
			Boolean result = paymentService.savePayment(payment);
			if (result == false) {
				return new Response("Failed", result, HttpStatus.CONFLICT, "payment not saved");
			}
			return new Response("Success", result, HttpStatus.OK, "payment saved");

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
	@Path("/listPayments/{pageNo}/{pageSize}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllByCondition(@Context ServletContext context, @PathParam("pageNo") int pageNo,
			@PathParam("pageSize") int pageSize) {
		try {
			MongoSortVO sort = new MongoSortVO();
			sort.setPrimaryKey("_id");
			sort.setPrimaryOrderBy(MongoOrderByEnum.DESC);
			List<Payment> list;
			List<PaymentMapper> mapperList;
			try {
				list = paymentService.getAllObjects(sort, pageNo, pageSize);
				mapperList = convertDomainToMapperList(list);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			int count = paymentService.getCount(sort);
			int pages = paymentService.getPages(sort, pageSize);
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
	 * @param nameOftheProperty
	 * @param valueOftheProperty
	 * @return
	 */
	@Path("/findPaymentByCondition/{nameOftheProperty}/{valueOftheProperty}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findPaymentByCondition(@Context ServletContext context,
			@PathParam("nameOftheProperty") String nameOftheProperty,
			@PathParam("valueOftheProperty") String valueOftheProperty) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put(nameOftheProperty, valueOftheProperty);
			Payment payment;
			PaymentMapper paymentMapper;
			try {
				payment = paymentService.findOneByCondition(condition);
				paymentMapper = convertDomainToMappar(payment);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (paymentMapper == null) {
				return new Response("Failed", paymentMapper, HttpStatus.CONFLICT, "No record found");
			}
			return new Response("Success", paymentMapper, HttpStatus.OK, "record found");
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
	@Path("/findPaymentByPrimaryId/{value}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findPaymentByPrimaryId(@Context ServletContext context, @PathParam("value") ObjectId value)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", value);
			Payment payments;
			PaymentMapper payment;
			try {
				payments = paymentService.findOneByCondition(condition);
				payment = convertDomainToMappar(payments);
			} catch (RAException e) {
				return new Response(HttpStatus.CONFLICT, "No records found");
			}
			if (payment == null) {
				return new Response("Failed", payment, HttpStatus.CONFLICT, "No record found");
			}
			return new Response("Success", payment, HttpStatus.OK, "record found");
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param payments
	 * @return
	 */
	private List<PaymentMapper> convertDomainToMapperList(List<Payment> payments) {
		try {
			List<PaymentMapper> list = new ArrayList<PaymentMapper>();
			for (Payment payment : payments) {
				PaymentMapper paymentMapper = new PaymentMapper();
				BeanUtils.copyProperties(payment, paymentMapper);
				paymentMapper.set_id(payment.get_id().toString());
				list.add(paymentMapper);
			}
			return list;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param payment
	 * @return
	 */
	private PaymentMapper convertDomainToMappar(Payment payment) {
		try {
			PaymentMapper paymentMapper = new PaymentMapper();
			BeanUtils.copyProperties(payment, paymentMapper);
			paymentMapper.set_id(payment.get_id().toString());
			return paymentMapper;
		} catch (RAException e) {
			throw new RAException(e.getMessage());
		}
	}
}
