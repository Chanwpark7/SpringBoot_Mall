package com.fullstack.springboot.service.mall;
/*
 * 이 아이가 하는 일: accessToken 을 받아서 로그인 처리에 사용하는 MemberDTO 를 리턴하도록 정의함.
 */

import java.util.stream.Collectors;

import com.fullstack.springboot.dto.malldto.FullStackMemberDTO;
import com.fullstack.springboot.dto.malldto.MemberModifyDTO;
import com.fullstack.springboot.entity.mall.FullStackMember;

public interface MemberService {

	FullStackMemberDTO getKakaoMember(String accessToken);
	
	void modifyMember(MemberModifyDTO memberModifyDTO);
	
	default FullStackMemberDTO entityToDTO(FullStackMember member) {

		FullStackMemberDTO dto = new FullStackMemberDTO(
	      member.getEmail(), 
	      member.getPassword(), 
	      member.getName(), 
	      member.isFormSns(), 
	      member.getRoleSet().stream().map(memberRole -> memberRole.name()).collect(Collectors.toSet()));

	    return dto;
	  };
}
