package com.hana7.hanaro.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
	private final String code;
	private final String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<FieldError> fieldErrors;

	private final String path;
	private final Instant timestamp;

	@Getter
	@AllArgsConstructor
	public static class FieldError {
		private final String field;
		private final String reason;
	}
}
