package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.ay.talk.dao.ChatMsgDao;
import com.ay.talk.dao.RandomNameDao;
import com.ay.talk.dao.ReportDao;
import com.ay.talk.dao.SubjectDao;
import com.ay.talk.dao.SuspendedDao;
import com.ay.talk.dao.UserDao;
import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.entity.ChatMsg;
import com.ay.talk.entity.RandomName;
import com.ay.talk.entity.Report;
import com.ay.talk.entity.Subject;
import com.ay.talk.entity.Suspended;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;




@Repository
public class MongoRepository implements DbRepository{
	private final ChatMsgDao chatMsgDao;
	private final ReportDao reportDao;
	private final SubjectDao subjectDao;
	private final SuspendedDao suspendedDao;
	private final UserDao userDao;
	private final RandomNameDao randomNameDao;


	@Autowired
	public MongoRepository(ChatMsgDao chatMsgDao, ReportDao reportDao, SubjectDao subjectDao, SuspendedDao suspendedDao,
			UserDao userDao, RandomNameDao randomNameDao) {
			this.chatMsgDao = chatMsgDao;
			this.reportDao = reportDao;
			this.subjectDao = subjectDao;
			this.suspendedDao = suspendedDao;
			this.userDao = userDao;
			this.randomNameDao=randomNameDao;
	}
	
	//전체 과목 가져오기
	public List<Subject> findSubjects(){ 
		return subjectDao.findSubjects();
	}

	//랜덤 닉네임들 가져오기
	public List<RandomName> findRandomNames(){ 
		return randomNameDao.findRandomNames();
	}
	
	//유저 정보들 가져오기
	public List<User> findUserList(){ 
		return userDao.findUserList();
	}
	
	//방 정보들 가져오기
//	public List<Room> findRoomList(){
//		//return mongoTemplate.findAll(Room.class);
//	}
	
	//정지 유저 정보들 가져오기
	public List<Suspended> findSuspendedUserList(){
		return suspendedDao.findSuspendedUserList();
	}
	
	//정지 기간 풀린 유저 정보 삭제
	public void removeSuspendedUser(String studentId) {
		suspendedDao.removeSuspendedUser(studentId);
	}
	
	//새로운 유저 추가
	public void insertUser(User user) {
		userDao.insertUser(user);
	}
	
	//채팅 메시지 추가
	public void insertChatMsg(ChatMsg chatMsg) {
		chatMsgDao.insertChatMsg(chatMsg);
	}
	
	//유저 정보 가져오기
	public User findUser(String studentId) {
		return userDao.findUser(studentId);
	}
	
	//유저 정보 방에 대해서 업데이트
	public void updateUserRoom(String studentId,ArrayList<UserRoomData> roomIds) {
		userDao.updateUserRoom(studentId, roomIds);
	}
	
	//유저 정보 방과 토큰에 대해서 업데이트
	public  void updateUserRoomAndToken(String studentId,ArrayList<UserRoomData> roomIds, String fcm) {
		userDao.updateUserRoomAndToken(studentId, roomIds, fcm);
	}
	
	//유저 신고
	public void insertReport(UserRoomData userRoomData, ReqReportMsg reportMsg) {
		reportDao.insertReport(userRoomData, reportMsg);
	}
	
	//유저 목록
	@Override
	public List<Report> findReports() {
		// TODO Auto-generated method stub
		return reportDao.findReports();
	}

	//정지 회원 추가
	@Override
	public void insertSuspendedUser(ReqSuspend reqSuspend) {
		// TODO Auto-generated method stub
		suspendedDao.insertSuspendedUser(reqSuspend);
	}

	//처리 완료된 신고 항목 삭제
	@Override
	public void removeReports(String id) {
		// TODO Auto-generated method stub
		reportDao.removeReports(id);
	}
	
	
}