package com.hana7.hanaro.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequest {
	@NotBlank @Email
	private String email;
	@NotBlank @Size(min = 8)
	private String password;
	@NotBlank
	private String nickname;
}
