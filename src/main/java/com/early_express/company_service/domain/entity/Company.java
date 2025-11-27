package com.early_express.company_service.domain.entity;

import com.early_express.company_service.domain.vo.Type;
import com.early_express.company_service.global.common.utils.UuidUtils;
import com.early_express.company_service.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "p_company",
	indexes = {
		@Index(name = "idx_company_name", columnList = "name"),
		@Index(name = "idx_company_owner_hub", columnList = "ownerHubId"),
		@Index(name = "idx_created_at_updated_at", columnList = "createdAt DESC, updatedAt DESC")
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {
	@Id
	@Column(length = 36, updatable = false, nullable = false)
	private String id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(length = 6, nullable = false)
	private Type type;

	@Column(length = 36, nullable = false)
	private String managingHubId;

	@Column(length = 100, nullable = false)
	private String address;

	@Builder
	public Company(String name, Type type, String managingHubId, String address) {
		this.id = UuidUtils.generate();
		this.name = name;
		this.type = type;
		this.managingHubId = managingHubId;
		this.address = address;
	}

	@Column(length = 36)
	private String ownerId;

	public void updateProfile(String name, Type type, String managingHubId, String address, String ownerId) {
		if (name != null) this.name = name;
		if (type != null) this.type = type;
		if (address != null) this.address = address;
		if (ownerId != null) this.ownerId = ownerId;
	}

	// 테스트용
	public void assignOwner(String ownerId) {
		this.ownerId = ownerId;
	}

	public Company(String uuid, String name, Type type, String managingHubId, String address) {
		this.id = uuid;
		this.name = name;
		this.type = type;
		this.managingHubId = managingHubId;
		this.address = address;
	}
}
