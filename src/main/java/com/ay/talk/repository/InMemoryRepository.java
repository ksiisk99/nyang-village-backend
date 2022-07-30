package com.ay.talk.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRepository implements ServerRepository{
	private Map<String,Integer> subjects=null; //��� ����(�����,�ε���)
	private Map<String,String> strSubjects=null; //��� ����(�����,�ε������ڿ�);
	private boolean[][] checkRoomNames=null; //��� ��ȿ� �ִ� �����г��� üũ �ߺ�����
	private String[] randomNames=null; //�����г��� 200��. �� �濡 �������� �ѷ��ش�.
	private Map<String,Integer> idxRandomNames=null; //�����г���(�г���,�ε���)
	private final int VERSION=1; //������Ʈ ����
	private ListOperations<String, String> roomInNames;	//��ȿ� �ִ� ����ڵ� �����г���(���ȣ, �����г��ӵ�)
	private ListOperations<String, String> roomInTokens;	//��ȿ� �ִ� ����ڵ� ��ū(���ȣ, ������ū��)
	private ValueOperations<String, String> userInfos; //�й��� �ش��ϴ� �ĺ�,�����Ⱓ �����Ⱓ�� ������ 0
	@SuppressWarnings("rawtypes")
	private final RedisTemplate redisTemplate;
	
	
	@Autowired
	public InMemoryRepository(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	//��� ���� �ʱ�ȭ
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		subjects=new HashMap<String, Integer>();
		idxRandomNames=new HashMap<String, Integer>();
		randomNames=new String[200];
		strSubjects=new HashMap<String, String>();
		
		roomInNames=redisTemplate.opsForList();
		roomInTokens=redisTemplate.opsForList();
		userInfos=redisTemplate.opsForValue();
	}
	
	//checkRoomNames �ʱ�ȭ
	@Override
	public void initCheckRoomNames(int size) { 
		checkRoomNames=new boolean[size][200];
	}
	
	//subjects,strSubjects �ʱ�ȭ
	@Override
	public void initRoomIn(String subjectName, int roomId) { 
		subjects.put(subjectName, roomId);
		strSubjects.put(subjectName, String.valueOf(roomId));
	}
	
	//���� �г��� �ʱ�ȭ
	@Override
	public void initRandomName(String name,int idx) {
		randomNames[idx]=name;
		idxRandomNames.put(name, idx);
	}
	
	//����� ������ �����ͼ� �й��� �����ϴ� fcm��ū�� �����Ⱓ �ʱ�ȭ�ϰ� ������ �ʱ�ȭ
	@Override
	public void initUser(String fcm, String studentId, String roomId, String nickName, String suspendedPriod, String roomName) {
		userInfos.set(studentId, new StringBuilder().append(fcm+","+suspendedPriod).toString());
		roomInTokens.rightPush(roomId, fcm);
		roomInNames.rightPush(roomName,nickName);
		checkRoomNames[Integer.parseInt(roomId)][idxRandomNames.get(nickName)]=true;
	}

	
	//���� ��
	@Override
	public int getVersion() {
		return VERSION;
	}
	
	
	//���� �Ⱓ Ǯ�� ȸ�� ������ ����(=��ü)
	@Override
	public void removeSuspendedUser(String studentId, String userInfo) {	
		userInfos.set(studentId, userInfo);
	}
	
	
	//�� �濡 ���� ó�� �����г��� �ε��� �� �ο��� �����ŭ return�ϴ� ������ ���������� �����г����� �Ҵ��ϱ� ����
	@Override
	public int getStartRandomNickNameIdx(String roomName) {
		return (int) (subjects.get(roomName)==null?300:roomInNames.size(roomName)-1);
	}
	
	//�� �濡 ���� �����г����� ��������� �ƴ��� Ȯ��
	@Override
	public boolean isCheckRoomName(String roomName,int idx) {
		return checkRoomNames[subjects.get(roomName)][idx];
	}
	
	//�濡 ���� �����г��� üũ
	@Override
	public void setCheckRoomName(String roomName,int idx, boolean check) {
		checkRoomNames[subjects.get(roomName)][idx]=check;
		//System.out.println("checkRoomName"+checkRoomNames[subjects.get(roomName)]);
	}
	
	//�����г���
	@Override
	public String getRandomNickName(int idx){
		return randomNames[idx];
	}
	
	//����� ���� �� ���̵�
	@Override
	public int getRoomId(String roomName) {
		return subjects.get(roomName);
	}
	
	//�� ���� ����� �г��ӵ�
	@Override
	public List<String> getRoomInNames(String roomName){
		//System.out.println(roomInNames.get(subjects.get(roomName)));
		return roomInNames.range(roomName, 0, -1);
		//return roomInNames.get(subjects.get(roomName));
	}
	
	//�� ���� ����� ��ū��
	@Override
	public List<String> getRoomInTokens(String roomId){
		//System.out.println(roomInTokens.get(Integer.parseInt(roomId)));
		return roomInTokens.range(roomId, 0, -1);
	}
	
	//�� ���� ����� ��ū�� roomId�� ��ȸ
	@Override
	public List<String> getRoomInTokens2(int roomId){
		return roomInTokens.range(String.valueOf(roomId), 0, -1);
	}
	
	//randomNames �ε����� �� �ȿ� ����� �����г��� �߰�
	@Override
	public void addRoomInName(String roomName,int idx) {
		roomInNames.rightPush(roomName, randomNames[idx]);
	}
	
	//�����г����� �Ű������� �޾� �� �ȿ� ����� �г��� �߰�
	@Override
	public void addRoomInName(String roomName,String nickName) {
		roomInNames.rightPush(roomName, nickName);
		//System.out.println("addRoomInName2:"+roomInNames.get(roomId));
	}
	
	//�� �ȿ� ����� ��ū �߰�
	@Override
	public void addRoomInToken(String roomName,String fcm) {
		roomInTokens.rightPush(strSubjects.get(roomName), fcm);
		//System.out.println("addRoomInToken"+roomInTokens.get(subjects.get(roomName)));
	}
	

	
	//�� ���� ����� �г��� ����
	@Override
	public void removeRoomInName(String roomName, String nickName) {
		roomInNames.remove(roomName, 1, nickName);
		//System.out.println("removeRoomInName"+roomInNames.get(roomId));
	}
	
	//�� ���� ����� ��ū ����
	@Override
	public void removeRoomInToken(String roomName, String fcm) {
		roomInTokens.remove(strSubjects.get(roomName), 1, fcm);
		//System.out.println("removeRoomToken"+roomInTokens.get(roomId));
	}

	//���� ȸ�� �߰�
	@Override
	public void addSuspendedUser(String studentId, String userInfo) {
		// TODO Auto-generated method stub
		userInfos.set(studentId, userInfo);
	}
	
	//ȸ�� ���� ��ȸ(fcm,suspendedPriod)
	@Override
	public String getUserInfo(String studentId) {
		return userInfos.get(studentId);
	}
	
	//ȸ�� ���� �߰�
	@Override
	public void addUserInfo(String studentId, String userInfo) {
		userInfos.set(studentId, userInfo);
	}
	
}
