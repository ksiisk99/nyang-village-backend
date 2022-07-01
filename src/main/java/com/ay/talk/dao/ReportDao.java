package com.ay.talk.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.entity.Report;
import com.ay.talk.entity.UserRoomData;

public interface ReportDao {
	//���� �Ű�
	public void insertReport(UserRoomData userRoomData, ReqReportMsg reportMsg);
	
	//�Ű� ���
	public List<Report> findReports();
	
	//ó�� �Ϸ�� �Ű� �׸� ����
	public void removeReports(String id);
}
