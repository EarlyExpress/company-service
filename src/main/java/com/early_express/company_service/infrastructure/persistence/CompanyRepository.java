package com.early_express.company_service.infrastructure.persistence;

import com.early_express.company_service.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {
}
