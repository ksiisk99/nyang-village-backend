package com.ay.talk.service;

import com.ay.talk.dto.request.ReqReportMsg;

public interface ServerService {
	//사용자 신고
	void report(ReqReportMsg reportMsg);
	
	//서버 변수 초기화
	void init();
}
