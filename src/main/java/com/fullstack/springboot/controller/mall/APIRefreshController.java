package com.fullstack.springboot.controller.mall;

import java.util.Date;
import java.util.Map;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.util.CustomJWTException;
import com.fullstack.springboot.util.JWTUtil;


@RequiredArgsConstructor
@Log4j2
@RestController
public class APIRefreshController {

	  /*
	   * Access Token 은 일반적으로 짧은 유효기간을 설정함. 때문에 탈취가 되어도 위험도가 적음.
	   * 
	   * 이 짧은 유효기간은 장점이자 단점인데, 만약 Access Token 이 만료가 되면
	   * 
	   * 사용자는 Refresh Token 을 활용, 새로운 Token 을 받을 수 있도록 서버에서 전략을 써야함.
	   * 
	   * member/ 라는 path 를 두고, 이 경로를 통해 오는 경우 두개의 토큰을 검증하고 Access 토큰이 만료되고
	   * Refresh 토큰이 만료 전이라면 새로운 Access Token 을 생성하도록 구현할 예정.
	   * 
	   * 내용은 다음과 같음.
	   * 
	   * Access 토큰이 없거나 잘못된 JWT 인 경우 예외 던짐
	   * Access 토큰이 유효기간이 남은 경운 전달된 토큰 다시 리턴
	   * Access 토큰은 만료, Refresh 토큰은 유효할때는 새로운 Access 토큰 전송
	   * Refresh 토큰이 얼마 남지 않은 경우 새로운 Refresh 리턴
	   * Refresh 유효기간이 충분한 경우. 기존 값 리턴.
	   */
	
	@RequestMapping("/api/member/refresh")
	public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,@RequestParam(value = "refreshToken") String refreshToken) {
		
		if(refreshToken == null) {
			throw new CustomJWTException("NULL_REFRESH_TOKEN");
		}
		if(authHeader ==null || authHeader.length()<7) {
			throw new CustomJWTException("INVALID_STRING");
		}
		
		String accessToken = authHeader.substring(7);
		
		//Access 토큰의 만료기간 확인
		if(checkExpiredToken(accessToken)==false) {
			return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
		}
		
		//Refresh 검증
		Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
		
		log.error("refresh token claims;;;" + claims);
		
		String newAccessToken = JWTUtil.genToken(claims, 10);
		
		String newRefreshToken = null;
		
		newRefreshToken = (checkTime((Integer)claims.get("exp")) == true?JWTUtil.genToken(claims, 60*24):refreshToken);
		
		return Map.of("accessToken",newAccessToken,"refreshToken",newRefreshToken);
	}
	
	//리프레쉬 토큰의 재발행 유효기간을 1시간으로 설정하고 미만 여부를 확인.
	private boolean checkTime(Integer exp) {
		
		Date expDate = new Date((long)(exp * 1000));
		
		long gap = expDate.getTime()-System.currentTimeMillis();
		
		long leftMin = gap/(1000*60);
		
		return leftMin<60;
	}
	
	//토큰 유효기간 확인 메소드
	private boolean checkExpiredToken(String token) {
		try {
			JWTUtil.validateToken(token);
		} catch (Exception e) {
			if(e.getMessage().equals("Expired")) {
				return true;
			}
		}
		
		return false;
	}
	
}
