package com.ay.talk.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		// TODO Auto-generated constructor stub
		this.jwtTokenProvider=jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String token=jwtTokenProvider.resolveToken((HttpServletRequest)request);
		boolean valid=jwtTokenProvider.validateToken(token);
		if(token != null && valid) {
			//throw new JwtException("anyang");
			//System.out.println("토큰 존재: "+token);
		}else if(token==null) {
			response.setStatus(401);
			//System.out.println("No Token??");
			//throw new JwtException("토큰 없음");
		}else if(!valid){
			response.setStatus(401);
			//System.out.println("만료된 토큰");
			//throw new JwtException("만료된 토큰");
		}
		filterChain.doFilter(request, response);
	}
	
	
}
