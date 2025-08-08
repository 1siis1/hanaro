package com.hana7.hanaro.security;

import com.hana7.hanaro.dto.MemberDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

	private final Key key;

	@Value("${jwt.access-token.expiration}")
	private long accessTokenExpiration;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	// JWT 토큰 생성
	public String generateToken(Authentication authentication) {
		// 인증 객체에서 MemberDTO 추출
		MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();

		// 토큰 클레임(payload)에 사용자 정보와 역할(ROLE) 추가
		Claims claims = Jwts.claims();
		claims.put("id", memberDTO.getId());
		claims.put("email", memberDTO.getEmail());
		claims.put("nickname", memberDTO.getNickname());
		claims.put("role", memberDTO.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(",")));

		Date now = new Date();
		Date expiration = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(memberDTO.getEmail())
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	// JWT 토큰에서 인증 정보 추출
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		String roles = claims.get("roles", String.class);
		Collection<? extends GrantedAuthority> authorities = Arrays.stream(roles.split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	// JWT 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			// 토큰 파싱 실패 시 예외 처리
			return false;
		}
	}

	// 토큰 클레임 추출
	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
