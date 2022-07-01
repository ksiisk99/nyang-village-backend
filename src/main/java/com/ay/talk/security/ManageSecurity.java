package com.ay.talk.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ay.talk.jwt.JwtTokenProvider;

@Component
public class ManageSecurity {
	JwtTokenProvider jwtTokenProvider;
	String authority="Manager";
	
	@Autowired
	public ManageSecurity(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider=jwtTokenProvider;
	}
	public boolean isJwt(String jwt) {
		List<String> authorities=jwtTokenProvider.getAuthorities(jwt);
		if(authorities==null)return false;
		if(authorities.contains(authority))
			return true;
		return false;
	}
}