package com.ojas.ra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.ojas.ra.MongoDBClient;
import com.ojas.ra.dao.PaymentDAO;
import com.ojas.ra.domain.Payment;
import com.ojas.ra.exception.RAException;
import com.ojas.ra.service.PaymentService;
import com.ojas.ra.util.MongoSortVO;
import com.ojas.ra.util.MongoUtil;

/**
 * 
 * @author skkhadar
 *
 */
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	PaymentDAO paymentDAO;

	@Autowired
	MongoDBClient mongoDBClient;

	@Override
	public boolean createPayment(Payment payment) throws RAException {
		boolean b;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			paymentDAO.setPojo(new Payment());
			paymentDAO.getCollection("payment", db);

			b = paymentDAO.insert(payment);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean savePayment(Payment payment) throws RAException {
		boolean b;
		try {
			DB db = initializeDB();
			paymentDAO.getCollection("payment", db);
			paymentDAO.setPojo(new Payment());
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("_id", payment.get_id());
			b = paymentDAO.updateMapByCondition(condition, MongoUtil.getObjectByDBObject(payment));
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return b;
	}

	private DB initializeDB() {
		DB db = mongoDBClient.getReadMongoDB();
		paymentDAO.setPojo(new Payment());
		return db;
	}

	@Override
	public List<Payment> getAllObjects(MongoSortVO sort, int pageNo, int pageSize) throws RAException {
		List<Payment> list;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			paymentDAO.getCollection("payment", db);
			paymentDAO.setPojo(new Payment());
			try {
				list = paymentDAO.getAllObjects(sort, pageNo, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return list;
	}

	@Override
	public Payment findOneByCondition(Map<String, Object> condition) throws RAException {
		Payment payment;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			paymentDAO.setPojo(new Payment());
			paymentDAO.getCollection("payment", db);
			try {
				payment = paymentDAO.findOneByCondition(condition);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return payment;
	}

	@Override
	public Payment findOneByPrimaryId(String value) throws RAException {
		Payment payment;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			paymentDAO.setPojo(new Payment());
			paymentDAO.getCollection("payment", db);
			try {
				payment = paymentDAO.findOneByPrimaryId(value);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
			return payment;
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
	}

	@Override
	public int getPages(MongoSortVO sort, int pageSize) {
		int pages;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			paymentDAO.setPojo(new Payment());
			paymentDAO.getCollection("payment", db);
			try {
				pages = paymentDAO.getPages(sort, pageSize);
			} catch (RAException e) {
				mongoDBClient.closeMongoClient();
				throw new RAException("Data Not Found !!");
			}
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return pages;
	}

	@Override
	public int getCount(MongoSortVO sort) {
		int count;
		try {
			DB db = mongoDBClient.getReadMongoDB();
			paymentDAO.setPojo(new Payment());
			paymentDAO.getCollection("payment", db);

			count = paymentDAO.getCount(sort);
			mongoDBClient.closeMongoClient();
		} catch (RAException e) {
			mongoDBClient.closeMongoClient();
			throw new RAException(e.getMessage());
		}
		return count;
	}
}
