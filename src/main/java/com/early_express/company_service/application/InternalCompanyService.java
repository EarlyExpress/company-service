package com.early_express.company_service.application;

import com.early_express.company_service.application.exception.CompanyErrorCode;
import com.early_express.company_service.application.exception.CompanyException;
import com.early_express.company_service.domain.entity.Company;
import com.early_express.company_service.infrastructure.persistence.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InternalCompanyService {
	private final CompanyRepository repository;

	public void assignOwner(String companyId, String ownerId) {
		Company company = repository.findById(companyId)
									.orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND));

		company.assignOwner(ownerId);
	}
}
