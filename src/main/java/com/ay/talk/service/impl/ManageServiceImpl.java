package com.ay.talk.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ay.talk.dto.ReportDto;
import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.jpaentity.Report;
import com.ay.talk.jpaentity.Suspended;
import com.ay.talk.jpaentity.UserRoomInfo;
import com.ay.talk.jparepository.ReportRepository;
import com.ay.talk.jparepository.SuspendedRepository;
import com.ay.talk.jparepository.UserRoomInfoRepository;
import com.ay.talk.repository.ServerRepository;
import com.ay.talk.service.ManageService;

@Service
public class ManageServiceImpl implements ManageService{
	private final ModelMapper modelMapper;
	private final ServerRepository serverRepository;
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private SuspendedRepository suspendedRepository;
	@Autowired
	private UserRoomInfoRepository userRoomInfoRepository;
	
	@Autowired
	public ManageServiceImpl(ModelMapper modelMapper, ServerRepository serverRepository) {
		super();
		this.modelMapper = modelMapper;
		this.serverRepository = serverRepository;
	}
	
	@Override
	public List<ReportDto> displayReports() {
		// TODO Auto-generated method stub
		List<Report> reportList=reportRepository.findAll();
		
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
			suspendedRepository.save(new Suspended(reqSuspend.getStudentId(), reqSuspend.getPeriod()
					, reqSuspend.getReportContent(), reqSuspend.getReportWhy()));
			serverRepository.addSuspendedUser(reqSuspend.getStudentId()
					,new StringBuilder().append(userInfo[0]+","+reqSuspend.getPeriod()).toString());
			
			//처리 완료된 신고 항목 삭제
			reportRepository.deleteById(reqSuspend.getId());
			return true;
		}
	}

	//사용자 신고
	@Override
	public void report(ReqReportMsg reportMsg) {
		UserRoomInfo userRoomInfo=userRoomInfoRepository
				.findUserByRoomNameAndNickName(reportMsg.getRoomName(), reportMsg.getReportName()).get();	
		reportRepository.save(new Report(reportMsg.getRoomName(), reportMsg.getReportName(), 
				reportMsg.getReportContent(), reportMsg.getReportWhy(), 
				reportMsg.getReporter(), reportMsg.getStudentId(),userRoomInfo.getUser().getStudentId()));
	}
	
	
	
}