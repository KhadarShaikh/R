package com.ojas.ra;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ojas.ra.exception.RAException;

/**
 * 
 * @author skkhadar
 *
 */
public class MongoServiceFactory implements FactoryBean<MongoDBClient> {

	@Autowired
	MongoConfig mongoConfig;

	@Override
	public MongoDBClient getObject() throws RAException {
		return new MongoDBClientImpl(mongoConfig);
	}

	@Override
	public Class<?> getObjectType() {
		return MongoDBClientImpl.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
