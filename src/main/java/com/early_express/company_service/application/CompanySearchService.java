package com.early_express.company_service.application;

import com.early_express.company_service.application.dto.CompanyResponse;
import com.early_express.company_service.application.exception.CompanyException;
import com.early_express.company_service.domain.entity.Company;
import com.early_express.company_service.global.common.utils.PageUtils;
import com.early_express.company_service.global.presentation.dto.ApiResponse;
import com.early_express.company_service.global.presentation.dto.PageResponse;
import com.early_express.company_service.infrastructure.persistence.CompanyRepository;
import com.early_express.company_service.infrastructure.persistence.CustomCompanyRepository;
import com.early_express.company_service.presentation.dto.SearchCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.early_express.company_service.application.exception.CompanyErrorCode.COMPANY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanySearchService {
	private final CustomCompanyRepository customRepository;
	private final CompanyRepository companyRepository;

	@Transactional(readOnly = true)
	public PageResponse<CompanyResponse> searchCompanies(SearchCompanyRequest request) {
		Page<Company> companies = customRepository.searchCompanies(request);
		return PageUtils.toPageResponse(companies, CompanyResponse::from);
	}

	@Transactional(readOnly = true)
	public ApiResponse<CompanyResponse> searchCompanies(String companyId) {
		Company company = companyRepository.findByIdAndDeletedAtIsNull(companyId)
										   .orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
		return ApiResponse.success(CompanyResponse.from(company));
	}
}
