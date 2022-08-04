package com.ay.talk.jwt;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ay.talk.jpaentity.Authority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenProvider implements InitializingBean{
	@Value("${jwt.secret.key}")
	private String secretKey;
	@Value("${jwt.valid.time}")
	private long tokenValidTime; //2분
	private Key key;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
		this.key=Keys.hmacShaKeyFor(keyBytes);
	}



	public String createToken(String studentId,Authority authority) {
		Claims claims=Jwts.claims().setSubject(studentId);
		claims.put("authority", authority);
		Date now=new Date();
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime()+tokenValidTime))
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String resolveToken(HttpServletRequest request) { //헤더에서 토큰 추출
		return request.getHeader("AY_TOKEN");
	}
	
	public String getStudentId(String token) { //토큰에서 학번 가져오기
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateToken(String token) { //토큰 유효성 검사
		try {
			return !Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
		}catch(Exception e) {
			return false;
		}
	}
	
	public Authority getAuthority(String token){ //권한 가져오기
		if(validateToken(token)) {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("authority",Authority.class);
		}
		return null;
	}
}