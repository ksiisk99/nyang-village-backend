package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRepository implements ServerRepository{
	private boolean settingFlag=false; //�������õ��ȿ� ����� ������ ����
	private Map<String,Integer> subjects=null; //��� ����(�����, �ε���)
	private Map<Integer,ArrayList<String>> roomInTokens=null; //��ȿ� �ִ� ����ڵ� ��ū(���ȣ, ������ū��)
	private Map<Integer,ArrayList<String>> roomInNames=null; //��ȿ� �ִ� ����ڵ� �����г���(���ȣ, �����г��ӵ�)
	private Map<String,String> fbTokens=null; //�����(�й�, �ĺ���ū)
	private boolean[][] checkRoomNames=null; //��� ��ȿ� �ִ� �����г��� üũ �ߺ�����
	private String[] randomNames=null; //�����г��� 200��. �� �濡 �������� �ѷ��ش�.
	private Map<String,Integer> idxRandomNames=null; //�����г���(�г���,�ε���)
	private Map<String,String> suspendedUsers=null; //����ȸ��(�й�,�����Ⱓ)
	private final int VERSION=1; //������Ʈ ����
	
	
	//��� ���� �ʱ�ȭ
	@PostConstruct
	public void init2() {
		subjects=new HashMap<String, Integer>();
		roomInTokens=new HashMap<Integer, ArrayList<String>>();
		roomInNames=new HashMap<Integer, ArrayList<String>>();
		fbTokens=new HashMap<String, String>();
		idxRandomNames=new HashMap<String, Integer>();
		randomNames=new String[200];
		suspendedUsers=new HashMap<String, String>();
	}
	
	//checkRoomNames �ʱ�ȭ
	public void initCheckRoomNames(int size) { 
		checkRoomNames=new boolean[size][200];
	}
	
	//subjects, roomInNames, roomInTokens �ʱ�ȭ
	public void initRoomIn(String subjectName, int roomId) { 
		subjects.put(subjectName, roomId);
		roomInNames.put(roomId, new ArrayList<String>());
		ArrayList<String> tokens=new ArrayList<String>();
		tokens.add("tmp");
		roomInTokens.put(roomId, tokens); //�ʱ� ���� ������ �޽��� ������ ���� ���.
	}
	
	//���� �г��� �ʱ�ȭ
	public void initRandomName(String name,int idx) {
		randomNames[idx]=name;
		idxRandomNames.put(name, idx);
	}
	
	//����� ������ �����ͼ� fbTokens������ �й��� �����ϴ� fcm��ū���� �ʱ�ȭ�ϰ� �濡 �ش��ϴ� �г��� �ʱ�ȭ
	public void initUser(String fcm, String studentId, int roomId, String nickName) {
		fbTokens.put(studentId,fcm); //�й��� �ش��ϴ� �ĺ���ū
		roomInTokens.get(roomId).add(fcm);
		roomInNames.get(roomId).add(nickName);
	}
	
	//���� ȸ�� ������ �����ͼ� suspendedUsers �ʱ�ȭ
	public void initSuspendedUser(String studentId, String period) {
		suspendedUsers.put(studentId, period);
	}
	
	//���� ��
	public int getVersion() {
		return VERSION;
	}
	
	//���� ȸ�� ��¥ �ֱ� ���� ȸ���� �ƴϸ� null
	public String getSuspendedUser(String studentId) {
		return suspendedUsers.get(studentId);
	}
	
	//���� �Ⱓ Ǯ�� ȸ�� ������ ����
	public void removeSuspendedUser(String studentId) {
		suspendedUsers.remove(studentId);
	}
	
	//ȸ�� fcm��ū
	public String getUserFbToken(String studentId) {
		//System.out.println("getUserFbToken "+fbTokens.get(studentId));
		return fbTokens.get(studentId);
	}
	
	//�� �濡 ���� ó�� �����г��� �ε��� �� �ο��� �����ŭ return�ϴ� ������ ���������� �����г����� �Ҵ��ϱ� ����
	public int getStartRandomNickNameIdx(String roomName) {
		return subjects.get(roomName)==null?300: roomInNames.get(subjects.get(roomName)).size();
	}
	
	//�� �濡 ���� �����г����� ��������� �ƴ��� Ȯ��
	public boolean isCheckRoomName(String roomName,int idx) {
		return checkRoomNames[subjects.get(roomName)][idx];
	}
	
	//�濡 ���� �����г��� üũ
	public void setCheckRoomName(String roomName,int idx, boolean check) {
		checkRoomNames[subjects.get(roomName)][idx]=check;
		//System.out.println("checkRoomName"+checkRoomNames[subjects.get(roomName)]);
	}
	
	//�����г���
	public String getRandomNickName(int idx){
		return randomNames[idx];
	}
	
	//����� ���� �� ���̵�
	public int getRoomId(String roomName) {
		return subjects.get(roomName);
	}
	
	//�� ���� ����� �г��ӵ�
	public ArrayList<String> getRoomInNames(String roomName){
		//System.out.println(roomInNames.get(subjects.get(roomName)));
		return roomInNames.get(subjects.get(roomName));
	}
	
	//�� ���� ����� ��ū��
	public ArrayList<String> getRoomInTokens(String roomId){
		//System.out.println(roomInTokens.get(Integer.parseInt(roomId)));
		return roomInTokens.get(Integer.parseInt(roomId));
	}
	
	//�� ���� ����� ��ū�� roomId�� ��ȸ
	public ArrayList<String> getRoomInTokens2(int roomId){
		return roomInTokens.get(roomId);
	}
	
	//randomNames �ε����� �� �ȿ� ����� �����г��� �߰�
	public void addRoomInName(String roomName,int idx) {
		roomInNames.get(subjects.get(roomName)).add(randomNames[idx]);
	}
	
	//�����г����� �Ű������� �޾� �� �ȿ� ����� �г��� �߰�
	public void addRoomInName2(int roomId,String nickName) {
		roomInNames.get(roomId).add(nickName);
		//System.out.println("addRoomInName2:"+roomInNames.get(roomId));
	}
	
	//�� �ȿ� ����� ��ū �߰�
	public void addRoomInToken(String roomName,String fcm) {
		roomInTokens.get(subjects.get(roomName)).add(fcm);
		//System.out.println("addRoomInToken"+roomInTokens.get(subjects.get(roomName)));
	}
	
	
	
	//����� ��ū �߰�
	public void addFbToken(String studentId, String fcm) {
		fbTokens.put(studentId, fcm);
	}
	
	//�� ���� ����� �г��� ����
	public void removeRoomInName(int roomId, String nickName) {
		roomInNames.get(roomId).remove(nickName);
		//System.out.println("removeRoomInName"+roomInNames.get(roomId));
	}
	
	//�� ���� ����� ��ū ����
	public void removeRoomInToken(int roomId, String fcm) {
		roomInTokens.get(roomId).remove(fcm);
		//System.out.println("removeRoomToken"+roomInTokens.get(roomId));
	}

	//���� ȸ�� �߰�
	@Override
	public void addSuspendedUser(String studentId, String period) {
		// TODO Auto-generated method stub
		suspendedUsers.put(studentId, period);
	}
	
	
	
}
