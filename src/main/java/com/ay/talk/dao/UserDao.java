package com.ay.talk.dao;

import java.util.ArrayList;
import java.util.List;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;

public interface UserDao {
	
	//유저 정보들 가져오기
	public List<User> findUserList();

	//새로운 유저 추가
	public void insertUser(User user);

	//유저 정보 가져오기
	public User findUser(String studentId);

	//유저 정보 방에 대해서 업데이트
	public void updateUserRoom(String studentId,ArrayList<UserRoomData> roomIds);

	//유저 정보 방과 토큰에 대해서 업데이트
	public  void updateUserRoomAndToken(String studentId,ArrayList<UserRoomData> roomIds, String fcm);
}