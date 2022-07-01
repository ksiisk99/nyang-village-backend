package com.ay.talk.jwt;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

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
	private long tokenValidTime; //2��
	private Key key;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
		this.key=Keys.hmacShaKeyFor(keyBytes);
	}



	public String createToken(String studentId, List<String> authorities) {
		Claims claims=Jwts.claims().setSubject(studentId);
		claims.put("authorities", authorities);
		Date now=new Date();
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime()+tokenValidTime))
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String resolveToken(HttpServletRequest request) { //������� ��ū ����
		return request.getHeader("AY_TOKEN");
	}
	
	public String getStudentId(String token) { //��ū���� �й� ��������
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateToken(String token) { //��ū ��ȿ�� �˻�
		try {
			return !Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
		}catch(Exception e) {
			return false;
		}
	}
	
	public ArrayList<String> getAuthorities(String token){ //���� ��������
		if(validateToken(token)) {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("authorities",ArrayList.class);
		}
		return null;
	}
}
