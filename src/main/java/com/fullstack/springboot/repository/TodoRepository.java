package com.fullstack.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.springboot.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
