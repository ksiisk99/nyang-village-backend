package com.ay.talk.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ay.talk.jpaentity.Suspended;

public interface SuspendedRepository extends JpaRepository<Suspended, String>{
	
}
