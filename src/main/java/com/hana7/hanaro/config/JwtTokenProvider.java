package com.hana7.hanaro.config;

import com.hana7.hanaro.auth.dto.TokenResponseDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j; // Slf4j import 추가
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component; // ❗️ [추가] Component import
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
	private final Key key;
	private final long accessTokenValidityInMilliseconds;

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.access-token.expiration:3600000}") long accessTokenValidityInMilliseconds
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
	}

	public TokenResponseDTO generateToken(Authentication authentication) {
		long now = (new Date()).getTime();
		Date accessTokenExpiresIn = new Date(now + accessTokenValidityInMilliseconds);
		String accessToken = Jwts.builder()
			.setSubject(authentication.getName())
			.setExpiration(accessTokenExpiresIn)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
		// DTO 이름 통일
		return new TokenResponseDTO(accessToken);
	}

	public String getUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			log.warn("Invalid JWT Token: {}", e.getMessage());
			return false;
		}
	}
}
