package com.ojas.ra.service;

import com.ojas.ra.domain.AccessToken;

/**
 * 
 * @author skkhadar
 *
 */
public interface TokenService {

	/**
	 * 
	 * @param token
	 * @return
	 */
	public AccessToken getAccessToken(String token);
}
