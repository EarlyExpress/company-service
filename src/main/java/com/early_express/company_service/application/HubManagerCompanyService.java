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
public class HubManagerCompanyService {
	private final CompanyRepository companyRepository;

	public void createCompany(CreateCompanyRequest req) {
		Company company = Company.builder()
								 .name(req.name())
								 .type(req.companyType())
								 .managingHubId(req.hubId())
								 .address(req.address()).build();

		companyRepository.save(company);
	}

	public CompanyResponse updateCompany(String companyId, UpdateCompanyRequest req) {
		Company company = companyRepository.findById(companyId)
										   .orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

		// 요청자의 허브가 해당 업체를 실제로 관리하는지 체크
		if (!company.getManagingHubId().equals(req.hubId())) {
			throw new CompanyException(COMPANY_ACCESS_DENIED);
		}

		company.updateProfile(req.name(), req.companyType(), req.hubId(), req.address(), req.ownerId());
		return CompanyResponse.from(company);
	}

	public void deleteCompany(String companyId, String hubId, String deletedBy) {
		Company company = companyRepository.findById(companyId)
										   .orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

		// 요청자의 허브가 해당 업체를 실제로 관리하는지 체크
		if (!company.getManagingHubId().equals(hubId)) {
			throw new CompanyException(COMPANY_ACCESS_DENIED);
		}

		company.delete(deletedBy);
	}
}
