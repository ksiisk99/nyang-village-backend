package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;




@Repository
public class MongoRepository implements DbRepository{
	private final ChatMsgDao chatMsgDao;
	private final ReportDao reportDao;
	private final SubjectDao subjectDao;
	private final SuspendedDao suspendedDao;
	private final UserDao userDao;
	private final RandomNameDao randomNameDao;
	private final MongoTemplate mongoTemplate;
	private final MongoDatabase mongoDatabase;
//	private MongoCollection<ChatMsg> chatMsgCollection; //ä�� �޽��� insert
//	private MongoCollection<Report> reportCollection; //�Ű� insert
	
//	@Autowired
//	public MongoRepository(MongoTemplate mongoTemplate, MongoDatabase mongoDatabase) {
//		this.mongoTemplate=mongoTemplate; //find ��
//		this.mongoDatabase=mongoDatabase; //insert update��
//	}
//	
//	@PostConstruct
//	public void initMongoCollections() {
//		chatMsgCollection=mongoDatabase.getCollection("chat",ChatMsg.class); //ä�ø޽��� �÷��� �ʱ�ȭ
//		reportCollection=mongoDatabase.getCollection("reports",Report.class); //����� �Ű� �÷��� �ʱ�ȭ
//	}

	@Autowired
	public MongoRepository(ChatMsgDao chatMsgDao, ReportDao reportDao, SubjectDao subjectDao, SuspendedDao suspendedDao,
			UserDao userDao, RandomNameDao randomNameDao,MongoTemplate mongoTemplate, MongoDatabase mongoDatabase) {
			this.chatMsgDao = chatMsgDao;
			this.reportDao = reportDao;
			this.subjectDao = subjectDao;
			this.suspendedDao = suspendedDao;
			this.userDao = userDao;
			this.randomNameDao=randomNameDao;
			this.mongoTemplate=mongoTemplate; //find ��
			this.mongoDatabase=mongoDatabase; //insert update��
	}
	
	//��ü ���� ��������
	public List<Subject> findSubjects(){ 
		return subjectDao.findSubjects();
	}

	//���� �г��ӵ� ��������
	public List<RandomName> findRandomNames(){ 
		return randomNameDao.findRandomNames();
	}
	
	//���� ������ ��������
	public List<User> findUserList(){ 
		return userDao.findUserList();
	}
	
	//�� ������ ��������
//	public List<Room> findRoomList(){
//		//return mongoTemplate.findAll(Room.class);
//	}
	
	//���� ���� ������ ��������
	public List<Suspended> findSuspendedUserList(){
		return suspendedDao.findSuspendedUserList();
	}
	
	//���� �Ⱓ Ǯ�� ���� ���� ����
	public void removeSuspendedUser(String studentId) {
		suspendedDao.removeSuspendedUser(studentId);
	}
	
	//���ο� ���� �߰�
	public void insertUser(User user) {
		userDao.insertUser(user);
	}
	
	//ä�� �޽��� �߰�
	public void insertChatMsg(ChatMsg chatMsg) {
		chatMsgDao.insertChatMsg(chatMsg);
	}
	
	//���� ���� ��������
	public User findUser(String studentId) {
		return userDao.findUser(studentId);
	}
	
	//���� ���� �濡 ���ؼ� ������Ʈ
	public void updateUserRoom(String studentId,ArrayList<UserRoomData> roomIds) {
		userDao.updateUserRoom(studentId, roomIds);
	}
	
	//���� ���� ��� ��ū�� ���ؼ� ������Ʈ
	public  void updateUserRoomAndToken(String studentId,ArrayList<UserRoomData> roomIds, String fcm) {
		userDao.updateUserRoomAndToken(studentId, roomIds, fcm);
	}
	
	//���� �Ű�
	public void insertReport(UserRoomData userRoomData, ReqReportMsg reportMsg) {
		reportDao.insertReport(userRoomData, reportMsg);
	}
	
	//���� ���
	@Override
	public List<Report> findReports() {
		// TODO Auto-generated method stub
		return reportDao.findReports();
	}

	//���� ȸ�� �߰�
	@Override
	public void insertSuspendedUser(ReqSuspend reqSuspend) {
		// TODO Auto-generated method stub
		suspendedDao.insertSuspendedUser(reqSuspend);
	}

	//ó�� �Ϸ�� �Ű� �׸� ����
	@Override
	public void removeReports(String id) {
		// TODO Auto-generated method stub
		reportDao.removeReports(id);
	}
	
	
}
