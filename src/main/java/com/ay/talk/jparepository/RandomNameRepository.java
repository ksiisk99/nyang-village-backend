package com.ay.talk.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ay.talk.jpaentity.RandomName;

public interface RandomNameRepository extends JpaRepository<RandomName, Long>{

}
