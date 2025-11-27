package com.early_express.company_service.application.dto;

import com.early_express.company_service.domain.entity.Company;

import java.time.LocalDateTime;

public record CompanyResponse(
	String id,
	String name,
	String companyType,
	String hubId,
	String address,
	LocalDateTime createdAt,
	String createdBy,
	LocalDateTime updatedAt,
	String updatedBy
) {
	public static CompanyResponse from(Company company) {
		return new CompanyResponse(
				company.getId(),
				company.getName(),
				company.getType().getDescription(),
				company.getManagingHubId(),
				company.getAddress(),
				company.getCreatedAt(),
				company.getCreatedBy(),
				company.getUpdatedAt(),
				company.getUpdatedBy());
	}
}
