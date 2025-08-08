package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.MemberResponseDTO;
import com.hana7.hanaro.dto.MemberJoinRequestDTO;
import com.hana7.hanaro.dto.MemberLoginRequestDTO;
import com.hana7.hanaro.entity.Member;
import com.hana7.hanaro.entity.MemberRole;
import com.hana7.hanaro.repository.MemberRepository;
import com.hana7.hanaro.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public void join(MemberJoinRequestDTO request) {
		if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}

		Member member = Member.builder()
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.nickname(request.getNickname())
			.role(MemberRole.MEMBER)
			.build();

		memberRepository.save(member);
	}

	public MemberResponseDTO login(MemberLoginRequestDTO request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		);

		String token = jwtTokenProvider.generateToken(authentication);
		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		return MemberResponseDTO.builder()
			.accessToken(token)
			.id(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.role(member.getRole().name())
			.build();
	}

	public void delete(Long userId) {
	}
}
