package com.ay.talk.service;

import com.ay.talk.dto.request.ReqReportMsg;

public interface ServerService {
	//����� �Ű�
	void report(ReqReportMsg reportMsg);
	
	//���� ���� �ʱ�ȭ
	void init();
}
