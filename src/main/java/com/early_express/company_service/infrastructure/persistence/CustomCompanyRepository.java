package com.early_express.company_service.infrastructure.persistence;

import com.early_express.company_service.domain.entity.Company;
import com.early_express.company_service.domain.entity.QCompany;
import com.early_express.company_service.domain.vo.Type;
import com.early_express.company_service.presentation.dto.SearchCompanyRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCompanyRepository {
	private final JPAQueryFactory queryFactory;

	public Page<Company> searchCompanies(SearchCompanyRequest req) {
		QCompany company = QCompany.company;

		PageRequest pageRequest = PageRequest.of(req.getPage(), req.getSize());

		List<Company> content = queryFactory
				.selectFrom(company)
				.where(
						isNotDeleted(),
						nameContains(req.name()),
						typeEquals(req.companyType()),
						hubIdEquals(req.hubId())
				)
				.orderBy(getOrderSpecifiers(req))
				.offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize())
				.fetch();

		Long total = queryFactory.select(company.count())
								 .from(company)
								 .where(
									isNotDeleted(),
									nameContains(req.name()),
									typeEquals(req.companyType()),
									hubIdEquals(req.hubId())
								 )
								 .fetchOne();

		return new PageImpl<>(content, pageRequest, total != null ? total : 0L);
	}

	// 삭제되지 않은 업체만 조회
	private BooleanExpression isNotDeleted() {
		QCompany company = QCompany.company;
		return company.deletedAt.isNull();
	}

	// 업체명 검색 (부분 일치)
	private BooleanExpression nameContains(String name) {
		QCompany company = QCompany.company;
		return StringUtils.hasText(name) ? company.name.contains(name) : null;
	}

	// 업체 타입 검색
	private BooleanExpression typeEquals(Type type) {
		QCompany company = QCompany.company;
		return type != null ? company.type.eq(type) : null;
	}

	// 허브 ID 검색
	private BooleanExpression hubIdEquals(String hubId) {
		QCompany company = QCompany.company;
		return hubId != null && !hubId.isEmpty() ? company.managingHubId.eq(hubId) : null;
	}

	// 정렬 조건 생성 (생성일 -> 수정일 순)
	private OrderSpecifier<?>[] getOrderSpecifiers(SearchCompanyRequest request) {
		QCompany company = QCompany.company;
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

		if (request.getSortDirection().equalsIgnoreCase("ASC")) {
			orderSpecifiers.add(company.createdAt.asc());
			orderSpecifiers.add(company.updatedAt.asc());
		} else {
			orderSpecifiers.add(company.createdAt.desc());
			orderSpecifiers.add(company.updatedAt.desc());
		}

		return orderSpecifiers.toArray(new OrderSpecifier[0]);
	}
}
