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
	private final ServerRepository serverRepository; //���� �� �޸� ����
	
	@Autowired
	public ServerServiceImpl(DbRepository dbRepository, ServerRepository serverRepository) {
		this.dbRepository=dbRepository;
		this.serverRepository=serverRepository;
	}
	
	//���� ����� �� �θ޸� ������ �ʱ�ȭ
	@Override
	public void init() {
		
		//�������� �ʱ�ȭ
		List<Subject> subjectList=dbRepository.findSubjects();
		for(int roomId=0;roomId<subjectList.size();roomId++) {
			serverRepository.initRoomIn(subjectList.get(roomId).getName(),roomId);
		}
		serverRepository.initCheckRoomNames(subjectList.size()); //checkRoomNames �ʱ�ȭ
		
		
		//���� �г��� �ʱ�ȭ
		List<RandomName> randomNameList=dbRepository.findRandomNames();
		for(int idx=0; idx<randomNameList.size();idx++) {
			serverRepository.initRandomName(randomNameList.get(idx).getName(),idx);
		}
		
		//����� ������ �����ͼ� fbTokens������ �й��� �����ϴ� fcm��ū���� �ʱ�ȭ
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
		
		//���� ȸ�� ������ �����ͼ� suspendedUsers �ʱ�ȭ
		List<Suspended> suspendedUserList=dbRepository.findSuspendedUserList();
		for(int i=0;i<suspendedUserList.size();i++) {
			serverRepository.initSuspendedUser(suspendedUserList.get(i).getStudentId(), suspendedUserList.get(i).getPeriod());
		}	
	}
	
	//����� �Ű�
	@Override
	public void report(ReqReportMsg reportMsg) {
		UserRoomData userRoomData=new UserRoomData(reportMsg.getReportName(),
				serverRepository.getRoomId(reportMsg.getRoomName()),reportMsg.getRoomName(),reportMsg.getProfessorName());
		dbRepository.insertReport(userRoomData,reportMsg);
	}

}
