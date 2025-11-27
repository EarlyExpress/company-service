package com.early_express.company_service.global.config;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableFeignClients(basePackages = "com.early_express.company_service")
public class FeignConfig {

	private static final String HEADER_USER_ID = "X-User-Id";
	private static final String HEADER_USERNAME = "X-Username";
	private static final String HEADER_ROLES = "X-User-Role";
	private static final String HEADER_EMAIL = "X-User-Email";



	/**
     * Feign 로깅 레벨 설정
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * Request 옵션 설정
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                10, TimeUnit.SECONDS,  // connectTimeout
                30, TimeUnit.SECONDS,  // readTimeout
                true                   // followRedirects
        );
    }

    /**
     * 재시도 설정
     */
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                100,         // 재시도 간격 (ms)
                1000,        // 최대 재시도 간격 (ms)
                3            // 최대 재시도 횟수
        );
    }

    /**
     * 에러 디코더
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }


	/**
	 * OpenFeign 요청시 JWT 토큰이 있다면 함께 전송
	 * @return
	 */
	@Bean
	public RequestInterceptor requestInterceptor() {


		return tpl -> {
			RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
			if (attrs instanceof ServletRequestAttributes) {
				HttpServletRequest req = ((ServletRequestAttributes) attrs).getRequest();
				String authorization = req.getHeader("Authorization");
				if (StringUtils.hasText(authorization)) { // user-service 인증 용
					tpl.header("Authorization", authorization);
				}

				// 그외 나머지 서비스들은 하기 헤더 정보로 인증
				String userId = req.getHeader(HEADER_USER_ID);
				String username = req.getHeader(HEADER_USERNAME);
				String email = req.getHeader(HEADER_EMAIL);
				String roles = req.getHeader(HEADER_ROLES);

				if (StringUtils.hasText(userId)) tpl.header(HEADER_USER_ID, userId);
				if (StringUtils.hasText(username)) tpl.header(HEADER_USERNAME, username);
				if (StringUtils.hasText(email)) tpl.header(HEADER_EMAIL, email);
				if (StringUtils.hasText(roles)) tpl.header(HEADER_ROLES, roles);
			}

		};
	}

}