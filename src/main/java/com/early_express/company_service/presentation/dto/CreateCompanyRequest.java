package com.early_express.company_service.presentation.dto;

import com.early_express.company_service.domain.vo.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCompanyRequest(
		@NotBlank(message = "업체명은 필수입니다.")
		String name,

		@NotNull(message = "업체 타입은 필수입니다.")
		Type companyType,

		@NotBlank(message = "허브 ID는 필수입니다.")
		String hubId,

		@NotBlank(message = "주소는 필수입니다.")
		String address
) {
}
