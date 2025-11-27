package com.early_express.company_service.presentation.controller;

import com.early_express.company_service.application.CompanyManagerCompanyService;
import com.early_express.company_service.application.dto.CompanyResponse;
import com.early_express.company_service.application.dto.UserProfileDto;
import com.early_express.company_service.application.exception.CompanyException;
import com.early_express.company_service.global.presentation.dto.ApiResponse;
import com.early_express.company_service.infrastructure.feign.HubFeignClient;
import com.early_express.company_service.infrastructure.feign.UserFeignClient;
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
@RequestMapping("/web/company-manager/my-company")
@PreAuthorize("hasRole('COMPANY')")
@Slf4j
public class CompanyManagerController {
	private final CompanyManagerCompanyService companyService;
	private final UserFeignClient userFeignClient;
	private final HubFeignClient hubFeignClient;

	@PatchMapping()
	public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(@RequestBody UpdateCompanyRequest req) {
		try {
			// 사용자 정보 조회
			UserProfileDto dto = userFeignClient.checkUsersProfile();
			String companyId = dto.companyId();
			String hubId = dto.hubId();

			// 허브가 존재하는지 확인
			hubFeignClient.existsHub(Long.parseLong(hubId));

			// 실제 업체 담당자인지 확인
			if (StringUtils.hasText(companyId)) {
				CompanyResponse res = companyService.updateCompany(companyId, req);
				return ResponseEntity.ok(ApiResponse.success(res, "업체가 수정되었습니다"));
			}

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("업체 담당자가 아닙니다"));
		} catch (CompanyException e) {
			return ResponseEntity.status(e.getErrorCode().getStatus())
								 .body(ApiResponse.fail(e.getMessage()));

		} catch (FeignException e) {
			return ResponseEntity.badRequest().body(ApiResponse.fail("사용자 정보 / 허브 조회 실패"));
		}
	}
}
