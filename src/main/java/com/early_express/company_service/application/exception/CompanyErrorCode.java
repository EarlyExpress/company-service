package com.early_express.company_service.application.exception;


import com.early_express.company_service.global.presentation.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyErrorCode implements ErrorCode {
	COMPANY_NOT_FOUND("COMPANY_001", "업체를 찾을 수 없습니다", 404),
	COMPANY_ACCESS_DENIED("COMPANY_002", "해당 업체에 접근할 권한이 없습니다.", 403);

	private final String code;
	private final String message;
	private final int status;
}
