package com.fullstack.springboot.dto.malldto;

import lombok.Data;

@Data
public class MemberModifyDTO {

	private String email;
	private String name;
	private String password;
}
