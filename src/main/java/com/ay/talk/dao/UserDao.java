package com.ay.talk.dao;

import java.util.ArrayList;
import java.util.List;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;


public interface UserDao {
	
	//���� ������ ��������
	public List<User> findUserList();

	//���ο� ���� �߰�
	public void insertUser(User user);

	//���� ���� ��������
	public User findUser(String studentId);

	//���� ���� �濡 ���ؼ� ������Ʈ
	public void updateUserRoom(String studentId,ArrayList<UserRoomData> roomIds);

	//���� ���� ��� ��ū�� ���ؼ� ������Ʈ
	public  void updateUserRoomAndToken(String studentId,ArrayList<UserRoomData> roomIds, String fcm);
}
