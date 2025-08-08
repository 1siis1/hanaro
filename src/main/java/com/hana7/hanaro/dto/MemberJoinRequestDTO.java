package com.hana7.hanaro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequestDTO {
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
	private String password;

	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
	private String nickname;
}
