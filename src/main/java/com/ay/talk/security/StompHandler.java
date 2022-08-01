package com.ay.talk.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.ay.talk.jwt.JwtTokenProvider;

@Component
public class StompHandler implements ChannelInterceptor{
	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public StompHandler(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider=jwtTokenProvider;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// TODO Auto-generated method stub
		StompHeaderAccessor accessor=StompHeaderAccessor.wrap(message);
		
		if(accessor.getCommand()==StompCommand.CONNECT) {
			if(!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("jwt"))) {//stomp 연결 전 jwt 인증
				throw new AccessDeniedException("No JWT");
			}
		}
		return ChannelInterceptor.super.preSend(message, channel);
	}
}
