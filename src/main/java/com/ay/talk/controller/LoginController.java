package com.ay.talk.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.ay.talk.dto.response.ResLogin;
import com.ay.talk.dto.response.ResPcLogin;
import com.ay.talk.dto.request.ReqLogin;
import com.ay.talk.dto.request.ReqPcLogin;
import com.ay.talk.service.LoginService;
import com.ay.talk.service.ServerService;
import com.google.firebase.messaging.FirebaseMessagingException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.ApiOperation;





/**
 * Handles requests for the application home page.
 */
@ApiOperation("swagger")
@RestController
@RequestMapping("/ay")
@PropertySource("classpath:application.properties")
public class LoginController{
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LoginController.class);
	LoggerContext loggerContext=(LoggerContext) LoggerFactory.getILoggerFactory();
	ch.qos.logback.classic.Logger logger2=loggerContext.getLogger("org.mongodb.driver");	
	private final ServerService serverService;
	private final LoginService loginService;
	private Logger logger;
	
	@Autowired
	public LoginController(ServerService serverService, LoginService loginService) {
		this.serverService=serverService;
		this.loginService=loginService;
		logger=LogManager.getLogger();
	}
	
	@EventListener(ContextRefreshedEvent.class)
	public void ServerSetting() throws IOException {
		//몽고디비 로그가 계속 발생함 이걸 막는다.
		logger2.setLevel(Level.OFF);
		//fcmService.Init(); //fcm세팅
		serverService.init(); //서버 인 메모리 초기화
		logger.info("server start");
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws FirebaseMessagingException 
	 * @throws IOException 
	 */
	

		
	//모바일 로그인
	@PostMapping(path = "/login")
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
	public @ResponseBody ResPcLogin pcLogin(@RequestBody ReqPcLogin reqPcLogin) throws IOException {
	
		return loginService.pcLogin(reqPcLogin);
	}

	
	
}
