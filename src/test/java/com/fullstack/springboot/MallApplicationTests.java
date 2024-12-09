package com.fullstack.springboot;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.TodoDTO;
import com.fullstack.springboot.entity.Todo;
import com.fullstack.springboot.repository.TodoRepository;
import com.fullstack.springboot.service.TodoService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class MallApplicationTests {

	@Autowired
	TodoRepository todoRepository;
	
	@Autowired
	TodoService todoService;
	
	@Test
//	  public void testInsert() {
//
//	    for (int i = 1; i <= 100; i++) {
//
//	      Todo todo = Todo.builder()
//	      .title("Title..." + i)
//	      .dueDate(LocalDate.of(2023,12,31))
//	      .writer("user00")
//	      .build();
//
//	      todoRepository.save(todo);
//	    }
//	  }

//	public void testRead() {
//
//	    //존재하는 번호로 확인 
//	    Long tno = 33L;
//
//	    java.util.Optional<Todo> result = todoRepository.findById(tno);
//
//	    Todo todo = result.orElseThrow();
//
//	    log.info(todo);
//	  }

//	  public void testModify() {
//
//	    Long tno = 33L;
//
//	    java.util.Optional<Todo> result = todoRepository.findById(tno); //java.util 패키지의 Optional
//
//	    Todo todo = result.orElseThrow();
//	    todo.changeTitle("Modified 33!!!...");
//	    todo.changeComplete(true);
//	    todo.changeDueDate(LocalDate.of(2023,10,10));
//
//	    todoRepository.save(todo);
//
//	  }

//	public void testDelete() {
//	    Long tno = 1L;
//
//	    todoRepository.deleteById(tno);
//
//	  }

//	public void testPaging() {
//
//	    //import org.springframework.data.domain.Pageable;
//
//	    Pageable pageable = PageRequest.of(0,10, Sort.by("tno").descending());
//
//	    Page<Todo> result = todoRepository.findAll(pageable);
//
//	    log.info(result.getTotalElements());
//
//	    result.getContent().stream().forEach(todo -> log.info(todo));
//
//	  }

//	public void testRegister() {
//
//	    TodoDTO todoDTO = TodoDTO.builder()
//	    .title("서비스 테스트")
//	    .writer("tester")
//	    .dueDate(LocalDate.of(2023,10,10))
//	    .build();
//
//	    Long tno = todoService.register(todoDTO);
//
//	    log.info("TNO: " + tno);
//	    
//	  }

//	  public void testGet() {
//
//	    Long tno = 101L;
//
//	    TodoDTO todoDTO = todoService.get(tno);
//
//	    log.info(todoDTO);
//
//	  }

	  public void testList() {

	    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
	    .page(2)
	    .size(10)
	    .build();

	    PageResponseDTO<TodoDTO> response = todoService.list(pageRequestDTO);

	    log.info(response);

	  }

}
