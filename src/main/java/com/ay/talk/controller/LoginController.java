package com.ay.talk.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.ay.talk.dto.response.ResLogin;
import com.ay.talk.dto.response.ResLogout;
import com.ay.talk.dto.response.ResPcLogin;
import com.ay.talk.dto.request.ReqLogin;
import com.ay.talk.dto.request.ReqLogout;
import com.ay.talk.dto.request.ReqPcLogin;
import com.ay.talk.service.LoginService;
import com.google.firebase.messaging.FirebaseMessagingException;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Handles requests for the application home page.
 */
@ApiOperation("swagger")
@Api(tags = "로그인 API")
@RestController
@RequestMapping("/ay")
@PropertySource("classpath:application.properties")
public class LoginController{
	LoggerContext loggerContext=(LoggerContext) LoggerFactory.getILoggerFactory();
	ch.qos.logback.classic.Logger redisLogger=loggerContext.getLogger("io.lettuce.core");
	private final LoginService loginService;
	private Logger logger;
	
	@Autowired
	public LoginController(LoginService loginService) {
		this.loginService=loginService;
		logger=LogManager.getLogger();
	}
	
	@PostConstruct
	public void init() {
		redisLogger.setLevel(Level.OFF);
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws FirebaseMessagingException 
	 * @throws IOException 
	 */

	//모바일 로그인
	@PostMapping(path = "/login")
	@ApiOperation(value = "모바일 로그인", notes = "모바일 전용 로그인")
	public @ResponseBody ResLogin login(@RequestBody ReqLogin reqLogin) throws FirebaseMessagingException, IOException {
		long beforeTime = System.currentTimeMillis();

		ResLogin resLogin=loginService.login(reqLogin);

		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
		logger.info("login: {}ms",secDiffTime);
		return resLogin;
	}
	
	//pc 로그인
	@PostMapping(path="/pc/login")
	@ApiOperation(value = "PC 로그인", notes = "PC 전용 로그인")
	public @ResponseBody ResPcLogin pcLogin(@RequestBody ReqPcLogin reqPcLogin) throws IOException {
		long beforeTime = System.currentTimeMillis();
		ResPcLogin resPcLogin=loginService.pcLogin(reqPcLogin);
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
		logger.info("pcLogin: {}ms",secDiffTime);
		return resPcLogin;
	}

	//로그아웃
	@PostMapping(path="/logout")
	@ApiOperation(value = "로그아웃", notes = "로그아웃")
	public @ResponseBody ResLogout logout(@RequestBody ReqLogout reqLogout) {
		long beforeTime = System.currentTimeMillis();
		ResLogout resLogout=loginService.logout(reqLogout);		
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
		logger.info("logout: {}ms",secDiffTime);
		return resLogout;
	}
	
	
}