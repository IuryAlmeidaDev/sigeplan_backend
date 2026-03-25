package com.sejus.sigeplan.infrastructure.persistence.repository;

import com.sejus.sigeplan.domain.model.Permission;
import com.sejus.sigeplan.domain.model.Role;
import com.sejus.sigeplan.domain.model.Unit;
import com.sejus.sigeplan.domain.model.User;
import com.sejus.sigeplan.domain.repository.UserRepository;
import com.sejus.sigeplan.infrastructure.persistence.entity.PermissionEntity;
import com.sejus.sigeplan.infrastructure.persistence.entity.RoleEntity;
import com.sejus.sigeplan.infrastructure.persistence.entity.UnitEntity;
import com.sejus.sigeplan.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataUserJpaRepository jpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.builder()
                .id(user.id())
                .fullName(user.fullName())
                .email(user.email())
                .cpf(user.cpf())
                .passwordHash(user.passwordHash())
                .active(user.active())
                .roles(toRoleEntities(user.roles()))
                .units(toUnitEntities(user.units()))
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .build();

        return toDomain(jpaRepository.save(entity));
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getPasswordHash(),
                entity.isActive(),
                toRoles(entity.getRoles()),
                toUnits(entity.getUnits()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private Set<Role> toRoles(Set<RoleEntity> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }

        Set<Role> result = new HashSet<>();
        for (RoleEntity role : roles) {
            result.add(new Role(
                    role.getId(),
                    role.getName(),
                    role.getDescription(),
                    toPermissions(role.getPermissions()),
                    role.getCreatedAt(),
                    role.getUpdatedAt()
            ));
        }
        return result;
    }

    private Set<Permission> toPermissions(Set<PermissionEntity> permissions) {
        if (permissions == null) {
            return Collections.emptySet();
        }

        Set<Permission> result = new HashSet<>();
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

    private Set<Unit> toUnits(Set<UnitEntity> units) {
        if (units == null) {
            return Collections.emptySet();
        }

        Set<Unit> result = new HashSet<>();
        for (UnitEntity unit : units) {
            result.add(new Unit(
                    unit.getId(),
                    unit.getName(),
                    unit.getCode(),
                    unit.getDescription(),
                    unit.isActive(),
                    unit.getCreatedAt(),
                    unit.getUpdatedAt()
            ));
        }
        return result;
    }

    private Set<RoleEntity> toRoleEntities(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }

        Set<RoleEntity> result = new HashSet<>();
        for (Role role : roles) {
            result.add(RoleEntity.builder()
                    .id(role.id())
                    .name(role.name())
                    .description(role.description())
                    .permissions(toPermissionEntities(role.permissions()))
                    .createdAt(role.createdAt())
                    .updatedAt(role.updatedAt())
                    .build());
        }
        return result;
    }

    private Set<PermissionEntity> toPermissionEntities(Set<Permission> permissions) {
        if (permissions == null) {
            return Collections.emptySet();
        }

        Set<PermissionEntity> result = new HashSet<>();
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

    private Set<UnitEntity> toUnitEntities(Set<Unit> units) {
        if (units == null) {
            return Collections.emptySet();
        }

        Set<UnitEntity> result = new HashSet<>();
        for (Unit unit : units) {
            result.add(UnitEntity.builder()
                    .id(unit.id())
                    .name(unit.name())
                    .code(unit.code())
                    .description(unit.description())
                    .active(unit.active())
                    .createdAt(unit.createdAt())
                    .updatedAt(unit.updatedAt())
                    .build());
        }
        return result;
    }
}