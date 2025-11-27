package com.early_express.company_service.infrastructure.feign;

import com.early_express.company_service.application.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="user-service")
public interface UserFeignClient {

	@GetMapping("/user/internal/all/profile")
	UserProfileDto checkUsersProfile();
}
