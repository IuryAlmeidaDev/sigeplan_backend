package com.sejus.sigeplan.infrastructure.persistence.repository;

import com.sejus.sigeplan.domain.model.Permission;
import com.sejus.sigeplan.domain.model.Role;
import com.sejus.sigeplan.domain.repository.RoleRepository;
import com.sejus.sigeplan.infrastructure.persistence.entity.PermissionEntity;
import com.sejus.sigeplan.infrastructure.persistence.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final SpringDataRoleJpaRepository jpaRepository;

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = RoleEntity.builder()
                .id(role.id())
                .name(role.name())
                .description(role.description())
                .permissions(toPermissionEntities(role.permissions()))
                .createdAt(role.createdAt())
                .updatedAt(role.updatedAt())
                .build();

        return toDomain(jpaRepository.save(entity));
    }

    private Role toDomain(RoleEntity entity) {
        return new Role(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                toPermissions(entity.getPermissions()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private Set<Permission> toPermissions(Set<PermissionEntity> permissions) {
        Set<Permission> result = new HashSet<>();
        if (permissions == null) {
            return result;
        }

        for (PermissionEntity permission : permissions) {
            result.add(new Permission(
                    permission.getId(),
                    permission.getName(),
                    permission.getDescription(),
                    permission.getCreatedAt(),
                    permission.getUpdatedAt()
            ));
        }

        return result;
    }

    private Set<PermissionEntity> toPermissionEntities(Set<Permission> permissions) {
        Set<PermissionEntity> result = new HashSet<>();
        if (permissions == null) {
            return result;
        }

        for (Permission permission : permissions) {
            result.add(PermissionEntity.builder()
                    .id(permission.id())
                    .name(permission.name())
                    .description(permission.description())
                    .createdAt(permission.createdAt())
                    .updatedAt(permission.updatedAt())
                    .build());
        }

        return result;
    }
}