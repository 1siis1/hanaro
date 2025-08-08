package com.hana7.hanaro.member.controller;

import com.hana7.hanaro.dto.MemberJoinRequestDTO;
import com.hana7.hanaro.dto.MemberLoginRequestDTO;
import com.hana7.hanaro.dto.MemberResponseDTO; // MemberResponseDTO로 변경
import com.hana7.hanaro.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/join")
	public ResponseEntity<String> join(@Valid @RequestBody MemberJoinRequestDTO request) {
		memberService.join(request);
		return new ResponseEntity<>("회원 가입이 완료되었습니다.", HttpStatus.CREATED);
	}

	@PostMapping("/join/admin")
	public ResponseEntity<String> joinAdmin(@Valid @RequestBody MemberJoinRequestDTO request) {
		memberService.joinAdmin(request);
		return new ResponseEntity<>("관리자 회원가입 완료", HttpStatus.CREATED);
	}


	@PostMapping("/login")
	public ResponseEntity<MemberResponseDTO> login(@Valid @RequestBody MemberLoginRequestDTO request) {
		MemberResponseDTO response = memberService.login(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


}
