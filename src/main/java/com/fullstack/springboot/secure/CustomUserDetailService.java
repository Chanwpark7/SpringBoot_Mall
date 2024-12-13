package com.fullstack.springboot.secure;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.malldto.FullStackMemberDTO;
import com.fullstack.springboot.entity.mall.FullStackMember;
import com.fullstack.springboot.repository.mall.FullStackMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final FullStackMemberRepository fullStackMemberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.error("........... loadUserByUsername 수행됨.........");
		
		FullStackMember member = fullStackMemberRepository.getWithRoles(username);
		
		if(member==null) {
			throw new UsernameNotFoundException(username+"찾을 수 없음");
		}
		
		FullStackMemberDTO dto = new FullStackMemberDTO(member.getEmail(), 
				member.getPassword(), 
				member.getName(), 
				member.isFormSns(), 
				member.getRoleSet().stream().map(r -> r.name()).collect(Collectors.toSet()));
		log.error("사용자 정보 " + dto);
		
		return dto;
	}

}
