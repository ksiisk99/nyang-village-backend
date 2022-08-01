package com.ay.talk.service;

import java.util.List;

import com.ay.talk.dto.ReportDto;
import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.dto.request.ReqSuspend;

public interface ManageService {
	List<ReportDto> displayReports(); //신고목록
	boolean manageReport(ReqSuspend reqSuspend); //신고대상자 정지날짜 주기
	void report(ReqReportMsg reportMsg); //신고하기
}