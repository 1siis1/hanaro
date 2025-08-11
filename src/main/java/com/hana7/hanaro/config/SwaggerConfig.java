package com.hana7.hanaro.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		// 1. API 문서의 기본 정보 설정
		Info info = new Info()
			.version("v1.0.0")
			.title("하나로 쇼핑몰 API")
			.description("하나은행 IT 교육과정 쇼핑몰 프로젝트 API 명세서입니다.");

		// 2. JWT 인증 방식을 위한 SecurityScheme 정의
		String jwtSchemeName = "bearerAuth";
		SecurityScheme securityScheme = new SecurityScheme()
			.name(jwtSchemeName)
			.type(SecurityScheme.Type.HTTP) // 인증 타입
			.scheme("bearer")               // 스킴
			.bearerFormat("JWT");           // 포맷

		// 3. 모든 API에 전역적으로 SecurityRequirement 적용
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

		return new OpenAPI()
			.info(info)
			.components(new Components().addSecuritySchemes(jwtSchemeName, securityScheme))
			.addSecurityItem(securityRequirement);
	}
}
