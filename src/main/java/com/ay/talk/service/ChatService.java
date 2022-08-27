package com.ay.talk.service;

import com.ay.talk.dto.Msg;
import com.ay.talk.dto.request.ReqConnectChat;

public interface ChatService {
	//입장 퇴장 메시지 전송
	void enterExitMsg(int type, int roomId, String nickName);
	//채팅 메시지 전송
	void sendMsg(Msg msg);
	//첫 웹소켓 연결 시 버전, 이중로그인, 정지유무, 새학기인지를 확인한다.
	void connectChat(ReqConnectChat cc);
	//pc 채팅 메시지 전송
	//void sendPcMsg(Msg msg);
}