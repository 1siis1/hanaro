package com.hana7.hanaro.auth;

import com.hana7.hanaro.auth.dto.LoginRequestDTO;
import com.hana7.hanaro.auth.dto.SignUpRequestDTO;
import com.hana7.hanaro.auth.dto.TokenResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Tag(name = "회원")
	@Operation(summary = "회원가입", description = "회원가입입니다")
	@PostMapping("/signup")
	public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDTO request) {
		authService.signUp(request);
		return ResponseEntity.ok().build();
	}

	@Tag(name = "회원")
	@Operation(summary = "로그인", description = "회원 로그인입니다")
	@PostMapping("/login")
	public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
		TokenResponseDTO token = authService.login(request);
		return ResponseEntity.ok(token);
	}
}
