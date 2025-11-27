package com.early_express.company_service.presentation.dto;

import com.early_express.company_service.domain.vo.Type;

public record SearchCompanyRequest(
	String name,
	Type companyType,
	String hubId,
	String address,
	Integer page,
	Integer size,
	String sortDirection
) {
	public Integer getPage() {
		return page==null ? 0 : page;
	}

	public Integer getSize() {
		if (size == null || (size != 10 && size != 30 && size != 50)) {
			return 10;
		}
		return size;
	}

	public String getSortDirection() {
		if (sortDirection == null || (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC"))) {
			return "DESC";
		}
		return sortDirection.toUpperCase();
	}
}