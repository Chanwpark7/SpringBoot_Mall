package com.fullstack.springboot.repository.mall;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.mall.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

  @Query("select cart from Cart cart where cart.owner.email = :email")
  public Optional<Cart> getCartOfMember(@Param("email") String email);
  
}
