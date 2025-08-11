package com.hana7.hanaro.auth;

import com.hana7.hanaro.auth.dto.LoginRequestDTO;
import com.hana7.hanaro.auth.dto.SignUpRequestDTO;
import com.hana7.hanaro.auth.dto.TokenResponseDTO;
import com.hana7.hanaro.config.JwtTokenProvider;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.entity.Role;
import com.hana7.hanaro.member.repository.MemberRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public void signUp(@Valid SignUpRequestDTO request) {
		if (memberRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}
		Member member = Member.builder()
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.nickname(request.getNickname())
			.role(Role.USER)
			.build();
		memberRepository.save(member);
	}

	public TokenResponseDTO login(LoginRequestDTO request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		);
		return jwtTokenProvider.generateToken(authentication);
	}
}
