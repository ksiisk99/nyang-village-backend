package com.ay.talk.jparepository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.ay.talk.jpaentity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
	
}