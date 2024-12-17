package com.fullstack.springboot.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fullstack.springboot.dto.malldto.FullStackMemberDTO;
import com.fullstack.springboot.util.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
/*
 * Access Token 필터
 * 특정 경로(path) 에 필터를 걸어서, 요청 전에 이 필터를 통과하도록 하는 설정
 * 자바계열은 대부분이 요청을 처리하기 전-후에 이러한 필터를 chain 방식으로 연결해서 요청 전처리 후처리 작업을 함.
 * 이 필터계열은 스프링의 MVC 인터셉터, 시큐어의 filter 등을 이용해서 구현할 수 있음.
 * 아래의 OncePerRequestFilter 클래스는 주로 모든 요청에 대해서 체크하는데 사용됨. 이중 특정 메소드를 통해 필터 체크를 하지 않는 경로를 지정하여 사용함.
 * 
 * 설정된 필터는 Config 에 추가해서 컨테이너가 필터에 추가할 수 있도록 합니다.
 */
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

	//아래의 메소드는 체크하지 않을 경로나 메소드 (get/post) 등을 지정하기 위해 사용됨.
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		
		//이미지나 member 에 관련된 path 는 필터를 적용하지 않도록 선언.
		//또한 Options 에 관련된 메소드 요청도 필터 처리하지 않음.
		if(request.getMethod().equals("OPTIONS")) {
			return true;
		}
		
		String path = request.getRequestURI();
			
		log.error("check 대상 URI --> "+ path);
		
		if(path.startsWith("/api/member")) {
			return true;
		}
		if(path.startsWith("/api/products/view")) {//이미지 호출 path
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		log.error("체크되지 않을 필터;;;;");
		
		//여기서는 Token 에 대한 Validation 처리를 해서 인증 여부를 판독하도록 함.
		try {
			String authHeader = request.getHeader("Authorization");
			
			String accessToken = authHeader.substring(7);//이렇게 하는 이유는 토큰이 서버로 올때 bearer 라는 prefix 로 같이 오기 때문
			//실제 필요한 정보는 Bearer 를 제외한 7번째 자리수 이후 토큰 값.
			
			Map<String, Object> claims = JWTUtil.validateToken(accessToken);
			
			//요청에 따른 권한을 처리하는 로직 수행
			String email = (String)claims.get("email");
			String password = (String)claims.get("password");
			String name = (String)claims.get("name");
			Boolean foBoolean = (Boolean)claims.get("formSns");
			Set<String> roleNames = new HashSet<String>((ArrayList<String>)claims.get("roleNames"));
			
			log.error(roleNames);
			
			FullStackMemberDTO dto = new FullStackMemberDTO(email, password, name, foBoolean, roleNames);
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(dto, password, dto.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
			log.error("JWT claims : "+claims);
			
			filterChain.doFilter(request, response);//이건 통과
		} catch (Exception e) {
			log.error("JWT 체크 에러 발생");
			log.error(e.getMessage());
			
			//예외 발생했으니 클라이언트에게 메시지 전송
			
			Gson gson = new Gson();
			String msg = gson.toJson(Map.of("error","ERROR_TOKEN_ACCESS"));
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.println(msg);
			out.close();
		}
	}

}
