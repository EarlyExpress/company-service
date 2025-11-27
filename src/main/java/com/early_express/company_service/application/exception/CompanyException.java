package com.early_express.company_service.application.exception;


import com.early_express.company_service.global.presentation.exception.ErrorCode;
import com.early_express.company_service.global.presentation.exception.GlobalException;

public class CompanyException extends GlobalException {
	public CompanyException(ErrorCode errorCode) {
		super(errorCode);
	}
}
