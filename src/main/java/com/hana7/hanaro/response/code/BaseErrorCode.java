package com.hana7.hanaro.response.code;

public interface BaseErrorCode {

	public ErrorReasonDTO getReason();

	public ErrorReasonDTO getReasonHttpStatus();
}
