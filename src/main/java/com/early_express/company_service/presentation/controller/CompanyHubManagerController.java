package com.early_express.company_service.presentation.controller;

import com.early_express.company_service.application.CompanySearchService;
import com.early_express.company_service.application.HubManagerCompanyService;
import com.early_express.company_service.application.dto.CompanyResponse;
import com.early_express.company_service.application.dto.UserProfileDto;
import com.early_express.company_service.application.exception.CompanyException;
import com.early_express.company_service.global.presentation.dto.ApiResponse;
import com.early_express.company_service.infrastructure.feign.HubFeignClient;
import com.early_express.company_service.infrastructure.feign.UserFeignClient;
import com.early_express.company_service.presentation.dto.CreateCompanyRequest;
import com.early_express.company_service.presentation.dto.UpdateCompanyRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/hub-manager/companies")
@PreAuthorize("hasRole('HUB_MANAGER')")
@Slf4j
public class CompanyHubManagerController {
	private final HubManagerCompanyService companyService;
	private final UserFeignClient userFeignClient;
	private final HubFeignClient hubFeignClient;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createCompany(@RequestBody CreateCompanyRequest req) {
		try {
			String hubId = getUsersHubId();

			// 요청으로 보낸 hubId가 실제로 본인이 담당하는 hub인지 확인
			if (StringUtils.hasText(hubId) && hubId.equals(req.hubId())) {
				companyService.createCompany(req);
				return ResponseEntity.ok(ApiResponse.success(null, "업체가 생성되었습니다"));
			}

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("해당 허브 관리자가 아닙니다"));
		} catch (Exception e) {
			log.error("오류", e);
			return ResponseEntity.badRequest().body(ApiResponse.fail("사용자 정보 / 허브 조회 실패"));
		}
	}


	@PatchMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(@PathVariable String companyId,
																	  @RequestBody UpdateCompanyRequest req) {
		try {
			String hubId = getUsersHubId();

			// 요청으로 보낸 hubId가 실제로 본인이 담당하는 hub인지 확인
			if (StringUtils.hasText(hubId) && hubId.equals(req.hubId())) {
				CompanyResponse res = companyService.updateCompany(companyId, req);
				return ResponseEntity.ok(ApiResponse.success(res, "업체가 수정되었습니다"));
			}

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("해당 허브 관리자가 아닙니다"));
		} catch (CompanyException e) {
			return ResponseEntity.status(e.getErrorCode().getStatus())
								 .body(ApiResponse.fail(e.getMessage()));

		} catch (FeignException e) {
			return ResponseEntity.badRequest().body(ApiResponse.fail("사용자 정보 / 허브 조회 실패"));
		}
	}

	@DeleteMapping("/{companyId}")
	public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable String companyId,
														   @RequestHeader("X-User-Id") String deletedBy) {
		try {
			String hubId = getUsersHubId();
			companyService.deleteCompany(companyId, hubId, deletedBy);
			return ResponseEntity.ok(ApiResponse.success(null, "업체가 삭제되었습니다"));

		} catch (CompanyException e) {
			return ResponseEntity.status(e.getErrorCode().getStatus())
								 .body(ApiResponse.fail(e.getMessage()));

		} catch (FeignException e) {
			return ResponseEntity.badRequest().body(ApiResponse.fail("사용자 정보 / 허브 조회 실패"));
		}
	}

	private String getUsersHubId() {
		// 사용자 정보 조회
		UserProfileDto dto = userFeignClient.checkUsersProfile();

		String hubId = dto.hubId();
		// 허브가 존재하는지 확인
		hubFeignClient.existsHub(Long.parseLong(hubId));
		return hubId;
	}
}
