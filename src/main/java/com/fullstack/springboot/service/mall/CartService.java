package com.fullstack.springboot.service.mall;

import java.util.List;

import com.fullstack.springboot.dto.malldto.CartItemDTO;
import com.fullstack.springboot.dto.malldto.CartItemListDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface CartService {

  //장바구니 아이템 추가 혹은 변경 
  public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

  //모든 장바구니 아이템 목록
  public List<CartItemListDTO> getCartItems(String email);

  //아이템 삭제
  public List<CartItemListDTO> remove(Long cino);
  
}