package com.early_express.company_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="hub-service")
public interface HubFeignClient {

	@GetMapping("/v1/hub/internal/validate/{id}")
	ResponseEntity<Void> existsHub(@PathVariable("id") Long id);
}
