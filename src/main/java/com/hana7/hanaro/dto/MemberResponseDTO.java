package com.hana7.hanaro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
	private String accessToken;
	private String tokenType = "Bearer";
	private Long id;
	private String email;
	private String nickname;
	private String role;
}
