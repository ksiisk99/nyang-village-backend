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
import com.google.firebase.messaging.FirebaseMessagingException;

public interface LoginService {
	
	//모바일 로그인
	public ResLogin login(ReqLogin reqLogin) throws FirebaseMessagingException, IOException;
	
	//모바일에서 요청한 크롤링 정보
	public ArrayList<SubjectInfo> Crawling(String studentId, String password) throws IOException;
	
	//pc에서 요청한 크롤링 정보
	public void pcCrawling(ReqPcLogin reqPcLogin, ResPcLogin resPcLogin) throws IOException;
	 
	//pc 로그인
	public ResPcLogin pcLogin(ReqPcLogin reqPcLogin) throws IOException; 
	
	//로그아웃
	public ResLogout logout(ReqLogout reqLogout);
}