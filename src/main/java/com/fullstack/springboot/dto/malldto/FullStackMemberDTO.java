package com.fullstack.springboot.dto.malldto;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;

@Data
public class FullStackMemberDTO extends User {

	private String email;
	private String name;
	private boolean formSns;
	private String password;
	
	private Set<String> roleSet = new HashSet<String>();
	
	public FullStackMemberDTO(String email, String password, String name, boolean formSns, Set<String> roleNames) {
		super(email, password, roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toSet()));
		
		this.email = email;
		this.password = password;
		this.name = name;
		this.formSns = formSns;
		this.roleSet = roleNames;
	}

	//JWT 에서 생성한 암호화된 인증 토큰을 담은 DTO 를 View 단에 던질 예정.
	//리액트에서는 이 메소드에 정의된 토큰을 get 할 것.
	public Map<String, Object> getClaims(){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("email", email);
		dataMap.put("password", password);
		dataMap.put("name", name);
		dataMap.put("formSns", formSns);
		dataMap.put("roleNames", roleSet);
		
		return dataMap;
	}
}
