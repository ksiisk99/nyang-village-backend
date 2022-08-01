package com.ay.talk.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.ay.talk.dto.Msg;
import com.ay.talk.repository.ServerRepository;
import com.ay.talk.service.FcmService;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;


@Service
@PropertySource("classpath:application.properties")
public class FcmServiceImpl implements FcmService{

	@Value("${fcm.path}")
	private String path;
	private FirebaseMessaging instance;
	private FirebaseApp app=null;
	private final ServerRepository serverRepository;
	private ApnsConfig apnsConfig;
	private AndroidConfig androidConfig;
	
	@Autowired
	public FcmServiceImpl(ServerRepository serverRepository) {
		this.serverRepository=serverRepository;
	}
	
	
	//파이어베이스 세팅
	@PostConstruct
	public void Init() throws IOException{
		List<FirebaseApp> firebaseApps=FirebaseApp.getApps();
		
		if(firebaseApps!=null && !firebaseApps.isEmpty()) {
			for(FirebaseApp tmpApp:firebaseApps) {
				if(tmpApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
					app=tmpApp;
				}
			}
			this.instance=FirebaseMessaging.getInstance(app);
		}else {
			GoogleCredentials googleCredentials=GoogleCredentials.fromStream(new ClassPathResource(path).getInputStream());
			FirebaseOptions options=FirebaseOptions.builder()
					.setCredentials(googleCredentials).build();
			FirebaseApp app=FirebaseApp.initializeApp(options);
			this.instance=FirebaseMessaging.getInstance(app);
		}
		apnsConfig=ApnsConfig.builder()
				.setAps(Aps.builder().build())
				.putHeader("apns-priority", "5")
				.putHeader("content_available", "1")
				.putHeader("apns-push_type", "background")
				.putHeader("apns-expiration","1604750400").build();
		androidConfig=AndroidConfig.builder()
				.setTtl(86400000)
				.setPriority(Priority.HIGH).build();
	}
	
	//입장 퇴장 fcm
	@Override
	public void enterExitMsg(int type, int roomId, String nickName) throws FirebaseMessagingException {
		//type 0:입장 / 1:퇴장 / 2:메시지
		MulticastMessage message=MulticastMessage.builder() //fcm 전송
				.putData("type", String.valueOf(type))
				.putData("roomId", String.valueOf(roomId))
				.putData("nickName", nickName)
				.setAndroidConfig(androidConfig)
				.setApnsConfig(apnsConfig)
				.addAllTokens(serverRepository.getRoomInTokens2(roomId)).build();

		ApiFuture<BatchResponse> response=instance.sendMulticastAsync(message);
	}
	
	//메시지 fcm
	@Override
	public void sendMsg(Msg msg) throws FirebaseMessagingException {
		MulticastMessage message=MulticastMessage.builder() //fcm 전송
				.putData("type", String.valueOf(msg.getType()))
				.putData("roomId", msg.getRoomId())
				.putData("nickName", msg.getNickName())
				.putData("content", msg.getContent())
				.putData("time", msg.getTime())
				.putData("pmType", "0")
				.setAndroidConfig(androidConfig)
				.setApnsConfig(apnsConfig)
				.addAllTokens(serverRepository.getRoomInTokens(msg.getRoomId())).build();
				
		ApiFuture<BatchResponse> response=instance.sendMulticastAsync(message);			
	}


	@Override
	public void sendPcMsg(Msg msg) throws FirebaseMessagingException {
		// TODO Auto-generated method stub
		MulticastMessage message=MulticastMessage.builder() //fcm 전송
				.putData("type", String.valueOf(msg.getType()))
				.putData("roomId", msg.getRoomId())
				.putData("nickName", msg.getNickName())
				.putData("content", msg.getContent())
				.putData("time", msg.getTime())
				.putData("pmType", "1")
				.setAndroidConfig(androidConfig)
				.setApnsConfig(apnsConfig)
				.addAllTokens(serverRepository.getRoomInTokens(msg.getRoomId())).build();
				
		ApiFuture<BatchResponse> response=instance.sendMulticastAsync(message);
		
	}
	
	

}