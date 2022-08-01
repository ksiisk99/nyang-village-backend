package com.ay.talk.controller;


import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.web.bind.annotation.RestController;

import com.ay.talk.config.MongoConfig;
import com.ay.talk.dto.Msg;
import com.ay.talk.dto.request.ReqConnectChat;
import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.service.ChatService;
import com.ay.talk.service.FcmService;
import com.ay.talk.service.ManageService;
import com.google.firebase.messaging.FirebaseMessagingException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.ApiOperation;





/**
 * Handles requests for the application home page.
 */
@ApiOperation("swagger")
@RestController
public class ChatController{
	LoggerContext loggerContext=(LoggerContext) LoggerFactory.getILoggerFactory();
	ch.qos.logback.classic.Logger mongoLogger=loggerContext.getLogger("org.mongodb.driver");
	ch.qos.logback.classic.Logger redisLogger=loggerContext.getLogger("io.lettuce.core");
	private final ManageService manageService;
	private final FcmService fcmService;
	private final ChatService chatService;
	private Logger logger;
	
	@Autowired
	public ChatController(FcmService fcmService ,SimpMessagingTemplate simpMessagingTemplate
			,ChatService chatService,ManageService manageService) {
		this.fcmService=fcmService;
		this.chatService=chatService;
		this.manageService=manageService;
		logger=LogManager.getLogger();
	}
	
	@PostConstruct
	public void init() {
		mongoLogger.setLevel(Level.OFF);
		redisLogger.setLevel(Level.OFF);
	}
	
	@MessageMapping(value = "/ay/chat") //모바일채팅
	public void Message(Msg msg) throws FirebaseMessagingException {
		long beforeTime = System.currentTimeMillis();
		
		//System.out.println("Tokens: "+roomInTokens.get(Integer.parseInt(msg.getRoomId())));
		
		chatService.sendMsg(msg);
		
		fcmService.sendMsg(msg);	
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
//		System.out.println("chat : "+secDiffTime+"ms");
		logger.info("chat: {}ms",secDiffTime);
	}
	
	@MessageMapping(value = "/ay/pc/chat") //pc채팅
	public void PcMessage(Msg msg) throws FirebaseMessagingException {
		long beforeTime = System.currentTimeMillis();
		
		//System.out.println("Tokens: "+roomInTokens.get(Integer.parseInt(msg.getRoomId())));
		
		chatService.sendPcMsg(msg);
		
		fcmService.sendPcMsg(msg);	
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
//		System.out.println("chat : "+secDiffTime+"ms");
		logger.info("pcchat: {}ms",secDiffTime);
	}
	
	@MessageMapping(value="/ay/connectchat") //첫 웹소켓 연결 시 이중로그인, 정지유무와 새학기인지를 확인한다.
	public void startConnect(ReqConnectChat cc) {
		long beforeTime = System.currentTimeMillis();
		
		chatService.connectChat(cc);
		
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
		logger.info("connectchat: {}ms",secDiffTime);
//		System.out.println("connectchat : "+secDiffTime+"ms");
	}
	
	@MessageMapping(value="/ay/report") //신고
	public void Report(ReqReportMsg reportMsg) {
		long beforeTime = System.currentTimeMillis();
	
		manageService.report(reportMsg);
		
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
		logger.info("report: {}ms",secDiffTime);
//		System.out.println("report : "+secDiffTime+"ms");
	}
	
	
}
