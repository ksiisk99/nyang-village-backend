package com.ay.talk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.WebSphereRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.ay.talk.security.StompHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	private final StompHandler stompHandler;
	RequestUpgradeStrategy upgradeStrategy=new TomcatRequestUpgradeStrategy();
	
	@Autowired
	public WebSocketConfig(StompHandler stompHandler) {
		this.stompHandler=stompHandler;
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// TODO Auto-generated method stub
		//WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
		registry.enableSimpleBroker("/sub");
		registry.setApplicationDestinationPrefixes("/pub");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// TODO Auto-generated method stub
		//WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
		registry.addEndpoint("/stomp")
				.setAllowedOrigins("*")
				.setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()));	
				//.withSockJS();
				//.addInterceptors(new HttpSessionHandshakeInterceptor()) //현재 구동되고 있는 서버와 다른 도메인에서도 접근 가능
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// TODO Auto-generated method stub
		registration.interceptors(stompHandler);
	}
}