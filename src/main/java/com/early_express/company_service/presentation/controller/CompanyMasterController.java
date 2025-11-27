package com.early_express.company_service.presentation.controller;

import com.early_express.company_service.application.MasterCompanyService;
import com.early_express.company_service.application.dto.CompanyResponse;
import com.early_express.company_service.application.exception.CompanyException;
import com.early_express.company_service.global.presentation.dto.ApiResponse;
import com.early_express.company_service.infrastructure.feign.HubFeignClient;
import com.early_express.company_service.presentation.dto.CreateCompanyRequest;
import com.early_express.company_service.presentation.dto.UpdateCompanyRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/master/companies")
@PreAuthorize("hasRole('MASTER')")
@Slf4j
public class CompanyMasterController {
	private final MasterCompanyService companyService;
	private final HubFeignClient hubFeignClient;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createCompany(@RequestBody CreateCompanyRequest req) {
		try {
			// 허브 존재 여부 체크
			String hubId = req.hubId();
			hubFeignClient.existsHub(Long.parseLong(hubId));

			companyService.createCompany(req);
			return ResponseEntity.ok(ApiResponse.success(null, "업체가 생성되었습니다"));
		} catch (CompanyException e) {
			return ResponseEntity.status(e.getErrorCode().getStatus())
								 .body(ApiResponse.fail(e.getMessage()));

		} catch (FeignException e) {
			return ResponseEntity.badRequest().body(ApiResponse.fail("허브 조회 실패"));
		}
	}


	@PatchMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(@PathVariable String companyId,
																	  @RequestBody UpdateCompanyRequest req) {
		try {
			// 허브 존재 여부 체크
			String hubId = req.hubId();
			hubFeignClient.existsHub(Long.parseLong(hubId));

			CompanyResponse res = companyService.updateCompany(companyId, req);
			return ResponseEntity.ok(ApiResponse.success(res, "업체가 수정되었습니다"));
		} catch (CompanyException e) {
			return ResponseEntity.status(e.getErrorCode().getStatus())
								 .body(ApiResponse.fail(e.getMessage()));

		} catch (FeignException e) {
			return ResponseEntity.badRequest().body(ApiResponse.fail("허브 조회 실패"));
		}
	}

	@DeleteMapping("/{companyId}")
	public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable String companyId,
														   @RequestHeader("X-User-Id") String deletedBy) {
		companyService.deleteCompany(companyId, deletedBy);
		return ResponseEntity.ok(ApiResponse.success(null, "업체가 삭제되었습니다"));
	}
}
