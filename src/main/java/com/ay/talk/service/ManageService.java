package com.ay.talk.service;

import java.util.List;

import com.ay.talk.dto.ReportDto;
import com.ay.talk.dto.request.ReqSuspend;

public interface ManageService {
	List<ReportDto> displayReports(); //�Ű���
	boolean manageReport(ReqSuspend reqSuspend); //�Ű����� ������¥ �ֱ�
}
