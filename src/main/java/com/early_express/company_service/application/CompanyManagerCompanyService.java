package com.early_express.company_service.application;

import com.early_express.company_service.application.dto.CompanyResponse;
import com.early_express.company_service.application.exception.CompanyException;
import com.early_express.company_service.domain.entity.Company;
import com.early_express.company_service.infrastructure.persistence.CompanyRepository;
import com.early_express.company_service.presentation.dto.CreateCompanyRequest;
import com.early_express.company_service.presentation.dto.UpdateCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.early_express.company_service.application.exception.CompanyErrorCode.COMPANY_ACCESS_DENIED;
import static com.early_express.company_service.application.exception.CompanyErrorCode.COMPANY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyManagerCompanyService {
	private final CompanyRepository companyRepository;

	public CompanyResponse updateCompany(String companyId, UpdateCompanyRequest req) {
		Company company = companyRepository.findById(companyId)
										   .orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

		company.updateProfile(req.name(), req.companyType(), req.hubId(), req.address(), req.ownerId());
		return CompanyResponse.from(company);
	}
}
