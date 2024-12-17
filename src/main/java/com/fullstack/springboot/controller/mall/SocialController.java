package com.fullstack.springboot.controller.mall;

import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.malldto.FullStackMemberDTO;
import com.fullstack.springboot.service.mall.MemberService;
import com.fullstack.springboot.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {

	private final MemberService memberService;
	
	@GetMapping("/api/member/kakao")
	public Map<String, Object> getMemberFromKakao(@RequestParam(name = "accessToken") String accessToken) {
		log.error("요청된 엑세스 토큰 " + accessToken);

		FullStackMemberDTO fullStackMemberDTO = memberService.getKakaoMember(accessToken);
		
		 Map<String, Object> claims = fullStackMemberDTO.getClaims();
		
		String jwtAccessToken = JWTUtil.genToken(claims, 10);
	    String jwtRefreshToken = JWTUtil.genToken(claims,60*24);

	    claims.put("accessToken", jwtAccessToken);
	    claims.put("refreshToken", jwtRefreshToken);
		 
		return claims;//컨트롤러가 배열을 리턴함.
	}
}
