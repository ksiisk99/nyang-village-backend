package com.ay.talk.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ay.talk.jpaentity.User;

public interface UserRepository extends JpaRepository<User, String>{
	
}
