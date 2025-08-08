package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.MemberResponseDTO; // MemberResponseDTO로 변경
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

	public void join(MemberJoinRequestDTO requestDTO) {
		if (memberRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}

		Member member = Member.builder()
			.email(requestDTO.getEmail())
			.password(passwordEncoder.encode(requestDTO.getPassword()))
			.nickname(requestDTO.getNickname())
			.role(MemberRole.ROLE_MEMBER)
			.build();

		memberRepository.save(member);
	}

	public void joinAdmin(MemberJoinRequestDTO requestDTO) {
		if (memberRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}

		Member member = Member.builder()
			.email(requestDTO.getEmail())
			.password(passwordEncoder.encode(requestDTO.getPassword()))
			.nickname(requestDTO.getNickname())
			.role(MemberRole.ROLE_ADMIN)
			.build();

		memberRepository.save(member);
	}

	public MemberResponseDTO login(MemberLoginRequestDTO requestDTO) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword())
		);

		String token = jwtTokenProvider.generateToken(authentication);
		Member member = memberRepository.findByEmail(requestDTO.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		return MemberResponseDTO.builder()
			.accessToken(token)
			.tokenType("Bearer")
			.id(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.role(member.getRole().name())
			.build();
	}
}
