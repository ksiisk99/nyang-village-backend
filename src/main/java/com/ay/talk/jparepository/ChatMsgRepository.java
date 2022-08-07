package com.ay.talk.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ay.talk.jpaentity.ChatMsg;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long>{

}
