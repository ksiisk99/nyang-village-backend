package com.ay.talk.repository;

import java.util.List;

public interface ServerRepository {
		
		//checkRoomNames �ʱ�ȭ
		public void initCheckRoomNames(int size);
		
		//subjects, roomInNames, roomInTokens �ʱ�ȭ
		public void initRoomIn(String subjectName, int roomId);
		
		//���� �г��� �ʱ�ȭ
		public void initRandomName(String name,int idx);
		
		//����� ������ �����ͼ� �й��� �����ϴ� fcm��ū�� �����Ⱓ �ʱ�ȭ�ϰ� ������ �ʱ�ȭ
		public void initUser(String fcm, String studentId, String roomId, String nickName, String suspendedPriod,String roomName);
		
		//���� ��
		public int getVersion();
		
		//���� �Ⱓ Ǯ�� ȸ�� ������ ����(=��ü)
		public void removeSuspendedUser(String studentId, String fcm);
		

		
		//�� �濡 ���� ó�� �����г��� �ε��� �� �ο��� �����ŭ return�ϴ� ������ ���������� �����г����� �Ҵ��ϱ� ����
		public int getStartRandomNickNameIdx(String roomName);
		
		//�濡 ���� �����г��� üũ
		public boolean setCheckRoomName(String roomName,int idx);
		
		//�����г���
		public String getRandomNickName(int idx);
		
		//����� ���� �� ���̵�
		public int getRoomId(String roomName);
		
		//�� ���� ����� �г��ӵ�
		public List<String> getRoomInNames(String roomName);
		
		//�� ���� ����� ��ū��
		public List<String> getRoomInTokens(String roomId);
		
		//�� ���� ����� ��ū�� roomId�� ��ȸ
		public List<String> getRoomInTokens2(int roomId);
		
		//randomNames �ε����� �� �ȿ� ����� �����г��� �߰�
		public void addRoomInName(String roomName,int idx);
		
		//�����г����� �Ű������� �޾� �� �ȿ� ����� �г��� �߰�
		public void addRoomInName(String roomName,String nickName);
		
		//�� �ȿ� ����� ��ū �߰�
		public void addRoomInToken(String roomName,String fcm);
		
		
		
		//�� ���� ����� �г��� ����
		public void removeRoomInName(String roomName, String nickName);
		
		//�� ���� ����� ��ū ����
		public void removeRoomInToken(String roomName, String fcm);
		
		//���� ȸ�� �߰�
		public void addSuspendedUser(String studentId, String userInfo);
		
		//ȸ�� ���� ��ȸ(fcm,suspendedPriod)
		public String getUserInfo(String studentId);
		
		//ȸ�� ���� �߰�
		public void addUserInfo(String studentId, String userInfo);
}
