package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.HashMap;

public interface ServerRepository {
		
		//checkRoomNames �ʱ�ȭ
		public void initCheckRoomNames(int size);
		
		//subjects, roomInNames, roomInTokens �ʱ�ȭ
		public void initRoomIn(String subjectName, int roomId);
		
		//���� �г��� �ʱ�ȭ
		public void initRandomName(String name,int idx);
		
		//����� ������ �����ͼ� fbTokens������ �й��� �����ϴ� fcm��ū���� �ʱ�ȭ�ϰ� �濡 �ش��ϴ� �г��� �ʱ�ȭ
		public void initUser(String fcm, String studentId, int roomId, String nickName);
		
		//���� ȸ�� ������ �����ͼ� suspendedUsers �ʱ�ȭ
		public void initSuspendedUser(String studentId, String period);
		
		//���� ��
		public int getVersion();
		
		//���� ȸ�� ��¥ �ֱ� ���� ȸ���� �ƴϸ� null
		public String getSuspendedUser(String studentId);
		
		//���� �Ⱓ Ǯ�� ȸ�� ������ ����
		public void removeSuspendedUser(String studentId);
		
		//ȸ�� fcm��ū
		public String getUserFbToken(String studentId);
		
		//�� �濡 ���� ó�� �����г��� �ε��� �� �ο��� �����ŭ return�ϴ� ������ ���������� �����г����� �Ҵ��ϱ� ����
		public int getStartRandomNickNameIdx(String roomName);
		
		//�� �濡 ���� �����г����� ��������� �ƴ��� Ȯ��
		public boolean isCheckRoomName(String roomName,int idx);
		
		//�濡 ���� �����г��� üũ
		public void setCheckRoomName(String roomName,int idx, boolean check);
		
		//�����г���
		public String getRandomNickName(int idx);
		
		//����� ���� �� ���̵�
		public int getRoomId(String roomName);
		
		//�� ���� ����� �г��ӵ�
		public ArrayList<String> getRoomInNames(String roomName);
		
		//�� ���� ����� ��ū��
		public ArrayList<String> getRoomInTokens(String roomId);
		
		//�� ���� ����� ��ū�� roomId�� ��ȸ
		public ArrayList<String> getRoomInTokens2(int roomId);
		
		//randomNames �ε����� �� �ȿ� ����� �����г��� �߰�
		public void addRoomInName(String roomName,int idx);
		
		//�����г����� �Ű������� �޾� �� �ȿ� ����� �г��� �߰�
		public void addRoomInName2(int roomId,String nickName);
		
		//�� �ȿ� ����� ��ū �߰�
		public void addRoomInToken(String roomName,String fcm);
		
		
		
		//����� ��ū �߰�
		public void addFbToken(String studentId, String fcm);
		
		//�� ���� ����� �г��� ����
		public void removeRoomInName(int roomId, String nickName);
		
		//�� ���� ����� ��ū ����
		public void removeRoomInToken(int roomId, String fcm);
		
		//���� ȸ�� �߰�
		public void addSuspendedUser(String studentId, String period);
}
