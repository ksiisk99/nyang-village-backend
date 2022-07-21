package com.ay.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.entity.RandomName;
import com.ay.talk.entity.Subject;
import com.ay.talk.entity.Suspended;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;
import com.ay.talk.repository.DbRepository;
import com.ay.talk.repository.ServerRepository;
import com.ay.talk.service.ServerService;

@Service
public class ServerServiceImpl implements ServerService{

	private final DbRepository dbRepository;
	private final ServerRepository serverRepository; //서버 인 메모리 변수
	
	@Autowired
	public ServerServiceImpl(DbRepository dbRepository, ServerRepository serverRepository) {
		this.dbRepository=dbRepository;
		this.serverRepository=serverRepository;
	}
	
	//서버 재부팅 시 인메모리 변수들 초기화
	@Override
	public void init() {
		
		//수강과목 초기화
		List<Subject> subjectList=dbRepository.findSubjects();
		for(int roomId=0;roomId<subjectList.size();roomId++) {
			serverRepository.initRoomIn(subjectList.get(roomId).getName(),roomId);
		}
		serverRepository.initCheckRoomNames(subjectList.size()); //checkRoomNames 초기화
		
		
		//랜덤 닉네임 초기화
		List<RandomName> randomNameList=dbRepository.findRandomNames();
		for(int idx=0; idx<randomNameList.size();idx++) {
			serverRepository.initRandomName(randomNameList.get(idx).getName(),idx);
		}
		
		//사용자 정보를 가져와서 fbTokens변수에 학번에 대응하는 fcm토큰으로 초기화
		List<User> userList=dbRepository.findUserList();
		for(int i=0;i<userList.size();i++) {
			List<UserRoomData> roomIds=userList.get(i).getRoomIds();
			String fcm=userList.get(i).getFcm();
			String studentId=userList.get(i).getStudentId();
			
			for(int j=0;j<roomIds.size();j++) {
				serverRepository.initUser(fcm,studentId,
						roomIds.get(j).getRoomId(),roomIds.get(j).getNickName());
			}
		}
		
		//정지 회원 정보를 가져와서 suspendedUsers 초기화
		List<Suspended> suspendedUserList=dbRepository.findSuspendedUserList();
		for(int i=0;i<suspendedUserList.size();i++) {
			serverRepository.initSuspendedUser(suspendedUserList.get(i).getStudentId(), suspendedUserList.get(i).getPeriod());
		}	
	}
	
	//사용자 신고
	@Override
	public void report(ReqReportMsg reportMsg) {
		UserRoomData userRoomData=new UserRoomData(reportMsg.getReportName(),
				serverRepository.getRoomId(reportMsg.getRoomName()),reportMsg.getRoomName(),reportMsg.getProfessorName());
		dbRepository.insertReport(userRoomData,reportMsg);
	}

}
