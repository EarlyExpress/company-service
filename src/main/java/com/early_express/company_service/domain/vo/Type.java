package com.early_express.company_service.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {
	SELLER("생산 업체"), BUYER("수령 업체");

	private final String description;
}
