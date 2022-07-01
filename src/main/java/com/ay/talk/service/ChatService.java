package com.ay.talk.service;

import com.ay.talk.dto.Msg;
import com.ay.talk.dto.request.ReqConnectChat;

public interface ChatService {
	//���� ���� �޽��� ����
	void enterExitMsg(int type, int roomId, String nickName);
	//ä�� �޽��� ����
	void sendMsg(Msg msg);
	//ù ������ ���� �� ����, ���߷α���, ��������, ���б������� Ȯ���Ѵ�.
	void connectChat(ReqConnectChat cc);
	//pc ä�� �޽��� ����
	void sendPcMsg(Msg msg);
}
