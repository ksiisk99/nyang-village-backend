package com.ay.talk.service;

import java.io.IOException;

import com.ay.talk.dto.Msg;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface FcmService {
	//�޽��� ����
	void sendMsg(Msg msg) throws FirebaseMessagingException;
	//���� ���� �޽���
	void enterExitMsg(int type, int roomId, String nickName) throws FirebaseMessagingException;
	//fcm ���� �ʱ�ȭ
	//void Init() throws IOException;
	//pc �޽��� ����
	void sendPcMsg(Msg msg) throws FirebaseMessagingException;
}
