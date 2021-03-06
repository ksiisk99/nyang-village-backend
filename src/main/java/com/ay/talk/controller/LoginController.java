package com.ay.talk.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.ay.talk.dto.response.ResLogin;
import com.ay.talk.dto.response.ResLogout;
import com.ay.talk.dto.response.ResPcLogin;
import com.ay.talk.jpaentity.Member;
import com.ay.talk.jparepository.MemberRepository;
import com.ay.talk.dto.request.ReqLogin;
import com.ay.talk.dto.request.ReqLogout;
import com.ay.talk.dto.request.ReqPcLogin;
import com.ay.talk.service.LoginService;
import com.ay.talk.service.ServerService;
import com.google.firebase.messaging.FirebaseMessagingException;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Handles requests for the application home page.
 */
@ApiOperation("swagger")
@Api(tags = "?α??? API")
@RestController
@RequestMapping("/ay")
@PropertySource("classpath:application.properties")
public class LoginController{
	//private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LoginController.class);
	LoggerContext loggerContext=(LoggerContext) LoggerFactory.getILoggerFactory();
	ch.qos.logback.classic.Logger mongoLogger=loggerContext.getLogger("org.mongodb.driver");
	private final ServerService serverService;
	private final LoginService loginService;
	private Logger logger;
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	public LoginController(ServerService serverService, LoginService loginService) {
		this.serverService=serverService;
		this.loginService=loginService;
		logger=LogManager.getLogger();	
		//this.memberRepository=memberRepository;
	}
	
	@EventListener(ContextRefreshedEvent.class)
	public void ServerSetting() throws IOException {
		//???????? ?αװ? ???? ?߻??? ?̰? ???´?.
		mongoLogger.setLevel(Level.OFF);
		//fcmService.Init(); //fcm????
		serverService.init(); //???? ?? ?޸??? ?ʱ?ȭ
		logger.info("server start");
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws FirebaseMessagingException 
	 * @throws IOException 
	 */
	@GetMapping(path ="/jpa")
	public void goood() {
		Member member=new Member("memberB");
		memberRepository.save(member);
		System.out.println(member.getId());
//		if(savedMember==null) {
//			System.out.println("NULL");
//		}else {
//			System.out.println(savedMember.getId());
//		}
	}

		
	//?????? ?α???
	@PostMapping(path = "/login")
	@ApiOperation(value = "?????? ?α???", notes = "?????? ???? ?α???")
	public @ResponseBody ResLogin login(@RequestBody ReqLogin reqLogin) throws FirebaseMessagingException, IOException {
		long beforeTime = System.currentTimeMillis();

		ResLogin resLogin=loginService.login(reqLogin);

		long afterTime = System.currentTimeMillis(); // ?ڵ? ???? ?Ŀ? ?ð? ?޾ƿ???
		long secDiffTime = (afterTime - beforeTime); //?? ?ð??? ?? ????
		logger.info("login: {}ms",secDiffTime);
		return resLogin;
	}
	
	//pc ?α???
	@PostMapping(path="/pc/login")
	@ApiOperation(value = "PC ?α???", notes = "PC ???? ?α???")
	public @ResponseBody ResPcLogin pcLogin(@RequestBody ReqPcLogin reqPcLogin) throws IOException {
	
		return loginService.pcLogin(reqPcLogin);
	}

	//?α׾ƿ?
	@PostMapping(path="/logout")
	@ApiOperation(value = "?α׾ƿ?", notes = "?α׾ƿ?")
	public @ResponseBody ResLogout logout(@RequestBody ReqLogout reqLogout) {
		return loginService.logout(reqLogout);
	}
	
	
}
