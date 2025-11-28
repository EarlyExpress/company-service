package com.early_express.company_service.presentation.controller;

import com.early_express.company_service.application.InternalCompanyService;
import com.early_express.company_service.global.presentation.dto.ApiResponse;
import com.early_express.company_service.presentation.dto.AssignOwnerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/companies/{companyId}/owner")
@RequiredArgsConstructor
public class CompanyInternalController {
	private final InternalCompanyService service;

	@PatchMapping
	public ResponseEntity<ApiResponse<Void>> assignOwner(@PathVariable String companyId,
														 @RequestBody AssignOwnerRequest request) {
		service.assignOwner(companyId, request.userId());
		return ResponseEntity.ok(ApiResponse.success(null, "업체 담당자 ID를 저장했습니다."));
	}
}
