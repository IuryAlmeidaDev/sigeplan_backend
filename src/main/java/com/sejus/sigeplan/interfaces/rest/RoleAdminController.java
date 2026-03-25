package com.sejus.sigeplan.interfaces.rest;

import com.sejus.sigeplan.application.dto.admin.CreateRoleRequest;
import com.sejus.sigeplan.application.dto.admin.RoleResponse;
import com.sejus.sigeplan.application.dto.admin.UpdateRoleRequest;
import com.sejus.sigeplan.application.service.RoleAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleAdminController {

    private final RoleAdminService roleAdminService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Listar papéis")
    public List<RoleResponse> findAll() {
        return roleAdminService.findAll();
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Buscar papel por ID")
    public RoleResponse findById(@PathVariable UUID roleId) {
        return roleAdminService.findById(roleId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Criar papel")
    public RoleResponse create(@Valid @RequestBody CreateRoleRequest request) {
        return roleAdminService.create(request);
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Atualizar papel")
    public RoleResponse update(
            @PathVariable UUID roleId,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        return roleAdminService.update(roleId, request);
    }
}