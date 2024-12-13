package com.fullstack.springboot.util;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

//JWT 생성 및 validation 클래스 정의
//토큰 생성 및 검증만 할 예정이라 모두 static 으로 정의
public class JWTUtil {

	private static String key = "askgjqwi4uj2jh3rkl2h3lk4jg2lk3g41hg324kl1jg234lkjh123lk4h1lk2j3g4lk12g34lk123g";
	
	//암호화될 값을 가진 Map 과 Token 의 유지기간(exp)을 분(int) 로 할 메소드 정의
	public static String genToken(Map<String, Object> valueMap, int min) {
		
		//키 생성 로직
		//1. SecretKey 객체를 Keys 라는 클래스의 메소드를 통해 얻어냄.
		SecretKey key = null;
		
		try {
			key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		//2. JWT 객체를 이용해서 헤더 정보, claims(이게 메인 정보),발행시간,보존기간을 설정하고, 생성된 SecretKey 로 서명을 해줌
		//이 서명은 유일정을 보장받기 때문에 절대 변조가 불가능함.
		String jwStr = Jwts.builder()
		.setHeader(Map.of("typ","JWT"))
		.setClaims(valueMap)
		.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
		.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
		.signWith(key)
		.compact();
		
		return jwStr;
		
		/*
		 * JWT 토큰 : 크게 Access, Refresh 로 나뉘는데 토큰을 생성할 때 일반적으로 두개 모두 생성함.
		 * 목적으론, Access 토큰은 유효기간을 짧게(min 단위) 로 주는데, 서버에 인증을 요청할 때마다 사용됨.
		 * 이때, 짧은 시간만을 갖게되면 계속 서버에서 다시 생성해서 줘야하는 불편함이 있음.
		 * 이때, 유효기간이 긴 Refresh 토큰을 생성해서 Access 토큰의 유효기간을 연장하도록 설계됨.
		 */
	}
	
	//Access 토큰 validate
	public static Map<String, Object> validateToken(String token){
		Map<String, Object> claim = null;
		
		//서명값 get
		try {
			SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
			
			claim = Jwts.parser()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return claim;
	}
}
