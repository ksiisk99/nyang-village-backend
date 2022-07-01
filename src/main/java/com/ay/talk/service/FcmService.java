package com.ay.talk.service;

import java.io.IOException;

import com.ay.talk.dto.Msg;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface FcmService {
	//메시지 전송
	void sendMsg(Msg msg) throws FirebaseMessagingException;
	//입장 퇴장 메시지
	void enterExitMsg(int type, int roomId, String nickName) throws FirebaseMessagingException;
	//fcm 설정 초기화
	//void Init() throws IOException;
	//pc 메시지 전송
	void sendPcMsg(Msg msg) throws FirebaseMessagingException;
}
