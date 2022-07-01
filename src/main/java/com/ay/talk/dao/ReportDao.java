package com.ay.talk.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.entity.Report;
import com.ay.talk.entity.UserRoomData;

public interface ReportDao {
	//유저 신고
	public void insertReport(UserRoomData userRoomData, ReqReportMsg reportMsg);
	
	//신고 목록
	public List<Report> findReports();
	
	//처리 완료된 신고 항목 삭제
	public void removeReports(String id);
}
