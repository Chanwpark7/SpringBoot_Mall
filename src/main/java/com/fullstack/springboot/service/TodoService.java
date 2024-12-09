package com.fullstack.springboot.service;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.TodoDTO;

public interface TodoService {
  
  Long register(TodoDTO todoDTO);

  TodoDTO get(Long tno);

  void modify(TodoDTO todoDTO);

  void remove(Long tno);

  PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);

}
