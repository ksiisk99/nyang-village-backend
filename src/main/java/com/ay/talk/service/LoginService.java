package com.ay.talk.service;

import java.io.IOException;
import java.util.ArrayList;

import com.ay.talk.dto.SubjectInfo;
import com.ay.talk.dto.request.ReqLogin;
import com.ay.talk.dto.request.ReqLogout;
import com.ay.talk.dto.request.ReqPcLogin;
import com.ay.talk.dto.response.ResLogin;
import com.ay.talk.dto.response.ResLogout;
import com.ay.talk.dto.response.ResPcLogin;
import com.ay.talk.entity.UserRoomData;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface LoginService {
	
	//����� �α���
	public ResLogin login(ReqLogin reqLogin) throws FirebaseMessagingException, IOException;
	
	//����Ͽ��� ��û�� ũ�Ѹ� ����
	public ArrayList<SubjectInfo> Crawling(String studentId, String password) throws IOException;
	
	//pc���� ��û�� ũ�Ѹ� ����
	public void pcCrawling(ReqPcLogin reqPcLogin, ResPcLogin resPcLogin) throws IOException;
	 
	//pc �α���
	public ResPcLogin pcLogin(ReqPcLogin reqPcLogin) throws IOException; 
	
	public ResLogout logout(ReqLogout reqLogout);
}
