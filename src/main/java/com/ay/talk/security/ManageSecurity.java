package com.ay.talk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ay.talk.jpaentity.Authority;
import com.ay.talk.jwt.JwtTokenProvider;

@Component
public class ManageSecurity {
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public ManageSecurity(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider=jwtTokenProvider;
	}
	public boolean isJwt(String jwt) {
		Authority authority=jwtTokenProvider.getAuthority(jwt);
		if(authority==null)return false;
		if(authority==Authority.Manager)
			return true;
		return false;
	}
}