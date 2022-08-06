package com.ay.talk.jparepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ay.talk.jpaentity.User;
import java.lang.String;


public interface UserRepository extends JpaRepository<User, String>{
	@Override
	@EntityGraph(attributePaths = {"userRoomInfos"}, type = EntityGraphType.FETCH)
	List<User> findAll();
	
	@EntityGraph(attributePaths = {"userRoomInfos"}, type = EntityGraphType.FETCH)
	Optional<User> findByStudentId(String studentId);
}