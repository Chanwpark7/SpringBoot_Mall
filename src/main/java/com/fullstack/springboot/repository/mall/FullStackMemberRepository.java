package com.fullstack.springboot.repository.mall;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.mall.FullStackMember;

public interface FullStackMemberRepository extends JpaRepository<FullStackMember, String> {

	  @EntityGraph(attributePaths = {"roleSet"})
	  @Query("select m from FullStackMember m where m.email = :email")
	  FullStackMember getWithRoles(@Param("email") String email);

	}