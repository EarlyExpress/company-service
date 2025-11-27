package com.early_express.company_service.infrastructure.init;

import com.early_express.company_service.domain.entity.Company;
import com.early_express.company_service.domain.vo.Type;
import com.early_express.company_service.infrastructure.persistence.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
	private final CompanyRepository companyRepository;

	@Override
	public void run(String... args) throws Exception {
		// AuditorAware 구현체가 인증 객체를 필요
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("시스템", null, Collections.emptyList());
		SecurityContextHolder.getContext().setAuthentication(auth);

		Company seller1 = new Company("2a6d1bdc-41a8-4da4-a575-4da213804fd3", "한국식품제조", Type.SELLER, "1", "경기도 이천시 덕평로 257-21");
		seller1.assignOwner("f1e2d3c4-b5a6-4978-9c8d-7e6f5a4b3c2d");
		companyRepository.save(seller1);

		// 초기화 후 임시 인증 제거
		SecurityContextHolder.clearContext();
	}
}
