package com.hana7.hanaro.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		// 여러 에러 중 첫 번째 에러 메시지를 사용합니다.
		String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		log.warn("Validation error: {}", errorMessage);

		ErrorResponse response = new ErrorResponse("VALIDATION_FAILED", errorMessage);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		log.warn("Illegal argument: {}", ex.getMessage());

		ErrorResponse response = new ErrorResponse("ILLEGAL_ARGUMENT", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
		log.warn("Authentication failed: {}", ex.getMessage());

		ErrorResponse response = new ErrorResponse("AUTHENTICATION_FAILED", "아이디 또는 비밀번호가 일치하지 않습니다.");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * 처리되지 않은 나머지 모든 예외
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("Unhandled exception", ex);

		ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부에서 처리되지 않은 예외가 발생했습니다.");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
