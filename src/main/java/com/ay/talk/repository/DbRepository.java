package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.entity.ChatMsg;
import com.ay.talk.entity.RandomName;
import com.ay.talk.entity.Report;
import com.ay.talk.entity.Subject;
import com.ay.talk.entity.Suspended;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;

public interface DbRepository {
	//��ü ���� ��������
		public List<Subject> findSubjects();
		
		//���� �г��ӵ� ��������
		public List<RandomName> findRandomNames();
		
		//���� ������ ��������
		public List<User> findUserList();
		
		//�� ������ ��������
		//public List<Room> findRoomList();
		
		//���� ���� ������ ��������
		public List<Suspended> findSuspendedUserList();
		
		//���� �Ⱓ Ǯ�� ���� ���� ����
		public void removeSuspendedUser(String studentId);
		
		//���ο� ���� �߰�
		public void insertUser(User user);
		
		//ä�� �޽��� �߰�
		public void insertChatMsg(ChatMsg chatMsg);
		
		//���� ���� ��������
		public User findUser(String studentId);
		
		//���� ���� �濡 ���ؼ� ������Ʈ
		public void updateUserRoom(String studentId,ArrayList<UserRoomData> roomIds);
		
		//���� ���� ��� ��ū�� ���ؼ� ������Ʈ
		public  void updateUserRoomAndToken(String studentId,ArrayList<UserRoomData> roomIds, String fcm);
		
		//���� �Ű�
		public void insertReport(UserRoomData userRoomData, ReqReportMsg reportMsg);
		
		public List<Report> findReports();
		
		//���� ȸ�� �߰�
		public void insertSuspendedUser(ReqSuspend reqSuspend);

		//ó�� �Ϸ�� �Ű� �׸� ����
		public void removeReports(String id);
}
