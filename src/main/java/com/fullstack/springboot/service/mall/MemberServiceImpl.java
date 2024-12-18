package com.fullstack.springboot.service.mall;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fullstack.springboot.dto.malldto.FullStackMemberDTO;
import com.fullstack.springboot.dto.malldto.FullStackRole;
import com.fullstack.springboot.dto.malldto.MemberModifyDTO;
import com.fullstack.springboot.entity.mall.FullStackMember;
import com.fullstack.springboot.repository.mall.FullStackMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
/*
 * 이 아이가 하는 일: 카카오 서비스에 인증을 요청하도록 지정된 URI 에 요청을 하고 호출 결과(Map으로 리턴됨.) 중 email 을 추출함.
 */
public class MemberServiceImpl implements MemberService {

	private final FullStackMemberRepository fullStackMemberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public FullStackMemberDTO getKakaoMember(String accessToken) {
		String email = getEmailFromKakaoAccessToken(accessToken);
		
		log.error("email : "+email);
		
		//기존 회원 여부 검증
		Optional<FullStackMember> result = fullStackMemberRepository.findById(email);
		
		if(result.isPresent()) {//기존 회원
			FullStackMemberDTO fullStackMemberDTO = entityToDTO(result.get());
			return fullStackMemberDTO;
		}
		
		//신규 회원 처리
		FullStackMember socialMember = makeSocialMember(email);
		fullStackMemberRepository.save(socialMember);
		
		FullStackMemberDTO fullStackMemberDTO = entityToDTO(socialMember);
		
		return fullStackMemberDTO;
	}
	
	//카카오 서비스에 인증 요청하는 메소드 정의
	private String getEmailFromKakaoAccessToken(String accessToken) {
		String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
		
		if(accessToken==null) {
			throw new RuntimeException("카카오 인증 엑세스 토큰 없음");
		}
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+accessToken);//띄어쓰기 주의
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		HttpEntity<String> httpEntity = new HttpEntity(headers);
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();
		ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, LinkedHashMap.class);
		LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
		
		log.error(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
		log.error("카카오 요청 결과 : " + bodyMap);
		
		LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");
		
		log.error("카카오 계정 : " + kakaoAccount);
		
		return kakaoAccount.get("email");
	}

	 private String makeTempPassword() {

	    StringBuffer buffer = new StringBuffer();

	    for(int i = 0;  i < 10; i++){
	      buffer.append(  (char) ( (int)(Math.random()*55) + 65  ));
	    }
	    return buffer.toString();
	  }
	 
	 private FullStackMember makeSocialMember(String email) {

	   String tempPassword = makeTempPassword();

	   log.info("tempPassword: " + tempPassword);

	   String nickname = "소셜회원";

	   FullStackMember member = FullStackMember.builder()
	   .email(email)
	   .password(passwordEncoder.encode(tempPassword))
	   .name(nickname)
	   .formSns(true)
	   .build();

	   member.addMemberRoleSet(FullStackRole.USER);

	   return member;

	  }
	 
	 @Override
	public void modifyMember(MemberModifyDTO memberModifyDTO) {
		 Optional<FullStackMember> result = fullStackMemberRepository.findById(memberModifyDTO.getEmail());
		 
		 FullStackMember member = result.orElseThrow();
		 
		 member.changePw(passwordEncoder.encode(memberModifyDTO.getPassword()));
		 member.changeSocial(false);
		 
		 fullStackMemberRepository.save(member);
	}
}
