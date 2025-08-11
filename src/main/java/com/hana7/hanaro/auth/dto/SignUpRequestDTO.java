package com.hana7.hanaro.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequestDTO {
	@NotBlank @Email
	@Schema(name = "email", example = "tester@gmail.com")
	private String email;

	@NotBlank @Size(min = 8)
	@Schema(name = "password", example = "12345678")
	private String password;

	@NotBlank
	@Schema(name = "nickname", example = "tester")
	private String nickname;
}
