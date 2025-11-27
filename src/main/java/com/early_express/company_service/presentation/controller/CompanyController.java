package com.early_express.company_service.presentation.controller;

import com.early_express.company_service.application.CompanySearchService;
import com.early_express.company_service.application.dto.CompanyResponse;
import com.early_express.company_service.global.presentation.dto.ApiResponse;
import com.early_express.company_service.global.presentation.dto.PageResponse;
import com.early_express.company_service.presentation.dto.SearchCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/all/companies")
public class CompanyController {
	private final CompanySearchService companySearchService;

	/**
	 * GET /api/companies
	 * 업체 검색
	 *
	 * 쿼리 파라미터:
	 * - name: 업체명 (부분 검색)
	 * - companyType: 업체 타입 (SELLER, BUYER)
	 * - hubId: 허브 ID
	 * - page: 페이지 번호 (0부터 시작, 기본값: 0)
	 * - size: 페이지 크기 (10, 30, 50만 허용, 기본값: 10)
	 * - sortDirection: 정렬 방향 (ASC, DESC, 기본값: DESC)
	 *
	 * 정렬 기준: createdAt(생성일) -> updatedAt(수정일) 순으로 정렬
	 */
	@GetMapping
	public ResponseEntity<PageResponse<CompanyResponse>> searchCompanies(@ModelAttribute SearchCompanyRequest request) {
		PageResponse<CompanyResponse> response = companySearchService.searchCompanies(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyResponse>> searchCompanies(@PathVariable String companyId) {
		return ResponseEntity.ok(companySearchService.searchCompanies(companyId));
	}
}
