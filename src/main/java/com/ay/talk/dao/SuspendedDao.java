package com.ay.talk.dao;

import java.util.List;

import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.entity.Suspended;

public interface SuspendedDao {
	//���� ���� ������ ��������
	public List<Suspended> findSuspendedUserList();

	//���� �Ⱓ Ǯ�� ���� ���� ����
	public void removeSuspendedUser(String studentId);
	
	//���� ȸ�� �߰�
	public void insertSuspendedUser(ReqSuspend reqSuspend);
}
