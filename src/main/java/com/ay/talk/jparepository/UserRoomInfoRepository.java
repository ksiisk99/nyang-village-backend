package com.ay.talk.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ay.talk.jpaentity.UserRoomInfo;

public interface UserRoomInfoRepository extends JpaRepository<UserRoomInfo, Long>{
	Optional<UserRoomInfo> findUserByRoomNameAndNickName(String roomName, String nickName);
}
