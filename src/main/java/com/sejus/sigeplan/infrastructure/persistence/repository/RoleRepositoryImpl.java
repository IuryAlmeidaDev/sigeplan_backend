package com.sejus.sigeplan.infrastructure.persistence.repository;

import com.sejus.sigeplan.domain.model.Permission;
import com.sejus.sigeplan.domain.model.Role;
import com.sejus.sigeplan.domain.repository.RoleRepository;
import com.sejus.sigeplan.infrastructure.persistence.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final SpringDataRoleJpaRepository jpaRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name).map(this::toDomain);
    }

    private Role toDomain(RoleEntity entity) {
        return new Role(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPermissions().stream()
                        .map(permission -> new Permission(
                                permission.getId(),
                                permission.getName(),
                                permission.getDescription(),
                                permission.getCreatedAt(),
                                permission.getUpdatedAt()
                        ))
                        .collect(Collectors.toSet()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}