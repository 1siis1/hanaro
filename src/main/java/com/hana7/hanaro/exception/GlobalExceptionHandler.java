package com.hana7.hanaro.exception;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(
		MethodArgumentNotValidException ex, HttpServletRequest req) {

		List<ErrorResponse.FieldError> fields = ex.getBindingResult().getFieldErrors()
			.stream()
			.map(fe -> new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
			.toList();

		return build(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "요청 값이 유효하지 않습니다.", fields, req.getRequestURI());
	}

	// @Validated 파라미터/경로변수 검증 실패
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(
		ConstraintViolationException ex, HttpServletRequest req) {

		List<ErrorResponse.FieldError> fields = ex.getConstraintViolations().stream()
			.map(v -> new ErrorResponse.FieldError(v.getPropertyPath().toString(), v.getMessage()))
			.toList();

		return build(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "요청 값이 유효하지 않습니다.", fields, req.getRequestURI());
	}

	// JSON 파싱 오류
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleNotReadable(
		HttpMessageNotReadableException ex, HttpServletRequest req) {
		log.warn("Malformed JSON: {}", ex.getMessage());
		return build(HttpStatus.BAD_REQUEST, "MALFORMED_JSON", "요청 본문(JSON)이 올바르지 않습니다.", null, req.getRequestURI());
	}

	// 타입 불일치(예: /products?page=abc)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(
		MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", "파라미터 타입이 올바르지 않습니다.", null, req.getRequestURI());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(
		IllegalArgumentException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT", ex.getMessage(), null, req.getRequestURI());
	}

	// 인증/인가
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthentication(
		AuthenticationException ex, HttpServletRequest req) {
		return build(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED", "아이디 또는 비밀번호가 일치하지 않습니다.", null, req.getRequestURI());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(
		AccessDeniedException ex, HttpServletRequest req) {
		return build(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다.", null, req.getRequestURI());
	}

	// JWT 토큰 오류
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ErrorResponse> handleJwt(
		JwtException ex, HttpServletRequest req) {
		return build(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰입니다.", null, req.getRequestURI());
	}

	// 데이터 무결성(중복 키 등)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrity(
		DataIntegrityViolationException ex, HttpServletRequest req) {
		return build(HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION", "데이터 무결성 위반입니다.", null, req.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(
		Exception ex, HttpServletRequest req) {
		log.error("Unhandled exception", ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
			"서버 내부에서 처리되지 않은 예외가 발생했습니다.", null, req.getRequestURI());
	}

	private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, String message,
		List<ErrorResponse.FieldError> fieldErrors, String path) {
		ErrorResponse body = ErrorResponse.builder()
			.code(code)
			.message(message)
			.fieldErrors(fieldErrors)
			.path(path)
			.timestamp(Instant.now())
			.build();
		return ResponseEntity.status(status).body(body);
	}
}
