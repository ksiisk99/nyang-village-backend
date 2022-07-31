package com.ay.talk.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.ay.talk.entity.RandomName;
import com.ay.talk.entity.Subject;
import com.ay.talk.entity.Suspended;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;

@Repository
public class InMemoryRepository implements ServerRepository{
	private Map<String,Integer> subjects=null; //��� ����(�����,�ε���)
	private Map<String,String> strSubjects=null; //��� ����(�����,�ε������ڿ�);
	private String[] randomNames=null; //�����г��� 200��. �� �濡 �������� �ѷ��ش�.
	private Map<String,Integer> idxRandomNames=null; //�����г���(�г���,�ε���)
	private final int VERSION=1; //������Ʈ ����
	private ListOperations<String, String> roomInNames;	//��ȿ� �ִ� ����ڵ� �����г���(���ȣ, �����г��ӵ�)
	private ListOperations<String, String> roomInTokens;	//��ȿ� �ִ� ����ڵ� ��ū(���ȣ, ������ū��)
	private ValueOperations<String, String> userInfos; //�й��� �ش��ϴ� �ĺ�,�����Ⱓ �����Ⱓ�� ������ 0
	@SuppressWarnings("rawtypes")
	private final RedisTemplate redisTemplate;
	private final DbRepository dbRepository;
	private AtomicIntegerArray checkRoomNames; //��� ��ȿ� �ִ� �����г��� üũ �ߺ�����
	
	
	@Autowired
	public InMemoryRepository(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate
			,DbRepository dbRepository) {
		this.redisTemplate = redisTemplate;
		this.dbRepository=dbRepository;
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
		
		//�������� �ʱ�ȭ
		List<Subject> subjectList=dbRepository.findSubjects();
		for(int roomId=0;roomId<subjectList.size();roomId++) {
			initRoomIn(subjectList.get(roomId).getName(),roomId);
		}
		initCheckRoomNames(subjectList.size()); //checkRoomNames �ʱ�ȭ
		
		
		//���� �г��� �ʱ�ȭ
		List<RandomName> randomNameList=dbRepository.findRandomNames();
		for(int idx=0; idx<randomNameList.size();idx++) {
			initRandomName(randomNameList.get(idx).getName(),idx);
		}
		
		//���� ȸ�� ���� �ʱ�ȭ
		List<Suspended> suspendedUserList=dbRepository.findSuspendedUserList();
		Map<String,String>suspendedUserMap=new HashMap<String,String>();
		for(int i=0;i<suspendedUserList.size();i++) {
			suspendedUserMap.put(suspendedUserList.get(i).getStudentId(), suspendedUserList.get(i).getPeriod());
		}
		
		//����� ���� �ʱ�ȭ
		List<User> userList=dbRepository.findUserList();
		for(int i=0;i<userList.size();i++) {
			List<UserRoomData> roomIds=userList.get(i).getRoomIds();
			String fcm=userList.get(i).getFcm();
			String studentId=userList.get(i).getStudentId();
			String suspendedPriod;
			if(suspendedUserMap.get(studentId)!=null) {
				suspendedPriod=suspendedUserMap.get(studentId);
			}else {
				suspendedPriod="0";
			}
			for(int j=0;j<roomIds.size();j++) {
				initUser(fcm
						,studentId
						,String.valueOf(roomIds.get(j).getRoomId())
						,roomIds.get(j).getNickName()
						,suspendedPriod
						,roomIds.get(j).getRoomName());
			}
		}
	}
	
	//checkRoomNames �ʱ�ȭ
	@Override
	public void initCheckRoomNames(int size) { 
		checkRoomNames=new AtomicIntegerArray(size*200);
	}
	
	//subjects,strSubjects �ʱ�ȭ
	@Override
	public void initRoomIn(String subjectName, int roomId) { 
		
		subjects.put(subjectName, roomId);
		strSubjects.put(subjectName, String.valueOf(roomId));
		roomInTokens.rightPush(String.valueOf(roomId), "a");
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
		checkRoomNames.set((Integer.parseInt(roomId)*200)+(idxRandomNames.get(nickName)), 1);
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
		return (int) (subjects.get(roomName)==null?300:roomInNames.size(roomName));
	}
	
	//�濡 ���� �����г��� üũ
	@Override
	public boolean setCheckRoomName(String roomName, int idx) {
		// TODO Auto-generated method stub
		return checkRoomNames.compareAndSet(subjects.get(roomName)*200+idx, 0, 1);
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
