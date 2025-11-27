package com.early_express.company_service.application.dto;

public record UserProfileDto(
		String username,
		String email,
		String name,
		String role,
		String slackId,
		String phoneNumber,
		String address,
		String hubId,
		String companyId
) {
}
