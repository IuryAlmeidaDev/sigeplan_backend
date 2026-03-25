package com.sejus.sigeplan.application.service;

import com.sejus.sigeplan.application.dto.admin.CreateRoleRequest;
import com.sejus.sigeplan.application.dto.admin.RoleResponse;
import com.sejus.sigeplan.application.dto.admin.UpdateRoleRequest;
import com.sejus.sigeplan.domain.model.Role;
import com.sejus.sigeplan.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleAdminService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponse findById(UUID roleId) {
        return toResponse(getRoleOrThrow(roleId));
    }

    @Transactional
    public RoleResponse create(CreateRoleRequest request) {
        String normalizedName = normalizeRoleName(request.name());

        if (roleRepository.existsByName(normalizedName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Papel já cadastrado.");
        }

        OffsetDateTime now = OffsetDateTime.now();

        Role role = new Role(
                UUID.randomUUID(),
                normalizedName,
                normalizeDescription(request.description()),
                Set.of(),
                now,
                now
        );

        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleResponse update(UUID roleId, UpdateRoleRequest request) {
        Role currentRole = getRoleOrThrow(roleId);
        String normalizedName = normalizeRoleName(request.name());

        roleRepository.findByName(normalizedName)
                .filter(found -> !found.id().equals(roleId))
                .ifPresent(found -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Papel já cadastrado.");
                });

        Role updatedRole = new Role(
                currentRole.id(),
                normalizedName,
                normalizeDescription(request.description()),
                currentRole.permissions(),
                currentRole.createdAt(),
                OffsetDateTime.now()
        );

        return toResponse(roleRepository.save(updatedRole));
    }

    private Role getRoleOrThrow(UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Papel não encontrado."));
    }

    private RoleResponse toResponse(Role role) {
        return new RoleResponse(
                role.id().toString(),
                role.name(),
                role.description(),
                role.permissions().stream()
                        .map(permission -> permission.name())
                        .collect(Collectors.toSet())
        );
    }

    private String normalizeRoleName(String name) {
        String value = name == null ? null : name.trim().toUpperCase();
        return value != null && value.startsWith("ROLE_") ? value : "ROLE_" + value;
    }

    private String normalizeDescription(String description) {
        return description == null || description.isBlank() ? null : description.trim();
    }
}