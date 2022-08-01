package com.ay.talk.dao;

import com.ay.talk.entity.ChatMsg;

public interface ChatMsgDao {
	//채팅메시지 저장
	public void insertChatMsg(ChatMsg chatMsg);
}
