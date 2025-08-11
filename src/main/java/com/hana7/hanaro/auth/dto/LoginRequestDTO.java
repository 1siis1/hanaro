package com.hana7.hanaro.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
	@NotBlank @Email
	@Schema(name = "email", example = "hanaro@admin.com")
	private String email;

	@NotBlank
	@Schema(name = "password", example = "12345678")
	private String password;
}
