package com.ay.talk.dao;

import com.ay.talk.entity.ChatMsg;

public interface ChatMsgDao {
	//채팅 메시지 추가
	public void insertChatMsg(ChatMsg chatMsg);
}
