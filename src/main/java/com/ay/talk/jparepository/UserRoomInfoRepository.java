package com.ay.talk.jparepository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ay.talk.jpaentity.User;
import com.ay.talk.jpaentity.UserRoomInfo;

public interface UserRoomInfoRepository extends JpaRepository<UserRoomInfo, Long>{
	Optional<UserRoomInfo> findUserByRoomNameAndNickName(String roomName, String nickName);
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM UserRoomInfo u WHERE u.user=:user")
	void deleteUserRoomInfos(@Param("user")User user);
}
