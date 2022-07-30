package com.ay.talk.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ay.talk.dto.ReportDto;
import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.entity.Report;
import com.ay.talk.repository.DbRepository;
import com.ay.talk.repository.ServerRepository;
import com.ay.talk.service.ManageService;

@Service
public class ManageServiceImpl implements ManageService{
	private final ModelMapper modelMapper;
	private final ServerRepository serverRepository;
	private final DbRepository dbRepository;
	
	@Autowired
	public ManageServiceImpl(ModelMapper modelMapper, ServerRepository serverRepository, DbRepository dbRepository) {
		super();
		this.modelMapper = modelMapper;
		this.serverRepository = serverRepository;
		this.dbRepository = dbRepository;
	}
	
	@Override
	public List<ReportDto> displayReports() {
		// TODO Auto-generated method stub
		List<Report> reportList=dbRepository.findReports();
		return reportList.stream()
				.map(report -> modelMapper.map(report, ReportDto.class)).collect(Collectors.toList());
	}

	@Override
	public boolean manageReport(ReqSuspend reqSuspend) {
		// TODO Auto-generated method stub
		String[] userInfo=serverRepository.getUserInfo(reqSuspend.getStudentId()).split(",");
		if(!userInfo[1].equals("0"))return false; //이미 정지 회원
		else {
			//정지회원추가
			dbRepository.insertSuspendedUser(reqSuspend);
			serverRepository.addSuspendedUser(reqSuspend.getStudentId()
					,new StringBuilder().append(userInfo[0]+","+reqSuspend.getPeriod()).toString());
			
			//처리 완료된 신고 항목 삭제
			dbRepository.removeReports(reqSuspend.getId());
			return true;
		}
	}

	
	
	
}
