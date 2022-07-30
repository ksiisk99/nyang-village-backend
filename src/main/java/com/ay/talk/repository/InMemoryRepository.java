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
	private Map<String,Integer> subjects=null; //모든 과목(과목명,인덱스)
	private Map<String,String> strSubjects=null; //모든 과목(과목명,인덱스문자열);
	private boolean[][] checkRoomNames=null; //모든 방안에 있는 랜덤닉네임 체크 중복제거
	private String[] randomNames=null; //랜덤닉네임 200개. 각 방에 랜덤으로 뿌려준다.
	private Map<String,Integer> idxRandomNames=null; //랜덤닉네임(닉네임,인덱스)
	private final int VERSION=1; //업데이트 버전
	private ListOperations<String, String> roomInNames;	//방안에 있는 사용자들 랜덤닉네임(방번호, 유저닉네임들)
	private ListOperations<String, String> roomInTokens;	//방안에 있는 사용자들 토큰(방번호, 유저토큰들)
	private ValueOperations<String, String> userInfos; //학번에 해당하는 파베,정지기간 정지기간이 없으면 0
	@SuppressWarnings("rawtypes")
	private final RedisTemplate redisTemplate;
	
	
	@Autowired
	public InMemoryRepository(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	//모든 변수 초기화
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
	
	//checkRoomNames 초기화
	@Override
	public void initCheckRoomNames(int size) { 
		checkRoomNames=new boolean[size][200];
	}
	
	//subjects,strSubjects 초기화
	@Override
	public void initRoomIn(String subjectName, int roomId) { 
		subjects.put(subjectName, roomId);
		strSubjects.put(subjectName, String.valueOf(roomId));
	}
	
	//랜덤 닉네임 초기화
	@Override
	public void initRandomName(String name,int idx) {
		randomNames[idx]=name;
		idxRandomNames.put(name, idx);
	}
	
	//사용자 정보를 가져와서 학번에 대응하는 fcm토큰과 정지기간 초기화하고 방정보 초기화
	@Override
	public void initUser(String fcm, String studentId, String roomId, String nickName, String suspendedPriod, String roomName) {
		userInfos.set(studentId, new StringBuilder().append(fcm+","+suspendedPriod).toString());
		roomInTokens.rightPush(roomId, fcm);
		roomInNames.rightPush(roomName,nickName);
		checkRoomNames[Integer.parseInt(roomId)][idxRandomNames.get(nickName)]=true;
	}

	
	//버전 비교
	@Override
	public int getVersion() {
		return VERSION;
	}
	
	
	//정지 기간 풀린 회원 데이터 삭제(=교체)
	@Override
	public void removeSuspendedUser(String studentId, String userInfo) {	
		userInfos.set(studentId, userInfo);
	}
	
	
	//각 방에 대한 처음 랜덤닉네임 인덱스 방 인원의 사이즈만큼 return하는 이유는 순차적으로 랜덤닉네임을 할당하기 때문
	@Override
	public int getStartRandomNickNameIdx(String roomName) {
		return (int) (subjects.get(roomName)==null?300:roomInNames.size(roomName)-1);
	}
	
	//각 방에 대한 랜덤닉네임을 사용중인지 아닌지 확인
	@Override
	public boolean isCheckRoomName(String roomName,int idx) {
		return checkRoomNames[subjects.get(roomName)][idx];
	}
	
	//방에 대한 랜덤닉네임 체크
	@Override
	public void setCheckRoomName(String roomName,int idx, boolean check) {
		checkRoomNames[subjects.get(roomName)][idx]=check;
		//System.out.println("checkRoomName"+checkRoomNames[subjects.get(roomName)]);
	}
	
	//랜덤닉네임
	@Override
	public String getRandomNickName(int idx){
		return randomNames[idx];
	}
	
	//과목명에 대한 방 아이디값
	@Override
	public int getRoomId(String roomName) {
		return subjects.get(roomName);
	}
	
	//방 안의 사용자 닉네임들
	@Override
	public List<String> getRoomInNames(String roomName){
		//System.out.println(roomInNames.get(subjects.get(roomName)));
		return roomInNames.range(roomName, 0, -1);
		//return roomInNames.get(subjects.get(roomName));
	}
	
	//방 안의 사용자 토큰들
	@Override
	public List<String> getRoomInTokens(String roomId){
		//System.out.println(roomInTokens.get(Integer.parseInt(roomId)));
		return roomInTokens.range(roomId, 0, -1);
	}
	
	//방 안의 사용자 토큰들 roomId로 조회
	@Override
	public List<String> getRoomInTokens2(int roomId){
		return roomInTokens.range(String.valueOf(roomId), 0, -1);
	}
	
	//randomNames 인덱스로 방 안에 사용자 랜덤닉네임 추가
	@Override
	public void addRoomInName(String roomName,int idx) {
		roomInNames.rightPush(roomName, randomNames[idx]);
	}
	
	//랜덤닉네임을 매개변수로 받아 방 안에 사용자 닉네임 추가
	@Override
	public void addRoomInName(String roomName,String nickName) {
		roomInNames.rightPush(roomName, nickName);
		//System.out.println("addRoomInName2:"+roomInNames.get(roomId));
	}
	
	//방 안에 사용자 토큰 추가
	@Override
	public void addRoomInToken(String roomName,String fcm) {
		roomInTokens.rightPush(strSubjects.get(roomName), fcm);
		//System.out.println("addRoomInToken"+roomInTokens.get(subjects.get(roomName)));
	}
	

	
	//방 안의 사용자 닉네임 삭제
	@Override
	public void removeRoomInName(String roomName, String nickName) {
		roomInNames.remove(roomName, 1, nickName);
		//System.out.println("removeRoomInName"+roomInNames.get(roomId));
	}
	
	//방 안의 사용자 토큰 삭제
	@Override
	public void removeRoomInToken(String roomName, String fcm) {
		roomInTokens.remove(strSubjects.get(roomName), 1, fcm);
		//System.out.println("removeRoomToken"+roomInTokens.get(roomId));
	}

	//정지 회원 추가
	@Override
	public void addSuspendedUser(String studentId, String userInfo) {
		// TODO Auto-generated method stub
		userInfos.set(studentId, userInfo);
	}
	
	//회원 정보 조회(fcm,suspendedPriod)
	@Override
	public String getUserInfo(String studentId) {
		return userInfos.get(studentId);
	}
	
	//회원 정보 추가
	@Override
	public void addUserInfo(String studentId, String userInfo) {
		userInfos.set(studentId, userInfo);
	}
	
}
