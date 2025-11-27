package com.early_express.company_service.presentation.dto;

import com.early_express.company_service.domain.vo.Type;

public record UpdateCompanyRequest(
	String name,
	Type companyType,
	String hubId,
	String address,
	String ownerId
) {
}
